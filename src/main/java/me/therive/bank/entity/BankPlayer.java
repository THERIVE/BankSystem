package me.therive.bank.entity;

import me.therive.bank.Main;
import org.bson.Document;

import java.util.UUID;
import java.util.concurrent.ExecutionException;

public class BankPlayer {

    private UUID uuid;
    private Double money;

    public BankPlayer(UUID uuid, Double money) {
        this.uuid = uuid;
        this.money = money;
    }

    public void update() {
        Document toUpdate = new Document("uuid", String.valueOf(uuid));
        toUpdate.append("money", money);

        Main.getInstance().mongoDB.updateDocumentAsync("uuid", String.valueOf(uuid), toUpdate);
    }

    public void addMoney(Double toAdd) {
        this.money += toAdd;
    }

    public void removeMoney(Double toRemove) {
        this.money -= toRemove;
    }

    public void setMoney(Double money) {
        this.money = money;
    }

    public Double getMoney() {
        return money;
    }

    public UUID getUuid() {
        return uuid;
    }

    public static BankPlayer createBankPlayer(UUID uuid) {
        Document document = new Document("uuid", String.valueOf(uuid));
        document.append("money", 0.0D);

        Main.getInstance().mongoDB.insertDocumentAsync(document);

        BankPlayer bankPlayer = new BankPlayer(uuid, 0.0D);
        Main.getInstance().bankPlayers.put(uuid, bankPlayer);

        return bankPlayer;
    }

    public static BankPlayer findByUuid(UUID uuid) {
        if (Main.getInstance().bankPlayers.containsKey(uuid)) {
            return Main.getInstance().bankPlayers.get(uuid);
        }

        Document document = null;
        try {
            document = Main.getInstance().mongoDB.findDocumentAsync("uuid", String.valueOf(uuid)).get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

        if (document == null) {
            return createBankPlayer(uuid);
        }

        BankPlayer bankPlayer = new BankPlayer(uuid, document.getDouble("money"));
        Main.getInstance().bankPlayers.put(uuid, bankPlayer);

        return bankPlayer;
    }
}
