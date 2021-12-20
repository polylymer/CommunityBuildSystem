package de.hglabor.events

import de.hglabor.config.Settings
import de.hglabor.gui.SettingsGUI
import de.hglabor.localization.Locale.getByPlayer
import de.hglabor.localization.Localization
import net.axay.kspigot.event.listen
import net.axay.kspigot.extensions.bukkit.actionBar
import net.axay.kspigot.gui.openGUI
import net.axay.kspigot.utils.hasMark
import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.event.block.Action
import org.bukkit.event.block.BlockPlaceEvent
import org.bukkit.event.entity.EntityDamageEvent
import org.bukkit.event.entity.EntitySpawnEvent
import org.bukkit.event.entity.FoodLevelChangeEvent
import org.bukkit.event.entity.PlayerDeathEvent
import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.event.player.PlayerDropItemEvent
import org.bukkit.event.player.PlayerInteractEvent
import org.bukkit.event.player.PlayerPortalEvent
import org.bukkit.event.player.PlayerSwapHandItemsEvent
import org.bukkit.event.vehicle.VehicleCreateEvent
import org.bukkit.event.weather.WeatherChangeEvent
import java.util.*

object InteractListener {
    init {
        val disabledBlocks = ArrayList<Material> ()
        disabledBlocks.addAll(Arrays.stream(Material.values()).filter { material: Material -> material.name.contains("SHULKER") }.toList())
        disabledBlocks.addAll(Arrays.stream(Material.values()).filter { material: Material -> material.name.contains("SWORD") }.toList())
        disabledBlocks.addAll(Arrays.stream(Material.values()).filter { material: Material -> material.name.contains("AXE") }.toList())
        disabledBlocks.addAll(Arrays.stream(Material.values()).filter { material: Material -> material.name.contains("HOE") }.toList())
        disabledBlocks.addAll(Arrays.stream(Material.values()).filter { material: Material -> material.name.contains("PICKAXE") }.toList())
        disabledBlocks.addAll(Arrays.stream(Material.values()).filter { material: Material -> material.name.contains("SHOVEL") }.toList())
        disabledBlocks.addAll(Arrays.stream(Material.values()).filter { material: Material -> material.name.contains("CHEST") }.toList())
        disabledBlocks.addAll(Arrays.stream(Material.values()).filter { material: Material -> material.name.contains("LEGGINGS") }.toList())
        disabledBlocks.addAll(Arrays.stream(Material.values()).filter { material: Material -> material.name.contains("HELMET") }.toList())
        disabledBlocks.addAll(Arrays.stream(Material.values()).filter { material: Material -> material.name.contains("BOOTS") }.toList())
        disabledBlocks.addAll(Arrays.stream(Material.values()).filter { material: Material -> material.name.contains("BUCKET") }.toList())
        disabledBlocks.addAll(Arrays.stream(Material.values()).filter { material: Material -> material.name.contains("MINECART") }.toList())
        disabledBlocks.addAll(Arrays.stream(Material.values()).filter { material: Material -> material.name.contains("POTION") }.toList())
        disabledBlocks.addAll(Arrays.stream(Material.values()).filter { material: Material -> material.name.contains("SPAWN_EGG") }.toList())
        disabledBlocks.addAll(Arrays.stream(Material.values()).filter { material: Material -> material.name.contains("ENCHANTED_BOOK") }.toList())
        disabledBlocks.addAll(Arrays.stream(Material.values()).filter { material: Material -> material.name.contains("SPASH_POTION") }.toList())
        disabledBlocks.addAll(Arrays.stream(Material.values()).filter { material: Material -> material.name.contains("ARROW") }.toList())
        disabledBlocks.addAll(Arrays.stream(Material.values()).filter { material: Material -> material.name.contains("LINGERING_POTION") }.toList())
        disabledBlocks.addAll(Arrays.stream(Material.values()).filter { material: Material -> material.name.contains("ORE") }.toList())
        disabledBlocks.addAll(listOf(Material.DISPENSER, Material.IRON_BLOCK, Material.TNT, Material.SCUTE, Material.STRUCTURE_VOID,
            Material.DIAMOND_BLOCK, Material.FURNACE, Material.ENDER_CHEST, Material.CONDUIT, Material.BELL, Material.ENDER_EYE, Material.FIRE_CHARGE,
            Material.HOPPER, Material.DROPPER, Material.FLINT_AND_STEEL, Material.DIAMOND, Material.BEACON, Material.NAUTILUS_SHELL,
            Material.IRON_INGOT, Material.NETHERITE_INGOT, Material.NETHERITE_SCRAP, Material.GOLDEN_APPLE, Material.HEART_OF_THE_SEA,
            Material.ENCHANTED_GOLDEN_APPLE, Material.PAINTING, Material.ENDER_PEARL, Material.EXPERIENCE_BOTTLE, Material.NOTE_BLOCK,
            Material.ITEM_FRAME, Material.GOLDEN_CARROT, Material.WITHER_SKELETON_SKULL, Material.WITHER_SKELETON_WALL_SKULL, Material.STRUCTURE_BLOCK,
            Material.ARMOR_STAND, Material.END_CRYSTAL, Material.SHIELD, Material.ELYTRA, Material.TOTEM_OF_UNDYING, Material.BREWING_STAND,
            Material.SHULKER_SHELL, Material.IRON_NUGGET, Material.TRIDENT, Material.CROSSBOW, Material.BARREL, Material.RESPAWN_ANCHOR,
            Material.SMOKER, Material.BLAST_FURNACE, Material.NETHERITE_BLOCK, Material.ANCIENT_DEBRIS, Material.COMMAND_BLOCK, Material.NETHER_STAR,
            Material.AXOLOTL_BUCKET, Material.BUNDLE))

        listen<PlayerInteractEvent> {
            val player = it.player

            if (it.action == Action.LEFT_CLICK_AIR || it.action == Action.LEFT_CLICK_BLOCK || it.action == Action.RIGHT_CLICK_AIR) {
                if (it.hasItem() && it.item!!.type == Material.BARREL && it.item!!.hasMark("protected") && player.isOp) {
                    player.openGUI(SettingsGUI().gui)
                }
            }
        }

        listen<PlayerDropItemEvent> {
            it.isCancelled = true
            it.player.actionBar(Localization.getMessage("buildsystem.dropItems", getByPlayer(it.player)))
        }

        listen<InventoryClickEvent> {
            if (it.whoClicked is Player) {
                val player = it.whoClicked as Player
                if (it.currentItem != null) {
                    if (it.currentItem!!.hasMark("protected")) {
                        it.isCancelled = true
                    } else if (disabledBlocks.contains(it.currentItem!!.type) && Settings.forbiddenItems) {
                        it.isCancelled = true
                        player.inventory.remove(it.currentItem!!)
                        player.actionBar(Localization.getMessage("buildsystem.forbiddenItems", getByPlayer(player)))
                    } else if (!(it.cursor == null || it.cursor!!.type == Material.AIR)) {
                        if (disabledBlocks.contains(it.cursor!!.type) && Settings.forbiddenItems) {
                            it.isCancelled = true
                            it.whoClicked.itemOnCursor.type = Material.AIR
                            player.actionBar(Localization.getMessage("buildsystem.forbiddenItems", getByPlayer(player)))
                        }
                    }
                }
            }
        }

        listen<BlockPlaceEvent> {
            val player = it.player
            val block = it.blockPlaced

            if (Settings.forbiddenItems) {
                if (disabledBlocks.contains(block.type)) {
                    it.isCancelled = true
                    player.actionBar(Localization.getMessage("buildsystem.forbiddenItems", getByPlayer(player)))
                }
            }
        }

        listen<EntityDamageEvent> {
            it.isCancelled = true
        }

        listen<PlayerDeathEvent> {
            it.drops.clear()
        }

        listen<FoodLevelChangeEvent> {
            it.isCancelled = true
        }

        listen<WeatherChangeEvent> {
            if (Settings.clearSunnyWeather) {
                it.isCancelled = true
            }
        }

        listen<VehicleCreateEvent> {
            it.isCancelled = true
        }

        listen<EntitySpawnEvent> {
            it.isCancelled = true
        }

        listen<PlayerSwapHandItemsEvent> {
            it.isCancelled = true
        }

        listen<PlayerPortalEvent> {
            if (Settings.portal) {
                it.isCancelled = true
                it.player.actionBar(Localization.getMessage("buildsystem.portalDisabled", getByPlayer(it.player)))
            }
        }
    }
}