{-# LANGUAGE OverloadedStrings #-}
{-# LANGUAGE ExtendedDefaultRules #-}
{-# LANGUAGE TemplateHaskell #-}
{-# LANGUAGE FlexibleInstances #-}
{-# LANGUAGE DeriveGeneric #-}

module Api.Services.GameService where

import Control.Exception
import Control.Monad
import Control.Monad.IO.Class
import Database.MongoDB
import Database.MongoDB.Query as MQ
import Database.MongoDB.Connection
import Data.Bson as BS
import Api.Types
import qualified Control.Lens as CL
import Control.Monad.State.Class
import Data.List as DL
import Data.Aeson as DA
import Data.UUID as UUID
import Data.UUID.V4
import Snap.Core
import Snap.Snaplet as SN
import qualified Data.ByteString.Char8 as B
import qualified Data.Text as T
import qualified Data.Text.IO as TIO
import Data.Text.Encoding
import Data.Time.Clock.POSIX


data GameService = GameService { }

CL.makeLenses ''GameService

gameTimeout :: Int
gameTimeout = 3600 * 10000

mapWidth :: Int
mapWidth = 10

mapHeight :: Int
mapHeight = 10

--Routes
gameRoutes :: Host  -> Username -> Password -> Database -> FilePath -> FilePath -> [(B.ByteString, SN.Handler b GameService ())]
gameRoutes mongoHost mongoUser mongoPass mongoDb rulePath botsPath= [
    ("/", method GET $ getPublicGamesList mongoHost mongoUser mongoPass mongoDb),
    ("/", method POST $ createGame mongoHost mongoUser mongoPass mongoDb rulePath),
    ("/rules", method GET $ getRules rulePath),
    ("/bots", method GET $ getBots botsPath),
    ("/:gameid", method GET $ getGameShortInfo mongoHost mongoUser mongoPass mongoDb),
    ("/:gameid/:session/setmap", method POST $ sendMap mongoHost mongoUser mongoPass mongoDb rulePath),
    ("/:gameid/:session", method GET $ getStatus mongoHost mongoUser mongoPass mongoDb),
    ("/:gameid/:session/invitebot", method POST $ inviteBot mongoHost mongoUser mongoPass mongoDb botsPath),
    ("/:gameid/:session/setpublic", method POST $ setPublic mongoHost mongoUser mongoPass mongoDb),
    ("/:gameid/connect/player", method POST $ connectGamePlayer mongoHost mongoUser mongoPass mongoDb),
    ("/:gameid/connect/guest", method POST $ connectGameGuest mongoHost mongoUser mongoPass mongoDb),
    ("/:gameid/:session/shoot", method POST $ shoot mongoHost mongoUser mongoPass mongoDb),
    ("/:gameid/:session/chat", method POST $ sendMessage mongoHost mongoUser mongoPass mongoDb),
    ("/:gameid/:session/chat", method GET $ readMessages mongoHost mongoUser mongoPass mongoDb),
    ("/bots/:bot/games", method GET $ getBotsGamesList mongoHost mongoUser mongoPass mongoDb)
  ]


--Actions
getPublicGamesList :: Host -> Username -> Password -> Database -> SN.Handler b GameService ()
getPublicGamesList mongoHost mongoUser mongoPass mongoDb = do
  pipe <- liftIO $ connectAndAuth mongoHost mongoUser mongoPass mongoDb
  let a action = liftIO $ performAction pipe mongoDb action
  modifyResponse $ setHeader "Content-Type" "application/json"
  time <- liftIO $ round . (* 10000) <$> getPOSIXTime
  let action = rest =<< MQ.find (MQ.select ["date" =: ["$gte" =: time - gameTimeout], "public" =: True] "games")
                                {MQ.sort = ["date" =: -1]}
  games <- a $ action
  writeLBS . encode $ fmap (\d -> PublicGame (BS.at "game" d) (BS.at "name" (BS.at "owner" d)) (BS.at "message" d) (BS.at "rules" d) (getTurn $ BS.at "turn" d)) games
  liftIO $ closeConnection pipe
  modifyResponse . setResponseCode $ 200

getBotsGamesList :: Host -> Username -> Password -> Database -> SN.Handler b GameService ()
getBotsGamesList mongoHost mongoUser mongoPass mongoDb = do
  pbotid <- getParam "bot"
  modifyResponse $ setHeader "Content-Type" "application/json"
  case pbotid of Just botid -> do
                                let bid = B.unpack botid
                                pipe <- liftIO $ connectAndAuth mongoHost mongoUser mongoPass mongoDb
                                let a action = liftIO $ performAction pipe mongoDb action
                                time <- liftIO $ round . (* 10000) <$> getPOSIXTime
                                let action = rest =<< MQ.find (MQ.select
                                                                  ["date" =: ["$gte" =: time - gameTimeout]]
                                                                  (T.pack $ "botgames_" ++ bid))
                                games <- a $ action
                                writeLBS . encode $ fmap (\d -> (BS.at "game" d) :: String) games
                                liftIO $ closeConnection pipe
                 Nothing -> writeLBS . encode $ ([] :: [String])
  modifyResponse . setResponseCode $ 200

getGameShortInfo :: Host -> Username -> Password -> Database -> SN.Handler b GameService ()
getGameShortInfo mongoHost mongoUser mongoPass mongoDb = do
  pipe <- liftIO $ connectAndAuth mongoHost mongoUser mongoPass mongoDb
  let a action = liftIO $ performAction pipe mongoDb action
  modifyResponse $ setHeader "Content-Type" "application/json"
  pgame <- getParam "gameid"
  let game = case pgame of Just g -> B.unpack g
                           Nothing -> ""
  rights <- liftIO $ fillRights pipe mongoDb game Nothing
  case rights of
    GameRights True _ _ turn _ _ _ rules (Just gameinfo) -> do
           let owner = BS.at "owner" gameinfo
           let ownername = BS.at "name" owner
           let message = (BS.at "message" gameinfo)
           modifyResponse . setResponseCode $ 200
           writeLBS . encode $ PublicGame game ownername message rules turn
    _ -> do
           writeLBS . encode $ APIError "Game not found!"
           modifyResponse $ setResponseCode 404
  liftIO $ closeConnection pipe

createGame :: Host -> Username -> Password -> Database -> FilePath -> SN.Handler b GameService ()
createGame mongoHost mongoUser mongoPass mongoDb rulePath = do
  pipe <- liftIO $ connectAndAuth mongoHost mongoUser mongoPass mongoDb
  let a action = liftIO $ performAction pipe mongoDb action
  modifyResponse $ setHeader "Content-Type" "application/json"
  user <- fmap (\x -> decode x :: Maybe NewGameUser) $ readRequestBody 4096
  case user of Just (NewGameUser name message rules) -> do
                 gameId <- liftIO $ UUID.toString <$> nextRandom
                 sessionId <- liftIO $ UUID.toString <$> nextRandom
                 time <- liftIO $ round . (* 10000) <$> getPOSIXTime
                 crules <- liftIO $ currentRulesId rules rulePath
                 let game = [
                              "game" =: gameId,
                              "date" =: time,
                              "message" =: "",
                              "owner" =: ["name" =: (take 20 name), "message" =: (take 140 message), "session" =: sessionId],
                              "turn" =: ["notready"],
                              "public" =: False,
                              "rules" =: crules
                            ]
                 a $ MQ.insert "games" game
                 writeLBS $ encode $ NewGame gameId sessionId crules
                 modifyResponse $ setResponseCode 201
               Nothing -> do
                 writeLBS . encode $ APIError "Name and rules can't be empty!"
                 modifyResponse $ setResponseCode 400
  liftIO $ closeConnection pipe

sendMap :: Host -> Username -> Password -> Database -> FilePath -> SN.Handler b GameService ()
sendMap mongoHost mongoUser mongoPass mongoDb rulePath = do
  time <- liftIO $ round . (* 10000) <$> getPOSIXTime
  modifyResponse $ setHeader "Content-Type" "application/json"
  pgame <- getParam "gameid"
  session <- getParam "session"
  let game = case pgame of Just g -> B.unpack g
                           Nothing -> ""
  let msess = B.unpack <$> session
  mbseamap <- fmap (\x -> decode x :: Maybe [[Int]]) $ readRequestBody 4096
  case mbseamap of
    Just seamap -> do
      pipe <- liftIO $ connectAndAuth mongoHost mongoUser mongoPass mongoDb
      let a action = liftIO $ performAction pipe mongoDb action
      rights <- liftIO $ fillRights pipe mongoDb game msess
      let act user sm t = [(
                   [
                     "game" =: game
                   ]::Selector,
                   [
                     "$set" =: [(T.pack $ user ++ ".map") =: sm],
                     "$push" =: ["turn" =: t]
                   ]::Document,
                   [ ]::[UpdateOption]
                )]

      let chat n m= [ "game" =: game
                   , "name" =: n
                   , "session" =: msess
                   , "time" =: time
                   , "message" =: m
                   ]::Document
      let doit n u m t r = do
          myrules <- liftIO $ currentRules r rulePath
          case (isGood seamap myrules) of
            True -> do
              a $ MQ.updateAll "games" $ act u seamap t
              a $ MQ.insert "chats" $ chat n m
              writeLBS "ok"
              modifyResponse $ setResponseCode 200
            _ -> do
              writeLBS . encode $ APIError "Check your ships!"
              modifyResponse $ setResponseCode 406
      case rights of
        GameRights True True _ NOTREADY _ name _ rid _ -> do
          doit name "owner" "I've sent my map." "owner_map" rid
        GameRights True True _ NOTREADY_WITH_MAP _ name _ rid _ -> do
          doit name "owner" "I've sent a new map." "owner_map" rid
        GameRights True True _ CONFIG _ name _ rid _ -> do
          doit name "owner" "I've sent my map. Waiting for you!" "owner_map" rid
        GameRights True True _ CONFIG_WAIT_OWNER _ name _ rid _ -> do
          doit name "owner" "I've sent my map. Let's do this!" "owner_map" rid
        GameRights True True _ CONFIG_WAIT_PLAYER _ name _ rid _ -> do
          doit name "owner" "I've sent a new map. Waiting for you!" "owner_map" rid
        GameRights True _ True CONFIG _ name _ rid _ -> do
          doit name "player" "I've sent my map. Waiting for you!" "player_map" rid
        GameRights True _ True CONFIG_WAIT_OWNER _ name _ rid _ -> do
          doit name "player" "I've sent a new map. Waiting for you!" "player_map" rid
        GameRights True _ True CONFIG_WAIT_PLAYER _ name _ rid _ -> do
          doit name "player" "I've sent my map. Let's do this!" "player_map" rid
        _ -> do
           writeLBS . encode $ APIError "Can't send the map for this game or the game is not exists!"
           modifyResponse $ setResponseCode 403
      liftIO $ closeConnection pipe
    Nothing -> do
      writeLBS . encode $ APIError "Can't find your map!"
      modifyResponse $ setResponseCode 404

getStatus :: Host -> Username -> Password -> Database -> SN.Handler b GameService ()
getStatus mongoHost mongoUser mongoPass mongoDb = do
  time <- liftIO $ round . (* 10000) <$> getPOSIXTime
  pipe <- liftIO $ connectAndAuth mongoHost mongoUser mongoPass mongoDb
  let a action = liftIO $ performAction pipe mongoDb action
  modifyResponse $ setHeader "Content-Type" "application/json"
  pgame <- getParam "gameid"
  psession <- getParam "session"
  let game = case pgame of Just g -> B.unpack g
                           Nothing -> ""
  let session = case psession of Just s -> B.unpack s
                                 Nothing -> ""
  let msess = (B.unpack <$> psession)
  rights <- liftIO $ fillRights pipe mongoDb game msess
  let getGStatus you turn rules gameinfo isPublic = do
      let owner = BS.at "owner" gameinfo
      let ownername = BS.at "name" owner
      let ownermessage = BS.at "message" owner
      mbownermap <- try (BS.look "map" owner) :: IO (Either SomeException BS.Value)
      let ownermap = case mbownermap of
            Right (BS.Array m) -> [(case mbl of
              BS.Array l -> [(case mbc of BS.Int32 c -> head $ [c | c > 1 || you == "owner" || isGameFinished turn] ++ [0]
                                          _ -> 0) | mbc <- l]
              _ -> []) | mbl <- m]
            _ -> []
      mbplayer <- try (BS.look "player" gameinfo) :: IO (Either SomeException BS.Value)
      playerobj <- liftIO $ case mbplayer of
            Right (BS.Doc player) -> do
              let playername = BS.at "name" player
              mbplayermap <- try (BS.look "map" player) :: IO (Either SomeException BS.Value)
              let playermap = case mbplayermap of
                    Right (BS.Array m) -> [(case mbl of
                      BS.Array l -> [(case mbc of BS.Int32 c -> head $ [c | c > 1 || you == "player" || isGameFinished turn] ++ [0]
                                                  _ -> 0) | mbc <- l]
                      _ -> []) | mbl <- m]
                    _ -> []
              let playermessage = BS.at "message" player
              return $ object [ "name" .= T.pack playername
                              , "message" .= T.pack playermessage
                              , "map" .= playermap
                              ]
            _ -> return $ object []

      mbguests <- try (BS.look "guests" gameinfo) :: IO (Either SomeException BS.Value)
      let guestsobj = case mbguests of
            Right (BS.Array guests) -> [(case mbguest of
                                           (BS.Doc guest) -> object [ "name" .= T.pack (BS.at "name" guest)
                                                                    , "message" .= T.pack (BS.at "message" guest)
                                                                    ]
                                           _ -> object []) | mbguest <- guests]
            _ -> []
      let yourname = case you of
            "owner" -> ownername
            "player" -> BS.at "name" (BS.at "player" gameinfo)
            "guest" -> case mbguests of
                  Right (BS.Array guests) -> head $ [n | [n,s] <- [(case mbguest of
                                           (BS.Doc guest) -> [BS.at "name" guest, BS.at "session" guest]
                                           _ -> ["",""]) | mbguest <- guests], s==session]
                  _ -> ""

      let status = object [ "game" .= game
                          , "message" .= T.pack (BS.at "message" gameinfo)
                          , "you" .= you
                          , "yourname" .= yourname
                          , "rules" .= rules
                          , "turn" .= turn
                          , "owner" .= object [ "name" .= T.pack ownername
                                              , "message" .= T.pack ownermessage
                                              , "map" .= ownermap
                                              ]
                          , "player" .= playerobj
                          , "guests" .= guestsobj
                          , "isPublic" .= isPublic
                          ]
      return status
  case rights of
    GameRights True True False turn False _ isPublic rules (Just gameinfo) -> do
      status <- liftIO $ getGStatus "owner" turn rules gameinfo isPublic
      writeLBS . encode $ status
      modifyResponse . setResponseCode $ 200
    GameRights True False True turn False _ isPublic rules (Just gameinfo) -> do
      status <- liftIO $ getGStatus "player" turn rules gameinfo isPublic
      writeLBS . encode $ status
      modifyResponse . setResponseCode $ 200
    GameRights True False False turn True _ isPublic rules (Just gameinfo) -> do
      status <- liftIO $ getGStatus "guest" turn rules gameinfo isPublic
      writeLBS . encode $ status
      modifyResponse . setResponseCode $ 200
    _ -> do
      writeLBS . encode $ APIError "Can't find the game or you shouldn't see this game's status!"
      modifyResponse $ setResponseCode 400
  liftIO $ closeConnection pipe



