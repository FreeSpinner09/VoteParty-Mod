package spinner.voteparty;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.Text;

import java.io.Reader;
import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.Path;

public class VoteManager {

    public static VoteManager INSTANCE;

    private final Path configFile;
    private final Path dataFile;

    private final Gson gson = new GsonBuilder().setPrettyPrinting().create();

    public VotePartyConfig config;
    public int currentVotes = 0;

    public static void init(Path configDir) {
        INSTANCE = new VoteManager(configDir);
    }

    private VoteManager(Path path) {
        this.configFile = path.resolve("config.json");
        this.dataFile = path.resolve("votes.json");
        loadConfig();
        loadVotes();
    }

    public void reload() {
        loadConfig();
        loadVotes();
    }

    private void loadConfig() {
        try {
            if (Files.exists(configFile)) {
                try (Reader r = Files.newBufferedReader(configFile)) {
                    config = gson.fromJson(r, VotePartyConfig.class);
                }
            } else {
                config = new VotePartyConfig();
                saveConfig();
            }
        } catch (Exception e) {
            config = new VotePartyConfig();
        }
    }

    private void saveConfig() {
        try (Writer w = Files.newBufferedWriter(configFile)) {
            gson.toJson(config, w);
        } catch (Exception ignored) { }
    }

    private void loadVotes() {
        try {
            if (Files.exists(dataFile)) {
                try (Reader r = Files.newBufferedReader(dataFile)) {
                    currentVotes = gson.fromJson(r, Integer.class);
                }
            }
        } catch (Exception ignored) { }
    }

    private void saveVotes() {
        try (Writer w = Files.newBufferedWriter(dataFile)) {
            gson.toJson(currentVotes, w);
        } catch (Exception ignored) { }
    }

    public void addVote(MinecraftServer server) {
        currentVotes++;
        saveVotes();

        if (currentVotes >= config.requiredVotes) {
            triggerParty(server);
            currentVotes = 0;
            saveVotes();
        }
    }

    public void triggerManually(MinecraftServer server) {
        triggerParty(server);
        currentVotes = 0;
        saveVotes();
    }

    public void resetVotes() {
        currentVotes = 0;
        saveVotes();
    }

    private void triggerParty(MinecraftServer server) {
        String t = config.title.replace("%current_votes%", String.valueOf(currentVotes))
                .replace("%required_votes%", String.valueOf(config.requiredVotes));
        String st = config.subtitle.replace("%current_votes%", String.valueOf(currentVotes))
                .replace("%required_votes%", String.valueOf(config.requiredVotes));

        // Send messages to all players
        for (ServerPlayerEntity player : server.getPlayerManager().getPlayerList()) {
            player.sendMessage(Text.literal(t), false);
            player.sendMessage(Text.literal(st), false);
        }

        // Execute console commands
        for (String cmd : config.commands) {
            if (cmd.contains("{player}")) {
                for (ServerPlayerEntity player : server.getPlayerManager().getPlayerList()) {
                    String finalCmd = cmd.replace("{player}", player.getName().getString());
                    try {
                        server.getCommandManager().getDispatcher().execute(finalCmd, server.getCommandSource());
                    } catch (com.mojang.brigadier.exceptions.CommandSyntaxException e) {
                        e.printStackTrace();
                    }
                }
            } else {
                try {
                    server.getCommandManager().getDispatcher().execute(cmd, server.getCommandSource());
                } catch (com.mojang.brigadier.exceptions.CommandSyntaxException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    // Progress bar helper
    public static String progressBar(int current, int max) {
        int total = 30;
        int filled = (int) ((double) current / max * total);
        return "§a" + "█".repeat(Math.max(0, filled)) +
                "§c" + "░".repeat(Math.max(0, total - filled)) +
                " §e" + current + "§7/§e" + max;
    }

    public static String progressBarSmall(int current, int max) {
        int total = 15;
        int filled = (int) ((double) current / max * total);
        return "§a" + "█".repeat(Math.max(0, filled)) +
                "§c" + "░".repeat(Math.max(0, total - filled)) +
                " §e" + current + "§7/§e" + max;
    }

    // Helpers for plugins or tab lists
    public static String getVoteStatus() {
        if (INSTANCE == null || INSTANCE.config == null) return "0/0";
        return INSTANCE.currentVotes + "/" + INSTANCE.config.requiredVotes;
    }

    public static String getVoteProgressBar() {
        if (INSTANCE == null || INSTANCE.config == null) return "";
        return progressBar(INSTANCE.currentVotes, INSTANCE.config.requiredVotes);
    }

    public static String getVoteProgressBarSmall() {
        if (INSTANCE == null || INSTANCE.config == null) return "";
        return progressBarSmall(INSTANCE.currentVotes, INSTANCE.config.requiredVotes);
    }

    public static int getCurrentVotesStatic() {
        return INSTANCE != null ? INSTANCE.currentVotes : 0;
    }

    public static int getRequiredVotesStatic() {
        return INSTANCE != null && INSTANCE.config != null ? INSTANCE.config.requiredVotes : 0;
    }

    public int getCurrentVotes() { return currentVotes; }
    public int getRequiredVotes() { return config.requiredVotes; }
}
