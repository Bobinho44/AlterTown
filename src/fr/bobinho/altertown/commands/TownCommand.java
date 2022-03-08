package fr.bobinho.altertown.commands;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.*;
import co.aikar.commands.bukkit.contexts.OnlinePlayer;
import fr.bobinho.altertown.message.AlterTownMessage;
import fr.bobinho.altertown.message.AlterTownMessageManager;
import fr.bobinho.altertown.utils.AlterTown;
import fr.bobinho.altertown.utils.inventory.AlterTownInventory;
import fr.bobinho.altertown.utils.town.AlterTownManager;
import fr.bobinho.altertown.utils.town.AlterTownRequestManager;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.UUID;

@CommandAlias("town")
public class TownCommand extends BaseCommand {

    /**
     * Command town
     *
     * @param commandSender the sender
     */
    @Default
    @Syntax("/town")
    @CommandPermission("altertown.town")
    public void onTownCommand(CommandSender commandSender) {
        if (commandSender instanceof Player) {
            Player player = (Player) commandSender;
            UUID playerUuid = player.getUniqueId();

            //Checks if the player is in a town
            if (!AlterTownManager.isInAlterTown(playerUuid)) {
                AlterTownMessageManager.sendMessage(playerUuid, AlterTownMessage.NOT_IN_TOWN);
                return;
            }

            //Opens the town gui
            player.openInventory(AlterTownInventory.getAlterTownInventory(AlterTownManager.getPlayerAlterTown(playerUuid).get(), 0));
        }
    }

    /**
     * Command town invite
     *
     * @param commandSender the sender
     */
    @Subcommand("invite")
    @Syntax("/town invite <player>")
    @CommandPermission("altertown.town.invite")
    public void onTownInviteCommand(CommandSender commandSender, @Single OnlinePlayer commandTarget) {
        if (commandSender instanceof Player) {
            UUID receiverUuid = commandTarget.getPlayer().getUniqueId();
            UUID senderUuid = ((Player) commandSender).getUniqueId();

            //Checks if the sender is in a town
            if (!AlterTownManager.isInAlterTown(senderUuid)) {
                AlterTownMessageManager.sendMessage(senderUuid, AlterTownMessage.NOT_IN_TOWN);
                return;
            }

            //Checks if the sender is a citizen
            if (AlterTownManager.isAnAlterTownCitizen(senderUuid)) {
                AlterTownMessageManager.sendMessage(senderUuid, AlterTownMessage.NOT_LEADER_OR_OFFICIAL_OF_TOWN);
                return;
            }

            //Checks if the target is in a town
            if (AlterTownManager.isInAlterTown(receiverUuid)) {
                AlterTownMessageManager.sendMessage(senderUuid, receiverUuid, AlterTownMessage.PLAYER_ALREADY_IN_TOWN);
                return;
            }

            //Checks if the target has a request to join a town
            if (AlterTownRequestManager.hasAlterTownRequest(receiverUuid)) {
                AlterTownMessageManager.sendMessage(senderUuid, receiverUuid, AlterTownMessage.PLAYER_ALREADY_HAVE_INVITATION);
                return;
            }

            //Sends request
            AlterTownRequestManager.sendAlterTownRequest(senderUuid, receiverUuid);

            //Sends messages
            AlterTownMessageManager.sendMessage(senderUuid, receiverUuid, AlterTownMessage.PLAYER_SEND_INVITATION);
            AlterTownMessageManager.sendMessage(receiverUuid, senderUuid, AlterTownMessage.PLAYER_RECEIVE_INVITATION);
        }
    }

    /**
     * Command town accept
     *
     * @param commandSender the sender
     */
    @Subcommand("accept")
    @Syntax("/town accept")
    @CommandPermission("altertown.town.accept")
    public void onTownAcceptCommand(CommandSender commandSender) {
        if (commandSender instanceof Player) {
            UUID receiverUuid = ((Player) commandSender).getUniqueId();

            //Checks if the sender is in a town
            if (AlterTownManager.isInAlterTown(receiverUuid)) {
                AlterTownMessageManager.sendMessage(receiverUuid, AlterTownMessage.ALREADY_IN_TOWN);
                return;
            }

            //Checks if the target has a request to join a town
            if (!AlterTownRequestManager.hasAlterTownRequest(receiverUuid)) {
                AlterTownMessageManager.sendMessage(receiverUuid, AlterTownMessage.NO_INVITATION_FROM_TOWN);
                return;
            }

            //Gets towns
            AlterTown town = AlterTownManager.getPlayerAlterTown(AlterTownRequestManager.getAlterTownRequestSender(receiverUuid)).get();

            //Sends messages
            AlterTownMessageManager.sendMessage(receiverUuid, town, AlterTownMessage.JOIN_TOWN);
            AlterTownMessageManager.sendMessage(town, receiverUuid, AlterTownMessage.PLAYER_JOIN_TOWN);

            //Accepts the request
            AlterTownRequestManager.acceptAlterTownRequest(receiverUuid);
        }
    }

    /**
     * Command town deny
     *
     * @param commandSender the sender
     */
    @Subcommand("deny")
    @Syntax("/town deny")
    @CommandPermission("altertown.town.deny")
    public void onTownDenyCommand(CommandSender commandSender) {
        if (commandSender instanceof Player) {
            Player receiver = (Player) commandSender;
            UUID receiverUuid = receiver.getUniqueId();

            //Checks if the sender is in a town
            if (AlterTownManager.isInAlterTown(receiverUuid)) {
                AlterTownMessageManager.sendMessage(receiverUuid, AlterTownMessage.ALREADY_IN_TOWN);
                return;
            }

            //Checks if the target has a request to join a town
            if (!AlterTownRequestManager.hasAlterTownRequest(receiverUuid)) {
                AlterTownMessageManager.sendMessage(receiverUuid, AlterTownMessage.NO_INVITATION_FROM_TOWN);
                return;
            }

            //Gets towns
            AlterTown town = AlterTownManager.getPlayerAlterTown(AlterTownRequestManager.getAlterTownRequestSender(receiverUuid)).get();

            //Sends messages
            AlterTownMessageManager.sendMessage(receiverUuid, town, AlterTownMessage.DENY_INVITATION_TOWN);
            AlterTownMessageManager.sendMessage(town, receiverUuid, AlterTownMessage.PLAYER_DENY_INVITATION_TOWN);

            //Denies the request
            AlterTownRequestManager.denyAlterTownRequest(receiverUuid);
        }
    }

    /**
     * Command town remove
     *
     * @param commandSender the sender
     */
    @Subcommand("remove")
    @Syntax("/town remove")
    @CommandPermission("altertown.town.remove")
    public void onTownRemoveCommand(CommandSender commandSender, @Single OnlinePlayer commandTarget) {
        if (commandSender instanceof Player) {
            UUID receiverUuid = commandTarget.getPlayer().getUniqueId();
            UUID senderUuid = ((Player) commandSender).getUniqueId();

            //Checks if the sender is in a town
            if (!AlterTownManager.isInAlterTown(senderUuid)) {
                AlterTownMessageManager.sendMessage(senderUuid, AlterTownMessage.NOT_IN_TOWN);
                return;
            }

            //Checks if the receiver is in a town
            if (!AlterTownManager.isInAlterTown(receiverUuid)) {
                AlterTownMessageManager.sendMessage(senderUuid, receiverUuid, AlterTownMessage.PLAYER_NOT_IN_TOWN);
                return;
            }

            //Checks if the receiver is in your town
            if (!AlterTownManager.getPlayerAlterTown(senderUuid).get().equals(AlterTownManager.getPlayerAlterTown(receiverUuid).get())) {
                AlterTownMessageManager.sendMessage(senderUuid, receiverUuid, AlterTownMessage.PLAYER_NOT_IN_SAME_TOWN);
                return;
            }

            //Checks if the sender is a citizen
            if (AlterTownManager.isAnAlterTownCitizen(senderUuid)) {
                AlterTownMessageManager.sendMessage(senderUuid, AlterTownMessage.NOT_LEADER_OR_OFFICIAL_OF_TOWN);
                return;
            }

            //Checks if the target is a citizen
            if (AlterTownManager.isAnAlterTownCitizen(receiverUuid)) {
                AlterTownMessageManager.sendMessage(senderUuid, receiverUuid, AlterTownMessage.PLAYER_LEADER_OR_OFFICIAL_OF_TOWN);
                return;
            }

            //Gets towns
            AlterTown town = AlterTownManager.getPlayerAlterTown(senderUuid).get();

            //Removes the player from the team
            AlterTownManager.removeAlterTownCitizen(receiverUuid);

            //Sends messages
            AlterTownMessageManager.sendMessage(receiverUuid, AlterTownMessage.REMOVE_FROM_TOWN);
            AlterTownMessageManager.sendMessage(town, receiverUuid, AlterTownMessage.PLAYER_REMOVE_FROM_TOWN);
        }
    }

    /**
     * Command town promote
     *
     * @param commandSender the sender
     */
    @Subcommand("promote")
    @Syntax("/town promote")
    @CommandPermission("altertown.town.promote")
    public void onTownPromoteCommand(CommandSender commandSender, @Single OnlinePlayer commandTarget) {
        if (commandSender instanceof Player) {
            UUID receiverUuid = commandTarget.getPlayer().getUniqueId();
            UUID senderUuid = ((Player) commandSender).getUniqueId();

            //Checks if the sender is in a town
            if (!AlterTownManager.isInAlterTown(senderUuid)) {
                AlterTownMessageManager.sendMessage(senderUuid, AlterTownMessage.NOT_IN_TOWN);
                return;
            }

            //Checks if the receiver is in a town
            if (!AlterTownManager.isInAlterTown(receiverUuid)) {
                AlterTownMessageManager.sendMessage(senderUuid, receiverUuid, AlterTownMessage.PLAYER_NOT_IN_TOWN);
                return;
            }

            //Checks if the receiver is in your town
            if (!AlterTownManager.getPlayerAlterTown(senderUuid).get().equals(AlterTownManager.getPlayerAlterTown(receiverUuid).get())) {
                AlterTownMessageManager.sendMessage(senderUuid, receiverUuid, AlterTownMessage.PLAYER_NOT_IN_SAME_TOWN);
                return;
            }

            //Checks if the sender is a leader
            if (!AlterTownManager.isAnAlterTownLeader(senderUuid)) {
                AlterTownMessageManager.sendMessage(senderUuid, AlterTownMessage.NOT_LEADER_OF_TOWN);
                return;
            }

            //Checks if the target is a citizen
            if (!AlterTownManager.isAnAlterTownCitizen(receiverUuid)) {
                AlterTownMessageManager.sendMessage(senderUuid, receiverUuid, AlterTownMessage.PLAYER_NOT_CITIZEN_OF_TOWN);
                return;
            }

            //Gets towns
            AlterTown town = AlterTownManager.getPlayerAlterTown(senderUuid).get();

            //Checks if there is 6 official
            if (AlterTownManager.alterTownOfficialsIsFull(town.getName())) {
                AlterTownMessageManager.sendMessage(senderUuid, AlterTownMessage.OFFICIAL_IS_FULL);
                return;
            }

            //Removes the player from the team
            AlterTownManager.promoteAlterTownCitizen(receiverUuid);

            //Sends messages
            AlterTownMessageManager.sendMessage(town, receiverUuid, AlterTownMessage.PLAYER_PROMOTE);
        }
    }

    /**
     * Command town demote
     *
     * @param commandSender the sender
     */
    @Subcommand("demote")
    @Syntax("/town demote")
    @CommandPermission("altertown.town.demote")
    public void onTownDemoteCommand(CommandSender commandSender, @Single OnlinePlayer commandTarget) {
        if (commandSender instanceof Player) {
            UUID receiverUuid = commandTarget.getPlayer().getUniqueId();
            UUID senderUuid = ((Player) commandSender).getUniqueId();

            //Checks if the sender is in a town
            if (!AlterTownManager.isInAlterTown(senderUuid)) {
                AlterTownMessageManager.sendMessage(senderUuid, AlterTownMessage.NOT_IN_TOWN);
                return;
            }

            //Checks if the receiver is in a town
            if (!AlterTownManager.isInAlterTown(receiverUuid)) {
                AlterTownMessageManager.sendMessage(senderUuid, receiverUuid, AlterTownMessage.PLAYER_NOT_IN_TOWN);
                return;
            }

            //Checks if the receiver is in your town
            if (!AlterTownManager.getPlayerAlterTown(senderUuid).get().equals(AlterTownManager.getPlayerAlterTown(receiverUuid).get())) {
                AlterTownMessageManager.sendMessage(senderUuid, receiverUuid, AlterTownMessage.PLAYER_NOT_IN_SAME_TOWN);
                return;
            }

            //Checks if the sender is a leader
            if (!AlterTownManager.isAnAlterTownLeader(senderUuid)) {
                AlterTownMessageManager.sendMessage(senderUuid, AlterTownMessage.NOT_LEADER_OF_TOWN);
                return;
            }

            //Checks if the target is an official
            if (!AlterTownManager.isAnAlterTownOfficial(receiverUuid)) {
                AlterTownMessageManager.sendMessage(senderUuid, receiverUuid, AlterTownMessage.PLAYER_NOT_OFFICIAL_OF_TOWN);
                return;
            }

            //Gets towns
            AlterTown town = AlterTownManager.getPlayerAlterTown(senderUuid).get();

            //Removes the player from the team
            AlterTownManager.demoteAlterTownOfficial(receiverUuid);

            //Sends messages
            AlterTownMessageManager.sendMessage(town, receiverUuid, AlterTownMessage.PLAYER_DEMOTE);
        }
    }

}