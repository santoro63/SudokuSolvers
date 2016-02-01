package com.kwh;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.LinkedList;
import java.util.List;

public class Main {

    public static void main(String[] args) throws IOException {

        Puzzle initialPuzzle = getPuzzleFromFile(args[0]);
	    List<Puzzle> currentPuzzles = new LinkedList<>();
        currentPuzzles.add(initialPuzzle);
        while (!currentPuzzles.get(0).solved()) {
            System.out.println("--> " + currentPuzzles.size());
            List<Puzzle> nextPuzzles = new LinkedList<>();
            for (Puzzle p: currentPuzzles) {
                nextPuzzles.addAll( p.expand());
            }
            currentPuzzles = nextPuzzles;
        }
        System.out.println(currentPuzzles.get(0));
    }

    private static Puzzle getPuzzleFromFile(String arg) throws IOException {
        List<String> lines = Files.readAllLines( Paths.get(arg));
        StringBuilder b = new StringBuilder();
        for (String line : lines) {
            b.append(line.substring(0,9));
        }
        return new Puzzle(b.toString().toCharArray());
    }
}
