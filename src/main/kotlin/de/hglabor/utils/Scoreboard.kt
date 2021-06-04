package de.hglabor.utils

import de.hglabor.BuildSystem
import de.hglabor.config.Settings
import dev.jcsoftware.jscoreboards.*
import net.axay.kspigot.chat.KColors
import net.axay.kspigot.extensions.onlinePlayers
import org.bukkit.Bukkit
import org.bukkit.entity.Player

class Scoreboard {
    private var scoreboard: JPerPlayerScoreboard? = null

    fun setScoreboard() {
        scoreboard = JPerPlayerScoreboard(
            { "${KColors.CORNFLOWERBLUE}HGLabor.de" },
            { player: Player ->
                val lines: MutableList<String> = ArrayList()
                lines.add("")
                lines.add("Players:")
                lines.add(KColors.LIGHTBLUE.toString() + onlinePlayers.size.toString())
                lines.add("")
                lines.add("BuildTime: (${Settings.getBuildTime() / 60000} minutes)")
                lines.add(KColors.LIGHTBLUE.toString() + BuildSystem.timer.getPlayerTimeOrderly(player))
                lines.add("")
                lines
            },
        )

        Bukkit.getScheduler().runTaskTimer(BuildSystem.INSTANCE, Runnable {
            this.scoreboard!!.updateScoreboard()
        }, 0, 20)
    }

    fun addPlayerToScoreboard(player: Player) {
        this.scoreboard!!.addPlayer(player)
    }
}
