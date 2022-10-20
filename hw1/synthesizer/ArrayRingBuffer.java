package synthesizer;

import java.util.Iterator;

//TODO: Make sure to make this class and all of its methods public
//TODO: Make sure to make this class extend AbstractBoundedQueue<t>
public class ArrayRingBuffer<T> extends AbstractBoundedQueue<T> {
    /* Index for the next dequeue or peek. */
    private int first;            // index for the next dequeue or peek
    /* Index for the next enqueue. */
    private int last;
    /* Array for storing the buffer data. */
    private T[] rb;

    /**
     * Create a new ArrayRingBuffer with the given capacity.
     */
    public ArrayRingBuffer(int capacity) {
        // TODO: Create new array with capacity elements.
        //       first, last, and fillCount should all be set to 0.
        //       this.capacity should be set appropriately. Note that the local variable
        //       here shadows the field we inherit from AbstractBoundedQueue, so
        //       you'll need to use this.capacity to set the capacity.
        first = 0;
        last = 0;
        fillCount = 0;
        rb = (T[]) new Object[capacity];
        this.capacity = capacity;
    }

    /**
     * Adds x to the end of the ring buffer. If there is no room, then
     * throw new RuntimeException("Ring buffer overflow"). Exceptions
     * covered Monday.
     */
    @Override
    public void enqueue(T x) {
        // TODO: Enqueue the item. Don't forget to increase fillCount and update last.
        // 先检查是否已满，否则可以继续加
        if (isFull()) {
            throw new RuntimeException("Ring buffer overflow");
        }
        // 继续添加，注意last如何移动
        rb[last] = x;
        if (last == this.capacity - 1) {
            last = 0;
        } else {
            last += 1;
        }
        fillCount += 1;
    }

    /**
     * Dequeue oldest item in the ring buffer. If the buffer is empty, then
     * throw new RuntimeException("Ring buffer underflow"). Exceptions
     * covered Monday.
     */
    @Override
    public T dequeue() {
        // TODO: Dequeue the first item. Don't forget to decrease fillCount and update
        // 先检查是否非空，否则可以dequeue
        if (isEmpty()) {
            throw new RuntimeException("Ring buffer underflow");
        }
        // 提取，注意first如何移动
        T item = rb[first];
        if (first == this.capacity - 1) {
            first = 0;
        } else {
            first += 1;
        }
        fillCount -= 1;
        return item;
    }

    /**
     * Return oldest item, but don't remove it.
     */
    @Override
    public T peek() {
        // TODO: Return the first item. None of your instance variables should change.
        // 先检查是否非空
        if (isEmpty()) {
            throw new RuntimeException("Ring buffer underflow");
        }
        return rb[first];
    }

    // TODO: When you get to part 5, implement the needed code to support iteration.
    /**
     * Add an iterator for the class
     */
    public Iterator<T> iterator() {
        return new ArrayRingBufferIterator();
    }
    /**
     * The explicit implementation for the method.
     */
    private class ArrayRingBufferIterator implements Iterator<T> {
        private int index;

        public ArrayRingBufferIterator() {
            index = first;
        }

        public boolean hasNext() {
            return index != last;
        }

        public T next() {
            T item = rb[index];
            if (index == rb.length - 1) {
                index = 0;
            } else {
                index += 1;
            }
            return item;
        }

    }
}
