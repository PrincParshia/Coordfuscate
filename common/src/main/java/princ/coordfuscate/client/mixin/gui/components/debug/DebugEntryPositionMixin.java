package princ.coordfuscate.client.mixin.gui.components.debug;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import net.minecraft.client.gui.components.debug.DebugEntryPosition;
import net.minecraft.resources.Identifier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import princ.coordfuscate.client.config.type.DebugInfo;

import static princ.coordfuscate.client.Constants.*;

@Mixin(DebugEntryPosition.class)
public class DebugEntryPositionMixin {

    @ModifyExpressionValue(
            method = "display",
            at = {
                    @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/Entity;getX()D"),
                    @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/Entity;getY()D"),
                    @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/Entity;getZ()D")
            }
    )
    double coordfuscate$maskXYZ(double coordinate) {
        if (config().debugInfo.mask$xyz && maskDebugInfo(DebugInfo.XYZ)) {
            return 0;
        }
        return coordinate;
    }

    @ModifyExpressionValue(
            method = "display",
            at = {
                    @At(value = "INVOKE", target = "Lnet/minecraft/core/BlockPos;getX()I"),
                    @At(value = "INVOKE", target = "Lnet/minecraft/core/BlockPos;getY()I"),
                    @At(value = "INVOKE", target = "Lnet/minecraft/core/BlockPos;getZ()I")
            }
    )
    int coordfuscate$maskBlock(int coordinate) {
        if (config().debugInfo.mask$block && maskDebugInfo(DebugInfo.BLOCK)) {
            return 0;
        }
        return coordinate;
    }

    @ModifyExpressionValue(
            method = "display",
            at = {
                    @At(value = "INVOKE", target = "Lnet/minecraft/world/level/ChunkPos;x()I"),
                    @At(value = "INVOKE", target = "Lnet/minecraft/world/level/ChunkPos;z()I"),
                    @At(value = "INVOKE", target = "Lnet/minecraft/world/level/ChunkPos;getRegionLocalX()I"),
                    @At(value = "INVOKE", target = "Lnet/minecraft/world/level/ChunkPos;getRegionLocalZ()I"),
                    @At(value = "INVOKE", target = "Lnet/minecraft/world/level/ChunkPos;getRegionX()I"),
                    @At(value = "INVOKE", target = "Lnet/minecraft/world/level/ChunkPos;getRegionZ()I")
            }
    )
    int coordfuscate$maskChunk(int coordinate) {
        if (config().debugInfo.mask$chunk && maskDebugInfo(DebugInfo.CHUNK)) {
            return 0;
        }
        return coordinate;
    }

    @ModifyArg(
            method = "display",
            at = @At(
                    value = "INVOKE",
                    target = "Ljava/lang/String;format(Ljava/util/Locale;Ljava/lang/String;[Ljava/lang/Object;)Ljava/lang/String;",
                    ordinal = 3
            ),
            index = 2
    )
    Object[] coordfuscate$maskFacingDirection(Object[] args) {
        if (config().debugInfo.mask$facing$direction && maskDebugInfo(DebugInfo.FACING$DIRECTION)) {
            args[0] = "-";
        }
        if (config().debugInfo.mask$facing$face && maskDebugInfo(DebugInfo.FACING$FACE)) {
            args[1] = "-";
        }
        return args;
    }

    @ModifyExpressionValue(
            method = "display",
            at = {
                    @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/Entity;getYRot()F"),
                    @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/Entity;getXRot()F")
            }
    )
    float coordfuscate$maskFacingYXRot(float rot) {
        if (config().debugInfo.mask$facing$yxrot && maskDebugInfo(DebugInfo.FACING$YXROT)) {
            return 0;
        }
        return rot;
    }

    @ModifyExpressionValue(
            method = "display",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/resources/ResourceKey;identifier()Lnet/minecraft/resources/Identifier;"
            )
    )
    Identifier coordfuscate$maskDimension(Identifier identifier) {
        if (config().debugInfo.mask$dimension && maskDebugInfo(DebugInfo.DIMENSION)) {
            return Identifier.withDefaultNamespace("-");
        }
        return identifier;
    }
}
