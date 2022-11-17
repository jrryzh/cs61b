package lab11.graphs;

import edu.princeton.cs.algs4.Stack;
import org.hamcrest.internal.ArrayIterator;

/**
 *  @author Josh Hug
 */
public class MazeCycles extends MazeExplorer {
    /* Inherits public fields:
    public int[] distTo;
    public int[] edgeTo;
    public boolean[] marked;
    */
    private boolean cycleFound = false;
    private int[] backupEdgeTo;
    private Maze maze;

    public MazeCycles(Maze m) {
        super(m);
        maze = m;
        backupEdgeTo = new int[m.V()];
    }

    @Override
    public void solve() {
        // TODO: Your code here!
        backupEdgeTo[0] = 0;
        distTo[0] = 0;
        solveHelper(0);
    }

    // Helper methods go here
    private void solveHelper(int v) {
        marked[v] = true;
        announce();

        for (int w : maze.adj(v)) {
            if (marked[w]) {
                if (backupEdgeTo[v] != w) {
                    backupEdgeTo[w] = v;
                    cycleFound = true;
                    cycleHelper(w);
                    return;
                }
            } else {
                backupEdgeTo[w] = v;
                distTo[w] = distTo[v] + 1;
                solveHelper(w);
                if (cycleFound) {
                    return;
                }
            }
        }
    }

    private void cycleHelper(int v) {
        int pointer = v;
        while (backupEdgeTo[pointer] != v) {
            edgeTo[pointer] = backupEdgeTo[pointer];
            pointer = backupEdgeTo[pointer];
        }
        edgeTo[pointer] = v;
        announce();
    }
}

