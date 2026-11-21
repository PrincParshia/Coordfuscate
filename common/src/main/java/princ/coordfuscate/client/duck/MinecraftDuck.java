package princ.coordfuscate.client.duck;

import net.minecraft.client.renderer.block.dispatch.BlockStateModel;
import net.minecraft.core.BlockPos;

import java.util.Map;

public interface MinecraftDuck {
    int coordfuscate$posOffset();
    void coordfuscate$posOffset(int offset);
    Map<BlockPos, BlockStateModel> coordfuscate$disguiseBedrockCache();
    boolean coordfuscate$maskDebugInfo();
    void coordfuscate$maskDebugInfo(boolean mask);
}
