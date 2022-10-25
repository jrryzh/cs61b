package byog.Core;

import byog.TileEngine.TETile;

public class Position {
    int x, y;

    public Position(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public Position move(int dx, int dy) {
        return new Position(this.x + dx, this.y + dy);
    }

    public boolean isInMap(TETile[][] world) {
        int w = world.length;
        int h = world[0].length;
        return  0 <= this.x && this.x <= w - 1 && 0 <= this.y && this.y <= h - 1;
    }

    public static boolean isInMap(TETile[][] world, int x, int y) {
        int w = world.length;
        int h = world[0].length;
        return  0 <= x && x <= w - 1 && 0 <= y && y <= h - 1;
    }
}
