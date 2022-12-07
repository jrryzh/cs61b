import java.io.Serializable;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.PriorityQueue;

public class BinaryTrie implements Serializable {
    TrieNode root;

    public BinaryTrie(Map<Character, Integer> frequencyTable) {
        // 设置一个所有TrieNode的PQ，比较时用frequency，将所有word都先加入该pq
        // 每次取出最小的两个node进行合并，并重新加入，直到PQ中只剩一个TrieNode
        // 返回root即可

        // 设置一个所有TrieNode的PQ，比较时用frequency，将所有word都先加入该pq
        PriorityQueue<TrieNode> TrieNodePQ = new PriorityQueue<>(new TNComparator());
        for (char c : frequencyTable.keySet()) {
            TrieNodePQ.add(new TrieNode(c, frequencyTable.get(c)));
        }
        // 每次取出最小的两个node进行合并，并重新加入，直到PQ中只剩一个TrieNode
        while (TrieNodePQ.size() > 1) {
            TrieNode tn;
            TrieNode tn1 = TrieNodePQ.poll();
            TrieNode tn2 = TrieNodePQ.poll();
            if (tn1.freq < tn2.freq) {
                tn = new TrieNode('-', tn1.freq + tn2.freq, tn1, tn2);
            } else {
                tn = new TrieNode('-', tn1.freq + tn2.freq, tn2, tn1);
            }
            TrieNodePQ.add(tn);
        }
        // 返回root即可
        root = TrieNodePQ.poll();
    }

    private static class TrieNode implements Serializable {
        char ch;
        int freq;
        TrieNode left, right;

        public TrieNode(char ch, int freq, TrieNode left, TrieNode right) {
            this.ch = ch;
            this.freq = freq;
            this.left = left;
            this.right = right;
        }

        public TrieNode(char ch, int freq) {
            this.ch = ch;
            this.freq = freq;
            this.left = null;
            this.right = null;
        }
    }

    private class TNComparator implements Comparator<TrieNode> {

        @Override
        public int compare(TrieNode o1, TrieNode o2) {
            return o1.freq - o2.freq;
        }
    }

    public Match longestPrefixMatch(BitSequence querySequence) {
        return lpmHelper(root, querySequence, new BitSequence(), 0);
    }

    private Match lpmHelper(TrieNode node, BitSequence querySequence, BitSequence subSequence, int index) {
        // 如果当前node为leaf，返回char作为symbol, subsequence作为sequence
        // 如果是0，向左
        // 如果是1，向右
        if (node.left == null && node.right == null) {
            return new Match(subSequence, node.ch);
        }

        if (querySequence.bitAt(index) == 0) {
            return lpmHelper(node.left, querySequence, subSequence.appended(0), index + 1);
        } else {
            return lpmHelper(node.right, querySequence, subSequence.appended(1), index + 1);
        }
    }

    public Map<Character, BitSequence> buildLookupTable() {
        Map<Character, BitSequence> res = new HashMap<>();
        bltHelper(root, new BitSequence(), res);
        return res;
    }

    private void bltHelper(TrieNode node, BitSequence curSequence, Map<Character, BitSequence> res) {
        if (node.left == null && node.right == null) {
            res.put(node.ch, curSequence);
        } else {
            bltHelper(node.left, curSequence.appended(0), res);
            bltHelper(node.right, curSequence.appended(1), res);
        }
    }
}
