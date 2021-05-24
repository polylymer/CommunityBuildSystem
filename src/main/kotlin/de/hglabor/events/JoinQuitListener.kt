package de.hglabor.events

import de.hglabor.config.Settings
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

object JoinQuitListener {
    init {
        listen<PlayerJoinEvent> {
            val player = it.player

            player.heal()
            player.feedSaturate()
            it.joinMessage = "${KColors.GOLD}${player.name}${KColors.WHITE} joined the Server${KColors.GRAY}[${KColors.GOLD}${Bukkit.getOnlinePlayers().size}${KColors.GRAY}/${KColors.GOLD}${Bukkit.getServer().maxPlayers}${KColors.GRAY}]"

            val settingsItem = itemStack(Material.BARREL) {
                meta {
                    name = "${KColors.LIGHTBLUE}SETTINGS"
                }
            }
            settingsItem.mark("protected")
            if (player.isOp) {
                player.inventory.setItem(8, settingsItem)
            }

            when (Settings.gm1) {
                0 -> player.gameMode = GameMode.CREATIVE
                1 -> player.gameMode = GameMode.ADVENTURE
                2 -> player.gameMode = GameMode.SPECTATOR
                3 -> player.gameMode = GameMode.SURVIVAL
            }
        }
    }
}