package de.hglabor.events

import de.hglabor.config.Settings
import de.hglabor.localization.Locale.getByPlayer
import de.hglabor.localization.Localization
import net.axay.kspigot.chat.KColors
import net.axay.kspigot.event.listen
import net.axay.kspigot.extensions.bukkit.feedSaturate
import net.axay.kspigot.extensions.bukkit.heal
import net.axay.kspigot.items.itemStack
import net.axay.kspigot.items.meta
import net.axay.kspigot.items.name
import net.axay.kspigot.utils.mark
import org.bukkit.Bukkit
import org.bukkit.GameMode
import org.bukkit.Material
import org.bukkit.event.player.PlayerJoinEvent
import org.bukkit.event.player.PlayerLoginEvent

object JoinQuitListener {
    init {
        listen<PlayerJoinEvent> {
            val player = it.player

            player.heal()
            player.feedSaturate()
            it.joinMessage = "${KColors.GOLD}${player.name}${KColors.WHITE} joined the Server ${KColors.GRAY}[${KColors.WHITE}${Bukkit.getOnlinePlayers().size}${KColors.GRAY}/${KColors.WHITE}${Bukkit.getServer().maxPlayers}${KColors.GRAY}]"


            val settingsItem = itemStack(Material.BARREL) {
                meta {
                    name = "${KColors.LIGHTBLUE}SETTINGS"
                }
            }
            settingsItem.mark("protected")
            if (player.isOp) {
                player.inventory.setItem(8, settingsItem)
            }

            if (Settings.skulls) {
                player.sendMessage(Localization.getMessage("buildsystem.infoMessageOnJoin", getByPlayer(player)))
            }

            when (Settings.gm1) {
                0 -> player.gameMode = GameMode.CREATIVE
                1 -> player.gameMode = GameMode.ADVENTURE
                2 -> player.gameMode = GameMode.SPECTATOR
                3 -> player.gameMode = GameMode.SURVIVAL
            }
        }

        listen<PlayerLoginEvent> {
            if (Settings.opBypass) {
                if (it.result == PlayerLoginEvent.Result.KICK_FULL && it.player.isOp || it.player.hasPermission("group.moderator")) {
                    it.allow()
                }
            }
        }
    }
}