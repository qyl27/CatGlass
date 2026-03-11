package dev.yuluo.mc.catglass.neoforge.client.command;

import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import dev.yuluo.mc.catglass.neoforge.client.CatGlassClient;
import net.minecraft.commands.CommandSourceStack;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.RegisterClientCommandsEvent;

import static net.minecraft.commands.Commands.literal;

@EventBusSubscriber(modid = CatGlassClient.MODID)
public class ModCommand {
    @SubscribeEvent
    public static void onRegisterCommand(RegisterClientCommandsEvent event) {
        event.getDispatcher().register(ROOT);
    }

    public static final LiteralArgumentBuilder<CommandSourceStack> ROOT = literal("catglassc")
            .then(CookieCommand.COOKIES)
            .then(IgnoreCommand.IGNORE);

}
