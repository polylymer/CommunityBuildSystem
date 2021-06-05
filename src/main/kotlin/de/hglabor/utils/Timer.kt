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
                if (!(player.isOp || Settings.isAdmin(player) || Settings.isDeveloper(player) || Settings.isModerator(player) || Settings.isCreativity(player) || Settings.isBuilder(player))) {
                    if (config.get("player.timePlayed.${player.name}") == null) {
                        if (System.currentTimeMillis() - timePlayed[player]!! > Settings.getBuildTime()) {
                            player.kick(Localization.getMessage("buildsystem.buildTimeOver", Locale.getByPlayer(player)))
                        }
                    } else {
                        if (System.currentTimeMillis() - timePlayed[player]!! + config.getLong("player.timePlayed.${player.name}") > Settings.getBuildTime()) {
                            player.kick(Localization.getMessage("buildsystem.buildTimeOver", Locale.getByPlayer(player)))
                        }
                    }
                }
            }
        }, 0, 20)
    }

    fun saveTimeOnQuit(player: Player) {
        if (config.get("player.timePlayed.${player.name}") == null) {
            config.set("player.timePlayed.${player.name}", System.currentTimeMillis() - timePlayed[player]!!)
            BuildSystem.INSTANCE.saveConfig()
        } else {
            config.set("player.timePlayed.${player.name}", System.currentTimeMillis() + config.getLong("player.timePlayed.${player.name}") - timePlayed[player]!!)
            BuildSystem.INSTANCE.saveConfig()
        }
    }

    fun getPlayerTimeOrderly(player: Player): String {
        return if (!(player.isOp || Settings.isAdmin(player) || Settings.isDeveloper(player) || Settings.isModerator(player) || Settings.isCreativity(player) || Settings.isBuilder(player))) {
            val minutes: Long
            val seconds: Long
            if (config.get("player.timePlayed.${player.name}") == null) {
                minutes = ((System.currentTimeMillis() - timePlayed[player]!!) / 1000 / 60)
                seconds = ((System.currentTimeMillis() - timePlayed[player]!!) / 1000 % 60)
                "${minutes}:${seconds}"
            } else {
                minutes = ((System.currentTimeMillis() - timePlayed[player]!! + config.getLong("player.timePlayed.${player.name}")) / 1000 / 60)
                seconds = ((System.currentTimeMillis() - timePlayed[player]!! + config.getLong("player.timePlayed.${player.name}")) / 1000 % 60)
                "${minutes}:${seconds}"
            }
        } else {
            "you aren't tracked"
        }
    }
}
