package byog.Core;

import byog.TileEngine.TETile;
import byog.TileEngine.Tileset;

public class PartsUtils {
    public static void makeRoom (TETile[][] world, int posX, int posY, int width, int height) {
        // room floor 生成
        for (int x = posX; x < posX + width; x += 1) {
            for (int y = posY; y < posY + height; y += 1) {
                world[x][y] = Tileset.FLOOR;
            }
        }
        // room wall 生成
        // top + under
        for (int x = posX - 1; x <= posX + width; x += 1) {
            world[x][posY - 1] = Tileset.WALL;
            world[x][posY + height] = Tileset.WALL;
        }
        // left + right
        for (int y = posY - 1; y <= posY + height; y += 1) {
            world[posX - 1][y] = Tileset.WALL;
            world[posX + width][y] = Tileset.WALL;
        }
    }
//
//    public static void main(String[] args) {
//        Game g = new Game();
//        g.playWithInputString("123");
//    }
}
