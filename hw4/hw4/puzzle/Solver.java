package hw4.puzzle;

import edu.princeton.cs.algs4.MinPQ;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;

public class Solver {

    private SearchNode initialSN;
    private SearchNode endSN;

    /**
     * Solver(initial): Constructor which solves the puzzle, computing
     everything necessary for moves() and solution() to
     not have to solve the problem again. Solves the
     puzzle using the A* algorithm. Assumes a solution exists.
     */
    public Solver(WorldState initial) {

        MinPQ<SearchNode> fringe = new MinPQ<>(new WordComparator());
        initialSN = new SearchNode(initial, 0, null);
        fringe.insert(initialSN);

        while (!fringe.isEmpty()) {
            SearchNode currentSN = fringe.delMin();
            if (currentSN.worldState.isGoal()) {
                endSN = currentSN;
                return;
            } else {
                for (WorldState nextWS: currentSN.worldState.neighbors()) {
                    if (currentSN.equals(initialSN) || !nextWS.equals(currentSN.previousSN.worldState)) {
                        SearchNode newSN = new SearchNode(nextWS, currentSN.numberOfMoves + 1, currentSN);
                        fringe.insert(newSN);
                    }
                }
            }
        }

    }

    /**
     * moves(): Returns the minimum number of moves to solve the puzzle starting
     at the initial WorldState.
     */
    public int moves() {
        return endSN.numberOfMoves;
    }

    /**
     * solution():      Returns a sequence of WorldStates from the initial WorldState
     to the solution.
     */
    public Iterable<WorldState> solution() {
        ArrayList<WorldState> sol = new ArrayList<>();
        SearchNode sn = endSN;
        while (!sn.equals(initialSN)) {
            sol.add(0, sn.worldState);
            sn = sn.previousSN;
        }
        sol.add(0, sn.worldState);
        return sol;
    }

    private class SearchNode {
        WorldState worldState;
        int numberOfMoves;
        SearchNode previousSN;

        public SearchNode (WorldState worldState, int numberOfMoves, SearchNode previousSN) {
            this.worldState = worldState;
            this.numberOfMoves = numberOfMoves;
            this.previousSN = previousSN;
        }
    }

    private static class WordComparator implements Comparator<SearchNode> {
        static HashMap<SearchNode, Integer> estimatedDTGSet = new HashMap<>();

        @Override
        public int compare(SearchNode SN1, SearchNode SN2) {
            int estimatedDTG1, estimatedDTG2;
            if (estimatedDTGSet.containsKey(SN1)) {
                estimatedDTG1 = estimatedDTGSet.get(SN1);
            } else {
                estimatedDTG1 = SN1.worldState.estimatedDistanceToGoal();
                estimatedDTGSet.put(SN1, estimatedDTG1);
            }
            if (estimatedDTGSet.containsKey(SN2)) {
                estimatedDTG2 = estimatedDTGSet.get(SN2);
            } else {
                estimatedDTG2 = SN2.worldState.estimatedDistanceToGoal();
                estimatedDTGSet.put(SN2, estimatedDTG2);
            }
            return (SN1.numberOfMoves + estimatedDTG1) - (SN2.numberOfMoves + estimatedDTG2);
        }
    }
}
