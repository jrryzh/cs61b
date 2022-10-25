package byog.Core;

import byog.TileEngine.TERenderer;
import byog.TileEngine.TETile;
import byog.TileEngine.Tileset;

import java.util.Arrays;
import java.util.Random;

public class Game {
    TERenderer ter = new TERenderer();
    /* Feel free to change the width and height. */
    public static final int WIDTH = 80;
    public static final int HEIGHT = 30;

    /**
     * Method used for playing a fresh game. The game should start from the main menu.
     */
    public void playWithKeyboard() {
    }

    /**
     * Method used for autograding and testing the game code. The input string will be a series
     * of characters (for example, "n123sswwdasdassadwas", "n123sss:q", "lwww". The game should
     * behave exactly as if the user typed these characters into the game after playing
     * playWithKeyboard. If the string ends in ":q", the same world should be returned as if the
     * string did not end with q. For example "n123sss" and "n123sss:q" should return the same
     * world. However, the behavior is slightly different. After playing with "n123sss:q", the game
     * should save, and thus if we then called playWithInputString with the string "l", we'd expect
     * to get the exact same world back again, since this corresponds to loading the saved game.
     * @param input the input string to feed to your program
     * @return the 2D TETile[][] representing the state of the world
     */
    public TETile[][] playWithInputString(String input) {
        // TODO: Fill out this method to run the game using the input passed in,
        // and return a 2D tile representation of the world that would have been
        // drawn if the same inputs had been given to playWithKeyboard().

        // 暂不处理seed和input到关系
        long SEED = 198;
        Random RANDOM = new Random(SEED);

        ter.initialize(WIDTH, HEIGHT);
        // 初始化
        TETile[][] finalWorldFrame = new TETile[WIDTH][HEIGHT];
        for (int x = 0; x < WIDTH; x += 1) {
            for (int y = 0; y < HEIGHT; y += 1) {
                finalWorldFrame[x][y] = Tileset.NOTHING;
            }
        }
        // 添加房间
        int roomNum = RANDOM.nextInt(40) + 40;
        int roomCounter = 0;
        Room[] roomsList = new Room[roomNum];
        for (int n = 0; n < roomNum; n += 1) {
            Room r = new Room(RANDOM.nextInt(WIDTH), RANDOM.nextInt(HEIGHT), RANDOM.nextInt(10) + 3,
                    RANDOM.nextInt(10) + 3);
            if (r.isInMap(finalWorldFrame) && !r.isOverlap(finalWorldFrame)) {
                roomsList[roomCounter] = r;
                roomCounter += 1;
                r.makeFloor(finalWorldFrame);
                r.makeWall(finalWorldFrame);
            }
        }
        // 房间排序
        Room[] finalRoomsList = new Room[roomCounter];
        for (int i = 0; i <= roomCounter - 1; i += 1) {
            finalRoomsList[i] = roomsList[i];
        }
        Arrays.sort(finalRoomsList);
        // 绘制走廊
        for (int i = 0; i <= roomCounter - 2; i += 1) {
            Room r1 = roomsList[i];
            Room r2 = roomsList[i + 1];
            Position startPos = new Position(r1.posX + RANDOM.nextInt(r1.width), r1.posY + RANDOM.nextInt(r1.height));
            Position endPos = new Position(r2.posX + RANDOM.nextInt(r2.width), r2.posY + RANDOM.nextInt(r2.height));
            Hallway h = new Hallway(startPos, endPos, RANDOM);
            h.makeFloor(finalWorldFrame);
        }


        ter.renderFrame(finalWorldFrame);

        return finalWorldFrame;
    }
}
