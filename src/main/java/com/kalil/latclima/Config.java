package com.kalil.latclima;

import net.neoforged.neoforge.common.ModConfigSpec;

public class Config {
    public static final ModConfigSpec.Builder BUILDER = new ModConfigSpec.Builder();
    public static final ModConfigSpec SPEC;

    public static final ModConfigSpec.DoubleValue POLAR_RADIUS;
    public static final ModConfigSpec.IntValue ALTITUDE_THRESHOLD;
    public static final ModConfigSpec.DoubleValue ALTITUDE_DROP;

    static {
        BUILDER.push("Configuracoes de Clima e Altitude");
        
        POLAR_RADIUS = BUILDER.comment("Distância (Z) máxima até os polos onde a temperatura chega ao mínimo")
                .defineInRange("polarRadius", 60000.0, 1000.0, 10000000.0);
                
        ALTITUDE_THRESHOLD = BUILDER.comment("Altura (Y) onde o frio começa a aumentar. Configurado para 340 para picos altissimos.")
                .defineInRange("altitudeThreshold", 340, -64, 1000);
                
        ALTITUDE_DROP = BUILDER.comment("Queda de temperatura por bloco acima do limite")
                .defineInRange("altitudeDrop", 0.005, 0.0, 0.1);
                
        BUILDER.pop();
        SPEC = BUILDER.build();
    }
}