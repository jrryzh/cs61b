package byog.Core;

import byog.TileEngine.TETile;
import byog.TileEngine.Tileset;

import java.io.Serializable;
import java.util.Random;

public class Player implements Serializable {
    // 初始化位置
    Position pos;

    // 随即初始化constructor
    public Player(TETile[][] world, long seed) {
        Random random = new Random(seed);
        int width = world.length;
        int height = world[0].length;

        while (true) {
            Position p = new Position(random.nextInt(width), random.nextInt(height));
            if (world[p.x][p.y] == Tileset.FLOOR) {
                pos = p;
                break;
            }
        }
        world[pos.x][pos.y] = Tileset.PLAYER;
    }

    // 加载constructor
    public Player(TETile[][] world, Position position) {
        pos = position;
        // 这个应该是重复了
        world[pos.x][pos.y] = Tileset.PLAYER;
    }

    // 根据指令移动
    public void move(TETile[][] world, char step) {
        switch (step) {
            case 'w':
                this.move(world, 0, 1);
                break;
            case 's':
                this.move(world, 0, -1);
                break;
            case 'a':
                this.move(world, -1, 0);
                break;
            case 'd':
                this.move(world, 1, 0);
                break;
            default:
                break;
        }
    }

    // 真实移动
    public void move(TETile[][] world, int dx, int dy) {
        if (Position.isInMap(world, pos.x + dx, pos.y + dy) && world[pos.x + dx][pos.y + dy].description().equals("floor")) {
            world[pos.x][pos.y] = Tileset.FLOOR;
            pos = pos.move(dx, dy);
            world[pos.x][pos.y] = Tileset.PLAYER;
        }
    }

}
