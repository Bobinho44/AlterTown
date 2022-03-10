package fr.bobinho.altertown.utils.item;

import fr.bobinho.altertown.message.AlterTownMessage;
import fr.bobinho.altertown.message.AlterTownMessageManager;
import fr.bobinho.altertown.utils.AlterTown;
import org.apache.commons.lang.Validate;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.inventory.ItemStack;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class AlterTownItemUtil {

    /**
     * Gets the towns gui divider item
     *
     * @param color the towns gui divider item color
     * @return the towns gui divider item
     */
    @Nonnull
    public static ItemStack getAlterTownDiviserItem(ChatColor color) {
        return new AlterTownItemBuilder(switch (color) {

            //Gets glass pane color
            case RED -> Material.RED_STAINED_GLASS_PANE;
            case GREEN -> Material.LIME_STAINED_GLASS_PANE;
            default -> Material.BLACK_STAINED_GLASS_PANE;

        }).name(" ").build();
    }

    /**
     * Gets the towns gui previous page item
     *
     * @return the towns gui previous page item
     */
    @Nonnull
    public static ItemStack getAlterTownPreviousItem() {
        return new AlterTownItemBuilder(Material.MAGENTA_GLAZED_TERRACOTTA)
                .name(AlterTownMessageManager.getColoredText(AlterTownMessage.PREVIOUS_PAGE.getRawText()))
                .build();
    }

    /**
     * Gets the towns gui next page item
     *
     * @return the towns gui next page item
     */
    @Nonnull
    public static ItemStack getAlterTownNextItem() {
        return new AlterTownItemBuilder(Material.MAGENTA_GLAZED_TERRACOTTA)
                .name(AlterTownMessageManager.getColoredText(AlterTownMessage.NEXT_PAGE.getRawText()))
                .build();
    }

    /**
     * Gets the towns gui information item
     *
     * @return the towns gui information item
     */
    @Nonnull
    public static ItemStack getAlterTownInformationItem() {
        return new AlterTownItemBuilder(Material.BOOK)
                .name(AlterTownMessageManager.getColoredText(AlterTownMessage.INFORMATION.getRawText()))
                .build();
    }

    /**
     * Gets the towns gui town item
     *
     * @param alterTown the town
     * @return the towns gui town item
     */
    @Nullable
    public static ItemStack getAlterTownItem(AlterTown alterTown) {
        return alterTown != null ? alterTown.getGUIItem() : null;
    }

    /**
     * Gets the towns gui town leader item
     *
     * @param alterTown the town
     * @return the towns gui town leader item
     */
    @Nullable
    public static ItemStack getAlterTownLeaderItem(@Nonnull AlterTown alterTown) {
        Validate.notNull(alterTown, "alterTown is null");

        if (alterTown.getLeader().isPresent()) {
            OfflinePlayer player = Bukkit.getOfflinePlayer(alterTown.getLeader().get());
            return new AlterTownItemBuilder(player)
                    .name(AlterTownMessageManager.getColoredText(AlterTownMessage.LEADER_COLOR.getRawText() + player.getName()))
                    .build();
        }
        return null;
    }

    /**
     * Gets the towns gui leader divider item
     *
     * @return the towns gui leader divider item
     */
    @Nonnull
    public static ItemStack getAlterTownLeaderDiviserItem() {
        return new AlterTownItemBuilder(Material.PINK_STAINED_GLASS_PANE).name(AlterTownMessage.LEADER_COLOR.getRawText() + "Leader").build();
    }

    /**
     * Gets the towns gui town official item
     *
     * @param alterTown the town
     * @param i         the official number
     * @return the towns gui town official item
     */
    @Nullable
    public static ItemStack getAlterTownOfficialItem(@Nonnull AlterTown alterTown, int i) {
        Validate.notNull(alterTown, "alterTown is null");

        if (alterTown.getOfficials().size() > i) {
            OfflinePlayer player = Bukkit.getOfflinePlayer(alterTown.getOfficials().get(i));
            return new AlterTownItemBuilder(player)
                    .name(AlterTownMessageManager.getColoredText(AlterTownMessage.OFFICIAL_COLOR.getRawText() + player.getName()))
                    .build();
        }
        return null;
    }

    /**
     * Gets the towns gui official divider item
     *
     * @return the towns gui official divider item
     */
    @Nonnull
    public static ItemStack getAlterTownOfficialDiviserItem() {
        return new AlterTownItemBuilder(Material.PINK_STAINED_GLASS_PANE).name(AlterTownMessage.OFFICIAL_COLOR.getRawText() + "Officials").build();
    }

    /**
     * Gets the towns gui town build item
     *
     * @param alterTown the town
     * @param i         the build number
     * @return the towns gui town build item
     */
    @Nullable
    public static ItemStack getAlterTownBuildItem(@Nonnull AlterTown alterTown, int i) {
        Validate.notNull(alterTown, "alterTown is null");

        return alterTown.getBuilds().size() > i ? alterTown.getBuilds().get(i).getGUIItem() : null;
    }

}