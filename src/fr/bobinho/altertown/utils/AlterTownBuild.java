package fr.bobinho.altertown.utils;

import fr.bobinho.altertown.utils.item.AlterTownItemBuilder;
import org.apache.commons.lang.Validate;
import org.bukkit.ChatColor;
import org.bukkit.inventory.ItemStack;

import javax.annotation.Nonnull;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class AlterTownBuild {

    /**
     * Fields
     */
    private final ItemStack item;
    private final String name;
    private final String level;
    private final String description;

    /**
     * Creates a new town build
     *
     * @param item        the town build item for gui
     * @param name        the town build name
     * @param description the town build description
     * @param level       the town level description
     */
    public AlterTownBuild(@Nonnull ItemStack item, @Nonnull String name, @Nonnull String level, @Nonnull String... description) {
        Validate.notNull(item, "item is null");
        Validate.notNull(name, "name is null");
        Validate.notNull(level, "level is null");
        Validate.notNull(description, "description is null");

        this.item = item;
        this.name = name;
        this.level = level;
        this.description = Arrays.stream(description).collect(Collectors.joining(" "));
    }

    /**
     * Gets the town build material
     *
     * @return the town build material
     */
    @Nonnull
    public ItemStack getItem() {
        return item;
    }

    /**
     * Gets the town build name
     *
     * @return the town build name
     */
    @Nonnull
    public String getName() {
        return name;
    }

    /**
     * Gets the town build description
     *
     * @return the town build description
     */
    @Nonnull
    public String getDescription() {
        return description;
    }

    /**
     * Gets the town build level
     *
     * @return the town build level
     */
    @Nonnull
    public String getLevel() {
        return level;
    }

    /**
     * Gets the town build item
     *
     * @return the town build item
     */
    @Nonnull
    public ItemStack getGUIItem() {
        return new AlterTownItemBuilder(getItem().clone())
                .name(ChatColor.WHITE + getName())
                .setLore(List.of((ChatColor.WHITE + getDescription()).replace("%nl%", "%nl%" + ChatColor.WHITE).split("%nl%")))
                .lore(ChatColor.WHITE + ChatColor.getLastColors(getLevel()) + "Level: " + getLevel())
                .build();
    }

}