package dev.yuluo.mc.catglass.neoforge.client.mixin;

import dev.yuluo.mc.catglass.neoforge.client.Config;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.multiplayer.ClientHandshakePacketListenerImpl;
import net.minecraft.client.multiplayer.LevelLoadTracker;
import net.minecraft.client.multiplayer.ServerData;
import net.minecraft.client.multiplayer.TransferState;
import net.minecraft.network.Connection;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.time.Duration;
import java.util.Map;
import java.util.function.Consumer;

@Mixin(ClientHandshakePacketListenerImpl.class)
public class ClientHandshakePacketListenerImplMixin {
    @Shadow
    @Final
    private Map<Identifier, byte[]> cookies;

    @Inject(method = "<init>", at = @At("TAIL"))
    private void catglassc$after$init(Connection connection, Minecraft minecraft, ServerData serverData, Screen parent,
                                      boolean newWorld, Duration worldLoadDuration, Consumer<Component> updateStatus,
                                      LevelLoadTracker levelLoadTracker, TransferState transferState, CallbackInfo ci) {
        if (transferState == null) {
            cookies.putAll(Config.getPresets());
        }
    }
}
