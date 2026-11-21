package princ.coordfuscate.client.mixin.gui.components.debug;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import net.minecraft.client.gui.components.debug.DebugEntrySectionPosition;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import princ.coordfuscate.client.config.type.DebugInfo;

import static princ.coordfuscate.client.Constants.*;

@Mixin(DebugEntrySectionPosition.class)
public class DebugEntrySectionPositionMixin {

    @ModifyExpressionValue(
            method = "display",
            at = {
                    @At(value = "INVOKE", target = "Lnet/minecraft/core/BlockPos;getX()I"),
                    @At(value = "INVOKE", target = "Lnet/minecraft/core/BlockPos;getY()I"),
                    @At(value = "INVOKE", target = "Lnet/minecraft/core/BlockPos;getZ()I")
            }
    )
    int coordfuscate$maskXYZ(int coordinate) {
        if (config().debugInfo.mask$sectionPosition && maskDebugInfo(DebugInfo.SECTION_POSITION)) {
            return 0;
        }
        return coordinate;
    }
}
