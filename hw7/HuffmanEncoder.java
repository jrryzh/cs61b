import edu.princeton.cs.algs4.In;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class HuffmanEncoder {
    public static Map<Character, Integer> buildFrequencyTable(char[] inputSymbols) {
        Map<Character, Integer> res = new HashMap<>();
        for (char c : inputSymbols) {
            if (res.containsKey(c)) {
                res.replace(c, res.get(c) + 1);
            } else {
                res.put(c, 1);
            }
        }
        return res;
    }

    public static void main(String[] args) {
        // 1: Read the file as 8 bit symbols.
        char[] charactersArr = FileUtils.readFile(args[0]);
        // 2: Build frequency table.
        Map<Character, Integer> frenquencyTable = buildFrequencyTable(charactersArr);
        // 3: Use frequency table to construct a binary decoding trie.
        BinaryTrie binaryTrie = new BinaryTrie(frenquencyTable);
        // 4: Write the binary decoding trie to the .huf file.
        ObjectWriter ow = new ObjectWriter(args[0] + ".huf");
        ow.writeObject(binaryTrie);
        // 5: (optional: write the number of symbols to the .huf file)
        ow.writeObject(charactersArr.length);
        // 6: Use binary trie to create lookup table for encoding.
        Map<Character, BitSequence> lookUpTable = binaryTrie.buildLookupTable();
        // 7: Create a list of bitsequences.
        List<BitSequence> bitSequenceList = new LinkedList<>();
        // 8: For each 8 bit symbol:
        //    Lookup that symbol in the lookup table.
        //    Add the appropriate bit sequence to the list of bitsequences.
        for (char c : charactersArr) {
            bitSequenceList.add(lookUpTable.get(c));
        }
        // 9: Assemble all bit sequences into one huge bit sequence.
        BitSequence res = BitSequence.assemble(bitSequenceList);
        // 10: Write the huge bit sequence to the .huf file.
        ow.writeObject(res);
    }
}
