// Copyright(c) 2016 Target Corporation
package com.kwh;

import java.util.*;

/**
 * Created by alexandre.santoro@target.com.
 */
public class Puzzle {

    private static int[] rowLabels = new int[81];
    private static int[] colLabels = new int[81];
    private static int[] grpLabels = new int[81];

    private static Set<Character> tileSet = new HashSet<>();

    static {
        for (int i=0; i<81; i++) {
            rowLabels[i] = i / 9;
            colLabels[i] = i % 9;
            grpLabels[i] = 3 * (rowLabels[i]/3) + (colLabels[i]/3);
        }
        tileSet.add('1');
        tileSet.add('2');
        tileSet.add('3');
        tileSet.add('4');
        tileSet.add('5');
        tileSet.add('6');
        tileSet.add('7');
        tileSet.add('8');
        tileSet.add('9');
    }

    private char[] thePuzzle;

    public Puzzle(char[] chars) {
        thePuzzle = chars;
    }

    /**
     * defines if puzzle is solved
     * @return
     */
    public boolean solved() {
        for (char c: thePuzzle) {
            if (c == '_') {
                return false;
            }
        }
        return true;
    }

    /**
     * Creates an expanded set of the Puzzle, or returns an empty
     * list if no expansion is possible. Expansion means creating
     * a set of up to nine new puzzles where the first empty tile
     * ('_') is replaced by the values '1'...'9'. Only valid expansions
     * are returned.
     *
     * @return
     */
    public List<? extends Puzzle> expand() {
        int pos = firstEmptyPos();
        Set<Character> feasibleChars = getFeasibleChars(pos);
        List<Puzzle> expandedPuzzles = new LinkedList<>();
        for (Character c: feasibleChars) {
            expandedPuzzles.add ( new Puzzle( copyAndReplace(thePuzzle, pos, c)));
        }
        return expandedPuzzles;
    }

    public String toString() {
        StringBuilder b = new StringBuilder();
        for (int i=0; i<9; i++) {
            for (int j=0; j<9; j++) {
                b.append(thePuzzle[9*i+j]);
            }
            b.append('\n');
        }
        return b.toString();
    }

    private int firstEmptyPos() {
        for (int i = 0; i<81; i++) {
            if (thePuzzle[i] == '_') {
                return i;
            }
        }
        return -1;
    }

    /**
     * Gets the list of feasible tiles. This would be the set of chars '1' .. '9'
     * less the ones that already appear in the group, column or row for that
     * position.
     *
     * @param pos the position for which we want the group, row and column tiles
     * @return a (potentially empty) set of available tiles.
     */
    private Set<Character> getFeasibleChars(int pos) {
        Set<Character> candidates = new HashSet<>();
        candidates.addAll(tileSet);
        for (int i=0; i<81; i++) {
            if ( (rowLabels[i] == rowLabels[pos])
                    || (colLabels[i] == colLabels[pos])
                    || (grpLabels[i] == grpLabels[pos])){
                candidates.remove( thePuzzle[i]);
            }
        }
        return candidates;
    }

    private char[] copyAndReplace(char[] thePuzzle, int pos, Character c) {
        char[] newPuzzle = new char[81];
        System.arraycopy(thePuzzle,0,newPuzzle,0,81);
        newPuzzle[pos] = c;
        return newPuzzle;
    }

}
