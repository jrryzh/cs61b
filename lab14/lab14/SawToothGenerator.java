package lab14;

import edu.princeton.cs.algs4.StdAudio;
import lab14lib.Generator;
import lab14lib.GeneratorAudioVisualizer;

public class SawToothGenerator implements Generator{
    private final int period;
    private int state;

    public SawToothGenerator(int period) {
        this.state = 0;
        this.period = period;
    }

    public double next() {
        double result = normalize(state);
        state = (state + 1) % period;
        return result;
    }

    private double normalize(double x) {
        return x / (period - 1) * 2 - 1;
    }


    public static void main(String args[]) {
        Generator generator = new SawToothGenerator(512);
        GeneratorAudioVisualizer gav = new GeneratorAudioVisualizer(generator);
        gav.drawAndPlay(4096, 1000000);
    }
}
