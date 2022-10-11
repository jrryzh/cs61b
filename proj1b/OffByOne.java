public class OffByOne implements CharacterComparator {
    @Override
    public boolean equalChars(char x, char y) {
        // 这里比较草率，暂时先这么写
        return x - y == -1 || x - y == 1;
    }
}
