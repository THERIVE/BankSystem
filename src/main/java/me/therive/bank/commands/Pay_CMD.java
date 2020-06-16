package me.therive.bank.commands;

import me.therive.bank.Main;
import me.therive.bank.entity.BankPlayer;
import me.therive.bank.utils.UUIDFetcher;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;

import java.util.UUID;

public class Pay_CMD implements CommandExecutor {

    private Main plugin;

    public Pay_CMD(Main plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;
            BankPlayer bankPlayer = BankPlayer.findByUuid(player.getUniqueId());

            if (args.length == 2) {
                if (player.getName().equalsIgnoreCase(args[0])) {
                    player.sendMessage(plugin.prefix+"§cDu kannst dir selber kein Geld überweisen!");
                    return false;
                }

                UUID uuid = UUIDFetcher.getUUID(args[0]);
                if (uuid == null) {
                    player.sendMessage(plugin.prefix+"§cSpieler wurde nicht gefunden!");
                    return false;
                }
                String name = UUIDFetcher.getName(uuid);
                if (name == null) {
                    player.sendMessage(plugin.prefix+"§cSpieler wurde nicht gefunden!");
                    return false;
                }

                BankPlayer targetBankPlayer = BankPlayer.findByUuid(uuid);
                if (targetBankPlayer == null) {
                    player.sendMessage(plugin.prefix+"§cSpieler wurde nicht gefunden!");
                    return false;
                }

                double money;
                try {
                    money = Double.parseDouble(args[1]);
                } catch (NumberFormatException nfe) {
                    player.sendMessage(plugin.prefix+"§cEs wurde kein korrekter Wert angegeben!");
                    return false;
                }

                if (money > bankPlayer.getMoney()) {
                    player.sendMessage(plugin.prefix+"§cDu hast nicht genügend Geld, um diesen Betrag zu überweisen!");
                    return false;
                }

                Player target = Bukkit.getPlayer(uuid);
                if (target != null) {
                    target.sendMessage(plugin.prefix+"§7Du hast von §e"+player.getName()+"§a "+money+ " Euro §7erhalten!");
                }

                player.sendMessage(plugin.prefix+"§7Du hast §a"+money+" Euro §7an §e"+name+"§7 überwiesen!");
                bankPlayer.removeMoney(money);
                bankPlayer.update();

                targetBankPlayer.addMoney(money);
                targetBankPlayer.update();

                updatePlayerScoreboard(player.getUniqueId(), bankPlayer);
                updatePlayerScoreboard(uuid, targetBankPlayer);
                return true;
            } else {
                player.sendMessage(plugin.prefix + "§7Verwende§8: §e/pay <name> <money> §8| §7Geld überweisen");
                return false;
            }
        } else {
            sender.sendMessage(plugin.prefix+"§cDieser Befehl ist nur für Spieler verfügbar!");
            return false;
        }
    }

    private void updatePlayerScoreboard(UUID uuid, BankPlayer bankPlayer) {
        Player player = Bukkit.getPlayer(uuid);

        if (player == null) {
            return;
        }

        Scoreboard scoreboard = player.getScoreboard();
        Team money = scoreboard.getTeam("money");
        money.setSuffix("§7"+bankPlayer.getMoney()+" Euro");
    }
}
