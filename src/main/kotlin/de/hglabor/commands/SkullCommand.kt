package de.hglabor.commands

import com.google.common.collect.ImmutableMap
import de.hglabor.config.Settings
import de.hglabor.localization.Locale.getByPlayer
import de.hglabor.localization.Localization
import net.axay.kspigot.chat.KColors
import net.axay.kspigot.items.itemStack
import net.kyori.adventure.text.Component
import org.bukkit.Material
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player
import org.bukkit.inventory.meta.SkullMeta


object SkullCommand : CommandExecutor{

    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<out String>): Boolean {
        if (sender is Player) {
            if (Settings.skulls) {
                if (args.isEmpty()) {
                    val skull = itemStack(Material.PLAYER_HEAD) {
                        val meta = itemMeta as SkullMeta
                        meta.displayName(Component.text("${KColors.LIGHTBLUE}" + sender.name))
                        meta.owningPlayer = sender
                        itemMeta = meta
                    }
                    sender.inventory.addItem(skull)
                    sender.sendMessage(Localization.getMessage("buildsystem.addedSkull", ImmutableMap.of("skull", sender.name), getByPlayer(sender)))
                }
            } else {
                sender.sendMessage(Localization.getMessage("buildsystem.skullIsDisabled", getByPlayer(sender)))
            }
        }
        return false
    }
}