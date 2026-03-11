package dev.yuluo.mc.catglass.neoforge.client;

import com.google.common.collect.ImmutableMap;
import net.minecraft.resources.Identifier;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.event.config.ModConfigEvent;
import net.neoforged.neoforge.common.ModConfigSpec;
import org.jetbrains.annotations.Nullable;

import java.util.*;

@EventBusSubscriber(modid = CatGlassClient.MODID)
public class Config {
    private static final ModConfigSpec.Builder BUILDER = new ModConfigSpec.Builder();

    private static final ModConfigSpec.ConfigValue<List<? extends String>> FIELD_IGNORED_KEYS = BUILDER
            .comment("Ignored keys list")
            .translation(ModConstants.Translation.CONFIG_IGNORED_KEYS)
            .defineList("ignoredKeys", List.of(), () -> "", Config::validateIdentifier);

    private static final ModConfigSpec.ConfigValue<List<? extends String>> FIELD_PRESET_COOKIES = BUILDER
            .comment("Preset cookies list")
            .translation(ModConstants.Translation.CONFIG_PRESET_COOKIES)
            .defineList("presetCookies", List.of(), () -> "", Config::validatePreset);

    static final ModConfigSpec SPEC = BUILDER.build();

    private static boolean validateIdentifier(Object o) {
        if (o instanceof String s) {
            return Identifier.tryParse(s) != null;
        }
        return false;
    }

    private static boolean validatePreset(Object o) {
        if (o instanceof String s) {
            var firstColon = s.indexOf(':');
            if (firstColon == -1) {
                return false;
            }
            var secondColon = s.indexOf(':', firstColon + 1);
            if (secondColon == -1) {
                return false;
            }
            var key = s.substring(0, secondColon);
            var value = s.substring(secondColon + 1);
            try {
                var result = SerializationFormat.HEX_STRING.serialize(value);
            } catch (IllegalArgumentException ignored) {
                return false;
            }
            return Identifier.tryParse(key) != null;
        }
        return false;
    }

    @SubscribeEvent
    static void onLoad(final ModConfigEvent event) {
        load();
    }

    private static final Set<Identifier> IGNORED_KEYS = new HashSet<>();
    private static final Map<Identifier, byte[]> PRESET_COOKIES = new HashMap<>();

    private static void load() {
        IGNORED_KEYS.clear();
        for (var s : FIELD_IGNORED_KEYS.get()) {
            var id = Identifier.parse(s);
            IGNORED_KEYS.add(id);
        }

        PRESET_COOKIES.clear();
        for (var s : FIELD_PRESET_COOKIES.get()) {
            var firstColon = s.indexOf(':');
            var secondColon = s.indexOf(':', firstColon + 1);
            var k = s.substring(0, secondColon);
            var v = s.substring(secondColon + 1);
            var key = Identifier.parse(k);
            var value = SerializationFormat.HEX_STRING.serialize(v);
            PRESET_COOKIES.put(key, value);
        }
    }

    private static void save() {
        var list = IGNORED_KEYS.stream().map(Identifier::toString).toList();
        FIELD_IGNORED_KEYS.set(list);

        var list2 = PRESET_COOKIES.entrySet().stream().map(e -> e.getKey().toString() + ":" + SerializationFormat.HEX_STRING.deserialize(e.getValue())).toList();
        FIELD_PRESET_COOKIES.set(list2);
    }

    // region Ignored keys

    public static List<Identifier> listIgnored() {
        return IGNORED_KEYS.stream().toList();
    }

    public static boolean checkIgnored(Identifier key) {
        return IGNORED_KEYS.contains(key);
    }

    public static void addIgnored(Identifier key) {
        IGNORED_KEYS.add(key);
        save();
    }

    public static void removeIgnored(Identifier key) {
        IGNORED_KEYS.remove(key);
        save();
    }

    public static void clearIgnored() {
        IGNORED_KEYS.clear();
        save();
    }

    // endregion


    // region Preset cookies

    public static List<Identifier> listPresets() {
        return PRESET_COOKIES.keySet().stream().toList();
    }

    public static Map<Identifier, byte[]> getPresets() {
        return ImmutableMap.copyOf(PRESET_COOKIES);
    }

    public static byte @Nullable [] getPreset(Identifier key) {
        return PRESET_COOKIES.get(key);
    }

    public static void setPreset(Identifier key, byte[] value) {
        PRESET_COOKIES.put(key, value);
        save();
    }

    public static void unsetPreset(Identifier key) {
        PRESET_COOKIES.remove(key);
        save();
    }

    public static void clearPreset() {
        PRESET_COOKIES.clear();
        save();
    }

    // endregion
}
