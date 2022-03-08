package fr.bobinho.altertown.utils.town;

import fr.bobinho.altertown.AlterTownCore;
import fr.bobinho.altertown.utils.AlterTown;
import fr.bobinho.altertown.utils.AlterTownBuild;
import org.apache.commons.lang.Validate;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.inventory.ItemStack;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.*;

public class AlterTownManager {

    /**
     * The towns list
     */
    private static final List<AlterTown> alterTownsList = new ArrayList<>();

    /**
     * Gets all towns
     *
     * @return all towns
     */
    @Nonnull
    private static List<AlterTown> getAlterTownsList() {
        return alterTownsList;
    }

    /**
     * Gets a town
     *
     * @param i the town number
     * @return the town
     */
    @Nullable
    public static AlterTown getAlterTown(int i) {
        return getAlterTownsList().size() > i ? getAlterTownsList().get(i) : null;
    }

    public static Optional<AlterTown> getAlterTown(@Nonnull String name) {
        Validate.notNull(name, "name is null");

        return getAlterTownsList().stream().filter(town ->
                ChatColor.stripColor(ChatColor.translateAlternateColorCodes('&', town.getName())).equalsIgnoreCase(ChatColor.stripColor(ChatColor.translateAlternateColorCodes('&', name)))).findFirst();
    }

    public static Optional<AlterTown> getPlayerAlterTown(@Nonnull UUID player) {
        Validate.notNull(player, "player is null");

        return getAlterTownsList().stream().filter(town -> town.getMembers().containsKey(player)).findFirst();
    }

    public static boolean isInAlterTown(@Nonnull UUID player) {
        Validate.notNull(player, "player is null");

        return getPlayerAlterTown(player).isPresent();
    }

    public static boolean isItAlterTown(@Nonnull String name) {
        Validate.notNull(name, "name is null");

        return getAlterTown(name).isPresent();
    }

    public static boolean alterTownsIsFull() {

        return getAlterTownsList().size() >= 28;
    }

    public static void createAlterTown(@Nonnull ItemStack item, @Nonnull String name) {
        Validate.notNull(item, "item is null");
        Validate.notNull(name, "name is null");
        Validate.isTrue(!isItAlterTown(name), "name is already use");

        getAlterTownsList().add(new AlterTown(item.clone(), name));
    }

    public static void deleteAlterTown(@Nonnull String name) {
        Validate.notNull(name, "name is null");
        Validate.isTrue(isItAlterTown(name), "name is not use");

        getAlterTownsList().add(getAlterTown(name).get());
    }

    public static boolean isAnAlterTownLeader(@Nonnull UUID player) {
        Validate.notNull(player, "player is null");
        Validate.isTrue(isInAlterTown(player), "player is not in an town");

        return getPlayerAlterTown(player).get().getLeader().stream().anyMatch(leader -> leader.equals(player));
    }

    public static boolean isAnAlterTownOfficial(@Nonnull UUID player) {
        Validate.notNull(player, "player is null");
        Validate.isTrue(isInAlterTown(player), "player is not in an town");

        return getPlayerAlterTown(player).get().getOfficials().stream().anyMatch(official -> official.equals(player));
    }

    public static boolean alterTownOfficialsIsFull(@Nonnull String name) {
        Validate.notNull(name, "name is null");
        Validate.isTrue(isItAlterTown(name), "name is not a valid name");

        return getAlterTown(name).get().getOfficials().size() >= 6;
    }

    public static boolean isAnAlterTownCitizen(@Nonnull UUID player) {
        Validate.notNull(player, "player is null");
        Validate.isTrue(isInAlterTown(player), "player is not in an town");

        return getPlayerAlterTown(player).get().getCitizens().stream().anyMatch(citizen -> citizen.equals(player));
    }

    public static void addAlterTownCitizen(@Nonnull UUID citizen, @Nonnull AlterTown town) {
        Validate.notNull(citizen, "citizen is null");
        Validate.notNull(town, "town is null");

        getPlayerAlterTown(citizen).ifPresent(citizenTown -> citizenTown.removeMember(citizen));
        town.addCitizen(citizen);
    }

    public static void removeAlterTownCitizen(@Nonnull UUID citizen) {
        Validate.notNull(citizen, "citizen is null");
        Validate.isTrue(isInAlterTown(citizen), "citizen is not in a town");

        getPlayerAlterTown(citizen).get().removeCitizen(citizen);
    }

    public static void promoteAlterTownCitizen(@Nonnull UUID citizen) {
        Validate.notNull(citizen, "citizen is null");
        Validate.isTrue(isInAlterTown(citizen), "citizen is not in a town");
        Validate.isTrue(isAnAlterTownCitizen(citizen), "citizen is not a citizen");

        AlterTown town = getPlayerAlterTown(citizen).get();
        town.removeCitizen(citizen);
        town.addOfficial(citizen);
    }

    public static void demoteAlterTownOfficial(@Nonnull UUID official) {
        Validate.notNull(official, "official is null");
        Validate.isTrue(isInAlterTown(official), "official is not in a town");
        Validate.isTrue(isAnAlterTownOfficial(official), "official is not an official");

        AlterTown town = getPlayerAlterTown(official).get();
        town.removeOfficial(official);
        town.addCitizen(official);
    }

    public static void setAlterTownLeader(@Nonnull UUID leader, @Nonnull AlterTown town) {
        Validate.notNull(leader, "leader is null");
        Validate.notNull(town, "town is null");

        getPlayerAlterTown(leader).ifPresent(citizenTown -> citizenTown.removeMember(leader));

        //Remove the player from his town
        if (town.getLeader().isPresent()) {
            town.addCitizen(town.getLeader().get());
        }

        //Sets the town leader
        town.setLeader(leader);
    }

    public static void addAlterTownOfficial(@Nonnull UUID official, @Nonnull AlterTown town) {
        Validate.notNull(official, "official is null");
        Validate.notNull(town, "town is null");

        getPlayerAlterTown(official).ifPresent(citizenTown -> citizenTown.removeMember(official));

        //Removes official from his town
        if (isInAlterTown(official)) {
            getPlayerAlterTown(official).get().removeOfficial(official);
        }

        //Adds official
        town.addOfficial(official);

    }

    public static void setAlterTownName(@Nonnull String actualName, @Nonnull String newName) {
        Validate.notNull(actualName, "actualName is null");
        Validate.notNull(newName, "newName is null");
        Validate.isTrue(isItAlterTown(actualName), "actual name is not a valid name");

        getAlterTown(actualName).get().setName(newName);
    }

    public static void setAlterTownDescription(@Nonnull String name, @Nonnull String description) {
        Validate.notNull(name, "name is null");
        Validate.notNull(description, "description is null");
        Validate.isTrue(isItAlterTown(name), "name is not a valid name");

        getAlterTown(name).get().setDescription(description);
    }

    public static void setAlterTownBiome(@Nonnull String name, @Nonnull String biome) {
        Validate.notNull(name, "name is null");
        Validate.notNull(biome, "biome is null");
        Validate.isTrue(isItAlterTown(name), "name is not a valid name");

        getAlterTown(name).get().setBiome(biome);
    }

    public static Optional<AlterTownBuild> getAlterTownBuild(@Nonnull AlterTown town, @Nonnull String name) {
        Validate.notNull(town, "town is null");
        Validate.notNull(name, "name is null");

        return town.getBuilds().stream().filter(build -> build.getName().equalsIgnoreCase(name)).findFirst();
    }

    public static boolean isItAlterTownBuild(@Nonnull AlterTown town, @Nonnull String name) {
        Validate.notNull(town, "town is null");
        Validate.notNull(name, "name is null");

        return getAlterTownBuild(town, name).isPresent();
    }

    public static void addAlterTownBuild(@Nonnull AlterTown town, @Nonnull ItemStack item, @Nonnull String name, @Nonnull String level, @Nonnull String... description) {
        Validate.notNull(town, "town is null");
        Validate.notNull(item, "item is null");
        Validate.notNull(name, "name is null");
        Validate.notNull(level, "level is null");
        Validate.notNull(description, "description is null");
        Validate.isTrue(!isItAlterTownBuild(town, name), "this build already exist");

        town.addBuild(new AlterTownBuild(item.clone(), name, level, description));
    }

    public static void removeAlterTownBuild(@Nonnull AlterTown town, @Nonnull String name) {
        Validate.notNull(town, "town is null");
        Validate.isTrue(isItAlterTownBuild(town, name), "this build doesn't exist");

        town.removeBuild(getAlterTownBuild(town, name).get());
    }

    public static void setAlterTownURL(@Nonnull String name, @Nonnull String url) {
        Validate.notNull(name, "name is null");
        Validate.notNull(url, "url is null");
        Validate.isTrue(isItAlterTown(name), "name is not a valid name");

        getAlterTown(name).get().setURL(url);
    }

    public static void loadAlterTowns() {
        YamlConfiguration configuration = AlterTownCore.getTownsSettings().getConfiguration();

        //Loads town
        for (String townName : configuration.getKeys(false)) {

            ItemStack item = configuration.getItemStack(townName + ".item", new ItemStack(Material.AIR));
            String description = configuration.getString(townName + ".description", "");
            String biome = configuration.getString(townName + ".biome", "");
            String url = configuration.getString(townName + ".url", "");
            createAlterTown(item, townName);
            setAlterTownDescription(townName, description);
            setAlterTownBiome(townName, biome);
            setAlterTownURL(townName, url);
            AlterTown town = getAlterTown(townName).get();

            //Loads members
            if (configuration.getConfigurationSection(townName + ".members") != null){
                for (String memberUUID : Objects.requireNonNull(configuration.getConfigurationSection(townName + ".members")).getKeys(false)) {
                    town.addMember(UUID.fromString(memberUUID), AlterTown.AlterTownRole.valueOf(configuration.getString(townName + ".members." + memberUUID)));
                }
            }

            //Loads building
            if (configuration.getConfigurationSection(townName + ".buildings") != null) {
                for (String buildingName : Objects.requireNonNull(configuration.getConfigurationSection(townName + ".buildings")).getKeys(false)) {

                    ItemStack buildingItem = configuration.getItemStack(townName + ".buildings." + buildingName + ".item", new ItemStack(Material.AIR));
                    String buildingLevel = configuration.getString(townName + ".buildings." + buildingName + ".level", "");
                    String buildingDescription = configuration.getString(townName + ".buildings." + buildingName + ".description", "");

                    addAlterTownBuild(town, buildingItem, buildingName, buildingLevel, buildingDescription);
                }
            }
        }
    }

    public static void saveAlterTowns() {
        YamlConfiguration configuration = AlterTownCore.getTownsSettings().getConfiguration();
        AlterTownCore.getTownsSettings().clear();

        //Saves town
        for (AlterTown town : getAlterTownsList()) {

            configuration.set(town.getName() + ".item", town.getItem());
            configuration.set(town.getName() + ".description", town.getDescription());
            configuration.set(town.getName() + ".biome", town.getBiome());
            configuration.set(town.getName() + ".url", town.getURL());

            //Saves members
            for (Map.Entry<UUID, AlterTown.AlterTownRole> member : town.getMembers().entrySet()) {
                configuration.set(town.getName() + ".members." + member.getKey(), member.getValue().name());
            }

            //Saves builds
            for (AlterTownBuild build : town.getBuilds()) {
                configuration.set(town.getName() + ".buildings." + build.getName() + ".item", build.getItem());
                configuration.set(town.getName() + ".buildings." + build.getName() + ".level", build.getLevel());
                configuration.set(town.getName() + ".buildings." + build.getName() + ".description", build.getDescription());
            }
        }

        AlterTownCore.getTownsSettings().save();
    }
}