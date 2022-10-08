public class ArrayDeque<T> {
    private T[] items;
    private int size;

    private int nextFirst;
    private int nextLast;
    private static int RFACTOR = 2;

    public ArrayDeque(){
        items = (T[]) new Object[8];
        size = 8;
        nextFirst = size / 2;
        nextLast = nextFirst + 1;
    }

    public ArrayDeque(ArrayDeque other){
        T[] new_items = (T[]) new Object[other.size];
        System.arraycopy(other, 0, new_items, 0, other.size);
        items = new_items;
        size = other.size;
        nextFirst = size / 2;
        nextLast = nextFirst + 1;
    }

    private void resize(int newsize){
        // 更新items， nextFirst, nextLast; size不变
        T[] new_items = (T[]) new Object[newsize];
        this.nextFirst = newsize / 4;
        int index = this.nextFirst+1;
        for (T item: this.items){
            new_items[index] = item;
            index += 1;
        }
        this.nextLast = index;
        this.items = new_items;
    }

    public void addFirst(T item){
        // 共需要检查改变item，size， nextFirst
        // 查看是否需要扩大
        if (size == items.length){
            resize(size * RFACTOR);
        }
        items[nextFirst] = item;
        // 检查是否需要nextFirst到另一边
        if (nextFirst == 0){
            nextFirst = items.length-1;;
        }
        else{
            nextFirst -= 1;
        }
        size += 1;
    }

    public void addLast(T item){
        // 共需要检查改变item，size， nextFirst
        // 查看是否需要扩大
        if (size == items.length){
            resize(size * RFACTOR);
        }
        items[nextLast] = item;
        // 检查是否需要nextLast到另一边
        if (nextLast == items.length-1){
            nextLast = 0;
        }
        else{
            nextLast += 1;
        }
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

    public void printDeque(){
        // 同样暂时没有考虑empty的情况
        if (nextFirst < nextLast){
            for (int index = nextFirst+1; index < nextLast; index ++){
                System.out.print(items[index]);
                System.out.print(" ");
            }
            System.out.println();
        }
        else{
            for (int index = nextFirst+1; index < items.length; index ++){
                System.out.print(items[index]);
                System.out.print(" ");
            }
            for (int index = 0; index < nextLast; index++){
                System.out.print(items[index]);
                System.out.print(" ");
            }
            System.out.println();
        }
    }

    public T removeFirst(){
        T result;

        // 检查是否为null
        if (this.isEmpty()){
            return null;
        }

        // 检查是否需要nextFirst到另一边
        if (nextFirst == items.length-1){
            nextFirst = 0;
            result = items[nextFirst];
            items[nextFirst] = null;
        }
        else{
            nextFirst += 1;
            result = items[nextFirst];
            items[nextFirst] = null;
        }
        size -= 1;

        if ((float) size / items.length < 0.25){
            resize(items.length / RFACTOR);
        }

        return result;
    }

    public T removeLast(){
        T result;

        // 检查是否为null
        if (this.isEmpty()){
            return null;
        }

        // 检查是否需要nextLast到另一边
        if (nextLast == 0){
            nextLast = items.length-1;
            result = items[nextLast];
            items[nextLast] = null;
        }
        else{
            nextLast -= 1;
            result = items[nextLast];
            items[nextLast] = null;
        }
        size -= 1;

        if ((float) size / items.length < 0.25){
            resize(items.length / RFACTOR);
        }

        return result;
    }

    public T get(int index){
        if (index > size-1){
            return null;
        }
        else if(nextFirst + index + 1 > items.length-1){
            return items[index - (items.length - nextFirst)];
        }
        else{
            return items[nextFirst + index + 1];
        }
    }
}
