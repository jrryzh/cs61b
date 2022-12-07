public class HuffmanDecoder {
    public static void main(String[] args) {
        // 1: Read the Huffman coding trie.
        ObjectReader or = new ObjectReader(args[0]);
        BinaryTrie binaryTrie = (BinaryTrie) or.readObject();
        // 2: If applicable, read the number of symbols.
        int numberOfSymbols = (int) or.readObject();
        // 3: Read the massive bit sequence corresponding to the original txt.
        BitSequence bitSequence = (BitSequence) or.readObject();
        // 4: Repeat until there are no more symbols:
        //    4a: Perform a longest prefix match on the massive sequence.
        //    4b: Record the symbol in some data structure.
        //    4c: Create a new bit sequence containing the remaining unmatched bits.
        char[] res = new char[numberOfSymbols];
        for (int i = 0; i < numberOfSymbols; i++) {
            Match match = binaryTrie.longestPrefixMatch(bitSequence);
            res[i] = match.getSymbol();
            bitSequence = bitSequence.allButFirstNBits(match.getSequence().length());
        }
        // 5: Write the symbols in some data structure to the specified file.
        FileUtils.writeCharArray(args[1], res);
    }
}
