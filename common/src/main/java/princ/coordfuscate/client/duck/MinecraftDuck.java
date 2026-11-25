package princ.coordfuscate.client.duck;

import net.minecraft.client.renderer.block.model.BlockStateModel;
import net.minecraft.core.BlockPos;

import java.util.Map;

public interface MinecraftDuck {
    int coordfuscate$posOffset();
    void coordfuscate$posOffset(int offset);
    Map<BlockPos, BlockStateModel> coordfuscate$disguiseBedrockCache();
    Map<BlockPos, Long> coordfuscate$revealBedrock();
    boolean coordfuscate$maskDebugInfo();
    void coordfuscate$maskDebugInfo(boolean mask);
}
