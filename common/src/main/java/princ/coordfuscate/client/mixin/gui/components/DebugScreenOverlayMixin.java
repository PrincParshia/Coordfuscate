package princ.coordfuscate.client.mixin.gui.components;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import net.minecraft.client.gui.components.DebugScreenOverlay;
import net.minecraft.resources.ResourceLocation;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import princ.coordfuscate.client.config.type.DebugInfo;

import static princ.coordfuscate.client.Constants.*;

@Mixin(DebugScreenOverlay.class)
public class DebugScreenOverlayMixin {

    @ModifyExpressionValue(
            method = "getGameInformation",
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

    @ModifyArg(
            method = "getGameInformation",
            at = @At(
                    value = "INVOKE",
                    target = "Ljava/lang/String;format(Ljava/util/Locale;Ljava/lang/String;[Ljava/lang/Object;)Ljava/lang/String;",
                    ordinal = 4
            ),
            index = 2
    )
    Object[] coordfuscate$maskBlock(Object[] args) {
        if (config().debugInfo.mask$block && maskDebugInfo(DebugInfo.BLOCK)) {
            for (int i = 0; i <= 6; i++) {
                args[i] = 0;
            }
        }
        return args;
    }

    @ModifyExpressionValue(
            method = "getGameInformation",
            at = {
                    @At(value = "FIELD", target = "Lnet/minecraft/world/level/ChunkPos;x:I", opcode = Opcodes.GETFIELD),
                    @At(value = "FIELD", target = "Lnet/minecraft/world/level/ChunkPos;z:I", opcode = Opcodes.GETFIELD),
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
            method = "getGameInformation",
            at = @At(
                    value = "INVOKE",
                    target = "Ljava/lang/String;format(Ljava/util/Locale;Ljava/lang/String;[Ljava/lang/Object;)Ljava/lang/String;",
                    ordinal = 6
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
            method = "getGameInformation",
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
            method = "getGameInformation",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/resources/ResourceKey;location()Lnet/minecraft/resources/ResourceLocation;"
            )
    )
    ResourceLocation coordfuscate$maskDimension(ResourceLocation identifier) {
        if (config().debugInfo.mask$dimension && maskDebugInfo(DebugInfo.DIMENSION)) {
            return ResourceLocation.withDefaultNamespace("-");
        }
        return identifier;
    }

    @ModifyReturnValue(method = "printBiome", at = @At("RETURN"))
    private static String coordfuscate$maskBiome(String biome) {
        if (config().debugInfo.mask$biome && maskDebugInfo(DebugInfo.BIOME)) {
            return "minecraft:-";
        }
        return biome;
    }

    @ModifyExpressionValue(
            method = "getSystemInformation",
            at = {
                    @At(value = "INVOKE", target = "Lnet/minecraft/core/BlockPos;getX()I"),
                    @At(value = "INVOKE", target = "Lnet/minecraft/core/BlockPos;getY()I"),
                    @At(value = "INVOKE", target = "Lnet/minecraft/core/BlockPos;getZ()I")
            }
    )
    int coordfuscate$maskLookingAtXYZ(int coordinate) {
        if (config().debugInfo.mask$lookingAt$xyz && maskDebugInfo(DebugInfo.LOOKING_AT$XYZ)) {
            return 0;
        }
        return coordinate;
    }

    @ModifyExpressionValue(
            method = "getSystemInformation",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/core/DefaultedRegistry;getKey(Ljava/lang/Object;)Lnet/minecraft/resources/ResourceLocation;"
            )
    )
    ResourceLocation coordfuscate$maskLookingAtName(ResourceLocation identifier) {
        if (config().debugInfo.mask$lookingAt$name && maskDebugInfo(DebugInfo.LOOKING_AT$NAME)) {
            return ResourceLocation.withDefaultNamespace("-");
        }
        return identifier;
    }

    @ModifyExpressionValue(
            method = "getGameInformation",
            at = {
                    @At(value = "INVOKE", target = "Lnet/minecraft/core/BlockPos;getX()I"),
                    @At(value = "INVOKE", target = "Lnet/minecraft/core/BlockPos;getY()I"),
                    @At(value = "INVOKE", target = "Lnet/minecraft/core/BlockPos;getZ()I")
            }
    )
    int coordfuscate$maskChunkXYZ(int coordinate) {
        if (config().debugInfo.mask$chunkPosition && maskDebugInfo(DebugInfo.CHUNK_POSITION)) {
            return 0;
        }
        return coordinate;
    }
}
