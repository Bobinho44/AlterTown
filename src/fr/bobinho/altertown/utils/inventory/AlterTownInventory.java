package fr.bobinho.altertown.utils.inventory;

import fr.bobinho.altertown.utils.AlterTown;
import fr.bobinho.altertown.utils.inventory.holder.TownHolder;
import fr.bobinho.altertown.utils.inventory.holder.TownsHolder;
import fr.bobinho.altertown.utils.item.AlterTownItemUtil;
import fr.bobinho.altertown.utils.town.AlterTownManager;
import net.kyori.adventure.text.Component;
import org.apache.commons.lang.Validate;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.inventory.Inventory;

import javax.annotation.Nonnull;

public class AlterTownInventory {

    /**
     * Gets the towns inventory
     *
     * @return the towns inventory
     */
    @Nonnull
    public static Inventory getAlterTownsInventory() {
        Inventory inventory = Bukkit.createInventory(new TownsHolder(), 54, Component.text(""));

        //Places divider
        for (int i = 0; i < 54; i++) {
            inventory.setItem(i, AlterTownItemUtil.getAlterTownDiviserItem(ChatColor.RED));
        }

        //Places item town
        for (int i = 1; i < 29; i++) {
            inventory.setItem(i + 9 + 2 * ((i - 1) / 7), AlterTownItemUtil.getAlterTownItem(AlterTownManager.getAlterTown(i - 1)));
        }

        //Returns the inventory
        return inventory;
    }

    /**
     * Gets a town inventory
     *
     * @param alterTown the town
     * @param page      the page
     * @return the town inventory
     */
    @Nonnull
    public static Inventory getAlterTownInventory(@Nonnull AlterTown alterTown, int page) {
        Validate.notNull(alterTown, "alterTown is null");

        Inventory inventory = Bukkit.createInventory(new TownHolder(alterTown, page), 54, Component.text(""));

        //Places divider
        for (int i = 0; i < 3; i++) {
            inventory.setItem(i, AlterTownItemUtil.getAlterTownDiviserItem(ChatColor.LIGHT_PURPLE));
        }

        //Places divider
        for (int i = 9; i < 18; i++) {
            inventory.setItem(i, AlterTownItemUtil.getAlterTownDiviserItem(ChatColor.GREEN));
        }

        //Places leader
        inventory.setItem(1, AlterTownItemUtil.getAlterTownLeaderItem(alterTown));

        //Places officials
        for (int i = 0; i < 6; i++) {
            inventory.setItem(i + 3, AlterTownItemUtil.getAlterTownOfficialItem(alterTown, i));
        }

        //Places builds
        for (int i = 0; i < 27; i++) {
            inventory.setItem(i + 18, AlterTownItemUtil.getAlterTownBuildItem(alterTown, i + 27 * (page - 1)));
        }

        //Places previous page
        if (page > 1) {
            inventory.setItem(45, AlterTownItemUtil.getAlterTownPreviousItem());
        }

        //Places next page
        if (alterTown.getBuilds().size() > 27 * page) {
            inventory.setItem(53, AlterTownItemUtil.getAlterTownNextItem());
        }

        //Place informations
        inventory.setItem(49, AlterTownItemUtil.getAlterTownInformationItem());

        //Returns the inventory
        return inventory;
    }

}