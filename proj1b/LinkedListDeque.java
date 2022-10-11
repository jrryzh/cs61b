import org.junit.Test;

public class LinkedListDeque<T> implements Deque<T>{

    private class TNode {
        T item;
        TNode next;
        TNode prev;

        public TNode(T i, TNode n, TNode p) {
            item = i;
            next = n;
            prev = p;
        }
    }

    private TNode sentinel;
    private int size;

    public LinkedListDeque() {
        sentinel = new TNode(null, null, null);
        sentinel.next = sentinel;
        sentinel.prev = sentinel;
        size = 0;
    }

    @Test
    public void addFirst(T item) {
        TNode newItem = new TNode(item, sentinel.next, sentinel);
        sentinel.next.prev = newItem;
        sentinel.next = newItem;

        size += 1;
    }

    @Test
    public void addLast(T item) {
        TNode newItem = new TNode(item, sentinel, sentinel.prev);
        sentinel.prev.next = newItem;
        sentinel.prev = newItem;

        size += 1;
    }

    @Test
    public boolean isEmpty() {
        return size == 0;
    }

    @Test
    public int size() {
        return size;
    }

    @Test
    public void printDeque() {
        // 暂时没有考虑empty的情况
        TNode p = sentinel;
        while (p.next != sentinel) {
            System.out.print(p.next.item);
            System.out.print(" ");
            p = p.next;
        }
        System.out.println();
    }

    @Test
    public T removeFirst() {
        // 检查是否为null
        if (this.isEmpty()) {
            return null;
        }

        TNode tempNode = sentinel.next;
        T result = tempNode.item;
        sentinel.next = sentinel.next.next;
        sentinel.next.prev = sentinel;

        size -= 1;
        tempNode = null;

        return result;
    }

    @Test
    public T removeLast() {
        // 检查是否为null
        if (this.isEmpty()) {
            return null;
        }

        TNode tempNode = sentinel.prev;
        T result = tempNode.item;
        sentinel.prev = sentinel.prev.prev;
        sentinel.prev.next = sentinel;

        size -= 1;
        tempNode = null;

        return result;
    }

    @Test
    public T get(int index) {
        // 排除index超出范围的情况
        if (index > size() - 1) {
            return null;
        }

        TNode p = sentinel;
        while (p.next != sentinel) {
            if (index == 0) {
                return p.next.item;
            }
            p = p.next;
            index -= 1;
        }

        return null;
    }

    private T getHelper(TNode node, int index) {
        if (index == 0) {
            return node.next.item;
        }
        return getHelper(node.next, index - 1);
    }

    public T getRecursive(int index) {
        if (index > size() - 1) {
            return null;
        }
        return getHelper(sentinel, index);
    }
}
