package org.achymake.replant.handlers;

import org.achymake.replant.Replant;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.data.Ageable;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class BlockHandler {
    private Replant getInstance() {
        return Replant.getInstance();
    }
    private FileConfiguration getConfig() {
        return getInstance().getConfig();
    }
    private MaterialHandler getMaterials() {
        return getInstance().getMaterialHandler();
    }
    private RandomHandler getRandomHandler() {
        return getInstance().getRandomHandler();
    }
    private WorldHandler getWorldHandler() {
        return getInstance().getWorldHandler();
    }
    public boolean isEnable(Block block) {
        return getConfig().getBoolean("blocks." + block.getType() + ".enable");
    }
    public int getAge(Block block) {
        if (block.getBlockData() instanceof Ageable ageable) {
            return ageable.getAge();
        } else return 0;
    }
    public boolean isRightAge(Block block) {
        return getAge(block) >= getConfig().getInt("blocks." + block.getType() + ".max-age");
    }
    public void playSound(Block block) {
        var type = getConfig().getString("replant.sound");
        if (type == null)return;
        var volume = getRandomHandler().nextDouble(0.75, 1.0);
        var pitch = getRandomHandler().nextDouble(0.75, 1.0);
        getWorldHandler().playSound(block.getLocation().add(0.5, 0.3, 0.5), type, volume, pitch);
    }
    public List<ItemStack> getDrops(Material material, int fortune) {
        var listed = new ArrayList<ItemStack>();
        var section = getConfig().getConfigurationSection("blocks." + material + ".drops");
        if (section != null) {
            section.getKeys(false).forEach(drop -> {
                var chance = section.getDouble(drop + ".chance");
                if (getRandomHandler().isTrue(chance)) {
                    var materialName = section.getString(drop + ".type");
                    if (materialName != null) {
                        if (section.isInt(drop + ".amount")) {
                            var amount = section.getInt(drop + ".amount");
                            if (section.getBoolean(drop + ".fortune-able")) {
                                if (fortune > 0) {
                                    if (!getRandomHandler().isTrue(0.5, fortune)) {
                                        var extra = getRandomHandler().nextInt(0, fortune);
                                        if (extra > 0) {
                                            listed.add(getMaterials().getItemStack(materialName, extra));
                                        } else listed.add(getMaterials().getItemStack(materialName, amount));
                                    } else listed.add(getMaterials().getItemStack(materialName, amount));
                                } else listed.add(getMaterials().getItemStack(materialName, amount));
                            } else listed.add(getMaterials().getItemStack(materialName, amount));
                        } else {
                            var min = section.getInt(drop + ".amount.min");
                            var max = section.getInt(drop + ".amount.max");
                            var amount = getRandomHandler().nextInt(min, max);
                            if (section.getBoolean(drop + ".fortune-able")) {
                                if (fortune > 0) {
                                    if (!getRandomHandler().isTrue(0.5, fortune)) {
                                        var extra = getRandomHandler().nextInt(0, fortune);
                                        if (extra > 0) {
                                            listed.add(getMaterials().getItemStack(materialName, extra));
                                        } else listed.add(getMaterials().getItemStack(materialName, amount));
                                    } else listed.add(getMaterials().getItemStack(materialName, amount));
                                } else listed.add(getMaterials().getItemStack(materialName, amount));
                            } else listed.add(getMaterials().getItemStack(materialName, amount));
                        }
                    }
                }
            });
        }
        return listed;
    }
    public void dropExperience(Block block) {
        var material = block.getType();
        var section = getConfig().getConfigurationSection("blocks." + material + ".experience");
        if (section == null)return;
        if (!section.getBoolean("enable"))return;
        if (!getRandomHandler().isTrue(section.getDouble("chance")))return;
        var location = block.getLocation().add(0.5, 0.3, 0.5);
        getWorldHandler().spawnExperience(location, section.getInt("amount"));
    }
    public void resetAge(Block block) {
        if (block.getBlockData() instanceof Ageable ageable) {
            ageable.setAge(0);
            block.setBlockData(ageable);
        } else block.setType(getMaterials().get("air"), true);
    }
}