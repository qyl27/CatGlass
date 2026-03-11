package dev.yuluo.mc.catglass.neoforge.client.datagen;

import dev.yuluo.mc.catglass.neoforge.client.ModConstants;
import net.minecraft.data.PackOutput;
import net.neoforged.neoforge.common.data.LanguageProvider;

public class EnUsLangProvider extends LanguageProvider {
    public EnUsLangProvider(PackOutput output, String modid) {
        super(output, modid, "en_us");
    }

    @Override
    protected void addTranslations() {
        add(ModConstants.Translation.TOTAL_N_COOKIES, "Total %1$s cookies");
        add(ModConstants.Translation.COOKIE_N, "%1$s: %2$s");
        add(ModConstants.Translation.KEY_NOT_FOUND, "Key %1$s not found");
        add(ModConstants.Translation.COOKIE_VALUE, "Cookie %1$s (%2$s): %3$s");
        add(ModConstants.Translation.COOKIE_SET, "Cookie %1$s was set to %3$s (%2$s)");
        add(ModConstants.Translation.VALUE_FORMAT_INVALID, "Value %2$s is invalid for format %1$s");
        add(ModConstants.Translation.COOKIE_UNSET, "Cookie %1$s was unset");
        add(ModConstants.Translation.COOKIE_CLEARED, "Cookie cleared");

        add(ModConstants.Translation.TOTAL_N_IGNORED, "Total ignored %1$s cookie keys");
        add(ModConstants.Translation.IGNORED_N, "%1$s: %2$s");
        add(ModConstants.Translation.IGNORED, "Key %1$s was ignored");
        add(ModConstants.Translation.NOT_IGNORED, "Key %1$s was NOT ignored");
        add(ModConstants.Translation.IGNORED_ADDED, "Added %1$s to ignored keys");
        add(ModConstants.Translation.IGNORED_REMOVED, "Removed %1$s from ignored keys");
        add(ModConstants.Translation.IGNORED_CLEARED, "Ignored keys cleared");

        add(ModConstants.Translation.TOTAL_N_PRESETS, "Total %1$s presets");
        add(ModConstants.Translation.PRESET_N, "%1$s: %2$s");
        add(ModConstants.Translation.PRESET_KEY_NOT_FOUND, "Preset key %1$s not found");
        add(ModConstants.Translation.PRESET_VALUE, "Preset %1$s (%2$s): %3$s");
        add(ModConstants.Translation.PRESET_SET, "Preset %1$s was set to %3$s (%2$s)");
        add(ModConstants.Translation.PRESET_UNSET, "Preset %1$s was unset");
        add(ModConstants.Translation.PRESET_CLEARED, "Presets cleared");

        add(ModConstants.Translation.CONFIG_IGNORED_KEYS, "Ignored keys");
        add(ModConstants.Translation.CONFIG_PRESET_COOKIES, "Preset cookies");
    }
}
