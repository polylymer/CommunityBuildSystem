package de.hglabor.gui

import de.hglabor.config.Settings
import de.hglabor.localization.Locale
import de.hglabor.localization.Localization
import net.axay.kspigot.chat.KColors
import net.axay.kspigot.chat.input.awaitAnvilInput
import net.axay.kspigot.gui.*
import net.axay.kspigot.items.*
import org.bukkit.Bukkit
import org.bukkit.GameRule
import org.bukkit.Material
import org.bukkit.WorldBorder
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

            button(Slots.RowTwoSlotEight, worldborder()) {
                val player = it.player
                it.player.closeInventory()
                player.awaitAnvilInput("Worldborder Size") { result ->
                    kotlin.runCatching {
                        Bukkit.getWorld("world")!!.worldBorder.size = result.input!!.toDouble()
                        Settings.openSettingsGUI(player)
                    }.onFailure {
                        Localization.getMessage("buildsystem.errorOcurred", Locale.getByPlayer(player))
                        Settings.openSettingsGUI(player)
                    }
                }
                it.bukkitEvent.currentItem = worldborder()
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
                    +Localization.getMessage("buildsystem.settings.description.gamemode", Locale.ENGLISH)
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
                    +Localization.getMessage("buildsystem.settings.description.forbiddenItems", Locale.ENGLISH)
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
                    +Localization.getMessage("buildsystem.settings.description.skulls", Locale.ENGLISH)
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
                    +Localization.getMessage("buildsystem.settings.description.clearSunnyWeather", Locale.ENGLISH)
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
                    +Localization.getMessage("buildsystem.settings.description.opBypass", Locale.ENGLISH)
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
                    +Localization.getMessage("buildsystem.settings.description.disablePortals", Locale.ENGLISH)
                }
            }
        }
    }

    private fun worldborder(): ItemStack {
        return itemStack(Material.BARRIER) {
            meta {
                flag(ItemFlag.HIDE_ATTRIBUTES)
                name = "${KColors.YELLOW}WORLDBORDER"
                addLore {
                    +""
                    +"${KColors.GRAY}current: ${KColors.LIGHTBLUE}${Bukkit.getWorld("world")!!.worldBorder.size}"
                    +""
                    +Localization.getMessage("buildsystem.settings.description.worldborder", Locale.ENGLISH)
                }
            }
        }
    }
}
