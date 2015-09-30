#!/usr/bin/env python -w
import sys


def row(pos):
    return pos/9


def col(pos):
    return pos % 9


def square(pos):
    row_g = row(pos)/3
    col_g = col(pos)/3
    return 10*row_g + col_g


def rcg(pos):
    return [row(pos), col(pos), square(pos)]

IDX = [ rcg(i) for i in range(0,81)]


def load_puzzle(fname):
    """Reads file and returns the array representation of the puzzle"""
    with open(fname,"r") as puzzle_file:
        puzzle_str = puzzle_file.read().replace('\n','')
        puzzle = list(puzzle_str)
    return puzzle


def expand_puzzles(puzzle_array):
    expanded_puzzles = [ ]
    pos = get_first_empty_position(puzzle_array[0])
    for puzzle in puzzle_array:
        expanded_puzzles += expand_puzzle(pos, puzzle)
    return expanded_puzzles


def get_first_empty_position(puzzle):
    return puzzle.index('_')


def replace_first(char, pos, puzzle):
    # this is the one place where we are not being functional, copying a value and replacing it in place.
    replaced = [] + puzzle
    replaced[pos] = char
    return replaced


def expand_puzzle(pos, puzzle):
    candidates = get_candidate_for_pos(pos, puzzle)
    return [ replace_first(x, pos, puzzle) for x in candidates ]


def get_candidate_for_pos(pos,puzzle):
    idx_puzzle = zip(IDX,puzzle)
    r,c,g = IDX[pos]
    vals =  [ x[1] for x in idx_puzzle if (x[0][0] == r) or (x[0][1] == c) or (x[0][2] == g) ]
    return set( ['1','2','3','4','5','6','7','8','9']) - set(vals)


def solved(puzzle):
    return puzzle.count('_') == 0


def solve( puzzle_array ):
    if solved(puzzle_array[0]):
        return puzzle_array
    print "%d puzzles" % len(puzzle_array)
    return solve( expand_puzzles(puzzle_array))


def print_puzzle( puzzle ):
    idx = 0
    for i in range(0,9):
        for j in range(0,9):
            print puzzle[idx],
            idx += 1
        print

def solve_puzzle(puzzle_name):
    puzzle = load_puzzle(puzzle_name)
    solutions = solve( [ puzzle ] )
    for solution in solutions:
        print_puzzle(solution)

if __name__ == "__main__":
    solve_puzzle(sys.argv[1])


