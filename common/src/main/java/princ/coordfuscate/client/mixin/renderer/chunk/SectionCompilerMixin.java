package princ.coordfuscate.client.mixin.renderer.chunk;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.block.BlockStateModelSet;
import net.minecraft.client.renderer.block.dispatch.BlockStateModel;
import net.minecraft.client.renderer.chunk.SectionCompiler;
import net.minecraft.core.BlockPos;
import net.minecraft.util.Mth;
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
    private BlockStateModelSet blockModelSet;

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
    long coordfuscate$modifySeed(long seed, @Local(name = "pos") BlockPos pos) {
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
                    target = "Lnet/minecraft/client/renderer/block/BlockStateModelSet;get(Lnet/minecraft/world/level/block/state/BlockState;)Lnet/minecraft/client/renderer/block/dispatch/BlockStateModel;"
            )
    )
    BlockStateModel coordfuscate$disguiseBedrockModel(BlockStateModel blockModel, @Local(name = "pos") BlockPos pos, @Local(name = "blockState") BlockState blockState) {
        if (config().disguiseBedrock && blockState.is(Blocks.BEDROCK)) {
            Map<BlockPos, BlockStateModel> cache = minecraftDuck().coordfuscate$disguiseBedrockCache();
            BlockState disguiseBlockState = this.coordfuscate$disguiseBlockState(minecraft(), blockState, pos);
            return cache.computeIfAbsent(pos.immutable(), ignored -> this.blockModelSet.get(disguiseBlockState));
        }
        return blockModel;
    }

    @Unique
    BlockState coordfuscate$disguiseBlockState(Minecraft minecraft, BlockState blockState, BlockPos pos) {
        ClientLevel level = minecraft.level;
        if (level == null) return blockState;

        int y = pos.getY();
        boolean overworld = level.dimension() == Level.OVERWORLD;
        boolean nether = level.dimension() == Level.NETHER;

        boolean overworldBedrockLayer = y > -64 && y <= -60;
        boolean netherBedrockLayer = (y > 0 && y <= 4) || (y >= 123 && y < 127);

        if (overworld && !overworldBedrockLayer) {
            return blockState;
        }
        if (nether && !netherBedrockLayer) {
            return blockState;
        }
        if (!overworld && !nether) {
            return blockState;
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
                                if (config().disguiseBedrock$whitelist.contains(disguiseBlockState.getBlock())) {
                                    return disguiseBlockState;
                                }
                            }
                        }
                    }
                }
            }
        }

        return blockState;
    }
}
