package de.hglabor.commands

import com.google.common.collect.ImmutableMap
import de.hglabor.config.Settings
import de.hglabor.localization.Locale.getByPlayer
import de.hglabor.localization.Localization
import net.axay.kspigot.chat.KColors
import org.bukkit.Material
import org.bukkit.SkullType
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.meta.SkullMeta


object SkullCommand : CommandExecutor{

    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<out String>): Boolean {
        if (sender is Player) {
            if (Settings.skulls) {
                if (args.isEmpty()) {
                    val skull = ItemStack(Material.PLAYER_HEAD, 1, SkullType.PLAYER.ordinal.toShort())
                    val meta = skull.itemMeta as SkullMeta
                    meta.setDisplayName("${KColors.LIGHTBLUE}" + sender.name)
                    meta.owner = sender.name
                    skull.itemMeta = meta

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