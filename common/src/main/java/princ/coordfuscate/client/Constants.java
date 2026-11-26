package princ.coordfuscate.client;

import me.fzzyhmstrs.fzzy_config.api.ConfigApiJava;
import me.fzzyhmstrs.fzzy_config.api.RegisterType;
import me.fzzyhmstrs.fzzy_config.validation.collection.ValidatedChoiceList;
import me.fzzyhmstrs.fzzy_config.validation.misc.ValidatedKeybind;
import net.minecraft.Util;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.levelgen.RandomSupport;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import princ.coordfuscate.client.config.CoordfuscateConfig;
import princ.coordfuscate.client.config.type.DebugInfo;
import princ.coordfuscate.client.duck.MinecraftDuck;

import static net.minecraft.world.level.border.WorldBorder.MAX_CENTER_COORDINATE;

public class Constants {
    public static final String NAMESPACE = "coordfuscate";
    public static final String NAME = "Coordfuscate";
    public static final String CONFIG_TRANSLATION_PREFIX = "config." + NAMESPACE;
    public static final Logger LOG = LoggerFactory.getLogger(NAME);
    static final CoordfuscateConfig config = ConfigApiJava.registerAndLoadConfig(CoordfuscateConfig::new, RegisterType.CLIENT);

    public static Minecraft minecraft() {
        return Minecraft.getInstance();
    }

    public static MinecraftDuck minecraftDuck() {
        return (MinecraftDuck) minecraft();
    }

    public static CoordfuscateConfig config() {
        return config;
    }

    public static ResourceLocation withDefaultNamespace(String path) {
        return ResourceLocation.fromNamespaceAndPath(NAMESPACE, path);
    }

    public static int posOffset() {
        return posOffset(RandomSupport.generateUniqueSeed(), 0);
    }

    public static int posOffset(long seed, int offset) {
        RandomSource random = RandomSource.create(seed ^ offset);
        int i = random.nextIntBetweenInclusive((int) MAX_CENTER_COORDINATE, Integer.MAX_VALUE);
        return random.nextBoolean() ? i : -i;
    }

    public static void revealBedrock(Minecraft minecraft) {
        if (!config().disguiseBedrock$reveal) return;
        MinecraftDuck duck = (MinecraftDuck) minecraft;
        long now = Util.getMillis();
        duck.coordfuscate$revealBedrock().entrySet().removeIf(entry -> {
            BlockPos pos = entry.getKey();
            boolean disguise = now - entry.getValue() >= 1000L;
            if (disguise) minecraft.levelRenderer.setBlocksDirty(pos.getX(), pos.getY(), pos.getZ(), pos.getX(), pos.getY(), pos.getZ());
            return disguise;
        });
    }

    public static boolean maskDebugInfo(DebugInfo debugInfo) {
        ValidatedKeybind key = config().debugInfo.unmask$key;
        ValidatedChoiceList<DebugInfo> list = config().debugInfo.unmask$reveal;
        boolean unmask = key.isPressed() && list.contains(debugInfo);
        if (config().debugInfo.smartMask) {
            unmask = unmask && !minecraftDuck().coordfuscate$maskDebugInfo();
        }
        return !unmask;
    }

    public static void removeSmartMask(Minecraft minecraft) {
        MinecraftDuck duck = (MinecraftDuck) minecraft;
        if (!config().debugInfo.unmask$key.isPressed() && duck.coordfuscate$maskDebugInfo()) {
            duck.coordfuscate$maskDebugInfo(false);
        }
    }
}