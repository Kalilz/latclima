package com.kalil.latclima;

import net.neoforged.fml.config.ModConfig;
import com.mojang.serialization.MapCodec;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.levelgen.DensityFunction;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.fml.ModContainer;

@Mod(LatClima.MOD_ID)
public class LatClima {
    public static final String MOD_ID = "latclima";

    public static final DeferredRegister<MapCodec<? extends DensityFunction>> DF =
            DeferredRegister.create(Registries.DENSITY_FUNCTION_TYPE, MOD_ID);

    public static final DeferredHolder<MapCodec<? extends DensityFunction>, MapCodec<LatitudeClimate>> LATITUDE =
            DF.register("latitude", () -> LatitudeClimate.MAP_CODEC);

    public LatClima(IEventBus modEventBus, ModContainer modContainer) {
        DF.register(modEventBus);
        // Registrando a configuração recém-criada
        modContainer.registerConfig(ModConfig.Type.COMMON, Config.SPEC);
    }
}