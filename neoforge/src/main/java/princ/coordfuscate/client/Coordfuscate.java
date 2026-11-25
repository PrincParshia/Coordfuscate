package princ.coordfuscate.client;

import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.client.event.ClientTickEvent;
import net.neoforged.neoforge.common.NeoForge;

import static princ.coordfuscate.client.Constants.*;

@Mod(value = Constants.NAMESPACE, dist = Dist.CLIENT)
public class Coordfuscate {

    public Coordfuscate(IEventBus eventBus) {
        NeoForge.EVENT_BUS.addListener((ClientTickEvent.Post event) -> {
            revealBedrock(minecraft());
            removeSmartMask(minecraft());
        });
    }
}