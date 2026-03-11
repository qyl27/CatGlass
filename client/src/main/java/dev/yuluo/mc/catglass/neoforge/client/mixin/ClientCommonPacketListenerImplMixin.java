package dev.yuluo.mc.catglass.neoforge.client.mixin;

import dev.yuluo.mc.catglass.neoforge.client.CatGlassClient;
import dev.yuluo.mc.catglass.neoforge.client.SerializationFormat;
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

    @Inject(method = "handleStoreCookie", at = @At("TAIL"))
    public void catglassc$after$handleStoreCookie(ClientboundStoreCookiePacket packet, CallbackInfo ci) {
        CatGlassClient.LOGGER.info("Received cookie: key={}, payload={}", packet.key(), SerializationFormat.BASE64.deserialize(packet.payload()));
    }

    @Inject(method = "handleRequestCookie", at = @At("TAIL"))
    public void catglassc$after$handleRequestCookie(ClientboundCookieRequestPacket packet, CallbackInfo ci) {
        var key = packet.key();
        var value = serverCookies.get(key);
        if (value == null) {
            CatGlassClient.LOGGER.info("Received cookie request: key={}, not found in client", packet.key());
        } else {
            CatGlassClient.LOGGER.info("Received cookie request: key={}, value={}", packet.key(), SerializationFormat.BASE64.deserialize(value));
        }
    }
}
