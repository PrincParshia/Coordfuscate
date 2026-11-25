package princ.coordfuscate.client;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;

import static princ.coordfuscate.client.Constants.*;

public class Coordfuscate implements ClientModInitializer {

    @Override
    public void onInitializeClient() {
        ClientTickEvents.END_CLIENT_TICK.register(minecraft -> {
            revealBedrock(minecraft);
            removeSmartMask(minecraft);
        });
    }
}
