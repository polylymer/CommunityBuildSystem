package de.hglabor.utils

import de.hglabor.BuildSystem
import de.hglabor.config.Settings
import de.hglabor.localization.Locale
import de.hglabor.localization.Localization
import net.axay.kspigot.extensions.bukkit.kick
import net.axay.kspigot.extensions.onlinePlayers
import org.bukkit.Bukkit
import org.bukkit.configuration.file.FileConfiguration
import org.bukkit.entity.Player

class Timer {

    private val timePlayed = HashMap<Player, Long> ()
    private val config: FileConfiguration = BuildSystem.INSTANCE.config

    fun putTimeInList(player: Player) {
        timePlayed[player] = System.currentTimeMillis()
    }

    fun checkPlayTime() {
        Bukkit.getScheduler().runTaskTimer(BuildSystem.INSTANCE, Runnable {
            for (player: Player in onlinePlayers) {
                if (!Settings.isStaff(player)) {
                    if (getPlayerTime(player) >= Settings.getBuildTime()) {
                        player.kick(Localization.getMessage("buildsystem.buildTimeOver", Locale.getByPlayer(player)))
                    }
                }
            }
        }, 0, 20)
    }

    fun saveTimeOnQuit(player: Player) {
        config.set("player.timePlayed.${player.name}", getPlayerTime(player))
        BuildSystem.INSTANCE.saveConfig()
    }

    fun getPlayerTimeOrderly(player: Player): String {
        return if (!Settings.isStaff(player)) {
            val minutes: Long = (getPlayerTime(player) / 1000 / 60)
            val seconds: Long = (getPlayerTime(player) / 1000 % 60)
            "${minutes}:${seconds}"
        } else {
            "you aren't tracked"
        }
    }

    fun getPlayerTime(player: Player): Long {
        return if (!Settings.isStaff(player)) {
            if (config.get("player.timePlayed.${player.name}") == null) {
                System.currentTimeMillis() - timePlayed[player]!!
            } else {
                System.currentTimeMillis() - timePlayed[player]!! + config.getLong("player.timePlayed.${player.name}")
            }
        } else {
            0
        }
    }
}
