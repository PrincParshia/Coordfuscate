package princ.coordfuscate.client.mixin.renderer;

import net.minecraft.client.renderer.CloudRenderer;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import princ.coordfuscate.client.config.type.CloudPosition;

import static princ.coordfuscate.client.Constants.*;

@Mixin(CloudRenderer.class)
public class CloudRendererMixin {

    @ModifyVariable(
            method = "render(ILnet/minecraft/client/CloudStatus;FLnet/minecraft/world/phys/Vec3;JF)V",
            at = @At("HEAD"),
            argsOnly = true
    )
    Vec3 coordfuscate$modifyCameraPosition(Vec3 cameraPosition) {
        if (config().cloudPosition == CloudPosition.RANDOM) {
            int offset = minecraftDuck().coordfuscate$posOffset();
            return new Vec3(cameraPosition.x + offset, cameraPosition.y, cameraPosition.z + offset);
        }
        return cameraPosition;
    }
}
