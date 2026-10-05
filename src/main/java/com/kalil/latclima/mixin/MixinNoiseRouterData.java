package com.kalil.latclima.mixin;

import net.minecraft.core.HolderGetter;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.levelgen.DensityFunction;
import net.minecraft.world.level.levelgen.NoiseRouter;
import net.minecraft.world.level.levelgen.NoiseRouterData;
import net.minecraft.world.level.levelgen.synth.NormalNoise;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(NoiseRouterData.class)
public class MixinNoiseRouterData {

    @Inject(method = "overworld", at = @At("RETURN"), cancellable = true)
    private static void latclima$injectClimate(HolderGetter<DensityFunction> densityFunctions, HolderGetter<NormalNoise.NoiseParameters> noiseParameters, boolean isLargeBiomes, boolean isAmplified, CallbackInfoReturnable<NoiseRouter> cir) {
        NoiseRouter original = cir.getReturnValue();

        // Resgata os seus arquivos JSON de clima
        DensityFunction temperature = densityFunctions.getOrThrow(ResourceKey.create(Registries.DENSITY_FUNCTION, ResourceLocation.parse("latclima:temperature"))).value();
        DensityFunction vegetation = densityFunctions.getOrThrow(ResourceKey.create(Registries.DENSITY_FUNCTION, ResourceLocation.parse("latclima:humidity"))).value();

        // Reconstrói o roteador de ruído do mundo mantendo o Tectonic, mas injetando seu clima
        NoiseRouter modified = new NoiseRouter(
                original.barrierNoise(),
                original.fluidLevelFloodednessNoise(),
                original.fluidLevelSpreadNoise(),
                original.lavaNoise(),
                temperature, // Sua temperatura!
                vegetation,  // Sua umidade!
                original.continents(),
                original.erosion(),
                original.depth(),
                original.ridges(),
                original.initialDensityWithoutJaggedness(),
                original.finalDensity(),
                original.veinToggle(),
                original.veinRidged(),
                original.veinGap()
        );

        cir.setReturnValue(modified);
    }
}