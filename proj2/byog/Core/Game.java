package byog.Core;

import byog.TileEngine.TERenderer;
import byog.TileEngine.TETile;
import byog.TileEngine.Tileset;
import edu.princeton.cs.introcs.StdDraw;

import java.awt.*;
import java.io.ObjectOutputStream;
import java.io.ObjectInputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.Arrays;
import java.util.HashMap;
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
        startMenu();
        String choice = makeChoice();
        switch (choice) {
            case "n":
                newGame();
                break;
            case "l":
                loadGame();
                break;
            case "q":
                System.exit(0);
                break;
        }
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
     *
     * @param input the input string to feed to your program
     * @return the 2D TETile[][] representing the state of the world
     */
    public TETile[][] playWithInputString(String input) {
        TETile[][] finalWorldFrame = null;
        char firstChar = input.charAt(0);
        if (firstChar == 'n') {
            finalWorldFrame = newGame(input);
        } else if (firstChar == 'l') {
            finalWorldFrame = loadGame(input);
        } else if (firstChar == 'q') {
            System.exit(0);
        }
        return finalWorldFrame;
    }

    private TETile[][] newGame(String input) {
        TETile[][] finalWorldFrame;
        // 处理input
        HashMap<String, String> inputMap = processInput(input);
        // 由seed生成世界和人物
        finalWorldFrame = generateWorld(Long.parseLong(inputMap.get("seed")));
        Player player = new Player(finalWorldFrame, Long.parseLong(inputMap.get("seed")));
        // 由steps完成游戏
        playGame(finalWorldFrame, player, inputMap.get("steps"));
        // 由input决定是否保存游戏
        if (inputMap.containsKey("save")) {
            saveGame(finalWorldFrame, player);
        }

        return finalWorldFrame;
    }

    private TETile[][] loadGame(String input) {
        TETile[][] finalWorldFrame = null;
        Player player = null;
        // 处理input
        HashMap<String, String> inputMap = processInput(input);
        // 加载世界
        try {
            ObjectInputStream in = new ObjectInputStream(new FileInputStream("savefile.txt"));
            finalWorldFrame = (TETile[][]) in.readObject();
            player = (Player) in.readObject();
            in.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        // 由steps运行游戏
        playGame(finalWorldFrame, player, inputMap.get("steps"));
        // 由input决定是否保存游戏
        if (inputMap.containsKey("save")) {
            saveGame(finalWorldFrame, player);
        }

        return finalWorldFrame;
    }

    private void newGame() {
        long seed = keyboardSeed();
        TETile[][] finalWorldFrame = generateWorld(seed);
        Player player = new Player(finalWorldFrame, seed);
        playGame(finalWorldFrame, player);
    }

    private void loadGame() {
        TETile[][] finalWorldFrame = null;
        Player player = null;
        // 加载世界
        try {
            ObjectInputStream in = new ObjectInputStream(new FileInputStream("savefile.txt"));
            finalWorldFrame = (TETile[][]) in.readObject();
            player = (Player) in.readObject();
            in.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        playGame(finalWorldFrame, player);
    }

    private void saveGame(TETile[][] finalWorldFrame, Player player) {
        try {
            ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream("savefile.txt"));
            out.writeObject(finalWorldFrame);
            out.writeObject(player);
            out.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
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

    // 只有N开头的有seed，都有steps，只有保存的有key"save"
    private HashMap<String, String> processInput(String input) {
        // 设置变量及input变小写
        int midIndex;
        input = input.toLowerCase();
        // 由specs所说，开头均为“N#S” or “L”
        HashMap<String, String> map = new HashMap<>();
        if (input.charAt(0) == 'n') {
            midIndex = input.indexOf("s");
            map.put("seed", input.substring(1, input.indexOf("s")));
        } else {
            midIndex = input.indexOf("l");
        }
        if (input.contains(":")) {
            map.put("steps", input.substring(midIndex + 1, input.indexOf(":")));
            map.put("save", ":q");
        } else {
            map.put("steps", input.substring(midIndex + 1));
        }
        return map;
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

    private TETile[][] playGame(TETile[][] world, Player player, String steps) {
//        ter.initialize(WIDTH, HEIGHT);
//        ter.renderFrame(world);
        for (int i = 0; i < steps.length(); i += 1) {
            player.move(world, steps.charAt(i));
//            ter.renderFrame(world);
        }
        return world;
    }

    private TETile[][] playGame(TETile[][] world, Player player) {
        TERenderer ter = new TERenderer();
        ter.initialize(WIDTH, HEIGHT);
        ter.renderFrame(world);
        while (true) {
            if (!StdDraw.hasNextKeyTyped()) {
                continue;
            }
            char s = StdDraw.nextKeyTyped();
            if (s == ':') {
                if (nextQ()) {
                    saveGame(world, player);
                    System.exit(0);
                }
            } else {
                playGame(world, player, String.valueOf(s));
                ter.renderFrame(world);
            }
        }
    }

    private boolean nextQ() {
        char s;
        while (true) {
            if (!StdDraw.hasNextKeyTyped()) {
                continue;
            }
            s = StdDraw.nextKeyTyped();
            break;
        }
        return (s == 'q');
    }

    private void startMenu() {
        // @Source: MemoryGameSolution.java
        StdDraw.setCanvasSize(WIDTH * 16, (HEIGHT + 1) * 16);
        Font font = new Font("Monaco", Font.BOLD, 30);
        StdDraw.setFont(font);
        StdDraw.setXscale(0, WIDTH);
        StdDraw.setYscale(0, HEIGHT + 1);
        StdDraw.clear(Color.BLACK);
        StdDraw.enableDoubleBuffering();

        Font smallFont = new Font("Monaco", Font.BOLD, 20);
        Font bigFont = new Font("Monaco", Font.BOLD, 35);
        StdDraw.setFont(bigFont);
        StdDraw.setPenColor(Color.white);
        StdDraw.text(WIDTH / 2, HEIGHT * 3 / 4, "CS61B: THE GAME");
        StdDraw.setFont(smallFont);
        StdDraw.setPenColor(Color.white);
        StdDraw.text(WIDTH / 2, HEIGHT / 2 + 2, "NEW GAME (N)");
        StdDraw.text(WIDTH / 2, HEIGHT / 2, "LOAD GAME (L)");
        StdDraw.text(WIDTH / 2, HEIGHT / 2 - 2, "QUIT (Q)");
        StdDraw.show();
    }

    private String makeChoice() {
        String choice = "";
        while (choice.equals("")) {
            if (!StdDraw.hasNextKeyTyped()) {
                continue;
            }
            choice = String.valueOf(StdDraw.nextKeyTyped());
        }
        return choice;
    }

    private Long keyboardSeed() {
        StringBuilder seed = new StringBuilder();
        while (true) {
            if (!StdDraw.hasNextKeyTyped()) {
                continue;
            }
            char s = StdDraw.nextKeyTyped();
            if (s == 's') {
                break;
            } else {
                seed.append(s);
            }
        }
        return Long.parseLong(seed.toString());
    }
}
