package com.kalil.latclima;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.util.KeyDispatchDataCodec;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.level.levelgen.DensityFunction;

public record LatitudeClimate(Kind kind, double radius) implements DensityFunction.SimpleFunction {

    public enum Kind implements StringRepresentable {
        TEMPERATURE, HUMIDITY;

        @Override
        public String getSerializedName() {
            return name().toLowerCase();
        }
    }

    private static final double[] LAT   = {0, 10, 20, 30, 40, 50, 60, 70, 90};
    private static final double[] TEMP  = {1.00, 0.90, 0.67, 0.43, 0.20, -0.03, -0.27, -0.45, -1.00};
    private static final double[] HUMID = {0.60, 0.35, -0.45, -0.50, -0.10, 0.30, 0.35, -0.10, -0.60};

    public static final MapCodec<LatitudeClimate> MAP_CODEC = RecordCodecBuilder.mapCodec(i -> i.group(
            StringRepresentable.fromEnum(Kind::values).fieldOf("kind").forGetter(LatitudeClimate::kind),
            Codec.doubleRange(1000, 10_000_000).fieldOf("radius").forGetter(LatitudeClimate::radius)
    ).apply(i, LatitudeClimate::new));

    public static final KeyDispatchDataCodec<LatitudeClimate> CODEC = KeyDispatchDataCodec.of(MAP_CODEC);

    @Override
    public double compute(FunctionContext ctx) {
        double lat = Math.min(Math.abs((double) ctx.blockZ()) / radius, 1.0) * 90.0;
        return interp(lat, kind == Kind.TEMPERATURE ? TEMP : HUMID);
    }

    // smoothstep entre pontos de controle: platos zonais com transicoes suaves
    private static double interp(double x, double[] ys) {
        for (int i = 1; i < LAT.length; i++) {
            if (x <= LAT[i]) {
                double t = (x - LAT[i - 1]) / (LAT[i] - LAT[i - 1]);
                t = t * t * (3 - 2 * t);
                return ys[i - 1] + (ys[i] - ys[i - 1]) * t;
            }
        }
        return ys[ys.length - 1];
    }

    @Override public double minValue() { return -1.0; }
    @Override public double maxValue() { return 1.0; }
    @Override public KeyDispatchDataCodec<? extends DensityFunction> codec() { return CODEC; }
}
