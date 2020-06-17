package me.therive.bank.listener;

import me.therive.bank.Main;
import me.therive.bank.entity.BankPlayer;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.scoreboard.*;

public class PlayerListener implements Listener {

    private Main plugin;

    public PlayerListener(Main plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void handleJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();

        BankPlayer.findByUuid(player.getUniqueId(), bankPlayer -> {
            Scoreboard scoreboard = Bukkit.getScoreboardManager().getNewScoreboard();
            Team money = scoreboard.registerNewTeam("money");
            money.setPrefix("§aGuthaben§8: §7");
            money.setSuffix("§7" + bankPlayer.getMoney() + " Euro");
            money.addEntry(ChatColor.RED.toString());

            Objective objective = scoreboard.registerNewObjective("scoreboard", "dummy",
                    ChatColor.YELLOW + player.getName());
            objective.setDisplaySlot(DisplaySlot.SIDEBAR);
            objective.getScore(ChatColor.AQUA.toString()).setScore(1);
            objective.getScore(ChatColor.RED.toString()).setScore(0);

            player.setScoreboard(scoreboard);
        });
    }
}
