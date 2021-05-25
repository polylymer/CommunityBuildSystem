package de.hglabor.commands

import de.hglabor.gui.SettingsGUI
import de.hglabor.localization.Locale.getByPlayer
import de.hglabor.localization.Localization
import net.axay.kspigot.gui.openGUI
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player

object SettingsCommand : CommandExecutor {
    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<out String>): Boolean {
        if (sender is Player) {
            if (sender.isOp) {
                sender.openGUI(SettingsGUI().gui)
            } else {
                sender.sendMessage(Localization.getMessage("buildsystem.noPermission", getByPlayer(sender)))
            }
        }
        return false
    }
}