package frc.robot;

import edu.wpi.first.wpilibj.AnalogInput;

public class MB1013 {
    private AnalogInput in;

    public MB1013(int n) {
        in = new AnalogInput(n);
        in.setAverageBits(4);
    }

    public int distMM() {
        return in.getAverageValue() / 4 * 5;
    }

    public AnalogInput getInput() {
        return in;
    }
}