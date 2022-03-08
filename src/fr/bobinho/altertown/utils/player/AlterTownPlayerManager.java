package fr.bobinho.altertown.utils.player;

import fr.bobinho.altertown.AlterTownCore;
import fr.bobinho.altertown.utils.format.AlterTownDateFormat;
import org.apache.commons.lang.Validate;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import javax.annotation.Nonnull;
import java.util.*;

public class AlterTownPlayerManager {

    /**
     * Checks if the player is online
     *
     * @param uuid the uuid of the player
     * @return if he is online
     */
    public static boolean isItOnline(@Nonnull UUID uuid) {
        Validate.notNull(uuid, "uuid is null");

        return Bukkit.getPlayer(uuid) != null;
    }

    /**
     * Gets the alterTown player name
     *
     * @param uuid the uuid of the alterTown player
     * @return the alterTown player name
     */
    @Nonnull
    public static String getName(@Nonnull UUID uuid) {
        Validate.notNull(uuid, "uuid is null");

        //Checks if the player is online
        if (isItOnline(uuid)) {
            return Objects.requireNonNull(Bukkit.getPlayer(uuid)).getName();
        }

        //Finds the player name in player settings file
        YamlConfiguration configuration = AlterTownCore.getPlayersSettings().getConfiguration();

        for (String uuidAsString : configuration.getKeys(false)) {
            if (UUID.fromString(uuidAsString).equals(uuid)) {
                return configuration.getString(uuidAsString + ".name", "N/D");
            }
        }

        //The player does not exist
        return "N/D";
    }

    public static void savePracticePlayerData(@Nonnull Player player) {
        Validate.notNull(player, "player is null");

        YamlConfiguration configuration = AlterTownCore.getPlayersSettings().getConfiguration();

        configuration.set(player.getUniqueId() + ".name", player.getName());
        configuration.set(player.getUniqueId() + ".connection", AlterTownDateFormat.getAsDMAFormat(Calendar.getInstance()));
        AlterTownCore.getPlayersSettings().save();
    }

}