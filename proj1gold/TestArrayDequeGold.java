import static org.junit.Assert.*;
import org.junit.Test;

public class TestArrayDequeGold {

    @Test
    public void testSAD() {
        ArrayDequeSolution<Integer> ads1 = new ArrayDequeSolution<>();
        StudentArrayDeque<Integer> sad1 = new StudentArrayDeque<>();
        String message = "";

        for (int i = 0; i < 500; i += 1) {
            double numberBetweenZeroAndOne = StdRandom.uniform();

            if (numberBetweenZeroAndOne < 0.2 && ads1.size() > 0) {
                Integer x = ads1.removeFirst();
                Integer y = sad1.removeFirst();
                message += "removeFirst()\n";
                assertEquals(message, x, y);
            } else if (numberBetweenZeroAndOne < 0.4 && ads1.size() > 0){
                Integer x = ads1.removeLast();
                Integer y = sad1.removeLast();
                message += "removeLast()\n";
                assertEquals(message, x, y);
            } else if (numberBetweenZeroAndOne < 0.7) {
                ads1.addFirst(i);
                sad1.addFirst(i);
                message += "addFirst(" + i +")\n";
            } else {
                ads1.addLast(i);
                sad1.addLast(i);
                message += "addLast(" + i +")\n";
            }
        }
    }
}
