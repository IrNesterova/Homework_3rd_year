{-# LANGUAGE OverloadedStrings #-}
{-# LANGUAGE FlexibleInstances #-}
module Api.Types where

import qualified Data.Text as T
import qualified Data.ByteString.Char8 as B
import Data.ByteString.Lazy.Internal as BL
import Data.ByteString.Lazy.Char8 as Char8
import Data.Bson as BS
import Data.Aeson
import Control.Monad

data Turn = OWNER|PLAYER|NOTREADY|NOTREADY_WITH_MAP|CONFIG|CONFIG_WAIT_OWNER|CONFIG_WAIT_PLAYER|PLAYER_WIN|OWNER_WIN

instance ToJSON Turn where
  toJSON t = case t of
    OWNER->"owner"
    PLAYER->"player"
    NOTREADY->"notready"
    NOTREADY_WITH_MAP->"notready"
    CONFIG->"config"
    CONFIG_WAIT_OWNER->"config"
    CONFIG_WAIT_PLAYER->"player"
    PLAYER_WIN->"player_win"
    OWNER_WIN->"owner_win"


data GameRights = GameRights { grIsExists :: Bool
                             , grIsOwner :: Bool
                             , grIsPlayer :: Bool
                             , grTurn :: Turn
                             , grIsGuest :: Bool
                             , grMyName :: String
                             , grPublic :: Bool
                             , grRules :: String
                             , grGame :: Maybe Document
                             }

data PublicGame = PublicGame { pgGameId :: String
                             , pgOwnerName :: String
                             , pgMessage :: String
                             , pgRules :: String
                             , pgTurn :: Turn
                             }

instance ToJSON PublicGame where
  toJSON (PublicGame g o m r t) = object ["game" .= g, "owner" .= o, "message" .= m, "rules" .= r, "turn" .= t]

data NewGameUser = NewGameUser {nguName :: String, nguMessage :: String, nguRules :: String}
instance FromJSON NewGameUser where
  parseJSON (Object v) =
    NewGameUser <$> v.: "name"
                <*> v .: "message"
                <*> v .: "rules"
  parseJSON _ = mzero

data GameUser = GameUser{guName :: String, guMessage :: String}

instance FromJSON GameUser where
  parseJSON (Object v) =
    GameUser <$> v .: "name"
             <*> v .: "message"
  parseJSON _ = mzero

data Message = Message { msgMessage :: String }
instance FromJSON Message where
  parseJSON (Object v) =
    Message <$> v .: "message"
  parseJSON _ = mzero

data APIError = APIError { e :: String }
instance ToJSON APIError where
  toJSON (APIError e) = toJSON $ e

data NewGame = NewGame { ngGame :: String, ngSession :: String, ngRules :: String }
instance ToJSON NewGame where
  toJSON (NewGame g s r) = object [ "game" .= g, "session" .= s, "rules" .= r ]

data SessionInfo = SessionInfo { siGame :: String, siSession :: String }
instance ToJSON SessionInfo where
  toJSON (SessionInfo g s) = object [ "game" .= g, "session" .= s ]

instance ToJSON B.ByteString where
  toJSON b = toJSON $ B.unpack b

instance ToJSON BL.ByteString where
  toJSON b = toJSON $ Char8.unpack b

instance ToJSON BS.Field where
  toJSON v = "field"

data ChatMessage = ChatMessage { cmGameId :: String
                               , cmName :: String
                               , cmSession :: String
                               , cmTime :: Int
                               , cmMessage :: String
                               }
instance ToJSON ChatMessage where
  toJSON (ChatMessage _ n _ t m) = object [ "name" .= n, "message" .= m, "time" .= t ]

data Rule = Rule { ruleId :: String
                 , ruleName :: String
                 , ruleDescription :: String
                 , ruleShips :: [[Int]]
                 , ruleOrder :: Int
                 }
instance FromJSON Rule where
  parseJSON (Object v) =
    Rule <$> v .: "id"
         <*> v .: "name"
         <*> v .: "rules"
         <*> v .: "ships"
         <*> v .: "order"
  parseJSON _ = mzero
instance ToJSON Rule where
  toJSON (Rule i n r _ o) = object ["id" .= i, "name" .= n, "rules" .= r, "order" .= o ]
--пусть будет бот

data Bot = Bot { botName :: String
               , botId :: String
               , botRulesList :: [String]
               }

instance FromJSON Bot where
  parseJSON (Object v) =
    Bot <$> v .: "name"
         <*> v .: "id"
         <*> v .: "rules"
  parseJSON _ = mzero

instance ToJSON Bot where
  toJSON (Bot n _ r) = object ["name" .= n, "rules" .= r ]

data Shot  = Shot { shotX :: Int, shotY :: Int }

instance FromJSON Shot where
  parseJSON (Object v) =
    Shot <$> v .: "x"
         <*> v .: "y"
  parseJSON _ = mzero

data BotsGame = BotsGame { bgGameId :: String
                         , bgStartTime :: Int
                         }

instance ToJSON BotsGame where
  toJSON (BotsGame g _) = toJSON g

data BotName = BotName { bnName :: String }

instance FromJSON BotName where
  parseJSON (Object v) =
    BotName <$> v .: "botname"
  parseJSON _ = mzero