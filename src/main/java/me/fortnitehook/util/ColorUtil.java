package me.fortnitehook.util;

import me.fortnitehook.features.modules.client.ClickGui;
import me.fortnitehook.features.modules.combat.CrystalAura;

import java.awt.*;
import java.util.HashMap;
import java.util.Map;

public class ColorUtil {
    public static int toARGB(int r, int g, int b, int a) {
        return new Color(r, g, b, a).getRGB();
    }

    public static int toRGBA(int r, int g, int b) {
        return ColorUtil.toRGBA(r, g, b, 255);
    }

    public static int toRGBA(int r, int g, int b, int a) {
        return (r << 16) + (g << 8) + b + (a << 24);
    }

    public static int toRGBA(float r, float g, float b, float a) {
        return ColorUtil.toRGBA((int) (r * 255.0f), (int) (g * 255.0f), (int) (b * 255.0f), (int) (a * 255.0f));
    }

    public static Color rainbow(int n) {
        double d = Math.ceil((double)(System.currentTimeMillis() + (long)n) / 20.0);
        return Color.getHSBColor((float)(d % 360.0 / 360.0), ClickGui.INSTANCE().rainbowSaturation.getValue() / 255.0f, ClickGui.INSTANCE().rainbowBrightness.getValue() / 255.0f);
    }

    public static Color gradientRainbow(int n) {
        double d = Math.ceil((double) (System.currentTimeMillis() + (long) n) / 20.0);
        return Color.getHSBColor((float) ((d %= 360.0) / 360.0), CrystalAura.getInstance().rainbowSaturation.getValue().floatValue() / 255.0f, CrystalAura.getInstance().rainbowBrightness.getValue().floatValue() / 255.0f);
    }

    public static Color invert(Color color) {
        int n;
        int n2;
        int n3 = color.getAlpha();
        int n4 = 255 - color.getRed();
        if (n4 + (n2 = 255 - color.getGreen()) + (n = 255 - color.getBlue()) > 740 || n4 + n2 + n < 20) {
            return new Color(255, 255, 40, n3);
        }
        return new Color(n4, n2, n, n3);
    }




    public static Color rainbowColor(Integer saturation, Integer brightness, int delay) {
        float hue;
        Map<Integer, Integer> colorHeightMap = new HashMap<Integer, Integer>();
        int colorDelay = 101 - delay;
        float tempHue = hue = (float) (System.currentTimeMillis() % (360L * colorDelay)) / (360.0f * (float) colorDelay);
        for (int i = 0; i <= 510; ++i) {
            colorHeightMap.put(i, Color.HSBtoRGB(tempHue, (float) saturation / 255.0f, (float) brightness / 255.0f));
            tempHue += 0.0013071896f;
        }
        return Color.getHSBColor(hue, (float) saturation / 255.0f, (float) brightness / 255.0f);
    }


    public static Color rainbow(Integer saturation, Integer brightness, int delay) {
        double rainbowState = Math.ceil((System.currentTimeMillis() + delay) / 20.0);

        return pulseColor(rainbowColor(saturation, brightness, delay), 50, delay);
    }

    public static int toRGBA(float[] colors) {
        if (colors.length != 4) {
            throw new IllegalArgumentException("colors[] must have a length of 4!");
        }
        return ColorUtil.toRGBA(colors[0], colors[1], colors[2], colors[3]);
    }

    public static int toRGBA(double[] colors) {
        if (colors.length != 4) {
            throw new IllegalArgumentException("colors[] must have a length of 4!");
        }
        return ColorUtil.toRGBA((float) colors[0], (float) colors[1], (float) colors[2], (float) colors[3]);
    }

    public static Color pulseColor(Color color, int n, int n2) {
        float[] fArray = new float[3];
        Color.RGBtoHSB(color.getRed(), color.getGreen(), color.getBlue(), fArray);
        float f = Math.abs(((float)(System.currentTimeMillis() % 2000L) / Float.intBitsToFloat(Float.floatToIntBits(0.0013786979f) ^ 0x7ECEB56D) + (float)n / (float)n2 * Float.intBitsToFloat(Float.floatToIntBits(0.09192204f) ^ 0x7DBC419F)) % Float.intBitsToFloat(Float.floatToIntBits(0.7858098f) ^ 0x7F492AD5) - Float.intBitsToFloat(Float.floatToIntBits(6.46708f) ^ 0x7F4EF252));
        f = Float.intBitsToFloat(Float.floatToIntBits(18.996923f) ^ 0x7E97F9B3) + Float.intBitsToFloat(Float.floatToIntBits(2.7958195f) ^ 0x7F32EEB5) * f;
        fArray[2] = f % Float.intBitsToFloat(Float.floatToIntBits(0.8992331f) ^ 0x7F663424);
        return new Color(Color.HSBtoRGB(fArray[0], fArray[1], fArray[2]));
    }
    public static int toRGBA(Color color) {
        return ColorUtil.toRGBA(color.getRed(), color.getGreen(), color.getBlue(), color.getAlpha());
    }
}

