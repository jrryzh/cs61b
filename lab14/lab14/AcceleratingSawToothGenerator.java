package lab14;

import lab14lib.Generator;
import lab14lib.GeneratorAudioVisualizer;

public class AcceleratingSawToothGenerator implements Generator {
    private int period, state;
    private double factor;

    public AcceleratingSawToothGenerator(int period, double factor) {
        this.period = period;
        this.factor = factor;
    }

    @Override
    public double next() {
        double result = normalize(state);
        if ((state + 1) % period == 0) {
            state = 0;
            period = (int) (period * factor);
        } else {
            state += 1;
        }
        return result;
    }

    private double normalize(double x) {
        return x / (period - 1) * 2 - 1;
    }

    public static void main(String args[]) {
        Generator generator = new AcceleratingSawToothGenerator(200, 1.1);
        GeneratorAudioVisualizer gav = new GeneratorAudioVisualizer(generator);
        gav.drawAndPlay(4096, 1000000);
    }
}
