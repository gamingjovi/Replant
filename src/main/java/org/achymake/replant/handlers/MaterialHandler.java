package org.achymake.replant.handlers;

import org.achymake.replant.Replant;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.Damageable;

public class MaterialHandler {
    private Replant getInstance() {
        return Replant.getInstance();
    }
    private FileConfiguration getConfig() {
        return getInstance().getConfig();
    }
    private RandomHandler getRandomHandler() {
        return getInstance().getRandomHandler();
    }
    private WorldHandler getWorldHandler() {
        return getInstance().getWorldHandler();
    }
    public Material get(String materialName) {
        return Material.valueOf(materialName.toUpperCase());
    }
    public ItemStack getItemStack(String materialName, int amount) {
        return new ItemStack(get(materialName.toUpperCase()), amount);
    }
    public boolean hasEnchantment(ItemStack itemStack) {
        var enchantmentName = getConfig().getString("replant.enchantment");
        if (enchantmentName.equalsIgnoreCase("none")) {
            return true;
        } else return itemStack.getItemMeta().hasEnchant(getEnchantment(enchantmentName));
    }
    public Enchantment getEnchantment(String enchantmentName) {
        return Enchantment.getByName(enchantmentName.toUpperCase());
    }
    public int getEnchantLevel(ItemStack itemStack, String enchantmentName) {
        return itemStack.getEnchantmentLevel(getEnchantment(enchantmentName));
    }
    public boolean isAir(ItemStack itemStack) {
        return itemStack == null || itemStack.getType().equals(get("air"));
    }
    public boolean isHoe(ItemStack itemStack) {
        return isWoodenHoe(itemStack) ||
                isStoneHoe(itemStack) ||
                isIronHoe(itemStack) ||
                isGoldenHoe(itemStack) ||
                isDiamondHoe(itemStack) ||
                isNetheriteHoe(itemStack);
    }
    public boolean isWoodenHoe(ItemStack itemStack) {
        return itemStack.getType().equals(get("wooden_hoe"));
    }
    public boolean isStoneHoe(ItemStack itemStack) {
        return itemStack.getType().equals(get("stone_hoe"));
    }
    public boolean isIronHoe(ItemStack itemStack) {
        return itemStack.getType().equals(get("iron_hoe"));
    }
    public boolean isGoldenHoe(ItemStack itemStack) {
        return itemStack.getType().equals(get("golden_hoe"));
    }
    public boolean isDiamondHoe(ItemStack itemStack) {
        return itemStack.getType().equals(get("diamond_hoe"));
    }
    public boolean isNetheriteHoe(ItemStack itemStack) {
        return itemStack.getType().equals(get("netherite_hoe"));
    }
    public void addDamage(ItemStack itemStack, int damage) {
        var unbreaking = getEnchantment("unbreaking");
        if (itemStack.containsEnchantment(unbreaking)) {
            var lvl = itemStack.getEnchantments().get(unbreaking);
            if (!getRandomHandler().isTrue(0.5, lvl))return;
            var toolHealthDamage = (Damageable) itemStack.getItemMeta();
            var result = toolHealthDamage.getDamage() + damage;
            toolHealthDamage.setDamage(result);
            itemStack.setItemMeta(toolHealthDamage);
        } else {
            var toolHealthDamage = (Damageable) itemStack.getItemMeta();
            var result = toolHealthDamage.getDamage() + damage;
            toolHealthDamage.setDamage(result);
            itemStack.setItemMeta(toolHealthDamage);
        }
    }
    public boolean isDestroyed(ItemStack heldItem) {
        if (isWoodenHoe(heldItem)) {
            var toolHealthAfter = (Damageable) heldItem.getItemMeta();
            if (toolHealthAfter.getDamage() >= 59) {
                heldItem.setAmount(0);
                return true;
            } else return false;
        } else if (isStoneHoe(heldItem)) {
            var toolHealthAfter = (Damageable) heldItem.getItemMeta();
            if (toolHealthAfter.getDamage() >= 131) {
                heldItem.setAmount(0);
                return true;
            } else return false;
        } else if (isIronHoe(heldItem)) {
            var toolHealthAfter = (Damageable) heldItem.getItemMeta();
            if (toolHealthAfter.getDamage() >= 250) {
                heldItem.setAmount(0);
                return true;
            } else return false;
        } else if (isGoldenHoe(heldItem)) {
            var toolHealthAfter = (Damageable) heldItem.getItemMeta();
            if (toolHealthAfter.getDamage() >= 32) {
                heldItem.setAmount(0);
                return true;
            } else return false;
        } else if (isDiamondHoe(heldItem)) {
            var toolHealthAfter = (Damageable) heldItem.getItemMeta();
            if (toolHealthAfter.getDamage() >= 1561) {
                heldItem.setAmount(0);
                return true;
            } else return false;
        } else if (isNetheriteHoe(heldItem)) {
            var toolHealthAfter = (Damageable) heldItem.getItemMeta();
            if (toolHealthAfter.getDamage() >= 2031) {
                heldItem.setAmount(0);
                return true;
            } else return false;
        } else return false;
    }
    public void breakItem(Location location, ItemStack itemStack) {
        itemStack.setAmount(itemStack.getAmount() - 1);
        var volume = getRandomHandler().nextDouble(0.75, 1.0);
        var pitch = getRandomHandler().nextDouble(0.75, 1.0);
        getWorldHandler().playSound(location, "entity_item_break", volume, pitch);
    }
}