package dev.yuluo.mc.catglass.neoforge.client;

import net.minecraft.resources.Identifier;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.event.config.ModConfigEvent;
import net.neoforged.neoforge.common.ModConfigSpec;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@EventBusSubscriber(modid = CatGlassClient.MODID)
public class Config {
    private static final ModConfigSpec.Builder BUILDER = new ModConfigSpec.Builder();

    private static final ModConfigSpec.ConfigValue<List<? extends String>> FIELD_IGNORED_KEYS = BUILDER
            .comment("Ignored keys list")
            .defineList("ignoredKeys", List.of(), () -> "", Config::validateIdentifier);

    static final ModConfigSpec SPEC = BUILDER.build();

    private static boolean validateIdentifier(Object o) {
        if (o instanceof String s) {
            return Identifier.tryParse(s) != null;
        }
        return false;
    }

    @SubscribeEvent
    static void onLoad(final ModConfigEvent event) {
        load();
    }

    public static Set<Identifier> IGNORED_KEYS = new HashSet<>();

    private static void load() {
        IGNORED_KEYS.clear();

        for (var s : FIELD_IGNORED_KEYS.get()) {
            var id = Identifier.parse(s);
            IGNORED_KEYS.add(id);
        }
    }

    private static void save() {
        var list = IGNORED_KEYS.stream().map(Identifier::toString).toList();
        FIELD_IGNORED_KEYS.set(list);
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

    public static List<Identifier> listIgnored() {
        return IGNORED_KEYS.stream().toList();
    }
}
