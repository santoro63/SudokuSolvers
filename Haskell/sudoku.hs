-- Sudoku Solver Challenge
--
-- Our solution is to keep a list of partial solutions, with each
-- iteration creating a new set of partial solutions by expanding
-- on the existing one, or removing it from the list of candidates
-- if no feasible solution can be derived from it.
import Data.List
import System.Environment

type Puzzle = [Char]

-- solve the puzzle
solve :: [Puzzle] -> Puzzle
solve [] = []   -- really should be raising an error
solve (x:xs)
  | solved x = x
  | otherwise = solve $ expand_puzzles (x:xs)

-- true if the puzzle is solved (i.e, no elements left with '_'
solved :: Puzzle -> Bool
solved []     = False
solved puzzle = '_' `notElem` puzzle

-- performs one iteration on solving the puzzle
expand_puzzles :: [Puzzle] -> [Puzzle]
expand_puzzles ps = flatmap expand_one_puzzle ps

-- expands one puzzle, creating a list of viable puzzles
-- derived from that one by replacing the first '_' with
-- the appropriate subset of '1' .. '9'
expand_one_puzzle :: Puzzle -> [Puzzle]
expand_one_puzzle puzzle =  map (add_to_puzzle puzzle) $ valid_candidates puzzle

-- adds the element to the puzzle, replacing the first '_'
add_to_puzzle :: Puzzle -> Char -> Puzzle
add_to_puzzle [] _ = []
add_to_puzzle ('_':xs) c = c:xs
add_to_puzzle (x:xs)   c = x : (add_to_puzzle xs c)

-- gets the subset of '1'..'9' that can be used to replace
-- the first '_' This involves finding the position for that
-- first '_', getting all the tiles for that group, column
-- and row and then doing a set difference to get only the
-- ones that are not defined. 
valid_candidates :: Puzzle -> [Char]
valid_candidates puzzle =
  let
    pos = position_of '_' puzzle
    row_t = row_tiles pos puzzle
    col_t = col_tiles pos puzzle
    grp_t = grp_tiles pos puzzle
  in
    "123456789" \\ (row_t ++ col_t ++ grp_t)

filter_by_first :: Int -> [(Int, Char)] -> [Char]
filter_by_first val zipped_puzzle = map snd $ filter (\v -> (fst v) == val) zipped_puzzle

-- returns the index of the first occurence of specified value in the array
position_of :: Char -> [Char] -> Int
position_of x arr =
  let
    arr_with_pos = zip [0..80] arr
    only_x = filter (\v -> (snd v) == x) arr_with_pos
  in
    if only_x == [] then -1 else fst (head only_x)

-- return the tiles belonging to the same row as position p
row_labels = map (`div` 9) [0..80]
row_tiles :: Int -> Puzzle -> [Char]
row_tiles pos puzzle = filter_by_first (row_labels !! pos) (zip row_labels puzzle)

-- returns the tiles belonging to the same columns as position p
col_labels = map (`mod` 9) [0..80]
col_tiles :: Int -> Puzzle -> [Char]
col_tiles pos puzzle = filter_by_first (col_labels !! pos) (zip col_labels puzzle)

-- returns the tiles belonging to the same group/square as position p
grp_func :: Int -> Int
grp_func pos =
  let
    r = row_labels !! pos
    c = col_labels !! pos
  in
    3 * (r `div` 3) + (c `div` 3)

grp_labels = map grp_func [0..80]
grp_tiles :: Int -> Puzzle -> [Char]
grp_tiles pos puzzle = filter_by_first (grp_labels !! pos) (zip grp_labels puzzle)

flatmap :: (a -> [b]) -> [a] -> [b]
flatmap func [] = []
flatmap func (x:xs) = func x ++ flatmap func xs

cleanup :: String -> Puzzle
cleanup str = filter (`elem` "123456789_") str

prettify :: Puzzle -> String
prettify puzzle = prettify_with_index $ zip [0..80] puzzle

prettify_with_index [] = "\n"
prettify_with_index (x:xs) 
  | (fst x) `mod` 9 == 0 = '\n' : (snd x) : (prettify_with_index xs)
  | otherwise            = (snd x) : (prettify_with_index xs)

main = do
    args <- getArgs
    input_puzzle <- readFile (head args)
    putStrLn input_puzzle
    putStrLn "---"
    putStrLn $  prettify $ solve [ ( cleanup input_puzzle ) ]
