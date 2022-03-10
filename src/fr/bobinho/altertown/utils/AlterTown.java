package fr.bobinho.altertown.utils;

import fr.bobinho.altertown.utils.item.AlterTownItemBuilder;
import fr.bobinho.altertown.utils.player.AlterTownPlayerManager;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.apache.commons.lang.Validate;
import org.bukkit.ChatColor;
import org.bukkit.inventory.ItemStack;

import javax.annotation.Nonnull;
import java.util.*;
import java.util.stream.Collectors;

public class AlterTown {

    public enum AlterTownRole {
        LEADER,
        OFFICIAL,
        CITIZEN
    }

    /**
     * Fields
     */
    private final ItemStack item;
    private String name;
    private String description;
    private String biome;
    private String URL;
    private final Map<UUID, AlterTownRole> members = new HashMap<>();
    private final List<AlterTownBuild> builds = new ArrayList<>();

    /**
     * Creates a new town
     *
     * @param item the town item for gui
     * @param name the town name
     */
    public AlterTown(@Nonnull ItemStack item, @Nonnull String name) {
        Validate.notNull(item, "item is null");
        Validate.notNull(name, "name is null");

        this.item = item;
        this.name = name;
        this.description = "";
        this.biome = "";
        this.URL = "";
    }

    /**
     * Gets the town item
     *
     * @return the town item
     */
    @Nonnull
    public ItemStack getItem() {
        return item;
    }

    /**
     * Gets the town name
     *
     * @return the town name
     */
    @Nonnull
    public String getName() {
        return name;
    }

    /**
     * Sets the town name
     *
     * @param name the town name
     */
    public void setName(@Nonnull String name) {
        Validate.notNull(name, "name is null");

        this.name = name;
    }

    /**
     * Gets the town description
     *
     * @return the town description
     */
    @Nonnull
    public String getDescription() {
        return description;
    }

    /**
     * Sets the town description
     *
     * @param description the town description
     */
    public void setDescription(@Nonnull String description) {
        Validate.notNull(description, "description is null");

        this.description = description;
    }

    /**
     * Gets the town biome
     *
     * @return the town biome
     */
    @Nonnull
    public String getBiome() {
        return biome;
    }

    /**
     * Sets the town biome
     *
     * @param biome the town biome
     */
    public void setBiome(@Nonnull String biome) {
        Validate.notNull(biome, "biome is null");

        this.biome = biome;
    }

    /**
     * Gets the town members
     *
     * @return the town members
     */
    @Nonnull
    public Map<UUID, AlterTownRole> getMembers() {
        return members;
    }

    /**
     * Gets the town population
     *
     * @return the town population
     */
    public int getPopulation() {
        return getMembers().size();
    }

    public void addMember(@Nonnull UUID member, @Nonnull AlterTownRole role) {
        getMembers().put(member, role);
    }

    public void removeMember(@Nonnull UUID member) {
        getMembers().remove(member);
    }

    /**
     * Gets the town leader
     *
     * @return the town leader
     */
    @Nonnull
    public Optional<UUID> getLeader() {
        return getMembers().entrySet().stream().filter(member -> member.getValue() == AlterTownRole.LEADER).map(Map.Entry::getKey).findFirst();
    }

    /**
     * Sets the town leader
     *
     * @param leader the town leader
     */
    public void setLeader(UUID leader) {
        getMembers().put(leader, AlterTownRole.LEADER);
    }

    /**
     * Gets the town officials
     *
     * @return the town officials
     */
    @Nonnull
    public List<UUID> getOfficials() {
        return getMembers().entrySet().stream().filter(member -> member.getValue() == AlterTownRole.OFFICIAL).map(Map.Entry::getKey).collect(Collectors.toList());
    }

    /**
     * Adds a town official
     *
     * @param official the town official
     */
    public void addOfficial(@Nonnull UUID official) {
        Validate.notNull(official, "official is null");

        getMembers().put(official, AlterTownRole.OFFICIAL);
    }

    /**
     * Removes a town official
     *
     * @param official the town official
     */
    public void removeOfficial(@Nonnull UUID official) {
        Validate.notNull(official, "official is null");

        getMembers().remove(official);
    }

    /**
     * Gets the town citizens
     *
     * @return the town citizens
     */
    @Nonnull
    public List<UUID> getCitizens() {
        return getMembers().entrySet().stream().filter(member -> member.getValue() == AlterTownRole.CITIZEN).map(Map.Entry::getKey).collect(Collectors.toList());
    }

    /**
     * Adds a town citizen
     *
     * @param citizen the town citizen
     */
    public void addCitizen(@Nonnull UUID citizen) {
        Validate.notNull(citizen, "official is null");

        getMembers().put(citizen, AlterTownRole.CITIZEN);
    }

    /**
     * Removes a town citizen
     *
     * @param citizen the town citizen
     */
    public void removeCitizen(@Nonnull UUID citizen) {
        Validate.notNull(citizen, "official is null");

        getMembers().remove(citizen);
    }

    /**
     * Gets the town builds
     *
     * @return the town builds
     */
    @Nonnull
    public List<AlterTownBuild> getBuilds() {
        return builds;
    }

    /**
     * Adds a town build
     *
     * @param build the town build
     */
    public void addBuild(@Nonnull AlterTownBuild build) {
        Validate.notNull(build, "build is null");

        getBuilds().add(build);
    }

    /**
     * Removes a town build
     *
     * @param build the town build
     */
    public void removeBuild(@Nonnull AlterTownBuild build) {
        Validate.notNull(build, "build is null");

        getBuilds().remove(build);
    }

    public String getURL() {
        return URL;
    }

    public void setURL(@Nonnull String URL) {
        Validate.notNull(URL, "URL is null");

        this.URL = URL;
    }

    public TextComponent getURLInformation() {
        TextComponent message = new TextComponent(ChatColor.GOLD + "Town Information: click here!");
        message.setClickEvent(new ClickEvent(ClickEvent.Action.OPEN_URL, getURL()));
        return message;
    }
    /**
     * Gets the town item
     *
     * @return the town item
     */
    @Nonnull
    public ItemStack getGUIItem() {
        return new AlterTownItemBuilder(getItem().clone())
                .name(ChatColor.WHITE + getName())
                .setLore(List.of((ChatColor.WHITE + getDescription()).replace("%nl%", "%nl%" + ChatColor.WHITE).split("%nl%")))
                .lore(ChatColor.WHITE + "Population: " + getPopulation())
                .lore(ChatColor.WHITE + ChatColor.getLastColors(getBiome()) + "Biome: " + getBiome())
                .build();
    }

    /**
     * Gets player role as string
     *
     * @param member the player
     * @return the player role as string
     */
    @Nonnull
    public String getRoleAsString(@Nonnull UUID member) {
        Validate.notNull(member, "member is null");

        //Checks if the member is a leader
        if (getLeader().isPresent() && getLeader().get().equals(member)) {
            return "**";
        }

        //Checks if member is an official
        if (getOfficials().contains(member)) {
            return "*";
        }

        return "";
    }

    /**
     * Gets all members list as string
     *
     * @return all members list as string
     */
    @Nonnull
    public String getMembersAsString() {

        //Creates the list header
        StringBuilder membersList = new StringBuilder(ChatColor.GOLD + getName() + "'s members:");

        //Builds the list
        for (UUID member : getMembers().keySet()) {
            membersList.append(ChatColor.YELLOW + "\n - " + ChatColor.GREEN + getRoleAsString(member) + AlterTownPlayerManager.getName(member));
        }

        return membersList.toString();
    }

}