package princ.coordfuscate.client.mixin.multiplayer;

import net.minecraft.Util;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.multiplayer.MultiPlayerGameMode;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.Blocks;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import princ.coordfuscate.client.duck.MinecraftDuck;

@Mixin(MultiPlayerGameMode.class)
public class MultiPlayerGameModeMixin {

    @Shadow
    @Final
    private Minecraft minecraft;

    @Inject(method = "startDestroyBlock", at = @At("HEAD"))
    void coordfuscate$revealBedrock(BlockPos pos, Direction direction, CallbackInfoReturnable<Boolean> callbackInfoReturnable) {
        ClientLevel level = this.minecraft.level;
        if (level != null && level.getBlockState(pos).is(Blocks.BEDROCK)) {
            ((MinecraftDuck) this.minecraft).coordfuscate$revealBedrock().put(pos.immutable(), Util.getMillis());
            this.minecraft.levelRenderer.setBlocksDirty(pos.getX(), pos.getY(), pos.getZ(), pos.getX(), pos.getY(), pos.getZ());
        }
    }
}
