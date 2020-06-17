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
    private MongoDB mongoDB;

    private final static ExecutorService EXECUTOR_SERVICE = Executors.newSingleThreadExecutor();
    private static Main instance;

    private final String PREFIX = "§8[§6Bank§8] §7";

    private HashMap<UUID, BankPlayer> bankPlayers;

    @Override
    public void onEnable() {
        /**
         * INITIALIZE DATABASE
         */
        mongoConnection = new MongoConnection(
                "88.99.62.231",
                "admin",
                "T6pZUjrRqz0l3LeHx65PQsAjn2uEyF8fmzgJHxCzvUHFtNMyqWAPlqDmScOyweSZ",
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

    public MongoConnection getMongoConnection() {
        return mongoConnection;
    }

    public MongoDB getMongoDB() {
        return mongoDB;
    }

    public static ExecutorService getExecutorService() {
        return EXECUTOR_SERVICE;
    }

    public static Main getInstance() {
        return instance;
    }

    public String getPrefix() {
        return PREFIX;
    }

    public HashMap<UUID, BankPlayer> getBankPlayers() {
        return bankPlayers;
    }
}
