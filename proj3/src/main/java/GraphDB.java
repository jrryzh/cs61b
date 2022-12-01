import org.xml.sax.SAXException;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import java.util.*;

/**
 * Graph for storing all of the intersection (vertex) and road (edge) information.
 * Uses your GraphBuildingHandler to convert the XML files into a graph. Your
 * code must include the vertices, adjacent, distance, closest, lat, and lon
 * methods. You'll also need to include instance variables and methods for
 * modifying the graph (e.g. addNode and addEdge).
 *
 * @author Alan Yao, Josh Hug
 */
public class GraphDB {
    /**
     * Your instance variables for storing the graph. You should consider
     * creating helper classes, e.g. Node, Edge, etc.
     */
    Map<Long, Node> nodeIdMap = new HashMap<>();
    Map<Long, Way> wayIdMap = new HashMap<>();
    Map<Long, List<Long>> nodeAdjMap = new HashMap<>();

    public static class Node {
        long id;
        Set<Long> wayIdSet;
        double lat, lon;
        String name;

        public Node(long id, double lat, double lon) {
            this.id = id;
            this.lat = lat;
            this.lon = lon;
            this.wayIdSet = new HashSet<>();
        }

        public void addName(String name) {
            this.name = name;
        }

        public void addWayId(long wayId) {
            this.wayIdSet.add(wayId);
        }
    }

    public static class Edge {
        long srcNodeId, dstNodeId;

        public Edge(long srcNodeId, long dstNodeId) {
            this.srcNodeId = srcNodeId;
            this.dstNodeId = dstNodeId;
        }
    }

    public static class Way {
        List<Edge> edgeList;
        long id;
        long currentNodeId, previousNodeId;
        String name, highway, maxspeed;

        public Way(long id) {
            this.edgeList = new LinkedList<>();
            this.id = id;
        }

        public void setName(String name) {
            this.name = name;
        }

        public void setHighway(String highway) {
            this.highway = highway;
        }

        public void setMaxspeed(String maxspeed) {
            this.maxspeed = maxspeed;
        }

        public void addConnection() {
            if (!(previousNodeId == 0)) {
                edgeList.add(new Edge(previousNodeId, currentNodeId));
            }
        }
    }


    /**
     * Example constructor shows how to create and start an XML parser.
     * You do not need to modify this constructor, but you're welcome to do so.
     *
     * @param dbPath Path to the XML file to be parsed.
     */
    public GraphDB(String dbPath) {
        try {
            File inputFile = new File(dbPath);
            FileInputStream inputStream = new FileInputStream(inputFile);
            // GZIPInputStream stream = new GZIPInputStream(inputStream);

            SAXParserFactory factory = SAXParserFactory.newInstance();
            SAXParser saxParser = factory.newSAXParser();
            GraphBuildingHandler gbh = new GraphBuildingHandler(this);
            saxParser.parse(inputStream, gbh);
        } catch (ParserConfigurationException | SAXException | IOException e) {
            e.printStackTrace();
        }
        clean();
    }

    public void addNodeInAdjMap(long nodeId) {
        nodeAdjMap.put(nodeId, new LinkedList<>());
    }

    /**
     * Helper to process strings into their "cleaned" form, ignoring punctuation and capitalization.
     *
     * @param s Input string.
     * @return Cleaned string.
     */
    static String cleanString(String s) {
        return s.replaceAll("[^a-zA-Z ]", "").toLowerCase();
    }

    /**
     * Remove nodes with no connections from the graph.
     * While this does not guarantee that any two nodes in the remaining graph are connected,
     * we can reasonably assume this since typically roads are connected.
     */
    private void clean() {
        // TODO: Your code here.
        List<Long> cleanList = new LinkedList<>();
        for (long nodeId : nodeIdMap.keySet()) {
            if (nodeAdjMap.get(nodeId).size() == 0) {
                cleanList.add(nodeId);
            }
        }
        for (long nodeId : cleanList) {
            nodeIdMap.remove(nodeId);
            nodeAdjMap.remove(nodeId);
        }
    }

    /**
     * Returns an iterable of all vertex IDs in the graph.
     *
     * @return An iterable of id's of all vertices in the graph.
     */
    Iterable<Long> vertices() {
        //YOUR CODE HERE, this currently returns only an empty list.
        return new ArrayList<>(nodeIdMap.keySet());
    }

    /**
     * Returns ids of all vertices adjacent to v.
     *
     * @param v The id of the vertex we are looking adjacent to.
     * @return An iterable of the ids of the neighbors of v.
     */
    Iterable<Long> adjacent(long v) {
        return new ArrayList<>(nodeAdjMap.get(v));
    }

    /**
     * Returns the great-circle distance between vertices v and w in miles.
     * Assumes the lon/lat methods are implemented properly.
     * <a href="https://www.movable-type.co.uk/scripts/latlong.html">Source</a>.
     *
     * @param v The id of the first vertex.
     * @param w The id of the second vertex.
     * @return The great-circle distance between the two locations from the graph.
     */
    double distance(long v, long w) {
        return distance(lon(v), lat(v), lon(w), lat(w));
    }

    static double distance(double lonV, double latV, double lonW, double latW) {
        double phi1 = Math.toRadians(latV);
        double phi2 = Math.toRadians(latW);
        double dphi = Math.toRadians(latW - latV);
        double dlambda = Math.toRadians(lonW - lonV);

        double a = Math.sin(dphi / 2.0) * Math.sin(dphi / 2.0);
        a += Math.cos(phi1) * Math.cos(phi2) * Math.sin(dlambda / 2.0) * Math.sin(dlambda / 2.0);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));

        if (c < 0) {
            System.out.println("Distance Error");
        }

        return 3963 * c;
    }

    /**
     * Returns the initial bearing (angle) between vertices v and w in degrees.
     * The initial bearing is the angle that, if followed in a straight line
     * along a great-circle arc from the starting point, would take you to the
     * end point.
     * Assumes the lon/lat methods are implemented properly.
     * <a href="https://www.movable-type.co.uk/scripts/latlong.html">Source</a>.
     *
     * @param v The id of the first vertex.
     * @param w The id of the second vertex.
     * @return The initial bearing between the vertices.
     */
    double bearing(long v, long w) {
        return bearing(lon(v), lat(v), lon(w), lat(w));
    }

    static double bearing(double lonV, double latV, double lonW, double latW) {
        double phi1 = Math.toRadians(latV);
        double phi2 = Math.toRadians(latW);
        double lambda1 = Math.toRadians(lonV);
        double lambda2 = Math.toRadians(lonW);

        double y = Math.sin(lambda2 - lambda1) * Math.cos(phi2);
        double x = Math.cos(phi1) * Math.sin(phi2);
        x -= Math.sin(phi1) * Math.cos(phi2) * Math.cos(lambda2 - lambda1);
        return Math.toDegrees(Math.atan2(y, x));
    }

    /**
     * Returns the vertex closest to the given longitude and latitude.
     *
     * @param lon The target longitude.
     * @param lat The target latitude.
     * @return The id of the node in the graph closest to the target.
     */
    long closest(double lon, double lat) {
        long closestId = 0;
        double closestDist = Integer.MAX_VALUE;
        for (long nodeId : nodeIdMap.keySet()) {
            Node node = nodeIdMap.get(nodeId);
            double dist = distance(lon, lat, node.lon, node.lat);
            if (dist < closestDist) {
                closestDist = dist;
                closestId = nodeId;
            }
        }
        return closestId;
    }

    /**
     * Gets the longitude of a vertex.
     *
     * @param v The id of the vertex.
     * @return The longitude of the vertex.
     */
    double lon(long v) {
        return nodeIdMap.get(v).lon;
    }

    /**
     * Gets the latitude of a vertex.
     *
     * @param v The id of the vertex.
     * @return The latitude of the vertex.
     */
    double lat(long v) {
        return nodeIdMap.get(v).lat;
    }
}
