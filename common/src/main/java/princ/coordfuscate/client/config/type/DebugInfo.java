package princ.coordfuscate.client.config.type;

import me.fzzyhmstrs.fzzy_config.util.EnumTranslatable;

import static princ.coordfuscate.client.Constants.CONFIG_TRANSLATION_PREFIX;

public enum DebugInfo implements EnumTranslatable {
    XYZ("xyz"),
    BLOCK("block"),
    CHUNK("chunk"),
    FACING$DIRECTION("facing$direction"),
    FACING$FACE("facing$face"),
    FACING$YXROT("facing$yxrot"),
    DIMENSION("dimension"),
    BIOME("biome"),
    LOOKING_AT$XYZ("lookingAt$xyz"),
    LOOKING_AT$NAME("lookingAt$name"),
    SECTION_POSITION("sectionPosition");

    final String identifier;

    DebugInfo(String identifier) {
        this.identifier = identifier;
    }

    public String identifier() {
        return this.identifier;
    }

    @Override
    public String prefix() {
        return CONFIG_TRANSLATION_PREFIX + ".debugInfo";
    }
}
