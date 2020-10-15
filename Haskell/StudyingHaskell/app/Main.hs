module Main where

import Lib

main :: IO ()
main = someFunc

tell:: (Show a) => [a] ->String
tell [] = "This list is empty"
tell (x:[])= "This list has only one element" ++ show x
tell (x:y:[]) = "This list has only two elements " ++ show x ++ " and " ++ show y
tell (x:y:_) = "This list has more than two elements "