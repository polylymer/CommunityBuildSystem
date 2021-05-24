package de.hglabor.gui

import de.hglabor.config.Settings
import net.axay.kspigot.chat.KColors
import net.axay.kspigot.gui.*
import net.axay.kspigot.items.*
import org.bukkit.Material
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
                it.player.closeInventory()
                it.player.openGUI(GamemodeGUI().gui)
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
                    when (Settings.gm1) {
                        0 -> +"${KColors.GRAY}current: ${KColors.GREEN}CREATIVE"
                        1 -> +"${KColors.GRAY}current: ${KColors.GREEN}ADVENTURE"
                        2 -> +"${KColors.GRAY}current: ${KColors.GREEN}SPECTATOR"
                        3 -> +"${KColors.GRAY}current: ${KColors.GREEN}SURVIVAL"
                    }
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
