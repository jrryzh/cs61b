import java.util.LinkedList;
import java.util.List;

public class TrieSet {
    // 这里支持a-z加space，其中"'"放在末尾
    private static final int R = 27;

    public class Node {
        boolean exists;
        Node[] links;

        public Node() {
            links = new Node[R];
            exists = false;
        }
    }

    private Node root = new Node();

    public Node getRoot() {
        return root;
    }

    public void put(String key) {
        put(root, key, 0);
    }

    /** 对于每一个Node，当前节点的字符由links[index]是否非空表示
     * 本身不存储字符 */
    private Node put(Node x, String key, int d) {
        if (x == null) {
            x = new Node();
        }

        if (d == key.length()) {
            x.exists = true;
            return x;
        }

        int index;
        char c = key.charAt(d);
        if (c == '\'') {
            index = 26;
        } else {
            index = c - 'a';
        }

        // 注意是对当前节点put，返回值也是当前节点
        x.links[index] = put(x.links[index], key, d + 1);
        return x;
    }

    public List<String> keysWithPrefix(String prefix) {
        List<String> results = new LinkedList<>();
        Node x = this.get(this.root, prefix, 0);
        collect(x, new StringBuilder(prefix), results);
        return results;
    }

    private void collect(Node x, StringBuilder prefix, List<String> results) {
        if (x != null) {
            if (x.exists) {
                results.add(prefix.toString());
            }

            for (int index = 0; index < 27; index += 1) {
                if (index < 26) {
                    prefix.append((char) (index + 'a'));
                } else {
                    prefix.append("'");
                }

                this.collect(x.links[index], prefix, results);
                prefix.deleteCharAt(prefix.length() - 1);
            }

        }
    }

    private Node get(Node x, String key, int d) {
        if (x == null) {
            return null;
        } else if (d == key.length()) {
            return x;
        } else {
            int index;
            char c = key.charAt(d);
            if (c == '\'') {
                index = 26;
            } else {
                index = c - 'a';
            }
            return get(x.links[index], key, d + 1);
        }
    }
}
