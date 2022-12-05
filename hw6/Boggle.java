import java.util.*;

import edu.princeton.cs.algs4.In;

public class Boggle {
    
    // File path of dictionary file
    static String dictPath = "words.txt";

    /**
     * Solves a Boggle puzzle.
     * 
     * @param k The maximum number of words to return.
     * @param boardFilePath The file path to Boggle board file.
     * @return a list of words found in given Boggle board.
     *         The Strings are sorted in descending order of length.
     *         If multiple words have the same length,
     *         have them in ascending alphabetical order.
     */
    public static List<String> solve(int k, String boardFilePath) {
        // YOUR CODE HERE
        // 先把所有words都存在一个TrieSet里 (先假设'都不算）
        // 对于每一个tile都进行深度搜索，只要以当前string为prefix的数量大于0就继续搜索，并将沿路搜索到的单词存到priorityQueue里
        // 返回前k个

        // Check1--检查k
        if (k <= 0) {
            throw new IllegalArgumentException("K is not allowed to be non-positive!");
        }

        // 读取所有dictionary里的单词，存在trieSet里
        In wordsIn = new In(dictPath);
        // Check2 -- 检查dictionary file是否exist
        if (!wordsIn.exists()) {
            throw new IllegalArgumentException("Dictionary file doesn't exist!");
        }
        TrieSet wordsTS = new TrieSet();
        while (wordsIn.hasNextLine()) {
            String newLine = wordsIn.readLine();
            if (validWord(newLine)) {
                wordsTS.put(cleanString(newLine));
            }
        }
        // 读取board，暂存在boardList里
        In boardIn = new In(boardFilePath);
        List<String> boardList = new LinkedList<>();
        while (boardIn.hasNextLine()) {
            String newLine = boardIn.readLine();
            boardList.add(newLine);
        }
        // Check3 -- 检查board是否为rectangle
        int height = boardList.size();
        int width = boardList.get(0).length();
        for (int i = 1; i < height; i++) {
            if (boardList.get(i).length() != width) {
                throw new IllegalArgumentException("Board is not rectangle!");
            }
        }
        // 初始化board
        char[][] board = new char[height][width];
        for (int y = 0; y < height; y++) {
            String line = boardList.get(y);
            for (int x = 0; x < width; x++) {
                board[y][x] = line.charAt(x);
            }
        }
        // 开始打算iteratively，但对于所有位置(x, y)开始深度搜索，因为很难implement exploredList，决定使用recursion
        // 决定使用另一种思路
        // @Source: https://www.geeksforgeeks.org/boggle-using-trie/
        // 这种思路是：从TrieSet出发,TrieSet的不同Node与当前board上某一位置对应
        // 初始化visted[][]和resPriorityQueue
        boolean[][] visited = new boolean[height][width];
        PriorityQueue<String> resPQ = new PriorityQueue<>(new WordComparator());
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                searchWord(wordsTS.getRoot(), board, x, y, visited, resPQ, "", true);
            }
        }
        // 整理结果
        List<String> res = new LinkedList<>();
        while (res.size() < k){
            if (resPQ.size() == 0) break;
            if (!res.contains(resPQ.peek())) {
                res.add(resPQ.poll());
            } else {
                resPQ.remove();
            }
        }
        return res;
    }

    private static void searchWord(TrieSet.Node root, char board[][], int x, int y, boolean visited[][], PriorityQueue<String> res, String str, boolean isRoot) {
        // base case1: 如果当前节点exist=true，则pq内添加当前词
        if (root.exists) {
            res.add(str);
        }
        // ToDo:
        //  base case2: 如果当前str已经不是trieSet中任何String的prefix，则返回
        // base case3: 如果没有可以继续搜的节点 说明已经到底，return （自动）
        Set<Character> charSet = new HashSet<>();
        for (int i = 0; i < root.links.length - 1; i++) {
            if (root.links[i] != null) {
                charSet.add((char) (i + 'a'));
            }
        }
        if (charSet.size() == 0) return;

        // 对于八个方向，如果当前方向位置在棋盘内且links中此字母为true，则explore
        if (!isRoot) {
            visited[y][x] = true;
        }
        for (int i = -1; i <= 1; i++) {
            for (int j = -1; j <= 1; j++) {
                if (i == 0 && j == 0) continue;
                if (inBoard(board.length, board[0].length, x + i, y + j) && charSet.contains(board[y + j][x + i]) && !visited[y + j][x + i]) {
                    searchWord(root.links[board[y + j][x + i] - 'a'], board, x + i, y + j, visited, res, str + board[y + j][x + i], false);
                }
            }
        }
        visited[y][x] = false;
    }

    private static class WordComparator implements Comparator<String>{
        @Override
        public int compare(String s1, String s2) {
            // 开始时忽略了
            // If multiple words have the same length,
            // print them in ascending alphabetical order.
            if (s1.length() != s2.length()) {
                return - s1.length() + s2.length();
            } else {
                return s1.compareTo(s2);
            }
        }
    }

    /** 去掉"'"，并全部转为小写 */
    private static String cleanString(String s) {
        if (s.contains("'")) {
            s = s.replace("'", "");
        }
        return s.toLowerCase();
    }

    /** 对于每个位置返回当前位置可搜索的位置，结构为List<（i，j）>
     * 此时不check是否 explored = true*/
    private static List<int[]> reach(int height, int width, int x, int y) {
        List<int[]> res = new LinkedList<>();
        for (int i = -1; i <= 1 ; i++) {
            for (int j = -1; j <= 1 ; j++) {
                if (i == 0 && j == 0) continue;
                if (inBoard(height, width, x + i, y + j)) {
                    res.add(new int[]{x + i, y + j});
                }
            }
        }
        return res;
    }

    /** 返回当前位置是否在board内 */
    private static boolean inBoard(int height, int width, int x, int y) {
        return y >= 0 && y <= height - 1 && x >= 0 && x <= width - 1;
    }

    /** 检查单词是否含有非法字母 如法语字母和'\'' */
    private static boolean validWord(String word) {
        for (int i = 0; i < word.length(); i++) {
            if (word.charAt(i) > 'z' || word.charAt(i) < 'a') {
                return false;
            }
        }
        return true;
    }

//    public static void main(String[] args) {
//        List<String> res = Boggle.solve(7, "exampleBoard.txt");
//        for (String s : res) {
//            System.out.println(s);
//        }
//    }
}
