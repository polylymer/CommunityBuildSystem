package de.hglabor.gui

import de.hglabor.config.Settings
import net.axay.kspigot.chat.KColors
import net.axay.kspigot.gui.*
import net.axay.kspigot.items.*
import org.bukkit.Bukkit
import org.bukkit.GameRule
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
                Settings.forbiddenItems = !Settings.forbiddenItems
                it.bukkitEvent.currentItem = forbiddenItems()
            }

            button(Slots.RowTwoSlotFour, skulls()) {
                Settings.skulls = !Settings.skulls
                it.bukkitEvent.currentItem = skulls()
            }

            button(Slots.RowTwoSlotFive, clearSunnyWeather()) {
                if (Settings.clearSunnyWeather) {
                    Bukkit.getWorld("world")?.setGameRule(GameRule.DO_DAYLIGHT_CYCLE, true)
                    Bukkit.getWorld("world")?.setGameRule(GameRule.DO_WEATHER_CYCLE, true)
                    Settings.clearSunnyWeather = false
                } else {
                    Bukkit.getWorld("world")?.time = 6000
                    Bukkit.getWorld("world")?.setStorm(false)
                    Bukkit.getWorld("world")?.isThundering = false
                    Bukkit.getWorld("world")?.setGameRule(GameRule.DO_DAYLIGHT_CYCLE, false)
                    Bukkit.getWorld("world")?.setGameRule(GameRule.DO_WEATHER_CYCLE, false)
                    Settings.clearSunnyWeather = true
                }
                it.bukkitEvent.currentItem = clearSunnyWeather()
            }

            button(Slots.RowTwoSlotSix, opBypass()) {
                Settings.opBypass = !Settings.opBypass
                it.bukkitEvent.currentItem = opBypass()
            }

            button(Slots.RowTwoSlotSeven, disablePortals()) {
                Settings.portal = !Settings.portal
                it.bukkitEvent.currentItem = disablePortals()
            }
        }
    }


    private fun gm(): ItemStack {
        return itemStack(Material.GRASS_BLOCK) {
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
                    +"${KColors.LIGHTBLUE}change the gamemode for all Players, also on Join"
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
                    +"${KColors.LIGHTBLUE}prohibits the use of all unwanted items"
                }
            }
        }
    }

    private fun skulls(): ItemStack {
        return itemStack(Material.SKELETON_SKULL) {
            meta {
                flag(ItemFlag.HIDE_ATTRIBUTES)
                name = "${KColors.YELLOW}SKULLS"
                addLore {
                    +""
                    +"${KColors.GRAY}current: ${if (Settings.skulls) "${KColors.GREEN}ON" else "${KColors.RED}OFF"}"
                    +""
                    +"${KColors.LIGHTBLUE}allows every player to add their own head with /skull"
                }
            }
        }
    }

    private fun clearSunnyWeather(): ItemStack {
        return itemStack(Material.SUNFLOWER) {
            meta {
                flag(ItemFlag.HIDE_ATTRIBUTES)
                name = "${KColors.YELLOW}ALWAYS DAY"
                addLore {
                    +""
                    +"${KColors.GRAY}current: ${if (Settings.clearSunnyWeather) "${KColors.GREEN}ON" else "${KColors.RED}OFF"}"
                    +""
                    +"${KColors.LIGHTBLUE}no description"
                }
            }
        }
    }

    private fun opBypass(): ItemStack {
        return itemStack(Material.BEACON) {
            meta {
                flag(ItemFlag.HIDE_ATTRIBUTES)
                name = "${KColors.YELLOW}OP/MOD BYPASS"
                addLore {
                    +""
                    +"${KColors.GRAY}current: ${if (Settings.opBypass) "${KColors.GREEN}ON" else "${KColors.RED}OFF"}"
                    +""
                    +"${KColors.LIGHTBLUE}join bypass if the server is full"
                }
            }
        }
    }

    private fun disablePortals(): ItemStack {
        return itemStack(Material.END_PORTAL_FRAME) {
            meta {
                flag(ItemFlag.HIDE_ATTRIBUTES)
                name = "${KColors.YELLOW}PORTALS"
                addLore {
                    +""
                    +"${KColors.GRAY}current: ${if (Settings.portal) "${KColors.GREEN}ON" else "${KColors.RED}OFF"}"
                    +""
                    +"${KColors.LIGHTBLUE}prohibits the use of all portals"
                }
            }
        }
    }
}
