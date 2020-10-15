module Lib
    ( someFunc
    ) where

someFunc :: IO ()
someFunc = putStrLn "someFunc"

tell:: (Show a) => [a] ->String
tell [] = "This list is empty"
tell (x:[])= "This list has only one element " ++ show x
tell (x:y:[]) = "This list has only two elements " ++ show x ++ " and " ++ show y
tell (x:y:_) = "This list has more than two elements "

head' :: [a] -> a
head' [] = error "This list is empty"
head' (x:_) = x

length' :: (Num b) => [a] -> b
length' [] = 0
length' (x:[]) = 1
length' (_:xs) = 1 + length' xs

sum' :: (Num a) => [a] -> a
sum' [] = 0
sum' (x:xs) = x + sum' xs

append' :: [a] -> [a] -> [a]
append' xs ys = foldr (:) xs ys

--(x:xs)

init1 :: [a] -> [a]
init1 []  = []
init1 (x:[]) = []
init1 (_x) = []
init1 (x:xs) = x : init1 xs

--functions

bmiTell :: (RealFloat a) => a -> a -> String

bmiTell weight height
  | bmi <= 18.5 = "You're underweight"
  | bmi <= 25.0 = "You're alright, pal"
  | bmi <= 30.0 = "You're fat!"
  | otherwise = "Well, you're really really fat"
  where bmi = weight / height ^ 2

max' :: (Ord a)=> a -> a -> a
max' a b
  | a > b = a
  | otherwise = b

initials :: String -> String -> String
initials firstName lastName = [f] ++ ". " ++ [l] ++ "."
  where (f:_) = firstName
        (l:_) = lastName

cylinder:: (RealFloat a) => a->a->a
cylinder r h =
    let sideArea = 2 * pi * r * h
        topArea = pi * r^2
    in sideArea + 2 * topArea
zoot :: Int-> Int->Int->Int
zoot x y z = x * y + z


describeList :: [a] -> String
describeList xs = "The list is " ++ what xs
      where what [] = "empty"
            what (x:[]) = "has one element"
            what xs = "a longer list"

recur' :: Int -> Int
recur' 0 = 1
recur' a = a * recur' (a-1)

maximum' :: Ord(a)=> [a] -> a
maxumum' [] = error "It's an empty list, you dolt"
maxumim' [x] = x
maximum' (x:xs)
  | x > maxTail = x
  | otherwise = maxTail
  where maxTail = maximum' xs

--replicate' :: (Num i, Ord i)=> i -> [i]
--replicate' n x
--  | n <=0 = []
--  | otherwise = x:replicate'(n-1) x


-- higher order functions

-- carried functions

-- function application. The space is sort of like an operator and it has the highest precedence.
-- Let's examine the type of max. It's max :: (Ord a) => a -> a -> a. That can also be written as max :: (Ord a) => a
-- -> (a -> a). That could be read as: max takes an a and returns (that's the ->) a function that takes an a and returns an a.
-- That's why the return type and the parameters of functions are all simply separated with arrows.

--func :: (Num a) => a -> a -> a -> a
--func a b c d = a + b + c + d

--λ: let n = func 2 3
--λ: let m = n 10
--λ: let g = m 7
--λ: g
--22

--higher-orderism

applyTwice :: (a->a) -> a -> a
applyTwice f x = f (f x)

-- f is a function, x is a number or smth

zipWith' :: (a->b->c) -> [a]->[b]->[c]
zipWith' _ [] _ = []
zipWith' _ _ [] = []
zipWith' f (x:xs) (y:ys) = f x y: zipWith' f xs ys

flip' :: (a->b->c) -> (b->a->c)
flip' f y x = f x y

map :: (a->b) -> [a]->[b]
map _ [] = []
map f (x:xs) = f x : Lib.map f xs

-- The type signature says that it takes a function that takes an a and returns a b, a list of a's and returns a list of b's.

filter :: (a-> Bool) -> [a] -> [a]
filter _ [] = []
filter p (x:xs)
  | p x = x : Lib.filter p xs
  | otherwise = Lib.filter p xs

--Pretty simple stuff. If p x evaluates to True, the element gets included in the new list.

quicksort :: (Ord a) => [a] ->[a]
quicksort [] = []
quicksort (x:xs) =
  let smallerSorted = quicksort (Lib.filter (<=x) xs)
      biggerSorted = quicksort (Lib.filter (>x) xs)
  in smallerSorted ++ [x] ++ biggerSorted

largestDivisible :: (Integral a) => a
largestDivisible = head (Lib.filter p [100000, 99999..])
  where p x = x `mod` 3829 == 0


chain :: (Integral a) => a -> [a]
chain 1 = [1]
chain n
  | even n = n:chain(n `div` 2)
  | odd n = n:chain (n*3 + 1)

numLongChain::Int
numLongChain = length (Lib.filter isLong (Lib.map chain [1..100]))
    where isLong xs = length xs > 15

--lambda

--Lambdas are basically anonymous functions that are used because we need some functions only once.

numLongChains1::Int
numLongChains1 = length (Lib.filter (\xs -> length xs > 15) (Lib.map chain [1..100]))

sum1' :: (Num a)=> [a]->a
sum1' xs = foldl(\acc x -> acc + x) 0 xs

elem1 :: (Eq a) => a -> [a] -> Bool
elem1 y ys = foldl (\acc x -> if x == y then True else acc) False ys

-- $ function applicator
($) :: (a->b) -> a-> b
f $ x = f x



