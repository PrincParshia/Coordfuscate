package princ.coordfuscate.client.config.type;

import me.fzzyhmstrs.fzzy_config.util.EnumTranslatable;

import static princ.coordfuscate.client.Constants.CONFIG_TRANSLATION_PREFIX;

public enum CompassTarget implements EnumTranslatable {
    LODESTONE,
    SPAWN,
    RECOVERY;

    @Override
    public String prefix() {
        return CONFIG_TRANSLATION_PREFIX + ".compassTarget";
    }
}
