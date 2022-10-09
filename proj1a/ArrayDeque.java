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

    private int resizeHelper(T[] sArray, T[] dArray, int sStart, int sEnd, int dIndex) {
        for (int i = sStart; i <= sEnd; i += 1) {
            dArray[dIndex] = sArray[i];
            dIndex += 1;
        }

        return dIndex;
    }

    private void resize(int newsize) {
        // 更新items， nextFirst, nextLast; size不变
        // 无论扩大还是缩小，应该都是填不满的，所以不用担心index越界
        // 同样暂时没有考虑empty的情况
        // first<last 说明1.当前顺序为从左到右 2.此时已经满了(此时 first+1 = last)
        // first>=last 说明1.first或next越界且未满（first跑到右边去了或者next跑到左边去了）2.此时已经满了（此时last=0 first=length-1）

        T[] newItems = (T[]) new Object[newsize];
        int newNextFirst = newsize / 4;
        int newNextLast;

        if (nextFirst < nextLast) {
            if (size < items.length) {
                newNextLast = resizeHelper(items, newItems,
                        nextFirst + 1, nextLast - 1, newNextFirst + 1);
            } else {
                int index = resizeHelper(items, newItems,
                        nextFirst + 1, items.length - 1, newNextFirst + 1);
                newNextLast = resizeHelper(items, newItems, 0, nextLast - 1, index);
            }
        } else {
            if (size < items.length) {
                int index = resizeHelper(items, newItems,
                        nextFirst + 1, items.length - 1, newNextFirst + 1);
                newNextLast = resizeHelper(items, newItems, 0, nextLast - 1, index);
            } else {
                newNextLast = resizeHelper(items, newItems, 0, items.length - 1, newNextFirst + 1);
            }
        }

//        int newNextFirst = newsize/4;
//        int index = newNextFirst + 1;
//
//        if (nextFirst < nextLast && size < items.length){
//            for (int i = nextFirst + 1; i <= nextLast - 1; i += 1){
//                newItems[index] = items[i];
//                index += 1;
//            }
//        } else {
//            for (int i = nextFirst + 1; i <= items.length - 1; i += 1){
//                newItems[index] = items[i];
//                index += 1;
//            }
//            for (int i = 0; i <= nextLast - 1; i += 1) {
//                newItems[index] = items[i];
//                index += 1;
//            }
//        }
//        int newNextLast = index;
//
        items = newItems;
        nextFirst = newNextFirst;
        nextLast = newNextLast;
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

    private void printHelper(int start, int end) {
        for (int index = start; index <= end; index += 1) {
            System.out.print(items[index]);
            System.out.print(" ");
        }
    }

    public void printDeque() {
        // 同样暂时没有考虑empty的情况
        // first<last 说明1.当前顺序为从左到右 2.此时已经满了(此时 first+1 = last)
        // first>=last 说明1.first或next越界且未满（first跑到右边去了或者next跑到左边去了）2.此时已经满了（此时last=0 first=length-1）
        if (nextFirst < nextLast) {
            if (size < items.length) {
                printHelper(nextFirst + 1, nextLast - 1);
            } else {
                printHelper(nextFirst + 1, items.length - 1);
                printHelper(0, nextLast - 1);
            }
        } else {
            if (size < items.length) {
                printHelper(nextFirst + 1, items.length - 1);
                printHelper(0, nextLast - 1);
            } else {
                printHelper(0, items.length - 1);
            }
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
        if ((float) size() / items.length <= 0.25 && items.length > 16) {
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
