import org.junit.Assert;
import org.junit.Test;

public class TestUnionFind {
    @Test
    public void test() {
        UnionFind uf = new UnionFind(15);
        uf.union(1, 5);
        uf.union(1, 6);

        uf.union(2, 4);
        uf.union(2, 6);

        Assert.assertTrue(uf.connected(1, 4));
    }

}
