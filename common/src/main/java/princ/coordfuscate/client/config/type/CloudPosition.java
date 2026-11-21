package princ.coordfuscate.client.config.type;

import me.fzzyhmstrs.fzzy_config.util.EnumTranslatable;

import static princ.coordfuscate.client.Constants.CONFIG_TRANSLATION_PREFIX;

public enum CloudPosition implements EnumTranslatable {
    DEFAULT,
    RANDOM;

    @Override
    public String prefix() {
        return CONFIG_TRANSLATION_PREFIX + ".cloudPosition";
    }
}
