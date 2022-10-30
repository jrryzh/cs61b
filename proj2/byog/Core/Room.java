package byog.Core;

import byog.TileEngine.TETile;
import byog.TileEngine.Tileset;

import java.io.Serializable;

public class Room implements Comparable<Room>, Serializable {
    int posX, posY, width, height;

    /**
     * Constructor
     *
     * @param posX   起始位置的x坐标
     * @param posY   起始位置的y坐标
     * @param width  整体宽度，包括wall
     * @param height 整体高度，包括wall
     */
    public Room(int posX, int posY, int width, int height) {
        this.posX = posX;
        this.posY = posY;
        this.width = width;
        this.height = height;
    }

    public boolean isOverlap(TETile[][] world) {
        for (int x = posX; x < posX + width; x += 1) {
            for (int y = posY; y < posY + height; y += 1) {
                if (world[x][y].equals(Tileset.FLOOR) || world[x][y].equals(Tileset.WALL)) {
                    return true;
                }
            }
        }
        return false;
    }

    public boolean isInMap(TETile[][] world) {
        int w = world.length;
        int h = world[0].length;
        boolean b1 = 0 <= posX && posX <= w - 1 && 0 <= posY && posY <= h - 1;
        boolean b2 = 0 <= posX + width - 1 && posX + width - 1 <= w - 1
                && 0 <= posY + height - 1 && posY + height - 1 <= h - 1;
        return b1 && b2;
    }

    public void makeFloor(TETile[][] world) {
        for (int x = posX; x < posX + width; x += 1) {
            for (int y = posY; y < posY + height; y += 1) {
                world[x][y] = Tileset.FLOOR;
            }
        }
    }

    public void makeWall(TETile[][] world) {
        // top + under
        for (int x = posX; x < posX + width; x += 1) {
            world[x][posY] = Tileset.WALL;
            world[x][posY + height - 1] = Tileset.WALL;
        }
        // left + right
        for (int y = posY; y < posY + height; y += 1) {
            world[posX][y] = Tileset.WALL;
            world[posX + width - 1][y] = Tileset.WALL;
        }
    }

    @Override
    public int compareTo(Room r) {
        return Integer.compare(this.posX, r.posX);
    }
}
