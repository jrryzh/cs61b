package lab14;

import lab14lib.Generator;
import lab14lib.GeneratorAudioVisualizer;

public class StrangeBitwiseGenerator implements Generator{
    private final int period;
    private int state;
    private int weirdState;

    public StrangeBitwiseGenerator(int period) {
        this.state = 0;
        this.period = period;
    }

    public double next() {
        double result = normalize(weirdState);
        state = state + 1;
//        weirdState = state & (state >>> 3) % period;
//        weirdState = state & (state >> 3) & (state >> 8) % period;
        // Illuminati time
        weirdState = state & (state >> 7) % period;
        return result;
    }

    private double normalize(double x) {
        return x / (period - 1) * 2 - 1;
    }


    public static void main(String args[]) {
//        Generator generator = new StrangeBitwiseGenerator(512);
//        GeneratorAudioVisualizer gav = new GeneratorAudioVisualizer(generator);
//        gav.drawAndPlay(4096, 1000000);
        Generator generator = new StrangeBitwiseGenerator(1024);
        GeneratorAudioVisualizer gav = new GeneratorAudioVisualizer(generator);
        gav.drawAndPlay(128000, 1000000);
    }
}
