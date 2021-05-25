package de.hglabor.events

import de.hglabor.config.Settings
import de.hglabor.gui.SettingsGUI
import net.axay.kspigot.chat.KColors
import net.axay.kspigot.event.listen
import net.axay.kspigot.extensions.broadcast
import net.axay.kspigot.extensions.bukkit.actionBar
import net.axay.kspigot.gui.openGUI
import net.axay.kspigot.items.itemStack
import net.axay.kspigot.utils.hasMark
import org.bukkit.Material
import org.bukkit.entity.Minecart
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
import org.bukkit.event.player.PlayerSwapHandItemsEvent
import org.bukkit.event.vehicle.VehicleCreateEvent
import org.bukkit.event.weather.WeatherChangeEvent
import java.util.*
import kotlin.streams.toList

object InteractListener {
    init {
        val disabledBlocks = ArrayList<Material> ()
        disabledBlocks.addAll(Arrays.stream(Material.values()).filter { material: Material -> material.name.contains("SHULKER") }.toList())
        disabledBlocks.addAll(Arrays.stream(Material.values()).filter { material: Material -> material.name.contains("SWORD") }.toList())
        disabledBlocks.addAll(Arrays.stream(Material.values()).filter { material: Material -> material.name.contains("AXE") }.toList())
        disabledBlocks.addAll(Arrays.stream(Material.values()).filter { material: Material -> material.name.contains("HOE") }.toList())
        disabledBlocks.addAll(Arrays.stream(Material.values()).filter { material: Material -> material.name.contains("PICKAXE") }.toList())
        disabledBlocks.addAll(Arrays.stream(Material.values()).filter { material: Material -> material.name.contains("CHESTPLATE") }.toList())
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
        disabledBlocks.addAll(listOf(Material.DISPENSER, Material.IRON_BLOCK, Material.TNT, Material.CHEST,
            Material.DIAMOND_BLOCK, Material.FURNACE, Material.ENDER_CHEST, Material.TRAPPED_CHEST,
            Material.HOPPER, Material.DROPPER, Material.FLINT_AND_STEEL, Material.DIAMOND,
            Material.IRON_INGOT, Material.NETHERITE_INGOT, Material.NETHERITE_SCRAP, Material.GOLDEN_APPLE,
            Material.ENCHANTED_GOLDEN_APPLE, Material.PAINTING, Material.ENDER_PEARL, Material.EXPERIENCE_BOTTLE,
            Material.ITEM_FRAME, Material.GOLDEN_CARROT, Material.WITHER_SKELETON_SKULL, Material.WITHER_SKELETON_WALL_SKULL,
            Material.ARMOR_STAND, Material.END_CRYSTAL, Material.SHIELD, Material.ELYTRA, Material.TOTEM_OF_UNDYING,
            Material.SHULKER_SHELL, Material.IRON_NUGGET, Material.TRIDENT, Material.CROSSBOW, Material.BARREL,
            Material.SMOKER, Material.BLAST_FURNACE, Material.NETHERITE_BLOCK, Material.ANCIENT_DEBRIS, Material.COMMAND_BLOCK))

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
            it.player.actionBar("${KColors.RED}you can't drop any items")
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
                        player.actionBar("${KColors.RED}not allowed")
                        player.sendMessage("${KColors.RED}not allowed")
                    } else if (!(it.cursor == null || it.cursor!!.type == Material.AIR)) {
                        if (disabledBlocks.contains(it.cursor!!.type) && Settings.forbiddenItems) {
                            it.isCancelled = true
                            it.cursor = itemStack(Material.AIR) {}
                            player.actionBar("${KColors.RED}not allowed (cursor)")
                            player.sendMessage("${KColors.RED}not allowed (cursor)")
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
                    player.actionBar("${KColors.RED}not allowed")
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
            it.isCancelled = true
        }

        listen<VehicleCreateEvent> {
            if (it.vehicle is Minecart) {
                broadcast(it.vehicle.name)
                broadcast(it.vehicle.type.name)
                it.isCancelled = true
            }
        }

        listen<EntitySpawnEvent> {
            it.isCancelled = true
        }

        listen<PlayerSwapHandItemsEvent> {
            it.isCancelled = true
        }
    }
}