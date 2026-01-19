package spinner.voteparty;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.fabricmc.loader.api.FabricLoader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.file.Files;
import java.nio.file.Path;

// Import Placeholder API
import eu.pb4.placeholders.api.Placeholders;
import eu.pb4.placeholders.api.PlaceholderContext;
import eu.pb4.placeholders.api.PlaceholderResult;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

public class VoteParty implements ModInitializer {

    public static final String MOD_ID = "voteparty";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

    @Override
    public void onInitialize() {
        Path configDir = FabricLoader.getInstance().getConfigDir().resolve(MOD_ID);
        try { Files.createDirectories(configDir); } catch (Exception ignored) {}

        VoteManager.init(configDir);
        CommandRegistrationCallback.EVENT.register(VoteCommands::register);

        // ✅ Register placeholders
        Placeholders.register(Identifier.of(MOD_ID, "progress"), (ctx, arg) ->
                PlaceholderResult.value(Text.literal(VoteManager.getVoteProgressBar()))
        );
        Placeholders.register(Identifier.of(MOD_ID, "progress-small"), (ctx, arg) ->
                PlaceholderResult.value(Text.literal(VoteManager.getVoteProgressBarSmall()))
        );
        Placeholders.register(Identifier.of(MOD_ID, "status"), (ctx, arg) ->
                PlaceholderResult.value(Text.literal(VoteManager.getVoteStatus()))
        );
        Placeholders.register(Identifier.of(MOD_ID, "current"), (ctx, arg) ->
                PlaceholderResult.value(Text.literal(String.valueOf(VoteManager.getCurrentVotesStatic())))
        );
        Placeholders.register(Identifier.of(MOD_ID, "required"), (ctx, arg) ->
                PlaceholderResult.value(Text.literal(String.valueOf(VoteManager.getRequiredVotesStatic())))
        );

        LOGGER.info("VoteParty loaded! Use /vpadd, /vp trigger, /vpreload, /vp info");
    }
}