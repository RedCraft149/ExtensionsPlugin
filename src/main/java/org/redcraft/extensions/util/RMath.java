package org.redcraft.extensions.util;

import java.util.Random;

public class RMath {


    private static final Random random;
    static {
        random = new Random(0);
    }

    public static float PI = (float) Math.PI;
    public static float EPSILON = 0.000001f;

    public static float random() {
        return random.nextFloat();
    }
    public static float random(float start, float end) {
        return random()*(end-start)+start;
    }
    public static float random(float precision) {
        return floor(random()/precision)*precision;
    }

    //Modular Arithmetic
    public static float nearestModularValue(float x, float p, float d) {
        x = mod(x,d);
        float s = x-p;
        float greatestDistance = d / 2f;
        if(abs(s)<=greatestDistance) return x;
        if(s<0) return x+d;
        else return x-d;
    }
    public static float modularDistance(float a, float b, float d) {
        a = nearestModularValue(a,b,d);
        return abs(a-b);
    }

    /**
     * Returns the sign of the number that, when added to x, has the smallest absolute value to equal dst.
     * @param x Current value
     * @param dst Destination value
     * @param b base
     * @return sign(a) : minimize(|a|) and x+a=dst mod b
     */
    public static int nearestModularDirection(float x, float dst, float b) {
        float b2 = b * 0.5f;
        float da = (dst-x + b2) % b - b2;
        if(da<0) return -1;
        if(da>0) return  1;
        return 0;
    }

    /**
     * @return {@code x mod b}, guaranteed to be positive for all {@code b > 0}.
     */
    public static float mod(float a, float b) {
        if(abs(b)<EPSILON) throw new ArithmeticException("Invalid argument: base cannot be 0.");
        if(a<0) a += -floor(a/b)*b;
        return a % b;
    }

    /**
     * See {@link RMath#mod(float, float)}.
     */
    public static int mod(int a, int b) {
        if(b==0) throw new ArithmeticException("Invalid argument: base cannot be 0.");
        while (a<0) a += b;
        return a % b;
    }


    //ceil, floor, abs
    public static int ceil(float a) {
        return (int) Math.ceil(a);
    }
    public static int floor(float a) {
        return (int) Math.floor(a);
    }
    public static float abs(float a) {
        return Math.abs(a);
    }

    public static int round(float a) {
        return Math.round(a);
    }
    public static float round(float a, float precision) {
        return round(a/precision)*precision;
    }
    public static float sin(float a) {
        return (float) Math.sin(a);
    }
    public static float cos(float a) {
        return (float) Math.cos(a);
    }

    /**
     * <p>Rounds {@code a} to the closest number b = 2\u03C0\u00D7precision\u00D7n; n \u2208 \u2115.
     */
    public static float roundRadians(float a, float precision) {
        float factor = 2f * PI * precision;
        int n = round(a / factor);
        return n*factor;
    }
}
