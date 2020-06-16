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

public class Money_CMD implements CommandExecutor {

    private Main plugin;

    public Money_CMD(Main plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] args) {
        if (args.length == 1) {
            UUID uuid = UUIDFetcher.getUUID(args[0]);
            if (uuid == null) {
                sender.sendMessage(plugin.prefix + "§cSpieler wurde nicht gefunden!");
                return false;
            }
            String name = UUIDFetcher.getName(uuid);
            if (name == null) {
                sender.sendMessage(plugin.prefix + "§cSpieler wurde nicht gefunden!");
                return false;
            }

            BankPlayer bankPlayer = BankPlayer.findByUuid(uuid);
            if (bankPlayer == null) {
                sender.sendMessage(plugin.prefix + "§cSpieler wurde nicht gefunden!");
                return false;
            }

            sender.sendMessage(plugin.prefix + "§7Der Kontostand von §e" + name + "§7 beträgt §a" +
                    bankPlayer.getMoney() + " Euro§7!");
            return true;
        } else if (args.length == 3) {
            UUID uuid = UUIDFetcher.getUUID(args[1]);
            if (uuid == null) {
                sender.sendMessage(plugin.prefix + "§cSpieler wurde nicht gefunden!");
                return false;
            }
            String name = UUIDFetcher.getName(uuid);
            if (name == null) {
                sender.sendMessage(plugin.prefix + "§cSpieler wurde nicht gefunden!");
                return false;
            }

            BankPlayer bankPlayer = BankPlayer.findByUuid(uuid);
            if (bankPlayer == null) {
                sender.sendMessage(plugin.prefix + "§cSpieler wurde nicht gefunden!");
                return false;
            }

            double money;
            try {
                money = Double.parseDouble(args[2]);
            } catch (NumberFormatException nfe) {
                sender.sendMessage(plugin.prefix + "§cEs wurde kein korrekter Wert angegeben!");
                return false;
            }

            if (args[0].equalsIgnoreCase("add")) {
                bankPlayer.addMoney(money);
                bankPlayer.update();
                updatePlayerScoreboard(uuid, bankPlayer);

                sender.sendMessage(plugin.prefix + "§7Der Spieler §e" + name + "§7" +
                        " hat nun §a" + bankPlayer.getMoney() + " Euro §7!");
                return true;
            } else if (args[0].equalsIgnoreCase("remove")) {
                bankPlayer.removeMoney(money);
                bankPlayer.update();
                updatePlayerScoreboard(uuid, bankPlayer);

                sender.sendMessage(plugin.prefix + "§7Der Spieler §e" + name + "§7" +
                        " hat nun §a" + bankPlayer.getMoney() + " Euro §7!");
                return true;
            } else if (args[0].equalsIgnoreCase("set")) {
                bankPlayer.setMoney(money);
                bankPlayer.update();
                updatePlayerScoreboard(uuid, bankPlayer);

                sender.sendMessage(plugin.prefix + "§7Der Spieler §e" + name + "§7" +
                        " hat nun §a" + bankPlayer.getMoney() + " Euro §7!");
                return true;
            } else {
                sender.sendMessage(plugin.prefix + "§7Verwende§8: §e/money <name> §8| §7Kontostand einsehen");
                sender.sendMessage(plugin.prefix + "§7Verwende§8: §e/money <add|remove|set> <name>" +
                        " <money> §8| §7Kontostand verwalten");
                return false;
            }
        } else {
            sender.sendMessage(plugin.prefix + "§7Verwende§8: §e/money <name> §8| §7Kontostand einsehen");
            sender.sendMessage(plugin.prefix + "§7Verwende§8: §e/money <add|remove|set> <name>" +
                    " <money> §8| §7Kontostand verwalten");
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
        money.setSuffix("§7" + bankPlayer.getMoney() + " Euro");
    }
}
