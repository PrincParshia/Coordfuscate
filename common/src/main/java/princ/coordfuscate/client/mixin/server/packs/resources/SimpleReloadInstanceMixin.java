package princ.coordfuscate.client.mixin.server.packs.resources;

import net.minecraft.server.packs.resources.PreparableReloadListener;
import net.minecraft.server.packs.resources.ReloadInstance;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.SimpleReloadInstance;
import net.minecraft.util.Unit;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;

import static princ.coordfuscate.client.Constants.*;

@Mixin(SimpleReloadInstance.class)
public class SimpleReloadInstanceMixin {

    @Inject(method = "create", at = @At("HEAD"))
    private static void coordfuscate$create(ResourceManager resourceManager, List<PreparableReloadListener> listeners, Executor backgroundExecutor, Executor mainThreadExecutor, CompletableFuture<Unit> initialTask, boolean enableProfiling, CallbackInfoReturnable<ReloadInstance> callbackInfoReturnable) {
        minecraftDuck().coordfuscate$posOffset(posOffset());
        minecraftDuck().coordfuscate$disguiseBedrockCache().clear();
    }
}
