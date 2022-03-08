package fr.bobinho.altertown.commands;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.CommandPermission;
import co.aikar.commands.annotation.Default;
import co.aikar.commands.annotation.Syntax;
import fr.bobinho.altertown.utils.inventory.AlterTownInventory;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

@CommandAlias("towns")
public class TownsCommand extends BaseCommand {

    /**
     * Command towns
     *
     * @param commandSender the sender
     */
    @Default
    @Syntax("/towns")
    @CommandPermission("altertown.towns")
    public void onTownsCommand(CommandSender commandSender) {
        if (commandSender instanceof Player) {

            //Opens the towns gui
            ((Player) commandSender).openInventory(AlterTownInventory.getAlterTownsInventory());
        }
    }

}