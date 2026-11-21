package princ.coordfuscate.client;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import princ.coordfuscate.client.duck.MinecraftDuck;

import static princ.coordfuscate.client.Constants.*;

public class Coordfuscate implements ClientModInitializer {

    @Override
    public void onInitializeClient() {
        ClientTickEvents.END_CLIENT_TICK.register(minecraft -> {
            MinecraftDuck duck = (MinecraftDuck) minecraft;
            if (!config().debugInfo.unmask$key.isPressed() && duck.coordfuscate$maskDebugInfo()) {
                duck.coordfuscate$maskDebugInfo(false);
            }
        });
    }
}
