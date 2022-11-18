package lab11.graphs;

import edu.princeton.cs.algs4.In;

import java.util.Comparator;
import java.util.PriorityQueue;

/**
 *  @author Josh Hug
 */
public class MazeAStarPath extends MazeExplorer {
    private int s;
    private int t;
    private boolean targetFound = false;
    private Maze maze;

    public MazeAStarPath(Maze m, int sourceX, int sourceY, int targetX, int targetY) {
        super(m);
        maze = m;
        s = maze.xyTo1D(sourceX, sourceY);
        t = maze.xyTo1D(targetX, targetY);
        distTo[s] = 0;
        edgeTo[s] = s;
    }

    /** Estimate of the distance from v to the target. */
    private int h(int v) {
        int sourceX = maze.toX(v);
        int sourceY = maze.toY(v);
        int targetX = maze.toX(t);
        int targetY = maze.toY(t);
        return Math.abs(sourceX - targetX) + Math.abs(sourceY - targetY);
    }

    /** Finds vertex estimated to be closest to target. */
    private int findMinimumUnmarked() {
        return -1;
        /* You do not have to use this method. */
    }

    /** Performs an A star search from vertex s. */
    private void astar(int s) {
        // 初始化 distTo (edgeTo无需初始化）
//        for (int i = 0; i < maze.V(); i += 1) {
//            distTo[i] = 10000;
//        }
//        distTo[s] = 0;
//        edgeTo[s] = 0;
        // 初始化 fringe
        PriorityQueue<Integer> fringe = new PriorityQueue<>(new CustomComparator());
//        for (int i = 0; i < maze.V(); i += 1) {
//            fringe.add(i);
//        }
        fringe.add(s);
        // 开始算法
        announce();
        while (!fringe.isEmpty()) {
            int v = fringe.remove();
            marked[v] = true;
            announce();

            if (v == t) {
                targetFound = true;
                return;
            }

            for (int w : maze.adj(v)) {
                if (!marked[w] && distTo[v] + 1 < distTo[w]) {
                    edgeTo[w] = v;
                    distTo[w] = distTo[v] + 1;
                    // 临时添加即可 因为发现没有被更改distTo的节点（即dist为无穷大）也不会被remove出来
                    // 反而开始就全部添加到fringe会导致后期无法更新priority queue
                    fringe.add(w);
                    announce();
                }
            }
        }
    }

    class CustomComparator implements Comparator<Integer> {
        @Override
        public int compare(Integer number1, Integer number2) {
            return (distTo[number1] + h(number1)) - (distTo[number2] + h(number2));
        }
    }

    @Override
    public void solve() {
        astar(s);
    }

}

