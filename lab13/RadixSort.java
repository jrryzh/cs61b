/**
 * Class for doing Radix sort
 *
 * @author Akhil Batra, Alexander Hwang
 *
 */
public class RadixSort {
    /**
     * Does LSD radix sort on the passed in array with the following restrictions:
     * The array can only have ASCII Strings (sequence of 1 byte characters)
     * The sorting is stable and non-destructive
     * The Strings can be variable length (all Strings are not constrained to 1 length)
     *
     * @param asciis String[] that needs to be sorted
     *
     * @return String[] the sorted array
     */
    public static String[] sort(String[] asciis) {
        // TODO: Implement LSD Sort
        int m = 0;
        for (String s : asciis) {
            m = Math.max(m, s.length());
        }
        for (int i = m-1; i >= 0 ; i--) {
            sortHelperLSD(asciis, i);
        }
        return asciis;
    }

    /**
     * LSD helper method that performs a destructive counting sort the array of
     * Strings based off characters at a specific index.
     * @param asciis Input array of Strings
     * @param index The position to sort the Strings on.
     */
    private static void sortHelperLSD(String[] asciis, int index) {
        // Optional LSD helper method for required LSD radix sort
        // inital lists
        String[] result = new String[asciis.length];
        int[] counts = new int[256 + 1];
        int[] starts = new int[256 + 1];
        // convert char to ints
        int[] charValues = new int[asciis.length];
        for (int i = 0; i < asciis.length; i++) {
            if (index > asciis[i].length() - 1) {
                charValues[i] = -1;
            } else {
                charValues[i] = asciis[i].charAt(index);
            }
        }
        // do the counts
        for (int v : charValues) {
            counts[v - (-1)] += 1;
        }
        // do the starts
        int k = 0;
        for (int i = 0; i < counts.length; i++) {
            starts[i] = k;
            k += counts[i];
        }
        // sorting
        for (String s : asciis) {
            int v;
            if (index > s.length() - 1) {
                v = -1;
            } else {
                v = s.charAt(index);
            }
            result[starts[v - (-1)]] = s;
            starts[v - (-1)] += 1;
        }
        // copying
        for (int i = 0; i < asciis.length; i++) {
            asciis[i] = result[i];
        }
    }

    /**
     * MSD radix sort helper function that recursively calls itself to achieve the sorted array.
     * Destructive method that changes the passed in array, asciis.
     *
     * @param asciis String[] to be sorted
     * @param start int for where to start sorting in this method (includes String at start)
     * @param end int for where to end sorting in this method (does not include String at end)
     * @param index the index of the character the method is currently sorting on
     *
     **/
    private static void sortHelperMSD(String[] asciis, int start, int end, int index) {
        // Optional MSD helper method for optional MSD radix sort
        return;
    }
}
