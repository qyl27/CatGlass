package dev.yuluo.mc.catglass.neoforge.client.command;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import dev.yuluo.mc.catglass.neoforge.client.Config;
import dev.yuluo.mc.catglass.neoforge.client.ModConstants;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.arguments.IdentifierArgument;
import net.minecraft.network.chat.Component;

import java.util.concurrent.CompletableFuture;

import static net.minecraft.commands.Commands.argument;
import static net.minecraft.commands.Commands.literal;

public class IgnoreCommand {
    private static final String KEY = "key";

    public static final LiteralArgumentBuilder<CommandSourceStack> IGNORE = literal("ignore")
            .then(literal("list")
                    .executes(IgnoreCommand::onList))
            .then(literal("check")
                    .then(argument(KEY, IdentifierArgument.id())
                            .suggests(IgnoreCommand::onSuggestKey)
                            .executes(IgnoreCommand::onCheck)))
            .then(literal("add")
                    .then(argument(KEY, IdentifierArgument.id())
                            .suggests(IgnoreCommand::onSuggestKey)
                            .executes(IgnoreCommand::onAdd)))
            .then(literal("remove")
                    .then(argument(KEY, IdentifierArgument.id())
                            .suggests(IgnoreCommand::onSuggestKey)
                            .executes(IgnoreCommand::onRemove)))
            .then(literal("clear")
                    .executes(IgnoreCommand::onClear));

    private static CompletableFuture<Suggestions> onSuggestKey(CommandContext<CommandSourceStack> context, SuggestionsBuilder builder) {
        var result = Config.listIgnored();
        for (var cookie : result) {
            builder.suggest(cookie.toString());
        }
        return builder.buildFuture();
    }

    private static int onList(CommandContext<CommandSourceStack> context) {
        var source = context.getSource();
        var list = Config.listIgnored();
        source.sendSuccess(() -> Component.translatable(ModConstants.Translation.TOTAL_N_IGNORED, list.size()), true);
        for (var i = 0; i < list.size(); i++) {
            source.sendSystemMessage(Component.translatable(ModConstants.Translation.IGNORED_N, i + 1, list.get(i).toString()));
        }
        return list.size();
    }

    private static int onCheck(CommandContext<CommandSourceStack> context) {
        var source = context.getSource();
        var key = IdentifierArgument.getId(context, KEY);
        if (Config.checkIgnored(key)) {
            source.sendSuccess(() -> Component.translatable(ModConstants.Translation.IGNORED, key.toString()), true);
        } else {
            source.sendSuccess(() -> Component.translatable(ModConstants.Translation.NOT_IGNORED, key.toString()), true);
        }
        return Command.SINGLE_SUCCESS;
    }

    private static int onAdd(CommandContext<CommandSourceStack> context) {
        var key = IdentifierArgument.getId(context, KEY);
        Config.addIgnored(key);
        context.getSource().sendSuccess(() -> Component.translatable(ModConstants.Translation.IGNORED_ADDED, key.toString()), true);
        return Command.SINGLE_SUCCESS;
    }

    private static int onRemove(CommandContext<CommandSourceStack> context) {
        var key = IdentifierArgument.getId(context, KEY);
        Config.removeIgnored(key);
        context.getSource().sendSuccess(() -> Component.translatable(ModConstants.Translation.IGNORED_REMOVED, key.toString()), true);
        return Command.SINGLE_SUCCESS;
    }

    private static int onClear(CommandContext<CommandSourceStack> context) {
        Config.clearIgnored();
        context.getSource().sendSuccess(() -> Component.translatable(ModConstants.Translation.IGNORED_CLEAR), true);
        return Command.SINGLE_SUCCESS;
    }
}
