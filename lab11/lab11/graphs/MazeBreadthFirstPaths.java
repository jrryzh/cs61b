package lab11.graphs;

import edu.princeton.cs.algs4.In;

import java.util.LinkedList;
import java.util.Queue;

/**
 *  @author Josh Hug
 */
public class MazeBreadthFirstPaths extends MazeExplorer {
    /* Inherits public fields:
    public int[] distTo;
    public int[] edgeTo;
    public boolean[] marked;
    */
    private int s;
    private int t;
    private Maze maze;

    public MazeBreadthFirstPaths(Maze m, int sourceX, int sourceY, int targetX, int targetY) {
        super(m);
        maze = m;
        s = maze.xyTo1D(sourceX, sourceY);
        t = maze.xyTo1D(targetX, targetY);
        distTo[s] = 0;
        edgeTo[s] = s;
        // Add more variables here!
    }

    /** Conducts a breadth first search of the maze starting at the source. */
    private void bfs() {
        // TODO: Your code here. Don't forget to update distTo, edgeTo, and marked, as well as call announce()
        marked[s] = true;
        announce();

        Queue<Integer> fringe = new LinkedList<>();
        fringe.add(s);
        while (!fringe.isEmpty()) {
            int v = fringe.remove();

            for (int v0: maze.adj(v)) {
                if (!marked[v0]) {
                    marked[v0] = true;
                    edgeTo[v0] = v;
                    distTo[v0] = distTo[v] + 1;
                    announce();
                    if (v0 == t) {
                        return;
                    }
                    fringe.add(v0);
                }
            }
        }
    }


    @Override
    public void solve() {
         bfs();
    }
}

