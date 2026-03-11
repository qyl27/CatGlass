package dev.yuluo.mc.catglass.neoforge.client;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.arguments.IdentifierArgument;
import net.minecraft.network.chat.Component;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.RegisterClientCommandsEvent;
import net.neoforged.neoforge.server.command.EnumArgument;

import static net.minecraft.commands.Commands.argument;
import static net.minecraft.commands.Commands.literal;

@EventBusSubscriber(modid = CatGlassClient.MODID)
public class ModCommand {
    @SubscribeEvent
    public static void onRegisterCommand(RegisterClientCommandsEvent event) {
        event.getDispatcher().register(ROOT);
    }

    private static final String KEY = "key";
    private static final String FORMAT = "format";
    private static final String VALUE = "value";

    public static final LiteralArgumentBuilder<CommandSourceStack> ROOT = literal("catglassc")
            .then(literal("cookies")
                    .then(literal("list")
                            .executes(ModCommand::onListCookies))
                    .then(literal("get")
                            .then(argument(KEY, IdentifierArgument.id())
                                    .then(argument(FORMAT, EnumArgument.enumArgument(SerializationFormat.class))
                                            .executes(ModCommand::onGetCookie))))
                    .then(literal("set")
                            .then(argument(KEY, IdentifierArgument.id())
                                    .then(argument(FORMAT, EnumArgument.enumArgument(SerializationFormat.class))
                                            .then(argument(VALUE, StringArgumentType.greedyString())
                                                    .executes(ModCommand::onSetCookie))
                                    )))
                    .then(literal("unset")
                            .then(argument(KEY, IdentifierArgument.id())
                                    .executes(ModCommand::onUnsetCookie))));

    private static int onListCookies(CommandContext<CommandSourceStack> context) {
        var source = context.getSource();
        var result = ClientCookieHelper.listCookies();
        source.sendSuccess(() -> Component.translatable(ModConstants.Translation.TOTAL_N_COOKIES, result.size()), true);
        for (var i = 0; i < result.size(); i++) {
            source.sendSystemMessage(Component.translatable(ModConstants.Translation.COOKIE_N, i + 1, result.get(i).toString()));
        }
        return result.size();
    }

    private static int onGetCookie(CommandContext<CommandSourceStack> context) {
        var source = context.getSource();
        var key = IdentifierArgument.getId(context, KEY);
        var format = context.getArgument(FORMAT, SerializationFormat.class);
        var result = ClientCookieHelper.getCookie(key);
        if (result == null) {
            source.sendFailure(Component.translatable(ModConstants.Translation.KEY_NOT_FOUND, key.toString()));
            return 0;
        }
        var formatted = format.deserialize(result);
        source.sendSuccess(() -> Component.translatable(ModConstants.Translation.COOKIE_VALUE, key.toString(), format.toString(), formatted), true);
        return Command.SINGLE_SUCCESS;
    }

    private static int onSetCookie(CommandContext<CommandSourceStack> context) {
        var source = context.getSource();
        var key = IdentifierArgument.getId(context, KEY);
        var format = context.getArgument(FORMAT, SerializationFormat.class);
        var value = StringArgumentType.getString(context, VALUE);
        try {
            var formatted = format.serialize(value);
            ClientCookieHelper.setCookie(key, formatted);
            source.sendSuccess(() -> Component.translatable(ModConstants.Translation.COOKIE_SET, key.toString(), format.toString(), value), true);
            return Command.SINGLE_SUCCESS;
        } catch (IllegalArgumentException ex) {
            source.sendFailure(Component.translatable(ModConstants.Translation.VALUE_FORMAT_INVALID, format.toString(), value));
            return 0;
        }
    }

    private static int onUnsetCookie(CommandContext<CommandSourceStack> context) {
        var source = context.getSource();
        var key = IdentifierArgument.getId(context, KEY);
        ClientCookieHelper.unsetCookie(key);
        source.sendSuccess(() -> Component.translatable(ModConstants.Translation.COOKIE_UNSET, key.toString()), true);
        return Command.SINGLE_SUCCESS;
    }
}
