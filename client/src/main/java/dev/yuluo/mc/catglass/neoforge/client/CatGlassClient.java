package dev.yuluo.mc.catglass.neoforge.client;

import com.mojang.logging.LogUtils;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import org.slf4j.Logger;

@Mod(value = CatGlassClient.MODID, dist = Dist.CLIENT)
public class CatGlassClient {
    public static final String MODID = "catglassc";
    public static final Logger LOGGER = LogUtils.getLogger();

    public CatGlassClient(IEventBus modEventBus, ModContainer modContainer) {
        modEventBus.addListener(this::commonSetup);

        modContainer.registerConfig(ModConfig.Type.CLIENT, Config.SPEC);
    }

    private void commonSetup(final FMLCommonSetupEvent event) {
    }
}
