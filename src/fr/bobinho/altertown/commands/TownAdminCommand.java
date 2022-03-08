package fr.bobinho.altertown.commands;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.*;
import fr.bobinho.altertown.message.AlterTownMessage;
import fr.bobinho.altertown.message.AlterTownMessageManager;
import fr.bobinho.altertown.utils.AlterTown;
import fr.bobinho.altertown.utils.town.AlterTownManager;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Arrays;
import java.util.Objects;
import java.util.UUID;

@CommandAlias("town")
public class TownAdminCommand extends BaseCommand {


    /**
     * Command town edit
     *
     * @param commandSender the sender
     */
    @Syntax("/town <name> <action> <field> <value>")
    @Default
    @CommandPermission("altertown.town.edit")
    public void onTownEditCommand(CommandSender commandSender, @Single String name, @Single String action, @Optional String field, @Optional String value) {
        if (commandSender instanceof Player) {

            Player sender = (Player) commandSender;
            UUID senderUuid = sender.getUniqueId();

            //Checks if the name is already use
            if (!AlterTownManager.isItAlterTown(name)) {
                AlterTownMessageManager.sendMessage(senderUuid, name, AlterTownMessage.NOT_NAME_TOWN);
                return;
            }

            //Sets leader
            if (action.equalsIgnoreCase("set") && field != null && field.equalsIgnoreCase("leader") && value != null) {

                //Checks if the player exist
                if (Bukkit.getPlayer(value) == null) {
                    AlterTownMessageManager.sendMessage(senderUuid, AlterTownMessage.NOT_CONNECTED);
                    return;
                }

                UUID receiverUuid = Objects.requireNonNull(Bukkit.getPlayer(value)).getUniqueId();

                //Gets towns
                AlterTown town = AlterTownManager.getAlterTown(name).get();

                //Sets town leader
                AlterTownManager.setAlterTownLeader(receiverUuid, town);

                //Sends messages
                AlterTownMessageManager.sendMessage(town, receiverUuid, AlterTownMessage.PLAYER_SET_LEADER_TOWN);
                AlterTownMessageManager.sendMessage(senderUuid, value, town, AlterTownMessage.ADMIN_LEADER_TOWN);
                return;
            }

            //Sets name
            else if (action.equalsIgnoreCase("set") && field != null && field.equalsIgnoreCase("name") && value != null) {

                //Sets town name
                AlterTownManager.setAlterTownName(name, value);

                //Sends messages
                AlterTownMessageManager.sendMessage(senderUuid, value, AlterTownMessage.ADMIN_NAME_TOWN);
                return;
            }

            //Sets description
            else if (action.equalsIgnoreCase("set") && field != null && field.equalsIgnoreCase("description") && value != null) {

                //Sets town description
                AlterTownManager.setAlterTownDescription(name, value);

                //Sends messages
                AlterTownMessageManager.sendMessage(senderUuid, value, AlterTownMessage.ADMIN_DESCRIPTION_TOWN);
                return;
            }

            //Sets biome
            else if (action.equalsIgnoreCase("set") && field != null && field.equalsIgnoreCase("biome") && value != null) {

                //Sets town biome
                AlterTownManager.setAlterTownBiome(name, value);

                //Sends messages
                AlterTownMessageManager.sendMessage(senderUuid, value, AlterTownMessage.ADMIN_BIOME_TOWN);
                return;
            }

            //Sets url
            else if (action.equalsIgnoreCase("set") && field != null && field.equalsIgnoreCase("url") && value != null) {

                //Sets town url
                AlterTownManager.setAlterTownURL(name, value);

                //Sends messages
                AlterTownMessageManager.sendMessage(senderUuid, value, AlterTownMessage.ADMIN_URL_TOWN);
                return;
            }

            //Gets town member
            else if (action.equalsIgnoreCase("list") && field == null && value == null) {

                //Sends list
                sender.sendMessage(AlterTownManager.getAlterTown(name).get().getMembersAsString());
                return;
            }

            //Adds official
            else if (action.equalsIgnoreCase("add") && field != null && field.equalsIgnoreCase("official") && value != null) {

                //Checks if the player exist
                if (Bukkit.getPlayer(value) == null) {
                    AlterTownMessageManager.sendMessage(senderUuid, AlterTownMessage.NOT_CONNECTED);
                    return;
                }

                UUID receiverUuid = Objects.requireNonNull(Bukkit.getPlayer(value)).getUniqueId();

                //Checks if the there is 6 officials
                if (AlterTownManager.alterTownOfficialsIsFull(name)) {
                    AlterTownMessageManager.sendMessage(senderUuid, AlterTownMessage.OFFICIAL_IS_FULL);
                    return;
                }

                //Gets towns
                AlterTown town = AlterTownManager.getAlterTown(name).get();

                //Adds an official
                AlterTownManager.addAlterTownOfficial(receiverUuid, town);

                //Sends messages
                AlterTownMessageManager.sendMessage(town, receiverUuid, AlterTownMessage.PLAYER_PROMOTE);
                AlterTownMessageManager.sendMessage(senderUuid, value, town, AlterTownMessage.ADMIN_PROMOTE);
                return;
            }

            //Removes official
            else if (action.equalsIgnoreCase("remove") && field != null && field.equalsIgnoreCase("official") && value != null) {

                //Checks if the player exist
                if (Bukkit.getPlayer(value) == null) {
                    AlterTownMessageManager.sendMessage(senderUuid, AlterTownMessage.NOT_CONNECTED);
                    return;
                }

                UUID receiverUuid = Objects.requireNonNull(Bukkit.getPlayer(value)).getUniqueId();


                //Checks if the receiver is in a town
                if (!AlterTownManager.isInAlterTown(receiverUuid)) {
                    AlterTownMessageManager.sendMessage(senderUuid, receiverUuid, AlterTownMessage.PLAYER_NOT_IN_TOWN);
                    return;
                }

                //Gets towns
                AlterTown town = AlterTownManager.getAlterTown(name).get();

                //Checks if the receiver is in a selected town
                if (!town.equals(AlterTownManager.getPlayerAlterTown(receiverUuid).get())) {
                    AlterTownMessageManager.sendMessage(senderUuid, town, AlterTownMessage.PLAYER_NOT_IN_SELECTED_TOWN);
                    return;
                }

                if (!AlterTownManager.isAnAlterTownOfficial(receiverUuid)) {
                    AlterTownMessageManager.sendMessage(senderUuid, receiverUuid, AlterTownMessage.PLAYER_NOT_OFFICIAL_OF_TOWN);
                    return;
                }

                //Adds an official
                AlterTownManager.demoteAlterTownOfficial(receiverUuid);

                //Sends messages
                AlterTownMessageManager.sendMessage(town, senderUuid, AlterTownMessage.PLAYER_DEMOTE);
                AlterTownMessageManager.sendMessage(senderUuid, value, town, AlterTownMessage.ADMIN_DEMOTE);
                return;
            }

            //Adds building
            else if (action.equalsIgnoreCase("add") && field != null && field.equalsIgnoreCase("building") && value != null && value.split(" ").length > 1) {

                //Checks if the player hand is empty
                if (sender.getInventory().getItemInMainHand().getType() == Material.AIR) {
                    AlterTownMessageManager.sendMessage(senderUuid, value, AlterTownMessage.ADMIN_EMPTY_HAND);
                    return;
                }

                //Gets town
                AlterTown town = AlterTownManager.getAlterTown(name).get();

                String[] values = value.split(" ");

                if (AlterTownManager.isItAlterTownBuild(town, values[0])) {
                    AlterTownMessageManager.sendMessage(senderUuid, town, values[0], AlterTownMessage.ADMIN_BUILDING_ALREADY_EXIST);
                    return;
                }

                //Adds build
                AlterTownManager.addAlterTownBuild(town, sender.getInventory().getItemInMainHand(), values[0], values[1], Arrays.copyOfRange(values, 2, values.length));
                AlterTownMessageManager.sendMessage(senderUuid, town, values[0], AlterTownMessage.ADMIN_NEW_BUILDING);
                return;
            }

            //Removes building
            else if (action.equalsIgnoreCase("remove") && field != null && field.equalsIgnoreCase("building") && value != null && value.split(" ").length == 1) {

                //Gets towns
                AlterTown town = AlterTownManager.getAlterTown(name).get();

                String[] values = value.split(" ");

                if (!AlterTownManager.isItAlterTownBuild(town, values[0])) {
                    AlterTownMessageManager.sendMessage(senderUuid, town, values[0], AlterTownMessage.ADMIN_BUILDING_NOT_EXIST);
                    return;
                }

                //Removes build
                AlterTownManager.removeAlterTownBuild(town, values[0]);
                AlterTownMessageManager.sendMessage(senderUuid, town, values[0], AlterTownMessage.ADMIN_REMOVE_BUILDING);
                return;
            }

            AlterTownMessageManager.sendMessage(senderUuid, AlterTownMessage.ADMIN_COMMAND_DOESNT_EXISTS);
        }
    }

    /**
     * Command town add
     *
     * @param commandSender the sender
     */
    @Subcommand("add")
    @Syntax("/town add <name>")
    @CommandPermission("altertown.town.add")
    public void onTownAddCommand(CommandSender commandSender, @Single String name) {
        if (commandSender instanceof Player) {
            Player sender = (Player) commandSender;
            UUID senderUuid = sender.getUniqueId();

            //Checks if the name is already use
            if (AlterTownManager.isItAlterTown(name)) {
                AlterTownMessageManager.sendMessage(senderUuid, name, AlterTownMessage.NAME_ALREADY_USE);
                return;
            }

            //Checks if there is 28 towns
            if (AlterTownManager.alterTownsIsFull()) {
                AlterTownMessageManager.sendMessage(senderUuid, AlterTownMessage.TOWN_FULL);
                return;
            }

            //Checks if the sender has an item in his hand
            if (sender.getInventory().getItemInMainHand().getType() == Material.AIR) {
                AlterTownMessageManager.sendMessage(senderUuid, AlterTownMessage.NOT_ITEM_IN_HAND);
                return;
            }

            //Creates the town
            AlterTownManager.createAlterTown(sender.getInventory().getItemInMainHand(), name);

            //Sends messages
            AlterTownMessageManager.sendMessage(senderUuid, name, AlterTownMessage.CREATE_TOWN);
        }
    }

    /**
     * Command town remove
     *
     * @param commandSender the sender
     */
    @Subcommand("remove")
    @Syntax("/town remove <name>")
    @CommandPermission("altertown.town.remove")
    public void onTownRemoveCommand(CommandSender commandSender, @Single String name) {
        if (commandSender instanceof Player) {
            Player sender = (Player) commandSender;
            UUID senderUuid = sender.getUniqueId();

            //Checks if the name is already use
            if (!AlterTownManager.isItAlterTown(name)) {
                AlterTownMessageManager.sendMessage(senderUuid, name, AlterTownMessage.NOT_NAME_TOWN);
                return;
            }

            //Delete the town
            AlterTownManager.deleteAlterTown(name);

            //Sends messages
            AlterTownMessageManager.sendMessage(senderUuid, name, AlterTownMessage.DELETE_TOWN);
        }
    }

}