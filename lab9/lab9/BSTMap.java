package lab9;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

/**
 * Implementation of interface Map61B with BST as core data structure.
 *
 * @author Your name here
 */
public class BSTMap<K extends Comparable<K>, V> implements Map61B<K, V> {

    private class Node {
        /* (K, V) pair stored in this Node. */
        private K key;
        private V value;

        /* Children of this Node. */
        private Node left;
        private Node right;

        private Node(K k, V v) {
            key = k;
            value = v;
        }
    }

    private Node root;  /* Root node of the tree. */
    private int size; /* The number of key-value pairs in the tree */
    private Set<K> keySet;

    /* Creates an empty BSTMap. */
    public BSTMap() {
        this.clear();
    }

    /* Removes all of the mappings from this map. */
    @Override
    public void clear() {
        root = null;
        size = 0;
        keySet = new HashSet<>();
    }

    /**
     * Returns the value mapped to by KEY in the subtree rooted in P.
     * or null if this map contains no mapping for the key.
     */
    private V getHelper(K key, Node p) {
        if (p == null) {
            return null;
        } else if (key.compareTo(p.key) < 0) {
            return getHelper(key, p.left);
        } else if (key.compareTo(p.key) > 0) {
            return getHelper(key, p.right);
        } else {
            return p.value;
        }
    }

    /**
     * Returns the value to which the specified key is mapped, or null if this
     * map contains no mapping for the key.
     */
    @Override
    public V get(K key) {
        return getHelper(key, this.root);
    }

    /**
     * Returns a BSTMap rooted in p with (KEY, VALUE) added as a key-value mapping.
     * Or if p is null, it returns a one node BSTMap containing (KEY, VALUE).
     */
    private Node putHelper(K key, V value, Node p) {
        if (p == null) {
            size += 1;
            return new Node(key, value);
        } else if (key.compareTo(p.key) < 0) {
            p.left = putHelper(key, value, p.left);
        } else if (key.compareTo(p.key) > 0) {
            p.right = putHelper(key, value, p.right);
        } else {
            p.value = value;
        }
        return p;
    }

    /**
     * Inserts the key KEY
     * If it is already present, updates value to be VALUE.
     */
    @Override
    public void put(K key, V value) {
        root = putHelper(key, value, root);
        keySet.add(key);
    }

    /* Returns the number of key-value mappings in this map. */
    @Override
    public int size() {
        return size;
    }

    //////////////// EVERYTHING BELOW THIS LINE IS OPTIONAL ////////////////

    /* Returns a Set view of the keys contained in this map. */
    @Override
    public Set<K> keySet() {
        return keySet;
    }

    /* 找到以p为root子树的最大节点 */
    public Node findMax(Node p) {
        if (p == null)  return p;
        if (p.right == null) return p;
        return findMax(p.right);
    }

    /* 删除以p为root子树的最大节点 */
    public Node removeMax(Node p) {
        if (p == null)  return null;
        // 右子树为null 则当前节点为最大点 删除即可 （only left child 或 no child）
        // 右子树不为null 则最大点在右子树 继续删除即可 （two children 或 only right child）
        if (p.right == null) {
            return p.left;
        } else {
            p.right = removeMax(p.right);
            return p;
        }
    }

    /**
     * removeHelper（思路与putHelper类似，都是返回改变后的子树）
     */
    public Node removeHelper(K key, Node p) {
        if (p == null) {
            return null;
        } else if (key.compareTo(p.key) < 0) {
            p.left = removeHelper(key, p.left);
            return p;
        } else if (key.compareTo(p.key) > 0) {
            p.right = removeHelper(key, p.right);
            return p;
        } else {
            // no children
            if (p.left == null && p.right == null) return null;
            // one children
            if (p.left == null) return p.right;
            if (p.right == null) return p.left;
            // two children
            Node maxNode = findMax(p.left);
            maxNode.left = removeMax(p.left);
            maxNode.right = p.right;
            return maxNode;
        }
    }

    /**
     * Removes KEY from the tree if present
     * returns VALUE removed,
     * null on failed removal.
     */
    @Override
    public V remove(K key) {
        V value = get(key);
        if (value == null) {
            return null;
        } else {
            size -= 1;
            root = removeHelper(key, root);
            keySet.remove(key);
            return value;
        }
    }

    /**
     * Removes the key-value entry for the specified key only if it is
     * currently mapped to the specified value.  Returns the VALUE removed,
     * null on failed removal.
     **/
    @Override
    public V remove(K key, V value) {
        if (value == get(key)) {
            remove(key);
            keySet.remove(key);
            return value;
        } else {
            return null;
        }
    }

    @Override
    public Iterator<K> iterator() {
        return keySet().iterator();
    }

    public static void main(String[] args) {
        BSTMap<String, Integer> bstmap = new BSTMap<>();
        bstmap.put("hello", 5);
        bstmap.put("cat", 10);
        bstmap.put("fish", 22);
        bstmap.put("zebra", 90);
        bstmap.remove("fish");
    }
}
