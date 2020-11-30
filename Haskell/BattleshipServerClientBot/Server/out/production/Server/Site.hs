{-# LANGUAGE OverloadedStrings #-}

module Site (app) where

import           Api.Core
import           Data.Configurator
import           Control.Lens
import           Control.Monad.Except
import           Control.Applicative
import           Data.ByteString (ByteString)
import qualified Data.Text as T
import           Snap.Core
import           Snap.Snaplet
import           Snap.Snaplet.Auth
import           Snap.Snaplet.Auth.Backends.JsonFile
import           Snap.Snaplet.Heist
import           Snap.Snaplet.Session.Backends.CookieSession
import           Snap.Util.FileServe
import           Heist
import qualified Heist.Interpreted as I

import           App

routes :: String -> [(ByteString, Handler App App())]
routes s = [ ("", serveDirectoryWith defaultDirectoryConfig s)
           , ("/games/:gameid", serveFile $ s ++ "/index.html")
           , ("/games/:gameid/:sessionid", serveFile $ s ++ "/index.html")
           ]

app :: SnapletInit App App
app = makeSnaplet "battleship" "Battleship application." Nothing $ do
  conf <- getSnapletUserConfig
  staticPath <- liftIO $ require conf "battleship.static.path"
  mongoHost <- liftIO $ require conf "battleship.mongo.host"
  mongoUser <- liftIO $ require conf "battleship.mongo.user"
  mongoPass <- liftIO $ require conf "battleship.mongo.pass"
  mongoDb <- liftIO $ require conf "battleship.mongo.db"
  rulesPath <- liftIO $ require conf "battleship.rules"
  botsPath <- liftIO $ require conf "battleship.bots"
  a <- nestSnaplet "api" api $ apiInit mongoHost mongoUser mongoPass mongoDb rulesPath botsPath
  addRoutes $ routes $ staticPath
  return $ App a

