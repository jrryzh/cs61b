public class Palindrome {
    /** Convert a String to a Deque */
    public Deque<Character> wordToDeque(String word) {
        Deque d = new LinkedListDeque();
        for (int index = 0; index <= word.length() - 1; index += 1) {
            d.addLast(word.charAt(index));
        }
        return d;
    }

    /** Helper function for checking isPalindrome */
    private boolean helperIsPalindrome(Deque d) {
        if (d.size() == 0 || d.size() == 1) {
            return true;
        }
        return d.removeFirst() == d.removeLast() && helperIsPalindrome(d);
    }

    /** Check whether a String is Palindrome */
    public boolean isPalindrome(String word) {
        Palindrome palindrome = new Palindrome();
        Deque d = palindrome.wordToDeque(word);
        return helperIsPalindrome(d);
    }

    /** Helper function for checking isPalindrome */
    private boolean helperIsPalindrome(Deque d, CharacterComparator cc) {
        if (d.size() == 0 || d.size() == 1) {
            return true;
        }
        return cc.equalChars((char) d.removeFirst(), (char) d.removeLast())
                && helperIsPalindrome(d, cc);
    }

    /** Check Palindrome using CharacterComparator */
    public boolean isPalindrome(String word, CharacterComparator cc) {
        Palindrome palindrome = new Palindrome();
        Deque d = palindrome.wordToDeque(word);
        return helperIsPalindrome(d, cc);
    }
}
