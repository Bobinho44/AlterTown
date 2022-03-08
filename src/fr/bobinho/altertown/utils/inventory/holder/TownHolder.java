package fr.bobinho.altertown.utils.inventory.holder;

import fr.bobinho.altertown.utils.AlterTown;
import fr.bobinho.altertown.utils.inventory.AlterTownInventory;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.jetbrains.annotations.NotNull;

public class TownHolder implements InventoryHolder {

    private final AlterTown town;
    private final int page;

    public TownHolder(AlterTown town, int page) {
        this.town = town;
        this.page = page;
    }

    public AlterTown getTown() {
        return town;
    }

    public int getPage() {
        return page;
    }

    public Inventory getNextPage() {
        return AlterTownInventory.getAlterTownInventory(getTown(), getPage() + 1);
    }

    public Inventory getPreviousPage() {
        return AlterTownInventory.getAlterTownInventory(getTown(), getPage() - 1);
    }

    @Override
    public @NotNull Inventory getInventory() {
        return null;
    }

}