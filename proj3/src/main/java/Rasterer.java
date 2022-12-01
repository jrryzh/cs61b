import java.util.HashMap;
import java.util.Map;

/**
 * This class provides all code necessary to take a query box and produce
 * a query result. The getMapRaster method must return a Map containing all
 * seven of the required fields, otherwise the front end code will probably
 * not draw the output correctly.
 */
public class Rasterer {

    private double rootLrlat = MapServer.ROOT_LRLAT;
    private double rootLrlon = MapServer.ROOT_LRLON;
    private double rootUllon = MapServer.ROOT_ULLON;
    private double rootUllat = MapServer.ROOT_ULLAT;

    public Rasterer() {
        // YOUR CODE HERE
    }

    /**
     * Takes a user query and finds the grid of images that best matches the query. These
     * images will be combined into one big image (rastered) by the front end. <br>
     * <p>
     * The grid of images must obey the following properties, where image in the
     * grid is referred to as a "tile".
     * <ul>
     *     <li>The tiles collected must cover the most longitudinal distance per pixel
     *     (LonDPP) possible, while still covering less than or equal to the amount of
     *     longitudinal distance per pixel in the query box for the user viewport size. </li>
     *     <li>Contains all tiles that intersect the query bounding box that fulfill the
     *     above condition.</li>
     *     <li>The tiles must be arranged in-order to reconstruct the full image.</li>
     * </ul>
     *
     * @param params Map of the HTTP GET request's query parameters - the query box and
     *               the user viewport width and height.
     * @return A map of results for the front end as specified: <br>
     * "render_grid"   : String[][], the files to display. <br>
     * "raster_ul_lon" : Number, the bounding upper left longitude of the rastered image. <br>
     * "raster_ul_lat" : Number, the bounding upper left latitude of the rastered image. <br>
     * "raster_lr_lon" : Number, the bounding lower right longitude of the rastered image. <br>
     * "raster_lr_lat" : Number, the bounding lower right latitude of the rastered image. <br>
     * "depth"         : Number, the depth of the nodes of the rastered image <br>
     * "query_success" : Boolean, whether the query was able to successfully complete; don't
     * forget to set this to true on success! <br>
     */
    public Map<String, Object> getMapRaster(Map<String, Double> params) {
        // System.out.println(params);
        String[][] render_grid = new String[0][];
        double raster_ul_lon = 0;
        double raster_ul_lat = 0;
        double raster_lr_lon = 0;
        double raster_lr_lat = 0;
        int depth;
        boolean query_success = true;

        double lrlon = params.get("lrlon");
        double ullon = params.get("ullon");
        double ullat = params.get("ullat");
        double lrlat = params.get("lrlat");
        double w = params.get("w");

        // box有意义（左上 && 右下）
        if (!(ullat > lrlat && ullon < lrlon)) {
            query_success = false;
        }
        // 检查box跟rootBox的关系
        // 要么box左上角或右下角至少一点在root中；要么左上角在root左上角左上方、右下角在root右下角右下方（暂时不写）
        if (!(inRootBounds(lrlon, lrlat) || inRootBounds(ullon, ullat))) {
            query_success = false;
        }

        double expectedLonDPP = (lrlon - ullon) / w;
        depth = getDepth(expectedLonDPP);

        if (query_success) {
            double unitLon = (rootLrlon - rootUllon) / Math.pow(2, depth);
            double unitLat = (rootUllat - rootLrlat) / Math.pow(2, depth);

            // get grid ready
            int ulx = (int) ((ullon - rootUllon) / unitLon);
            int uly = (int) (-(ullat - rootUllat) / unitLat);
            int lrx = (int) ((lrlon - rootUllon) / unitLon);
            int lry = (int) (-(lrlat - rootUllat) / unitLat);

            render_grid = new String[lry - uly + 1][lrx - ulx + 1];
            for (int x = ulx; x <= lrx; x++) {
                for (int y = uly; y <= lry; y++) {
                    render_grid[y - uly][x - ulx] = "d" + depth + "_x" + x + "_y" + y + ".png";
                }
            }

            // get raster data ready
            raster_ul_lon = rootUllon + ulx * unitLon;
            raster_ul_lat = rootUllat - uly * unitLat;
            raster_lr_lon = rootUllon + (lrx + 1) * unitLon;
            raster_lr_lat = rootUllat - (lry + 1) * unitLat;
        }

        // put results together
        Map<String, Object> results = new HashMap<>();

        results.put("render_grid", render_grid);
        results.put("raster_ul_lon", raster_ul_lon);
        results.put("raster_ul_lat", raster_ul_lat);
        results.put("raster_lr_lon", raster_lr_lon);
        results.put("raster_lr_lat", raster_lr_lat);
        results.put("depth", depth);
        results.put("query_success", query_success);

        return results;
    }

    private int getDepth(double expectedLonDPP) {
        int depth = 0;
        double rootUllon = MapServer.ROOT_ULLON;
        double rootLrlon = MapServer.ROOT_LRLON;
        int tileSize = MapServer.TILE_SIZE;
        while ((rootLrlon - rootUllon) / Math.pow(2, depth) / tileSize > expectedLonDPP && depth < 7) {
            depth += 1;
        }
        return depth;
    }

    public boolean inRootBounds(double lon, double lat) {
        return rootUllat > lat && lat > rootLrlat && rootUllon < lon && lon < rootLrlon;
    }
}
