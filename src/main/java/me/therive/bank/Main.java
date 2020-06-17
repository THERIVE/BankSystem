package me.therive.bank;

import me.therive.bank.commands.MoneyCommand;
import me.therive.bank.commands.PayCommand;
import me.therive.bank.database.MongoConnection;
import me.therive.bank.database.MongoDB;
import me.therive.bank.entity.BankPlayer;
import me.therive.bank.listener.PlayerListener;
import org.bukkit.Bukkit;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashMap;
import java.util.UUID;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Main extends JavaPlugin {

    private MongoConnection mongoConnection;
    public MongoDB mongoDB;

    public final static ExecutorService EXECUTOR_SERVICE = Executors.newSingleThreadExecutor();
    private static Main instance;

    public final String PREFIX = "§8[§6Bank§8] §7";

    public HashMap<UUID, BankPlayer> bankPlayers;

    @Override
    public void onEnable() {
        /**
         * INITIALIZE DATABASE
         */
        mongoConnection = new MongoConnection(
                "127.0.0.1",
                "admin",
                "123456",
                "admin");
        mongoDB = new MongoDB(mongoConnection, mongoConnection.getDatabase("THERIVE"),
                mongoConnection.getCollection("THERIVE", "BankSystem"));

        /**
         * INITIALIZE GENERAL STUFF
         */
        instance = this;

        /**
         * INITIALIZE MAPS AND LISTS
         */
        this.bankPlayers = new HashMap<>();

        /**
         * REGISTER LISTENER AND COMMANDS
         */
        PluginManager pluginManager = Bukkit.getPluginManager();
        pluginManager.registerEvents(new PlayerListener(this), this);

        this.getCommand("money").setExecutor(new MoneyCommand(this));
        this.getCommand("pay").setExecutor(new PayCommand(this));
    }

    public static Main getInstance() {
        return instance;
    }
}
