package princ.coordfuscate.client.mixin;

import me.fzzyhmstrs.fzzy_config.validation.misc.ValidatedKeybind;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.block.model.BlockStateModel;
import net.minecraft.core.BlockPos;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import princ.coordfuscate.client.duck.MinecraftDuck;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import static princ.coordfuscate.client.Constants.config;

@Mixin(Minecraft.class)
public class MinecraftMixin implements MinecraftDuck {

    @Unique
    int coordfuscate$seedOffset;

    @Unique
    boolean coordfuscate$debugInfo$mask;

    @Unique
    final Map<BlockPos, BlockStateModel> coordfuscate$disguiseBedrockCache = new ConcurrentHashMap<>();

    @Unique
    final Map<BlockPos, Long> coordfuscate$revealBedrock = new ConcurrentHashMap<>();

    @Override
    public int coordfuscate$posOffset() {
        return this.coordfuscate$seedOffset;
    }

    @Override
    public void coordfuscate$posOffset(int offset) {
        this.coordfuscate$seedOffset = offset;
    }

    @Override
    public Map<BlockPos, BlockStateModel> coordfuscate$disguiseBedrockCache() {
        return this.coordfuscate$disguiseBedrockCache;
    }

    @Override
    public Map<BlockPos, Long> coordfuscate$revealBedrock() {
        return this.coordfuscate$revealBedrock;
    }

    @Override
    public boolean coordfuscate$maskDebugInfo() {
        return this.coordfuscate$debugInfo$mask;
    }

    @Override
    public void coordfuscate$maskDebugInfo(boolean mask) {
        ValidatedKeybind key = config().debugInfo.unmask$key;
        if (mask && key.isPressed()) {
            this.coordfuscate$debugInfo$mask = true;
        } else if (!mask) {
            this.coordfuscate$debugInfo$mask = false;
        }
    }
}
