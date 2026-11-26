package princ.coordfuscate.client.mixin.renderer.item;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.item.CompassItemPropertyFunction;
import net.minecraft.core.GlobalPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.CompassItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import princ.coordfuscate.client.config.type.CompassTarget;

import static princ.coordfuscate.client.Constants.config;

@Mixin(CompassItemPropertyFunction.class)
public class CompassItemPropertyFunctionMixin {

    @Shadow
    @Final
    public CompassItemPropertyFunction.CompassTarget compassTarget;

    @ModifyExpressionValue(
            method = "getCompassRotation",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/renderer/item/CompassItemPropertyFunction;getRotationTowardsCompassTarget(Lnet/minecraft/world/entity/Entity;JLnet/minecraft/core/BlockPos;)F"
            )
    )
    float coordfuscate$compassDrift(float compassRotation, ItemStack itemStack, ClientLevel level, int seed, Entity entity, @Local long gameTime) {
        if (config().compassDrift && entity instanceof LivingEntity livingEntity) {
            boolean heldInHand = livingEntity.getMainHandItem() == itemStack || livingEntity.getOffhandItem() == itemStack;

            GlobalPos pos = this.compassTarget.getPos(level, itemStack, entity);
            int x = 0;
            int y = 0;
            if (pos != null) {
                x = pos.pos().getX();
                y = pos.pos().getY();
            }

            if (itemStack.is(Items.COMPASS)) {
                boolean lodestone = x != 0 && y != 0 && config().compassDrift$applyFor.contains(CompassTarget.LODESTONE);
                boolean spawn = x == 0 && y == 0 && config().compassDrift$applyFor.contains(CompassTarget.SPAWN);
                if (lodestone || spawn) {
                    if (!heldInHand) return ((CompassItemPropertyFunction) (Object) this).getRandomlySpinningRotation(seed, gameTime);
                }
            } else if (itemStack.is(Items.RECOVERY_COMPASS) && pos != null) {
                if (!heldInHand) return ((CompassItemPropertyFunction) (Object) this).getRandomlySpinningRotation(seed, gameTime);
            }
        }
        return compassRotation;
    }
}
