package spinner.voteparty;

import java.util.List;

public class VotePartyConfig {
    public int requiredVotes = 50;
    public String title = "§6§lVOTE PARTY!";
    public String subtitle = "§e%current_votes% votes reached!";
    public List<String> commands = List.of(
            "give {player} diamond 10",
            "eco give {player} 500"
    );
}
