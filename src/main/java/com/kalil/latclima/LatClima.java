package com.kalil.latclima;

import com.mojang.serialization.MapCodec;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.levelgen.DensityFunction;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

@Mod(LatClima.MOD_ID)
public class LatClima {
    public static final String MOD_ID = "latclima";

    public static final DeferredRegister<MapCodec<? extends DensityFunction>> DF =
            DeferredRegister.create(Registries.DENSITY_FUNCTION_TYPE, MOD_ID);

    public static final DeferredHolder<MapCodec<? extends DensityFunction>, MapCodec<LatitudeClimate>> LATITUDE =
            DF.register("latitude", () -> LatitudeClimate.MAP_CODEC);

    public LatClima(IEventBus modEventBus) {
        DF.register(modEventBus);
    }
}
