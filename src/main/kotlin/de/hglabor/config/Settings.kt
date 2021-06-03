package de.hglabor.config

import de.hglabor.BuildSystem
import de.hglabor.gui.SettingsGUI
import net.axay.kspigot.gui.openGUI
import org.bukkit.Difficulty
import org.bukkit.GameRule
import org.bukkit.World
import org.bukkit.configuration.file.FileConfiguration
import org.bukkit.entity.Player

object Settings {

    var gm1 = 1
    var forbiddenItems = true
    var skulls = true
    var clearSunnyWeather = true
    var opBypass = true
    var portal = true

    private val configuration: FileConfiguration = BuildSystem.INSTANCE.config
    fun setConfig() {
        if (configuration.get("buildsystem.settings.BuildTime") == null) {
            configuration.set("buildsystem.settings.BuildTime", 20*60*20*60000)
            BuildSystem.INSTANCE.saveConfig()
        }
    }

    fun getBuildTime(): Int {
        return configuration.getInt("buildsystem.settings.BuildTime")
    }

    fun setWorldSettings(world: World) {
        if (clearSunnyWeather) {
            world.time = 6000
            world.setStorm(false)
            world.isThundering = false
            world.setGameRule(GameRule.DO_DAYLIGHT_CYCLE, false)
            world.setGameRule(GameRule.DO_WEATHER_CYCLE, false)
        }
        world.difficulty = Difficulty.PEACEFUL
        world.setGameRule(GameRule.ANNOUNCE_ADVANCEMENTS, false)
        world.setGameRule(GameRule.SHOW_DEATH_MESSAGES, false)
        world.setGameRule(GameRule.MOB_GRIEFING, false)
        world.setGameRule(GameRule.DO_ENTITY_DROPS, false)
        world.setGameRule(GameRule.DO_MOB_SPAWNING, false)
        world.setGameRule(GameRule.DO_IMMEDIATE_RESPAWN, false)
        world.setGameRule(GameRule.DO_MOB_LOOT, false)
        world.setGameRule(GameRule.DO_TILE_DROPS, false)
        world.setGameRule(GameRule.DO_TRADER_SPAWNING, false)
        world.setGameRule(GameRule.DO_FIRE_TICK, false)
        world.setGameRule(GameRule.DISABLE_RAIDS, true)
        world.setGameRule(GameRule.FALL_DAMAGE, false)
        world.setGameRule(GameRule.FIRE_DAMAGE, false)
        world.setGameRule(GameRule.DO_PATROL_SPAWNING, false)
    }

    fun openSettingsGUI(player: Player) {
        player.openGUI(SettingsGUI().gui)
    }

    fun isAdmin(player: Player): Boolean {
        return player.hasPermission("group.admin")
    }

    fun isDeveloper(player: Player): Boolean {
        return player.hasPermission("group.developer")
    }

    fun isModerator(player: Player): Boolean {
        return player.hasPermission("group.moderator")
    }

    fun isCreativity(player: Player): Boolean {
        return player.hasPermission("group.creativity")
    }

    fun isBuilder(player: Player): Boolean {
        return player.hasPermission("group.builder")
    }
}