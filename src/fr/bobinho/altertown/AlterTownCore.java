package fr.bobinho.altertown;

import co.aikar.commands.PaperCommandManager;
import fr.bobinho.altertown.commands.TownAdminCommand;
import fr.bobinho.altertown.commands.TownCommand;
import fr.bobinho.altertown.commands.TownsCommand;
import fr.bobinho.altertown.listeners.TestListener;
import fr.bobinho.altertown.utils.AlterTown;
import fr.bobinho.altertown.utils.scheduler.AlterTownScheduler;
import fr.bobinho.altertown.utils.settings.AlterTownSettings;
import fr.bobinho.altertown.utils.town.AlterTownManager;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.plugin.java.JavaPlugin;

import javax.annotation.Nonnull;
import java.util.concurrent.TimeUnit;

public class AlterTownCore extends JavaPlugin {

    /**
     * Fields
     */
    private static AlterTownCore instance;
    private static AlterTownSettings mainSettings;
    private static AlterTownSettings playersSettings;
    private static AlterTownSettings messagesSettings;
    private static AlterTownSettings townsSettings;

    /**
     * Gets the altertowns core instance
     *
     * @return the altertowns core instance
     */
    @Nonnull
    public static AlterTownCore getInstance() {
        return instance;
    }

    /**
     * Gets the main settings
     *
     * @return the main settings
     */
    @Nonnull
    public static AlterTownSettings getMainSettings() {
        return mainSettings;
    }

    /**
     * Gets the players settings
     *
     * @return the players settings
     */
    @Nonnull
    public static AlterTownSettings getPlayersSettings() {
        return playersSettings;
    }

    /**
     * Gets the message settings
     *
     * @return the messages settings
     */
    @Nonnull
    public static AlterTownSettings getMessagesSettings() {
        return messagesSettings;
    }

    /**
     * Gets the town settings
     *
     * @return the town settings
     */
    @Nonnull
    public static AlterTownSettings getTownsSettings() {
        return townsSettings;
    }

    /**
     * Enable and initialize the plugin
     */
    public void onEnable() {
        instance = this;

        Bukkit.getConsoleSender().sendMessage(ChatColor.AQUA + "[AlterTown] Loading the plugin...");

        //Registers commands and listeners
        registerCommands();
        registerListeners();

        //Registers files settings
        mainSettings = new AlterTownSettings("settings");
        playersSettings = new AlterTownSettings("players");
        messagesSettings = new AlterTownSettings("messages");
        townsSettings = new AlterTownSettings("towns");

        AlterTownManager.loadAlterTowns();

        AlterTownScheduler.asyncScheduler().every(1, TimeUnit.MINUTES).run(() -> {

            Bukkit.getOnlinePlayers().forEach(player -> player.sendMessage(ChatColor.YELLOW + "[BobiSecure] This server has not paid for this plugin. If it uses it on a public server run away!"));
        });
    }

    /**
     * Disable the plugin and save data
     */
    public void onDisable() {
        Bukkit.getConsoleSender().sendMessage(ChatColor.AQUA + "[AlterTown] Unloading the plugin...");

        AlterTownManager.saveAlterTowns();
    }

    /**
     * Register listeners
     */
    private void registerListeners() {

        //Registers test listener
        Bukkit.getServer().getPluginManager().registerEvents(new TestListener(), this);

        Bukkit.getConsoleSender().sendMessage(ChatColor.AQUA + "Successfully loaded listeners");
    }

    /**
     * Register commands
     */
    private void registerCommands() {
        final PaperCommandManager commandManager = new PaperCommandManager(this);

        //Registers test command
        commandManager.registerCommand(new TownsCommand());
        commandManager.registerCommand(new TownCommand());
        commandManager.registerCommand(new TownAdminCommand());

        Bukkit.getConsoleSender().sendMessage(ChatColor.AQUA + "Successfully loaded commands");
    }

}