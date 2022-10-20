package synthesizer;
import org.junit.Test;

import java.util.Iterator;

import static org.junit.Assert.*;

/** Tests the ArrayRingBuffer class.
 *  @author Josh Hug
 */

public class TestArrayRingBuffer {
    @Test
    public void easyTest() {
        ArrayRingBuffer arb = new ArrayRingBuffer(10);
        for (int i = 1; i <= 5; i += 1) {
            arb.enqueue(i);
        }
        assertEquals(arb.peek(), 1);
        for (int i = 1; i <= 5; i += 1) {
            assertEquals(arb.dequeue(), i);
        }
    }

    @Test
    public void iterationTest() {
        ArrayRingBuffer arb = new ArrayRingBuffer(10);
        Iterator arbIterator = arb.iterator();
        for (int i = 1; i <= 5; i += 1) {
            arb.enqueue(i);
        }
        for (int i = 1; i <= 5; i += 1) {
            assertEquals(i, arbIterator.next());
        }
        assertFalse(arbIterator.hasNext());
    }

    /** Calls tests for ArrayRingBuffer. */
    public static void main(String[] args) {
        jh61b.junit.textui.runClasses(TestArrayRingBuffer.class);
    }
} 
