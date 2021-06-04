package de.hglabor

import de.hglabor.commands.SettingsCommand
import de.hglabor.commands.SkullCommand
import de.hglabor.config.Settings
import de.hglabor.events.InteractListener
import de.hglabor.events.JoinQuitListener
import de.hglabor.localization.Localization
import de.hglabor.utils.Scoreboard
import de.hglabor.utils.Timer
import net.axay.kspigot.chat.KColors
import net.axay.kspigot.extensions.broadcast
import net.axay.kspigot.extensions.bukkit.register
import net.axay.kspigot.main.KSpigot
import org.bukkit.Bukkit


class BuildSystem : KSpigot() {

    companion object {
        lateinit var INSTANCE: BuildSystem;
        lateinit var timer: Timer
        lateinit var scoreboard: Scoreboard
    }

    override fun load() {
        INSTANCE = this
        broadcast("${KColors.LIMEGREEN}ENABLING BUILD-SYSTEM")
    }

    override fun startup() {
        Settings.setWorldSettings(Bukkit.getWorld("world")!!)
        Settings.setConfig()
        Localization.init()
        timer = Timer()
        timer.checkPlayTime()
        scoreboard = Scoreboard()
        scoreboard.setScoreboard()
        JoinQuitListener
        InteractListener
        SkullCommand.register("skull")
        SettingsCommand.register("settings")
    }

    override fun shutdown() {
        broadcast("${KColors.DARKRED}DISABLING BUILD-SYSTEM")
        server.scheduler.cancelTasks(INSTANCE)
    }
}