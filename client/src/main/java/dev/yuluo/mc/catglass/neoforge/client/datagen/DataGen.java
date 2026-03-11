package dev.yuluo.mc.catglass.neoforge.client.datagen;

import dev.yuluo.mc.catglass.neoforge.client.CatGlassClient;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.data.event.GatherDataEvent;

@EventBusSubscriber(modid = CatGlassClient.MODID)
public class DataGen {
    @SubscribeEvent
    public static void onGatherData(GatherDataEvent.Client event) {
        var generator = event.getGenerator();
        var output = generator.getPackOutput();
        event.addProvider(new EnUsLangProvider(output, CatGlassClient.MODID));
    }
}
