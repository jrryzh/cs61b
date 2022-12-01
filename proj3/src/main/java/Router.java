import edu.princeton.cs.algs4.MinPQ;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * This class provides a shortestPath method for finding routes between two points
 * on the map. Start by using Dijkstra's, and if your code isn't fast enough for your
 * satisfaction (or the autograder), upgrade your implementation by switching it to A*.
 * Your code will probably not be fast enough to pass the autograder unless you use A*.
 * The difference between A* and Dijkstra's is only a couple of lines of code, and boils
 * down to the priority you use to order your vertices.
 */
public class Router {
    /**
     * Return a List of longs representing the shortest path from the node
     * closest to a start location and the node closest to the destination
     * location.
     * @param g The graph to use.
     * @param stlon The longitude of the start location.
     * @param stlat The latitude of the start location.
     * @param destlon The longitude of the destination location.
     * @param destlat The latitude of the destination location.
     * @return A list of node id's in the order visited on the shortest path.
     */
    public static List<Long> shortestPath(GraphDB g, double stlon, double stlat,
                                          double destlon, double destlat) {

        List<Long> shortestPath = new LinkedList<>();
        long stNodeId = g.closest(stlon, stlat);
        long destNodeId = g.closest(destlon, destlat);
        Map<Long, Double> checkedMap = new HashMap<>();

        // Initial the fringe
        PathNode stNode = new PathNode(stNodeId, 0, g.distance(stNodeId, destNodeId), null);
        PathNode destNode = null;
        MinPQ<PathNode> fringe = new MinPQ<>(new NodeComparator());
        fringe.insert(stNode);
        checkedMap.put(stNodeId, 0.0);

        // start the searching
        while (!fringe.isEmpty()) {
            PathNode currentNode = fringe.delMin();
            if (currentNode.nodeId == destNodeId) {
                destNode = currentNode;
                break;
            }
            for (long nodeId : g.adjacent(currentNode.nodeId)) {
                if (!checkedMap.containsKey(nodeId)) {
                    PathNode nextNode = new PathNode(nodeId, currentNode.distTo + g.distance(currentNode.nodeId, nodeId), g.distance(nodeId, destNodeId), currentNode);
                    fringe.insert(nextNode);
                    checkedMap.put(nodeId, currentNode.distTo + g.distance(currentNode.nodeId, nodeId));
                } else if (checkedMap.get(nodeId) > currentNode.distTo + g.distance(currentNode.nodeId, nodeId)) {
                    PathNode nextNode = new PathNode(nodeId, currentNode.distTo + g.distance(currentNode.nodeId, nodeId), g.distance(nodeId, destNodeId), currentNode);
                    fringe.insert(nextNode);
                    checkedMap.replace(nodeId, currentNode.distTo + g.distance(currentNode.nodeId, nodeId));
                }
            }
        }

        // organize the results
        PathNode currentNode = destNode;
        while (currentNode.nodeId != stNodeId) {
            shortestPath.add(0, currentNode.nodeId);
            currentNode = currentNode.pathTo;
        }
        shortestPath.add(0, currentNode.nodeId);

        return shortestPath;
    }

    private static class PathNode {
        long nodeId;
        double distTo, distH;
        PathNode pathTo;

        public PathNode(long nodeId, double distTo, double distH, PathNode pathTo) {
            this.nodeId = nodeId;
            this.distTo = distTo;
            this.distH = distH;
            this.pathTo = pathTo;
        }
    }

    private static class NodeComparator implements Comparator<PathNode> {

        @Override
        public int compare(PathNode n1, PathNode n2) {
            if ((n1.distTo + n1.distH) - (n2.distTo + n2.distH) > 0) {
                return 1;
            } else if ((n1.distTo + n1.distH) - (n2.distTo + n2.distH) == 0) {
                return 0;
            } else {
                return -1;
            }
        }
    }

    /**
     * Create the list of directions corresponding to a route on the graph.
     * @param g The graph to use.
     * @param route The route to translate into directions. Each element
     *              corresponds to a node from the graph in the route.
     * @return A list of NavigatiionDirection objects corresponding to the input
     * route.
     */
    public static List<NavigationDirection> routeDirections(GraphDB g, List<Long> route) {
        List<NavigationDirection> resList = new ArrayList<>();
        Long prevNodeId, curNodeId;

        NavigationDirection currentND = new NavigationDirection();
        currentND.direction = NavigationDirection.START;
        // 此时way为 wayname 或 UNKNOWN
        currentND.way = wayNameHelper(g, route.get(0), route.get(1));
        currentND.distance += g.distance(route.get(0), route.get(1));

        for (int i = 1, j = 2; j < route.size(); i += 1, j += 1) {
            prevNodeId = route.get(i);
            curNodeId = route.get(j);

            if (wayNameHelper(g, prevNodeId, curNodeId).equals(currentND.way)) {
                currentND.distance += g.distance(prevNodeId, curNodeId);
            } else {
                resList.add(currentND);
                currentND = new NavigationDirection();
                double prevBearing = g.bearing(route.get(i - 1), prevNodeId);
                double curBearing = g.bearing(prevNodeId, curNodeId);
                currentND.direction = directionHelper(g, prevBearing, curBearing);
                currentND.distance += g.distance(prevNodeId, curNodeId);
                currentND.way = wayNameHelper(g, prevNodeId, curNodeId);
            }
        }
        resList.add(currentND);
        return resList;
    }

    private static Long wayIdHelper(GraphDB g, long node1, long node2) {
        for (long id1 : g.nodeIdMap.get(node1).wayIdSet) {
            for (long id2 : g.nodeIdMap.get(node2).wayIdSet) {
                if (id1 == id2) {
                    return id1;
                }
            }
        }
        return null;
    }

    private static String wayNameHelper(GraphDB g, long node1, long node2) {
        Long wayId = wayIdHelper(g, node1, node2);
        String wayName = g.wayIdMap.get(wayId).name;
        return Objects.requireNonNullElse(wayName, "");
    }

    private static int directionHelper(GraphDB g, double prevBearing, double curBearing) {
        double relativeBearing = curBearing - prevBearing;
        if (relativeBearing > 180) {
            relativeBearing -= 360;
        } else if (relativeBearing < -180) {
            relativeBearing += 360;
        }

        if (relativeBearing < -100) {
            return NavigationDirection.SHARP_LEFT;
        } else if (relativeBearing < -30) {
            return NavigationDirection.LEFT;
        } else if (relativeBearing < -15) {
            return NavigationDirection.SLIGHT_LEFT;
        } else if (relativeBearing < 15) {
            return NavigationDirection.STRAIGHT;
        } else if (relativeBearing < 30) {
            return NavigationDirection.SLIGHT_RIGHT;
        } else if (relativeBearing < 100) {
            return NavigationDirection.RIGHT;
        } else {
            return NavigationDirection.SHARP_RIGHT;
        }
    }


    /**
     * Class to represent a navigation direction, which consists of 3 attributes:
     * a direction to go, a way, and the distance to travel for.
     */
    public static class NavigationDirection {

        /** Integer constants representing directions. */
        public static final int START = 0;
        public static final int STRAIGHT = 1;
        public static final int SLIGHT_LEFT = 2;
        public static final int SLIGHT_RIGHT = 3;
        public static final int RIGHT = 4;
        public static final int LEFT = 5;
        public static final int SHARP_LEFT = 6;
        public static final int SHARP_RIGHT = 7;

        /** Number of directions supported. */
        public static final int NUM_DIRECTIONS = 8;

        /** A mapping of integer values to directions.*/
        public static final String[] DIRECTIONS = new String[NUM_DIRECTIONS];

        /** Default name for an unknown way. */
        public static final String UNKNOWN_ROAD = "unknown road";
        
        /** Static initializer. */
        static {
            DIRECTIONS[START] = "Start";
            DIRECTIONS[STRAIGHT] = "Go straight";
            DIRECTIONS[SLIGHT_LEFT] = "Slight left";
            DIRECTIONS[SLIGHT_RIGHT] = "Slight right";
            DIRECTIONS[LEFT] = "Turn left";
            DIRECTIONS[RIGHT] = "Turn right";
            DIRECTIONS[SHARP_LEFT] = "Sharp left";
            DIRECTIONS[SHARP_RIGHT] = "Sharp right";
        }

        /** The direction a given NavigationDirection represents.*/
        int direction;
        /** The name of the way I represent. */
        String way;
        /** The distance along this way I represent. */
        double distance;

        /**
         * Create a default, anonymous NavigationDirection.
         */
        public NavigationDirection() {
            this.direction = STRAIGHT;
            this.way = UNKNOWN_ROAD;
            this.distance = 0.0;
        }

        public String toString() {
            return String.format("%s on %s and continue for %.3f miles.",
                    DIRECTIONS[direction], way, distance);
        }

        /**
         * Takes the string representation of a navigation direction and converts it into
         * a Navigation Direction object.
         * @param dirAsString The string representation of the NavigationDirection.
         * @return A NavigationDirection object representing the input string.
         */
        public static NavigationDirection fromString(String dirAsString) {
            String regex = "([a-zA-Z\\s]+) on ([\\w\\s]*) and continue for ([0-9\\.]+) miles\\.";
            Pattern p = Pattern.compile(regex);
            Matcher m = p.matcher(dirAsString);
            NavigationDirection nd = new NavigationDirection();
            if (m.matches()) {
                String direction = m.group(1);
                if (direction.equals("Start")) {
                    nd.direction = NavigationDirection.START;
                } else if (direction.equals("Go straight")) {
                    nd.direction = NavigationDirection.STRAIGHT;
                } else if (direction.equals("Slight left")) {
                    nd.direction = NavigationDirection.SLIGHT_LEFT;
                } else if (direction.equals("Slight right")) {
                    nd.direction = NavigationDirection.SLIGHT_RIGHT;
                } else if (direction.equals("Turn right")) {
                    nd.direction = NavigationDirection.RIGHT;
                } else if (direction.equals("Turn left")) {
                    nd.direction = NavigationDirection.LEFT;
                } else if (direction.equals("Sharp left")) {
                    nd.direction = NavigationDirection.SHARP_LEFT;
                } else if (direction.equals("Sharp right")) {
                    nd.direction = NavigationDirection.SHARP_RIGHT;
                } else {
                    return null;
                }

                nd.way = m.group(2);
                try {
                    nd.distance = Double.parseDouble(m.group(3));
                } catch (NumberFormatException e) {
                    return null;
                }
                return nd;
            } else {
                // not a valid nd
                return null;
            }
        }

        @Override
        public boolean equals(Object o) {
            if (o instanceof NavigationDirection) {
                return direction == ((NavigationDirection) o).direction
                    && way.equals(((NavigationDirection) o).way)
                    && distance == ((NavigationDirection) o).distance;
            }
            return false;
        }

        @Override
        public int hashCode() {
            return Objects.hash(direction, way, distance);
        }
    }
}
