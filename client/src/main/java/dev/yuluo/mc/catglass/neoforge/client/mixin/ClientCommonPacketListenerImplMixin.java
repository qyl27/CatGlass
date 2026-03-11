package dev.yuluo.mc.catglass.neoforge.client.mixin;

import dev.yuluo.mc.catglass.neoforge.client.CatGlassClient;
import dev.yuluo.mc.catglass.neoforge.client.Config;
import dev.yuluo.mc.catglass.neoforge.client.SerializationFormat;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientCommonPacketListenerImpl;
import net.minecraft.network.protocol.common.ClientboundStoreCookiePacket;
import net.minecraft.network.protocol.cookie.ClientboundCookieRequestPacket;
import net.minecraft.resources.Identifier;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Map;

@Mixin(ClientCommonPacketListenerImpl.class)
public abstract class ClientCommonPacketListenerImplMixin {
    @Shadow
    @Final
    public Map<Identifier, byte[]> serverCookies;

    @Shadow
    @Final
    protected Minecraft minecraft;

    @Inject(method = "handleStoreCookie", at = @At("HEAD"), cancellable = true)
    public void catglassc$before$handleStoreCookie(ClientboundStoreCookiePacket packet, CallbackInfo ci) {
        if (!minecraft.packetProcessor().isSameThread()) {
            return;
        }

        var key = packet.key();
        var value = SerializationFormat.BASE64.deserialize(packet.payload());

        if (Config.checkIgnored(key)) {
            CatGlassClient.LOGGER.info("Ignored server put cookie: key={}, payload={}", key, value);
            ci.cancel();
        } else {
            CatGlassClient.LOGGER.info("Server put cookie: key={}, payload={}", key, value);
        }
    }

    @Inject(method = "handleRequestCookie", at = @At("HEAD"))
    public void catglassc$before$handleRequestCookie(ClientboundCookieRequestPacket packet, CallbackInfo ci) {
        if (!minecraft.packetProcessor().isSameThread()) {
            return;
        }

        var key = packet.key();
        var value = serverCookies.get(key);
        if (value == null) {
            CatGlassClient.LOGGER.info("Server request cookie: key={}, not found in client", packet.key());
        } else {
            CatGlassClient.LOGGER.info("Server request cookie: key={}, value={}", packet.key(), SerializationFormat.BASE64.deserialize(value));
        }
    }
}
