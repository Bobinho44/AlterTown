package fr.bobinho.altertown.listeners;

import fr.bobinho.altertown.AlterTownCore;
import fr.bobinho.altertown.utils.AlterTown;
import fr.bobinho.altertown.utils.format.AlterTownDateFormat;
import fr.bobinho.altertown.utils.inventory.AlterTownInventory;
import fr.bobinho.altertown.utils.inventory.holder.TownHolder;
import fr.bobinho.altertown.utils.inventory.holder.TownsHolder;
import fr.bobinho.altertown.utils.item.AlterTownItemUtil;
import fr.bobinho.altertown.utils.player.AlterTownPlayerManager;
import fr.bobinho.altertown.utils.town.AlterTownManager;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.Calendar;
import java.util.Objects;
import java.util.UUID;

public class TestListener implements Listener {

    /**
     * Listen when a ...
     *
     * @param e the ... event
     */
    @EventHandler
    public void onPlayerClickInInventory(InventoryClickEvent e) {
        if (e.getClickedInventory() != null && e.getClickedInventory().getHolder() instanceof TownsHolder) {
            e.setCancelled(true);

            ItemStack item = e.getCurrentItem();
            if (item != null && !item.equals(AlterTownItemUtil.getAlterTownDiviserItem(ChatColor.RED))) {
                AlterTownManager.getAlterTown(ChatColor.stripColor(item.getItemMeta().getDisplayName())).ifPresent(town ->
                        e.getWhoClicked().openInventory(AlterTownInventory.getAlterTownInventory(town, 1)));
            }
        }

        if (e.getClickedInventory() != null && e.getClickedInventory().getHolder() instanceof TownHolder) {
            e.setCancelled(true);

            ItemStack item = e.getCurrentItem();
            if (item != null && item.equals(AlterTownItemUtil.getAlterTownNextItem())) {
                e.getWhoClicked().openInventory((Inventory) ((TownHolder) e.getClickedInventory().getHolder()).getNextPage());
            }

            if (item != null && item.equals(AlterTownItemUtil.getAlterTownPreviousItem())) {
                e.getWhoClicked().openInventory((Inventory) ((TownHolder) e.getClickedInventory().getHolder()).getPreviousPage());
            }

            if (item != null && item.equals(AlterTownItemUtil.getAlterTownInformationItem())) {
                e.getWhoClicked().sendMessage(((TownHolder) e.getClickedInventory().getHolder()).getTown().getURLInformation());
            }
        }
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent e) {
        AlterTownPlayerManager.savePracticePlayerData(e.getPlayer());
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent e) {
        YamlConfiguration configuration = AlterTownCore.getPlayersSettings().getConfiguration();

        for (String uuid : configuration.getKeys(false)) {
            if (AlterTownDateFormat.getAsDayIntervalFormat(configuration.getString(uuid + ".connection")) >= AlterTownCore.getMessagesSettings().getConfiguration().getInt("offlineCooldown")) {
                if (AlterTownManager.isAnAlterTownLeader(UUID.fromString(uuid))) {
                    AlterTownManager.getPlayerAlterTown(UUID.fromString(uuid)).get().removeMember(UUID.fromString(uuid));
                }
            }
        }
    }
}