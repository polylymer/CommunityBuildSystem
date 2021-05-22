package de.hglabor.gui

import de.hglabor.config.Settings
import net.axay.kspigot.chat.KColors
import net.axay.kspigot.gui.GUIType
import net.axay.kspigot.gui.Slots
import net.axay.kspigot.gui.kSpigotGUI
import net.axay.kspigot.gui.rectTo
import net.axay.kspigot.items.*
import org.bukkit.Bukkit
import org.bukkit.GameMode
import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemFlag
import org.bukkit.inventory.ItemStack

class SettingsGUI {

    val gui = kSpigotGUI(GUIType.THREE_BY_NINE) {
        title = "${KColors.LIGHTBLUE}Settings"
        defaultPage = 0

        page(0) {
            placeholder(Slots.Border, itemStack(Material.LIGHT_BLUE_STAINED_GLASS_PANE) { meta { name = null } })
            placeholder(Slots.RowOneSlotOne rectTo Slots.RowThreeSlotNine, itemStack(Material.LIGHT_BLUE_STAINED_GLASS_PANE) { meta { name = null } })

            button(Slots.RowTwoSlotTwo, gm()) {
                if (!Settings.gm1) {
                    for (players: Player in Bukkit.getOnlinePlayers()) {
                        players.gameMode = GameMode.CREATIVE
                        it.player.sendMessage("${KColors.GREEN}Set Gamemode for all Players to ${KColors.GREENYELLOW}Creative Mode")
                        Settings.gm1 = true
                    }
                } else {
                    for (players: Player in Bukkit.getOnlinePlayers()) {
                        players.gameMode = GameMode.ADVENTURE
                        it.player.sendMessage("${KColors.GREEN}Set Gamemode for all Players to ${KColors.GREENYELLOW}Adventure Mode")
                        Settings.gm1 = false
                    }
                }
                it.bukkitEvent.currentItem = gm()
            }

            button(Slots.RowTwoSlotThree, forbiddenItems()) {
                if (Settings.forbiddenItems) {
                    Settings.forbiddenItems = false
                    it.player.sendMessage("set to false")
                } else {
                    Settings.forbiddenItems = true
                    it.player.sendMessage("set to true")
                }
                it.bukkitEvent.currentItem = forbiddenItems()
            }

            button(Slots.RowTwoSlotFour, skulls()) {
                if (Settings.skulls) {
                    Settings.skulls = false
                    it.player.sendMessage("set to false")
                } else {
                    Settings.skulls = true
                    it.player.sendMessage("set to true")
                }
                it.bukkitEvent.currentItem = skulls()
            }
        }
    }


    private fun gm(): ItemStack {
        return itemStack(Material.TOTEM_OF_UNDYING) {
            meta {
                flag(ItemFlag.HIDE_ATTRIBUTES)
                name = "${KColors.YELLOW}GAMEMODE"
                addLore {
                    +""
                    +"${KColors.GRAY}current: ${if (Settings.gm1) "${KColors.GREEN}CREATIVE" else "${KColors.GREEN}ADVENTURE"}"
                    +""
                    +"${KColors.LIGHTBLUE}Zum Wechseln des Gamemodes"
                    +"${KColors.LIGHTBLUE}für alle Spieler zwischen"
                    +"${KColors.LIGHTBLUE}Adventure/Creative"
                }
            }
        }
    }

    private fun forbiddenItems(): ItemStack {
        return itemStack(Material.NETHERITE_BLOCK) {
            meta {
                flag(ItemFlag.HIDE_ATTRIBUTES)
                name = "${KColors.YELLOW}ITEMS"
                addLore {
                    +""
                    +"${KColors.GRAY}current: ${if (Settings.forbiddenItems) "${KColors.GREEN}ON" else "${KColors.RED}OFF"}"
                    +""
                    +"${KColors.LIGHTBLUE}Verbietet das Benutzen von"
                    +"${KColors.LIGHTBLUE}OP/unnötigen Items"
                    +"${KColors.LIGHTBLUE}(Netherite, Armorstands, usw.)"
                }
            }
        }
    }

    private fun skulls(): ItemStack {
        return itemStack(Material.PLAYER_HEAD) {
            meta {
                flag(ItemFlag.HIDE_ATTRIBUTES)
                name = "${KColors.YELLOW}SKULLS"
                addLore {
                    +""
                    +"${KColors.GRAY}current: ${if (Settings.skulls) "${KColors.GREEN}ON" else "${KColors.RED}OFF"}"
                    +""
                    +"${KColors.LIGHTBLUE}Erlaubt jedem Spieler sich mit"
                    +"${KColors.LIGHTBLUE}/skull seinen eigenen Kopf zu geben"
                }
            }
        }
    }
}
