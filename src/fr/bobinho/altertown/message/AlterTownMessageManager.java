package fr.bobinho.altertown.message;

import fr.bobinho.altertown.utils.AlterTown;
import fr.bobinho.altertown.utils.player.AlterTownPlayerManager;
import org.apache.commons.lang.Validate;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;

import javax.annotation.Nonnull;
import java.util.Objects;
import java.util.UUID;

public class AlterTownMessageManager {

    public static String getColoredText(@Nonnull String message) {
        return ChatColor.translateAlternateColorCodes('&', message);
    }

    public static void sendMessage(@Nonnull UUID uuid, @Nonnull AlterTownMessage message) {
        Validate.notNull(uuid, "uuid is null");
        Validate.notNull(message, "message is null");

        //Checks if the player is online
        if (AlterTownPlayerManager.isItOnline(uuid)) {
            Objects.requireNonNull(Bukkit.getPlayer(uuid)).sendMessage(getColoredText(message.getRawText()));
        }
    }

    public static void sendMessage(@Nonnull UUID uuid, @Nonnull UUID uuidPlaceHolder, @Nonnull AlterTownMessage message) {
        Validate.notNull(uuid, "uuid is null");
        Validate.notNull(uuidPlaceHolder, "uuidPlaceHolder is null");
        Validate.notNull(message, "message is null");

        //Checks if the player is online
        if (AlterTownPlayerManager.isItOnline(uuid)) {
            Objects.requireNonNull(Bukkit.getPlayer(uuid)).sendMessage(getColoredText(message.getRawText().replace("%player%", AlterTownPlayerManager.getName(uuidPlaceHolder))));
        }
    }

    public static void sendMessage(@Nonnull UUID uuid, @Nonnull String namePlaceHolder, @Nonnull AlterTownMessage message) {
        Validate.notNull(uuid, "uuid is null");
        Validate.notNull(namePlaceHolder, "namePlaceHolder is null");
        Validate.notNull(message, "message is null");

        //Checks if the player is online
        if (AlterTownPlayerManager.isItOnline(uuid)) {
            Objects.requireNonNull(Bukkit.getPlayer(uuid)).sendMessage(getColoredText(message.getRawText().replace("%name%", namePlaceHolder)));
        }
    }

    public static void sendMessage(@Nonnull UUID uuid, @Nonnull AlterTown townPlaceHolder, @Nonnull AlterTownMessage message) {
        Validate.notNull(uuid, "uuid is null");
        Validate.notNull(townPlaceHolder, "townPlaceHolder is null");
        Validate.notNull(message, "message is null");

        //Checks if the player is online
        if (AlterTownPlayerManager.isItOnline(uuid)) {
            Objects.requireNonNull(Bukkit.getPlayer(uuid)).sendMessage(getColoredText(message.getRawText().replace("%town%", ChatColor.stripColor(townPlaceHolder.getName()))));
        }
    }

    public static void sendMessage(@Nonnull UUID uuid, @Nonnull String playerPlaceHolder, @Nonnull AlterTown townPlaceHolder, @Nonnull AlterTownMessage message) {
        Validate.notNull(uuid, "uuid is null");
        Validate.notNull(playerPlaceHolder, "playerPlaceHolder is null");
        Validate.notNull(townPlaceHolder, "townPlaceHolder is null");
        Validate.notNull(message, "message is null");

        //Checks if the player is online
        if (AlterTownPlayerManager.isItOnline(uuid)) {
            Objects.requireNonNull(Bukkit.getPlayer(uuid)).sendMessage(getColoredText(message.getRawText().replace("%town%", ChatColor.stripColor(townPlaceHolder.getName())).replace("%player%", playerPlaceHolder)));
        }
    }

    public static void sendMessage(@Nonnull UUID uuid, @Nonnull AlterTown townPlaceHolder,  @Nonnull String namePlaceHolder, @Nonnull AlterTownMessage message) {
        Validate.notNull(uuid, "uuid is null");
        Validate.notNull(townPlaceHolder, "townPlaceHolder is null");
        Validate.notNull(namePlaceHolder, "namePlaceHolder is null");
        Validate.notNull(message, "message is null");

        //Checks if the player is online
        if (AlterTownPlayerManager.isItOnline(uuid)) {
            Objects.requireNonNull(Bukkit.getPlayer(uuid)).sendMessage(getColoredText(message.getRawText().replace("%town%", ChatColor.stripColor(townPlaceHolder.getName())).replace("%name%", namePlaceHolder)));
        }
    }

    public static void sendMessage(@Nonnull AlterTown town, @Nonnull UUID uuidPlaceHolder, @Nonnull AlterTownMessage message) {
        Validate.notNull(town, "town is null");
        Validate.notNull(uuidPlaceHolder, "uuidPlaceHolder is null");
        Validate.notNull(message, "message is null");

        //Checks if the player is online
        for (UUID uuid : town.getMembers().keySet()) {
            if (AlterTownPlayerManager.isItOnline(uuid)) {
                Objects.requireNonNull(Bukkit.getPlayer(uuid)).sendMessage(getColoredText(message.getRawText().replace("%player%", AlterTownPlayerManager.getName(uuidPlaceHolder))));
            }
        }
    }

}