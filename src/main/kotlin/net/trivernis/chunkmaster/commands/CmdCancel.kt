package net.trivernis.chunkmaster.commands

import net.md_5.bungee.api.ChatColor
import net.md_5.bungee.api.chat.ComponentBuilder
import net.trivernis.chunkmaster.Chunkmaster
import net.trivernis.chunkmaster.lib.Subcommand
import net.trivernis.chunkmaster.lib.generation.TaskEntry
import org.bukkit.command.Command
import org.bukkit.command.CommandSender

class CmdCancel(private val chunkmaster: Chunkmaster): Subcommand {
    override val name = "cancel"

    /**
     * TabComplete for subcommand cancel.
     */
    override fun onTabComplete(
        sender: CommandSender,
        command: Command,
        alias: String,
        args: List<String>
    ): MutableList<String> {
        val genManager = chunkmaster.generationManager
        val allTasks = genManager.allTasks
        return allTasks.filter {it.id.toString().indexOf(args[0]) == 0}
            .map { it.id.toString() }.toMutableList()
    }

    /**
     * Cancels the generation task if it exists.
     */
    override fun execute(sender: CommandSender, args: List<String>): Boolean {
        return if (args.isNotEmpty()) {
            val index = if (args[0].toIntOrNull() != null) {
                args[0].toInt()
            } else {
                chunkmaster.generationManager.tasks.find { it.generationTask.world.name == args[0] }?.id
            }

            if (index != null && chunkmaster.generationManager.removeTask(index)) {
                sender.sendMessage(chunkmaster.langManager.getLocalized("TASK_CANCELED", index))
                true
            } else {
                sender.sendMessage(chunkmaster.langManager.getLocalized("TASK_NOT_FOUND", args[0]))
                false
            }

        } else {
            sender.sendMessage(chunkmaster.langManager.getLocalized("TASK_ID_REQUIRED"));
            false
        }
    }
}