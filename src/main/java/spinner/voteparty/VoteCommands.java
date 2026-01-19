package spinner.voteparty;

import com.mojang.brigadier.CommandDispatcher;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.Text;

public class VoteCommands {

    public static void register(CommandDispatcher<ServerCommandSource> dispatcher,
                                net.minecraft.registry.RegistryWrapper.WrapperLookup registryLookup,
                                CommandManager.RegistrationEnvironment env) {

        // /vpadd
        dispatcher.register(CommandManager.literal("vpadd")
                .requires(src -> src.hasPermissionLevel(4))
                .executes(ctx -> {
                    VoteManager.INSTANCE.addVote(ctx.getSource().getServer());
                    ctx.getSource().sendFeedback(
                            () -> Text.literal("§aVote added!"),
                            false
                    );
                    return 1;
                })
        );

        // /vpreload
        dispatcher.register(CommandManager.literal("vpreload")
                .requires(src -> src.hasPermissionLevel(4))
                .executes(ctx -> {
                    VoteManager.INSTANCE.reload();
                    ctx.getSource().sendFeedback(
                            () -> Text.literal("§aVoteParty reloaded."),
                            true
                    );
                    return 1;
                })
        );

        // /vp trigger + info + reset
        dispatcher.register(CommandManager.literal("vp")
                .requires(src -> src.hasPermissionLevel(4))
                // /vp trigger
                .then(CommandManager.literal("trigger")
                        .executes(ctx -> {
                            VoteManager.INSTANCE.triggerManually(ctx.getSource().getServer());
                            VoteManager.INSTANCE.resetVotes(); // reset votes after triggering
                            ctx.getSource().sendFeedback(() -> Text.literal("§6Vote Party triggered!"), true);
                            return 1;
                        })
                )
                // /vp reset
                .then(CommandManager.literal("reset")
                        .executes(ctx -> {
                            VoteManager.INSTANCE.resetVotes();
                            ctx.getSource().sendFeedback(() -> Text.literal("§cVote count reset!"), true);
                            return 1;
                        })
                )
                // /vp info
                .then(CommandManager.literal("info")
                        .executes(ctx -> {
                            int cur = VoteManager.INSTANCE.getCurrentVotes();
                            int req = VoteManager.INSTANCE.getRequiredVotes();
                            ctx.getSource().sendFeedback(
                                    () -> Text.literal("§eVotes: " + VoteManager.progressBar(cur, req)),
                                    false
                            );
                            return 1;
                        })
                )
        );
    }
}
