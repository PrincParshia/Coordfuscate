package princ.coordfuscate.client.config;

import com.mojang.blaze3d.platform.InputConstants;
import me.fzzyhmstrs.fzzy_config.annotations.Translation;
import me.fzzyhmstrs.fzzy_config.config.Config;
import me.fzzyhmstrs.fzzy_config.config.ConfigGroup;
import me.fzzyhmstrs.fzzy_config.config.ConfigSection;
import me.fzzyhmstrs.fzzy_config.screen.context.FzzyKeybind;
import me.fzzyhmstrs.fzzy_config.validation.collection.ValidatedChoiceList;
import me.fzzyhmstrs.fzzy_config.validation.collection.ValidatedList;
import me.fzzyhmstrs.fzzy_config.validation.minecraft.ValidatedRegistryType;
import me.fzzyhmstrs.fzzy_config.validation.misc.ValidatedEnum;
import me.fzzyhmstrs.fzzy_config.validation.misc.ValidatedKeybind;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.level.EmptyBlockGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import princ.coordfuscate.client.config.type.CloudPosition;
import princ.coordfuscate.client.config.type.CompassTarget;
import princ.coordfuscate.client.config.type.DebugInfo;
import princ.coordfuscate.client.config.type.TextureRotation;

import java.util.Arrays;

import static princ.coordfuscate.client.Constants.*;

@Translation(prefix = CONFIG_TRANSLATION_PREFIX)
public class CoordfuscateConfig extends Config {

    public CoordfuscateConfig() {
        super(withDefaultNamespace(""), "", "", NAMESPACE);

        this.disguiseBedrock$imitate = new ValidatedList<>(
                BuiltInRegistries.BLOCK.stream().toList(),
                ValidatedRegistryType.of(BuiltInRegistries.BLOCK)
        ).toChoiceList(
                BuiltInRegistries.BLOCK.stream().filter(block -> {
                    BlockState blockState = block.defaultBlockState();
                    return !blockState.isAir() && !blockState.is(Blocks.BEDROCK) && blockState.getFluidState().isEmpty() && blockState.isCollisionShapeFullBlock(EmptyBlockGetter.INSTANCE, BlockPos.ZERO);
                }).toList(),
                ValidatedChoiceList.WidgetType.SCROLLABLE,
                (block, key) -> block.getName(),
                (block, key) -> Component.literal(BuiltInRegistries.BLOCK.getKey(block).toString())
        );
        this.compassDrift$applyFor = new ValidatedList<>(
                Arrays.asList(CompassTarget.values()),
                new ValidatedEnum<>(CompassTarget.class)
        ).toChoiceList(
                Arrays.asList(CompassTarget.values()),
                ValidatedChoiceList.WidgetType.POPUP,
                (compassTarget, identifier) -> Component.translatable(CONFIG_TRANSLATION_PREFIX + ".compassDrift$applyFor." + compassTarget.name())
        );
    }

    public TextureRotation textureRotation = TextureRotation.RANDOM;
    public CloudPosition cloudPosition = CloudPosition.RANDOM;
    public ConfigGroup disguiseBedrock$group = new ConfigGroup("disguiseBedrock");
    public boolean disguiseBedrock = true;
    public ValidatedChoiceList<Block> disguiseBedrock$imitate;
    @ConfigGroup.Pop
    public boolean disguiseBedrock$reveal = true;
    public ConfigGroup compassDrift$group = new ConfigGroup("compassDrift");
    public boolean compassDrift = true;
    @ConfigGroup.Pop
    public ValidatedChoiceList<CompassTarget> compassDrift$applyFor;
    public DebugInfoConfig debugInfo = new DebugInfoConfig();

    @Translation(prefix = CONFIG_TRANSLATION_PREFIX + ".debugInfo")
    public static class DebugInfoConfig extends ConfigSection {

        public DebugInfoConfig() {
            this.unmask$key = new ValidatedKeybind(new FzzyKeybind.Builder().keyboard(InputConstants.UNKNOWN.getValue()).build());
            this.unmask$reveal = new ValidatedList<>(
                    Arrays.asList(DebugInfo.values()),
                    new ValidatedEnum<>(DebugInfo.class)
            ).toChoiceList(
                    Arrays.asList(DebugInfo.values()),
                    ValidatedChoiceList.WidgetType.SCROLLABLE
            );
        }

        public ConfigGroup unmask$group = new ConfigGroup("unmask");
        public ValidatedKeybind unmask$key;
        @ConfigGroup.Pop
        public ValidatedChoiceList<DebugInfo> unmask$reveal;
        public boolean smartMask = true;
        public ConfigGroup mask$group = new ConfigGroup("mask");
        public boolean mask$xyz = true;
        public boolean mask$block = true;
        public boolean mask$chunk = true;
        public ConfigGroup mask$facing$group = new ConfigGroup("mask$facing");
        public boolean mask$facing$direction = true;
        public boolean mask$facing$face = true;
        @ConfigGroup.Pop
        public boolean mask$facing$yxrot = true;
        public boolean mask$dimension = true;
        public boolean mask$biome = true;
        public ConfigGroup mask$lookingAt$group = new ConfigGroup("mask$lookingAt");
        public boolean mask$lookingAt$xyz = true;
        @ConfigGroup.Pop
        public boolean mask$lookingAt$name = true;
        @ConfigGroup.Pop
        public boolean mask$chunkPosition = true;
    }

    @Override
    public void onUpdateClient() {
        Minecraft minecraft = Minecraft.getInstance();
        minecraft.levelRenderer.allChanged();
    }
}
