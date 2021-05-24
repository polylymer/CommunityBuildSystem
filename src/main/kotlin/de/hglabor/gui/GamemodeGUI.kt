package de.hglabor.gui

import de.hglabor.config.Settings
import net.axay.kspigot.chat.KColors
import net.axay.kspigot.gui.*
import net.axay.kspigot.items.flag
import net.axay.kspigot.items.itemStack
import net.axay.kspigot.items.meta
import net.axay.kspigot.items.name
import org.bukkit.Bukkit
import org.bukkit.GameMode
import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemFlag

class GamemodeGUI {

    val gui = kSpigotGUI(GUIType.THREE_BY_NINE) {
        title = "${KColors.LIGHTBLUE}Gamemode"
        defaultPage = 0

        page(0) {
            placeholder(Slots.Border, itemStack(Material.LIGHT_BLUE_STAINED_GLASS_PANE) { meta { name = null } })
            placeholder(Slots.RowOneSlotOne rectTo Slots.RowThreeSlotNine, itemStack(Material.LIGHT_BLUE_STAINED_GLASS_PANE) { meta { name = null } })

            button(Slots.RowTwoSlotTwo, itemStack(Material.LIME_WOOL) {
                meta {
                    flag(ItemFlag.HIDE_ATTRIBUTES)
                    name = "${KColors.YELLOW}GAMEMODE CREATIVE"
                }
            }) {
                for (players: Player in Bukkit.getOnlinePlayers()) {
                    players.gameMode = GameMode.CREATIVE
                    it.player.sendMessage("${KColors.GREEN}Set Gamemode for all Players to ${KColors.GREENYELLOW}Creative Mode")
                    Settings.gm1 = 0
                    it.player.closeInventory()
                    Settings.openSettingsGUI(it.player)
                }
            }

            button(Slots.RowTwoSlotFour, itemStack(Material.ORANGE_WOOL) {
                meta {
                    flag(ItemFlag.HIDE_ATTRIBUTES)
                    name = "${KColors.YELLOW}GAMEMODE ADVENTURE"
                }
            }) {
                for (players: Player in Bukkit.getOnlinePlayers()) {
                    players.gameMode = GameMode.ADVENTURE
                    it.player.sendMessage("${KColors.GREEN}Set Gamemode for all Players to ${KColors.GREENYELLOW}Adventure Mode")
                    Settings.gm1 = 1
                    it.player.closeInventory()
                    Settings.openSettingsGUI(it.player)
                }
            }

            button(Slots.RowTwoSlotSix, itemStack(Material.PURPLE_WOOL) {
                meta {
                    flag(ItemFlag.HIDE_ATTRIBUTES)
                    name = "${KColors.YELLOW}GAMEMODE SPECTATOR"
                }
            }) {
                for (players: Player in Bukkit.getOnlinePlayers()) {
                    players.gameMode = GameMode.SPECTATOR
                    it.player.sendMessage("${KColors.GREEN}Set Gamemode for all Players to ${KColors.GREENYELLOW}Spectator Mode")
                    Settings.gm1 = 2
                    it.player.closeInventory()
                    Settings.openSettingsGUI(it.player)
                }
            }

            button(Slots.RowTwoSlotEight, itemStack(Material.RED_WOOL) {
                meta {
                    flag(ItemFlag.HIDE_ATTRIBUTES)
                    name = "${KColors.YELLOW}GAMEMODE SURVIVAL"
                }
            }) {
                for (players: Player in Bukkit.getOnlinePlayers()) {
                    players.gameMode = GameMode.SURVIVAL
                    it.player.sendMessage("${KColors.GREEN}Set Gamemode for all Players to ${KColors.GREENYELLOW}Survival Mode")
                    Settings.gm1 = 3
                    it.player.closeInventory()
                    Settings.openSettingsGUI(it.player)
                }
            }
        }
    }
}