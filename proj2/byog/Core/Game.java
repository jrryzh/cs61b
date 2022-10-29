package byog.Core;

import byog.TileEngine.TERenderer;
import byog.TileEngine.TETile;
import byog.TileEngine.Tileset;

//import java.io.*;
import java.io.ObjectOutputStream;
import java.io.ObjectInputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
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
        TETile[][] finalWorldFrame = null;
        char firstChar = input.charAt(0);
        if (firstChar == 'n') {
            finalWorldFrame = startNewGame(input);
        } else if (firstChar == 'l') {
            finalWorldFrame = loadGame();
        } else if (firstChar == 'q') {
            System.exit(0);
        }
        return finalWorldFrame;
    }

    private TETile[][] startNewGame(String input) {
        long seed = getNumberFromInput(input);
        TETile[][] finalWorldFrame = generateWorld(seed);
        return finalWorldFrame;
    }

    private void saveGame(TETile[][] finalWorldFrame) {
        try {
            ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream("save.txt"));
            out.writeObject(finalWorldFrame);
            out.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private TETile[][] loadGame() {
        TETile[][] finalWorldFrame = null;
        try {
            ObjectInputStream in = new ObjectInputStream(new FileInputStream("savefile.txt"));
            finalWorldFrame = (TETile[][]) in.readObject();
            in.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return finalWorldFrame;
    }

    private TETile[][] generateWorld(long seed) {
        Random random = new Random(seed);
        // 创建world
        TETile[][] finalWorldFrame = new TETile[WIDTH][HEIGHT];
        // 初始化
        initWorld(finalWorldFrame);
        // 添加房间并排序
        Room[] RoomsList = addRooms(finalWorldFrame, random);
        // 绘制走廊
        addHallways(finalWorldFrame, RoomsList, random);
        return finalWorldFrame;
    }

    private long getNumberFromInput(String input) {
        int start = 0;
        int end = input.indexOf('s');
        if (input.charAt(start) == 'n') {
            start += 1;
        }
        return Long.parseLong(input.substring(start, end));
    }

    private void initWorld(TETile[][] world) {
        for (int x = 0; x < WIDTH; x += 1) {
            for (int y = 0; y < HEIGHT; y += 1) {
                world[x][y] = Tileset.NOTHING;
            }
        }
    }

    private Room[] addRooms(TETile[][] world, Random random) {
        int roomNum = random.nextInt(40) + 40;
        int roomCounter = 0;
        Room[] roomsList = new Room[roomNum];
        for (int n = 0; n < roomNum; n += 1) {
            Room r = new Room(random.nextInt(WIDTH), random.nextInt(HEIGHT), random.nextInt(10) + 3,
                    random.nextInt(10) + 3);
            if (r.isInMap(world) && !r.isOverlap(world)) {
                roomsList[roomCounter] = r;
                roomCounter += 1;
                r.makeFloor(world);
                r.makeWall(world);
            }
        }
        return sortRoomsList(roomsList, roomCounter);
    }

    private Room[] sortRoomsList(Room[] roomsList, int roomCounter) {
        Room[] finalRoomsList = new Room[roomCounter];
        System.arraycopy(roomsList, 0, finalRoomsList, 0, roomCounter);
        Arrays.sort(finalRoomsList);
        return finalRoomsList;
    }

    private void addHallways(TETile[][] world, Room[] roomsList, Random random) {
        for (int i = 0; i <= roomsList.length - 2; i += 1) {
            Room r1 = roomsList[i];
            Room r2 = roomsList[i + 1];
            Position startPos = new Position(r1.posX + random.nextInt(r1.width), r1.posY + random.nextInt(r1.height));
            Position endPos = new Position(r2.posX + random.nextInt(r2.width), r2.posY + random.nextInt(r2.height));
            Hallway h = new Hallway(startPos, endPos, random);
            h.make(world);
        }
    }
}
