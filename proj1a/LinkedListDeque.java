public class LinkedListDeque<T> {

    private class TNode{
        T item;
        TNode next;
        TNode prev;

        public TNode(T i, TNode n, TNode p){
            item = i;
            next = n;
            prev = p;
        }
    }

    public TNode sentinel;
    public int size;

    public LinkedListDeque(){
        sentinel = new TNode(null, null, null);
        sentinel.next = sentinel;
        sentinel.prev = sentinel;
        size = 0;
    }

    public LinkedListDeque(LinkedListDeque other){
        sentinel = new TNode(null, sentinel, sentinel);
        TNode p = other.sentinel;
        while (p.next != other.sentinel){
            this.addLast(p.next.item);
            p = p.next;
        }
    }

    public void addFirst(T item){
        TNode new_item = new TNode(item, sentinel.next, sentinel);
        sentinel.next.prev = new_item;
        sentinel.next = new_item;

        size += 1;
    }

    public void addLast(T item){
        TNode new_item = new TNode(item, sentinel, sentinel.prev);
        sentinel.prev.next = new_item;
        sentinel.prev = new_item;

        size += 1;
    }

    public boolean isEmpty(){
        if (size == 0){
            return true;
        }
        else{
            return false;
        }
    }

    public int size(){
        return size;
    }

    public void printDeque(){
        // 暂时没有考虑empty的情况
        TNode p = sentinel;
        while (p.next != sentinel){
            System.out.print(p.next.item);
            System.out.print(" ");
            p = p.next;
        }

        System.out.println();
    }

    public T removeFirst(){
        // 检查是否为null
        if (this.isEmpty()){
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

    public T removeLast(){
        // 检查是否为null
        if (this.isEmpty()){
            return null;
        }

        T result = sentinel.prev.item;
        sentinel.prev = sentinel.prev.prev;
        sentinel.prev.next = sentinel;

        return result;
    }

    public T get(int index){
        // 检查是否为null
        if (this.isEmpty()){
            return null;
        }

        TNode p = sentinel;

        while(p.next != sentinel){
            if (index == 0){
                return p.next.item;
            }
            p = p.next;
            index -= 1;
        }

        return null;
    }

    private T get_helper(TNode node, int index){
        if (index == 0){
            return node.next.item;
        }
        return get_helper(node.next, index-1);
    }

    public T getRecursive(int index) {
        if (index > this.size){
            return null;
        }
        return get_helper(sentinel, index);
    }
}
