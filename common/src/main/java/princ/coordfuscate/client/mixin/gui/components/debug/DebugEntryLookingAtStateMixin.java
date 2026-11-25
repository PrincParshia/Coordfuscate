package princ.coordfuscate.client.mixin.gui.components.debug;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import net.minecraft.client.gui.components.debug.DebugEntryLookingAtBlock;
import net.minecraft.resources.Identifier;
import org.jspecify.annotations.NonNull;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import princ.coordfuscate.client.config.type.DebugInfo;

import static princ.coordfuscate.client.Constants.*;

@Mixin(DebugEntryLookingAtBlock.class)
public class DebugEntryLookingAtStateMixin {

    @ModifyExpressionValue(
            method = "display",
            at = {
                    @At(value = "INVOKE", target = "Lnet/minecraft/core/BlockPos;getX()I"),
                    @At(value = "INVOKE", target = "Lnet/minecraft/core/BlockPos;getY()I"),
                    @At(value = "INVOKE", target = "Lnet/minecraft/core/BlockPos;getZ()I")
            }
    )
    int coordfuscate$maskXYZ(int coordinate) {
        if (config().debugInfo.mask$lookingAt$xyz && maskDebugInfo(DebugInfo.LOOKING_AT$XYZ)) {
            return 0;
        }
        return coordinate;
    }

    @ModifyExpressionValue(
            method = "display",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/core/DefaultedRegistry;getKey(Ljava/lang/Object;)Lnet/minecraft/resources/Identifier;"
            )
    )
    Identifier coordfuscate$maskName(Identifier identifier) {
        if (config().debugInfo.mask$lookingAt$name && maskDebugInfo(DebugInfo.LOOKING_AT$NAME)) {
            return Identifier.withDefaultNamespace("-");
        }
        return identifier;
    }
}
