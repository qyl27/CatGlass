package dev.yuluo.mc.catglass.gateway;

import com.google.inject.Inject;
import com.velocitypowered.api.event.proxy.ProxyInitializeEvent;
import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.plugin.Plugin;
import org.slf4j.Logger;

@Plugin(
    id = "catglass",
    name = "CatGlass",
    version = BuildConstants.VERSION
    ,authors = {"qyl27"}
)
public class Gateway {

    @Inject private Logger logger;

    @Subscribe
    public void onProxyInitialization(ProxyInitializeEvent event) {
    }
}
