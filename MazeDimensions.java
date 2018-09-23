import java.io.Serializable;

public class MazeDimensions implements Serializable {

    private final int N;
    private final int K;

    public MazeDimensions(int n, int k) {
        N = n;
        K = k;
    }

    public int getN() {
        return N;
    }

    public int getK() {
        return K;
    }
}
