package princ.coordfuscate.client.mixin.gui.components.debug;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import net.minecraft.client.gui.components.debug.DebugEntryBiome;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import princ.coordfuscate.client.config.type.DebugInfo;

import static princ.coordfuscate.client.Constants.*;

@Mixin(DebugEntryBiome.class)
public class DebugEntryBiomeMixin {

    @ModifyReturnValue(method = "printBiome", at = @At("RETURN"))
    private static String coordfuscate$maskBiome(String biome) {
        if (config().debugInfo.mask$biome && maskDebugInfo(DebugInfo.BIOME)) {
            return "minecraft:-";
        }
        return biome;
    }
}
