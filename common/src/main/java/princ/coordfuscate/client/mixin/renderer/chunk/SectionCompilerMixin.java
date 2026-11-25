package princ.coordfuscate.client.mixin.renderer.chunk;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.block.BlockRenderDispatcher;
import net.minecraft.client.renderer.block.model.BlockStateModel;
import net.minecraft.client.renderer.chunk.SectionCompiler;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.Mth;
import net.minecraft.util.Util;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.LevelChunk;
import net.minecraft.world.level.chunk.status.ChunkStatus;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import princ.coordfuscate.client.config.type.TextureRotation;

import java.util.Map;

import static princ.coordfuscate.client.Constants.*;

@Mixin(SectionCompiler.class)
public class SectionCompilerMixin {

    @Shadow
    @Final
    private BlockRenderDispatcher blockRenderer;

    @ModifyExpressionValue(
            method = {
                    "compile",
                    "compile(Lnet/minecraft/core/SectionPos;Lnet/minecraft/client/renderer/chunk/RenderSectionRegion;Lcom/mojang/blaze3d/vertex/VertexSorting;Lnet/minecraft/client/renderer/SectionBufferBuilderPack;Ljava/util/List;)Lnet/minecraft/client/renderer/chunk/SectionCompiler$Results;"
            },
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/level/block/state/BlockState;getSeed(Lnet/minecraft/core/BlockPos;)J"
            )
    )
    long coordfuscate$modifySeed(long seed, @Local(ordinal = 2) BlockPos pos) {
        TextureRotation textureRotation = config().textureRotation;
        int offset = minecraftDuck().coordfuscate$posOffset();
        if (textureRotation == TextureRotation.DEFAULT) {
            offset = 0;
        } else if (textureRotation == TextureRotation.RANDOM) {
            offset = posOffset(seed, offset);
        }
        return Mth.getSeed(pos.getX() + offset, pos.getY() + offset, pos.getZ() + offset);
    }

    @ModifyExpressionValue(
            method = {
                    "compile",
                    "compile(Lnet/minecraft/core/SectionPos;Lnet/minecraft/client/renderer/chunk/RenderSectionRegion;Lcom/mojang/blaze3d/vertex/VertexSorting;Lnet/minecraft/client/renderer/SectionBufferBuilderPack;Ljava/util/List;)Lnet/minecraft/client/renderer/chunk/SectionCompiler$Results;"
            },
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/renderer/block/BlockRenderDispatcher;getBlockModel(Lnet/minecraft/world/level/block/state/BlockState;)Lnet/minecraft/client/renderer/block/model/BlockStateModel;"
            )
    )
    BlockStateModel coordfuscate$disguiseBedrockModel(BlockStateModel blockModel, @Local(ordinal = 2) BlockPos pos, @Local BlockState blockState) {
        if (config().disguiseBedrock && blockState.is(Blocks.BEDROCK)) {
            if (config().disguiseBedrock$reveal) {
                Map<BlockPos, Long> reveal = minecraftDuck().coordfuscate$revealBedrock();
                if (reveal.get(pos) != null) {
                    if (Util.getMillis() - reveal.get(pos) < 1000L) {
                        return blockModel;
                    }
                    reveal.remove(pos, reveal.get(pos));
                }
            }
            Map<BlockPos, BlockStateModel> cache = minecraftDuck().coordfuscate$disguiseBedrockCache();
            return cache.computeIfAbsent(pos.immutable(), ignored -> this.coordfuscate$disguiseBedrockModel(minecraft(), blockModel, pos));
        }
        return blockModel;
    }

    @Unique
    BlockStateModel coordfuscate$disguiseBedrockModel(Minecraft minecraft, BlockStateModel blockModel, BlockPos pos) {
        ClientLevel level = minecraft.level;
        if (level == null) return blockModel;

        for (Direction direction : Direction.Plane.HORIZONTAL) {
            BlockPos disguisePos = pos.relative(direction);
            BlockState disguiseBlockState = level.getBlockState(disguisePos);
            if (config().disguiseBedrock$imitate.contains(disguiseBlockState.getBlock())) {
                return this.blockRenderer.getBlockModel(disguiseBlockState);
            }
        }

        for (Direction direction : Direction.Plane.HORIZONTAL) {
            BlockPos disguisePos = pos.relative(direction);
            BlockState disguiseBlockState = level.getBlockState(disguisePos);
            if (disguiseBlockState.is(Blocks.BEDROCK)) {
                BlockStateModel disguiseBlockModel = minecraftDuck().coordfuscate$disguiseBedrockCache().get(disguisePos.immutable());
                if (disguiseBlockModel != null) return disguiseBlockModel;
            }
        }

        for (Direction direction : Direction.Plane.VERTICAL) {
            BlockPos disguisePos = pos.relative(direction);
            BlockState disguiseBlockState = level.getBlockState(disguisePos);
            if (config().disguiseBedrock$imitate.contains(disguiseBlockState.getBlock())) {
                return this.blockRenderer.getBlockModel(disguiseBlockState);
            }
        }

        int y = pos.getY();
        boolean overworld = level.dimension() == Level.OVERWORLD;
        boolean nether = level.dimension() == Level.NETHER;

        boolean overworldBedrockLayer = y > -64 && y <= -60;
        boolean netherBedrockLayer = (y > 0 && y <= 4) || (y >= 123 && y < 127);

        if (overworld && !overworldBedrockLayer) {
            return blockModel;
        }
        if (nether && !netherBedrockLayer) {
            return blockModel;
        }
        if (!overworld && !nether) {
            return blockModel;
        }

        int[] searchYs;
        if (overworld) {
            searchYs = new int[] { -63, -62, -61, -60 };
        } else if (y <= 4) {
            searchYs = new int[] { 1, 2, 3, 4 };
        } else {
            searchYs = new int[] { 123, 124, 125, 126 };
        }

        int chunkX = pos.getX() >> 4;
        int chunkZ = pos.getZ() >> 4;
        int renderDistance = minecraft.options.renderDistance().get();

        for (int radius = 0; radius <= renderDistance; radius++) {
            for (int searchChunkX = chunkX - radius; searchChunkX <= chunkX + radius; searchChunkX++) {
                for (int searchChunkZ = chunkZ - radius; searchChunkZ <= chunkZ + radius; searchChunkZ++) {
                    if (Math.max(Math.abs(searchChunkX - chunkX), Math.abs(searchChunkZ - chunkZ)) != radius) {
                        continue;
                    }
                    LevelChunk chunk = level.getChunkSource().getChunk(searchChunkX, searchChunkZ, ChunkStatus.FULL, false);
                    if (chunk == null) continue;

                    int minX = searchChunkX << 4;
                    int minZ = searchChunkZ << 4;

                    for (int x = minX; x < minX + 16; x++) {
                        for (int z = minZ; z < minZ + 16; z++) {
                            for (int searchY : searchYs) {
                                BlockPos disguisePos = new BlockPos(x, searchY, z);
                                BlockState disguiseBlockState = chunk.getBlockState(disguisePos);
                                if (config().disguiseBedrock$imitate.contains(disguiseBlockState.getBlock())) {
                                    return this.blockRenderer.getBlockModel(disguiseBlockState);
                                }
                            }
                        }
                    }
                }
            }
        }

        return blockModel;
    }
}
