package princ.coordfuscate.client.mixin.renderer.item.properties.numeric;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.item.properties.numeric.CompassAngleState;
import net.minecraft.world.entity.ItemOwner;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;

import static princ.coordfuscate.client.Constants.config;

@Mixin(CompassAngleState.class)
public abstract class CompassAngleStateMixin {

    @Shadow
    @Final
    private CompassAngleState.CompassTarget compassTarget;

    @ModifyExpressionValue(
            method = "calculate",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/renderer/item/properties/numeric/CompassAngleState;getRotationTowardsCompassTarget(Lnet/minecraft/world/entity/ItemOwner;JLnet/minecraft/core/BlockPos;)F"
            )
    )
    float coordfuscate$compassDrift(float targetRotation, ItemStack itemStack, ClientLevel level, int seed, ItemOwner owner, @Local long gameTime) {
        if (config().compassDrift && config().compassDrift$applyFor.contains(this.compassTarget)) {
            LivingEntity entity = owner.asLivingEntity();
            boolean heldInHand = entity != null && (entity.getMainHandItem() == itemStack || entity.getOffhandItem() == itemStack);
            if (!heldInHand) return ((CompassAngleState) (Object) this).getRandomlySpinningRotation(seed, gameTime);
        }
        return targetRotation;
    }
}
