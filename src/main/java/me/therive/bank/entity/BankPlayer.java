package me.therive.bank.entity;

import me.therive.bank.Main;
import org.bson.Document;

import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;

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

        Main.getInstance().getMongoDB().updateDocumentAsync("uuid", String.valueOf(uuid), toUpdate);
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

    /**
     *
     * @param uuid UUID from Player
     * @return new created bankplayer
     */

    public static BankPlayer createBankPlayer(UUID uuid) {
        Document document = new Document("uuid", String.valueOf(uuid));
        document.append("money", 0.0D);

        Main.getInstance().getMongoDB().insertDocumentAsync(document);

        BankPlayer bankPlayer = new BankPlayer(uuid, 0.0D);
        Main.getInstance().getBankPlayers().put(uuid, bankPlayer);

        return bankPlayer;
    }

    /**
     *
     * @param uuid UUID from Player
     * @return found bankplayer from the database
     */

    public static void findByUuid(UUID uuid, Consumer<BankPlayer> consumer) {
        if (Main.getInstance().getBankPlayers().containsKey(uuid)) {
            consumer.accept(Main.getInstance().getBankPlayers().get(uuid));
            return;
        }

        CompletableFuture<Document> completableFuture =
                Main.getInstance().getMongoDB().findDocumentAsync("uuid", String.valueOf(uuid));

        completableFuture.thenApplyAsync(document -> {
            if (document == null) {
                return createBankPlayer(uuid);
            }

            BankPlayer bankPlayer = new BankPlayer(uuid, document.getDouble("money"));
            Main.getInstance().getBankPlayers().put(uuid, bankPlayer);
            return bankPlayer;
        }).thenAcceptAsync(consumer);
    }
}
