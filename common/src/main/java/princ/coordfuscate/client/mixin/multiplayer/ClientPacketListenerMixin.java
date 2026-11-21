package princ.coordfuscate.client.mixin.multiplayer;

import net.minecraft.client.multiplayer.ClientPacketListener;
import net.minecraft.network.protocol.game.ClientboundPlayerPositionPacket;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import static princ.coordfuscate.client.Constants.minecraftDuck;

@Mixin(ClientPacketListener.class)
public class ClientPacketListenerMixin {

    @Inject(method = "handleMovePlayer", at = @At("HEAD"))
    void coordfuscate$smartMaskDebugInfo(ClientboundPlayerPositionPacket packet, CallbackInfo callbackInfo) {
        minecraftDuck().coordfuscate$maskDebugInfo(true);
    }
}
