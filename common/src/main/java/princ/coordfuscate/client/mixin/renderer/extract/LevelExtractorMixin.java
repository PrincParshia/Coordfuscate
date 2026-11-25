package princ.coordfuscate.client.mixin.renderer.extract;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.extract.LevelExtractor;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import princ.coordfuscate.client.duck.MinecraftDuck;

import static princ.coordfuscate.client.Constants.posOffset;

@Mixin(LevelExtractor.class)
public class LevelExtractorMixin {

    @Shadow
    @Final
    private Minecraft minecraft;

    @Inject(method = "allChanged", at = @At("HEAD"))
    void coordfuscate$refresh(CallbackInfo callbackInfo) {
        MinecraftDuck duck = (MinecraftDuck) this.minecraft;
        duck.coordfuscate$posOffset(posOffset());
        duck.coordfuscate$disguiseBedrockCache().clear();
    }
}
