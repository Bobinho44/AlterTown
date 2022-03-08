package fr.bobinho.altertown.utils.town;

import fr.bobinho.altertown.utils.scheduler.AlterTownScheduler;
import org.apache.commons.lang.Validate;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

public class AlterTownRequestManager {

    /**
     * The requests list
     */
    private static final List<AlterTownRequest> alterTownRequests = new ArrayList<>();

    /**
     * Gets all request
     *
     * @return all requests
     */
    private static List<AlterTownRequest> getAlterTownRequests() {
        return alterTownRequests;
    }

    /**
     * Gets a specific request
     *
     * @param receiver the receiver
     * @return the request if found
     */
    private static Optional<AlterTownRequest> getAlterTownRequest(@Nonnull UUID receiver) {
        Validate.notNull(receiver, "receiver is null");

        //Gets the selected practice duel request
        return getAlterTownRequests().stream().filter(request -> request.getReceiver().equals(receiver)).findFirst();
    }

    /**
     * Checks if the player is in a town
     *
     * @param receiver the receiver
     * @return if he is in a town
     */
    public static boolean hasAlterTownRequest(@Nonnull UUID receiver) {
        Validate.notNull(receiver, "receiver is null");

        return getAlterTownRequests().stream().anyMatch(request -> request.getReceiver().equals(receiver));
    }

    public static UUID getAlterTownRequestSender(@Nonnull UUID receiver) {
        Validate.notNull(receiver, "receiver is null");
        Validate.isTrue(hasAlterTownRequest(receiver), "receiver dont have any request");

        return getAlterTownRequest(receiver).get().getSender();
    }

    /**
     * Sends a request
     *
     * @param senderUuid   the senderUuid
     * @param receiverUuid the receiverUuid
     */
    public static void sendAlterTownRequest(@Nonnull UUID senderUuid, @Nonnull UUID receiverUuid) {
        Validate.notNull(senderUuid, "senderUuid is null");
        Validate.notNull(receiverUuid, "receiverUuid is null");
        Validate.isTrue(!hasAlterTownRequest(receiverUuid), "receiver already have an invitation");

        //Creates the request
        getAlterTownRequests().add(new AlterTownRequest(senderUuid, receiverUuid));

        //Waits 60 seconds to clear the request
        AlterTownScheduler.syncScheduler().after(60, TimeUnit.SECONDS).run(() -> {

            //Checks if the request still exist
            if (hasAlterTownRequest(receiverUuid)) {

                //Removes the request
                removeAlterTownRequest(senderUuid, receiverUuid);

            }
        });
    }

    /**
     * Removes a request
     *
     * @param senderUuid   the sender
     * @param receiverUuid the receiver
     */
    public static void removeAlterTownRequest(@Nonnull UUID senderUuid, @Nonnull UUID receiverUuid) {
        Validate.notNull(senderUuid, "senderUuid is null");
        Validate.notNull(receiverUuid, "receiverUuid is null");
        Validate.isTrue(hasAlterTownRequest(receiverUuid), "receiver dont have any request");

        //Removes the request
        getAlterTownRequests().remove(getAlterTownRequest(receiverUuid).get());

        //Gets players
        Player sender = Bukkit.getPlayer(senderUuid);
        Player receiver = Bukkit.getPlayer(receiverUuid);
    }

    /**
     * Accepts a request
     *
     * @param receiverUuid the receiver
     */
    public static void acceptAlterTownRequest(@Nonnull UUID receiverUuid) {
        Validate.notNull(receiverUuid, "receiverUuid is null");
        Validate.isTrue(hasAlterTownRequest(receiverUuid), "receiver dont have any request");
        Validate.isTrue(AlterTownManager.isInAlterTown(getAlterTownRequest(receiverUuid).get().getSender()), "sender dont have town");

        AlterTownManager.addAlterTownCitizen(receiverUuid, AlterTownManager.getPlayerAlterTown(getAlterTownRequest(receiverUuid).get().getSender()).get());
        getAlterTownRequests().remove(getAlterTownRequest(receiverUuid).get());
    }

    /**
     * Denies a request
     *
     * @param receiverUuid the receiver
     */
    public static void denyAlterTownRequest(@Nonnull UUID receiverUuid) {
        Validate.notNull(receiverUuid, "receiverUuid is null");
        Validate.isTrue(hasAlterTownRequest(receiverUuid), "receiver dont have any request");

        removeAlterTownRequest(getAlterTownRequest(receiverUuid).get().getSender(), receiverUuid);
    }

}