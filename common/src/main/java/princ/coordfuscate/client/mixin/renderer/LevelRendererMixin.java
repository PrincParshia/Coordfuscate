package princ.coordfuscate.client.mixin.renderer;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.LevelRenderer;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import princ.coordfuscate.client.config.type.CloudPosition;
import princ.coordfuscate.client.duck.MinecraftDuck;

import static princ.coordfuscate.client.Constants.*;

@Mixin(LevelRenderer.class)
public class LevelRendererMixin {

    @Shadow
    @Final
    private Minecraft minecraft;

    @Inject(method = "allChanged", at = @At("HEAD"))
    void coordfuscate$refresh(CallbackInfo callbackInfo) {
        MinecraftDuck duck = (MinecraftDuck) this.minecraft;
        duck.coordfuscate$posOffset(posOffset());
        duck.coordfuscate$disguiseBedrockCache().clear();
    }

    @ModifyVariable(method = "renderClouds", at = @At("HEAD"), ordinal = 0, argsOnly = true)
    double coordfuscate$modifyCameraX(double camX) {
        if (config().cloudPosition == CloudPosition.RANDOM) {
            int offset = minecraftDuck().coordfuscate$posOffset();
            return camX + offset;
        }
        return camX;
    }

    @ModifyVariable(method = "renderClouds", at = @At("HEAD"), ordinal = 2, argsOnly = true)
    double coordfuscate$modifyCameraZ(double camZ) {
        if (config().cloudPosition == CloudPosition.RANDOM) {
            int offset = minecraftDuck().coordfuscate$posOffset();
            return camZ + offset;
        }
        return camZ;
    }
}
