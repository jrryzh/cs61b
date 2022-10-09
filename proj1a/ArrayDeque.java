public class ArrayDeque<T> {
    private T[] items;
    private int size;

    private int nextFirst;
    private int nextLast;
    private static int RFACTOR = 2;

    public ArrayDeque() {
        items = (T[]) new Object[8];
        size = 0;
        nextFirst = items.length / 4;
        nextLast = nextFirst + 1;
    }

//    public ArrayDeque(ArrayDeque other) {
//        T[] newItems = (T[]) new Object[other.items.length];
//        System.arraycopy(other, 0, newItems, 0, other.items.length);
//        items = newItems;
//        size = other.size;
//        nextFirst = other.nextFirst;
//        nextLast = other.nextLast;
//    }

    private void resize(int newsize) {
        // 更新items， nextFirst, nextLast; size不变
        // 无论扩大还是缩小，应该都是填不满的，所以不用担心index越界
        T[] newItems = (T[]) new Object[newsize];
        this.nextFirst = newsize / 4;
        int index = this.nextFirst + 1;
        for (T item: this.items) {
            newItems[index] = item;
            index += 1;
        }
        this.nextLast = index;
        this.items = newItems;
    }

    public void addFirst(T item) {
        // 查看是否需要扩大（size是否满了）
        if (size() == items.length) {
            resize(size() * RFACTOR);
        }

        // 共需要检查改变item，size， nextFirst
        items[nextFirst] = item;

        // 检查是否需要nextFirst到另一边
        if (nextFirst == 0) {
            nextFirst = items.length - 1;
        } else {
            nextFirst -= 1;
        }

        size += 1;
    }

    public void addLast(T item) {
        // 查看是否需要扩大
        if (size() == items.length) {
            resize(size() * RFACTOR);
        }

        // 共需要检查改变item，size， nextLast
        items[nextLast] = item;

        // 检查是否需要nextLast到另一边
        if (nextLast == items.length - 1) {
            nextLast = 0;
        } else {
            nextLast += 1;
        }

        size += 1;
    }

    public boolean isEmpty() {
        return size == 0;
    }

    public int size() {
        return size;
    }

    public void printDeque() {
        // 同样暂时没有考虑empty的情况
        // first<next 说明1. 当前顺序为从左到右 2.此时已经满了
        // first>next 说明first跑到右边去了
        // first==next 还是说明first跑到右边去，然后向左移动与last都重叠了，此时还差一个item即满
        if (nextFirst < nextLast && size() < items.length) {
            for (int index = nextFirst + 1; index < nextLast; index++) {
                System.out.print(items[index]);
                System.out.print(" ");
            }
            System.out.println();
        } else {
            // 先打印first向右，到链表末尾的item们
            for (int index = nextFirst + 1; index <= items.length - 1; index++) {
                System.out.print(items[index]);
                System.out.print(" ");
            }
            // 再打印链表开头向右，到last的item们
            for (int index = 0; index < nextLast; index++) {
                System.out.print(items[index]);
                System.out.print(" ");
            }
            System.out.println();
        }
    }

    public T removeFirst() {
        // 声明变量
        T result;

        // 检查是否为null
        if (isEmpty()) {
            return null;
        }

        // 检查是否需要nextFirst到另一边
        // 暂时不确定置为null是否正确，应该没问题，因为都同时指向一个地址吧
        if (nextFirst == items.length - 1) {
            result = items[0];
            nextFirst = 0;
            items[nextFirst] = null;
        } else {
            result = items[nextFirst + 1];
            nextFirst += 1;
            items[nextFirst] = null;
        }

        // 检查是否需要缩小array
        size -= 1;
        if ((float) size() / items.length < 0.25 && items.length > 16) {
            resize(items.length / RFACTOR);
        }

        return result;
    }

    public T removeLast() {
        // 声明变量
        T result;

        // 检查是否为null
        if (isEmpty()) {
            return null;
        }

        // 检查是否需要nextLast到另一边
        if (nextLast == 0) {
            nextLast = items.length - 1;
            result = items[nextLast];
            items[nextLast] = null;
        } else {
            nextLast -= 1;
            result = items[nextLast];
            items[nextLast] = null;
        }

        // 检查是否需要缩小array
        size -= 1;
        if ((float) size() / items.length < 0.25 && items.length > 16) {
            resize(items.length / RFACTOR);
        }

        return result;
    }

    public T get(int index) {
        // 首先排除index是否超出了当前size
        // 未超出，则须检查 first+index 是否以超出 length 而需要从头继续数
        if (index > size() - 1) {
            return null;
        } else if (nextFirst + index + 1 > items.length - 1) {
            return items[index - (items.length - 1 - nextFirst)];
        } else {
            return items[nextFirst + index + 1];
        }
    }
}
