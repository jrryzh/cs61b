package byog.Core;

import byog.TileEngine.TETile;
import byog.TileEngine.Tileset;

import java.io.Serializable;
import java.util.Random;

public class Hallway implements Serializable {
    Position startPos, endPos, turnPos;
    String type;
    Random random;

    /**
     * Constructor
     * 其中所有所有xy坐标均为hallway内部的floor的坐标，wall围绕添加即可
     */
    public Hallway(Position startPos, Position endPos, Random random) {
        this.startPos = startPos;
        this.endPos = endPos;
        this.random = random;
        int p = random.nextInt(10);
        if (startPos.x == endPos.x) {
            type = "Vertical";
        } else if (startPos.y == endPos.y) {
            type = "Horizontal";
        } else {
            if (p < 5) {
                type = "VFirst";
                turnPos = new Position(startPos.x, endPos.y);
            } else {
                type = "HFirst";
                turnPos = new Position(endPos.x, startPos.y);
            }
        }
    }

    public void makeHelper(TETile[][] world, Position startP, Position endP) {
        int dx = 0;
        int dy = 0;
        if (startP.x == endP.x) {
            if (startP.y < endP.y) {
                dy = 1;
            } else {
                dy = -1;
            }
        } else if (startP.y == endP.y) {
            if (startP.x < endP.x) {
                dx = 1;
            } else {
                dx = -1;
            }
        }
        for (Position pos = startP; pos.x != endP.x || pos.y != endP.y; pos = pos.move(dx, dy)) {
            world[pos.x][pos.y] = Tileset.FLOOR;
            wallHelper(world, pos);
        }
    }

    public void make(TETile[][] world) {
        if (type.equals("Vertical") || type.equals("Horizontal")) {
            makeHelper(world, startPos, endPos);
        } else if (type.equals("VFirst") || type.equals("HFirst")) {
            makeHelper(world, startPos, turnPos);
            makeHelper(world, turnPos, endPos);
        }
    }

    public void wallHelper(TETile[][] world, Position position) {
        for (int dx = -1; dx <= 1; dx += 1) {
            for (int dy = -1; dy <= 1; dy += 1) {
                if (Position.isInMap(world, position.x + dx, position.y + dy) && world[position.x + dx][position.y + dy] != Tileset.FLOOR) {
                    world[position.x + dx][position.y + dy] = Tileset.WALL;
                }
            }
        }
    }


}
