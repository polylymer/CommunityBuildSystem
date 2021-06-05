package de.hglabor.events

import de.hglabor.BuildSystem
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
import net.kyori.adventure.text.Component
import org.bukkit.Bukkit
import org.bukkit.GameMode
import org.bukkit.Material
import org.bukkit.event.player.PlayerJoinEvent
import org.bukkit.event.player.PlayerLoginEvent
import org.bukkit.event.player.PlayerQuitEvent

object JoinQuitListener {
    init {
        listen<PlayerJoinEvent> {
            val player = it.player
            player.heal()
            player.feedSaturate()
            it.joinMessage(Component.text("${KColors.GOLD}${player.name}${KColors.WHITE} joined the Server ${KColors.GRAY}[${KColors.WHITE}${Bukkit.getOnlinePlayers().size}${KColors.GRAY}/${KColors.WHITE}${Bukkit.getServer().maxPlayers}${KColors.GRAY}]"))
            player.sendMessage(Localization.getMessage("buildsystem.LimitedTimeInfoMessage", getByPlayer(player)))

            val settingsItem = itemStack(Material.BARREL) {
                meta {
                    name = "${KColors.LIGHTBLUE}SETTINGS"
                }
            }
            settingsItem.mark("protected")
            if (player.isOp) {
                player.inventory.setItem(8, settingsItem)
            }

            if (!(player.isOp || Settings.isAdmin(player) || Settings.isDeveloper(player) || Settings.isModerator(player) || Settings.isCreativity(player) || Settings.isBuilder(player))) {
                BuildSystem.timer.putTimeInList(player)
            }

            if (Settings.skulls) {
                player.sendMessage(Localization.getMessage("buildsystem.SkullInfoMessage", getByPlayer(player)))
            }

            when (Settings.gm1) {
                0 -> player.gameMode = GameMode.CREATIVE
                1 -> player.gameMode = GameMode.ADVENTURE
                2 -> player.gameMode = GameMode.SPECTATOR
                3 -> player.gameMode = GameMode.SURVIVAL
            }

            BuildSystem.scoreboard.addPlayerToScoreboard(player)
        }

        listen<PlayerQuitEvent> {
            val  player = it.player
            it.quitMessage(Component.text(""))
            if (!(player.isOp || Settings.isAdmin(player) || Settings.isDeveloper(player) || Settings.isModerator(player) || Settings.isCreativity(player) || Settings.isBuilder(player))) {
                BuildSystem.timer.saveTimeOnQuit(player)
            }
        }

        listen<PlayerLoginEvent> {
            if (Settings.opBypass) {
                val player = it.player
                if (it.result == PlayerLoginEvent.Result.KICK_FULL && player.isOp || Settings.isAdmin(player) || Settings.isDeveloper(player) || Settings.isModerator(player) || Settings.isCreativity(player) || Settings.isBuilder(player)) {
                    it.allow()
                }
            }
        }
    }
}