package ir.syrent.enhancedvelocity.command

import com.velocitypowered.api.command.SimpleCommand
import ir.syrent.enhancedvelocity.api.VanishHook
import ir.syrent.enhancedvelocity.storage.Message
import ir.syrent.enhancedvelocity.storage.Settings
import ir.syrent.enhancedvelocity.utils.TextReplacement
import ir.syrent.enhancedvelocity.utils.sendMessage
import ir.syrent.enhancedvelocity.vruom.VRuom
import java.util.concurrent.CompletableFuture

class FindCommand : SimpleCommand {

    init {
        VRuom.registerCommand(this, Settings.findCommand, *Settings.findAliases.toTypedArray())
    }

    override fun execute(invocation: SimpleCommand.Invocation) {
        val sender = invocation.source()
        val args = invocation.arguments()

        if (!sender.hasPermission(Permissions.Commands.FIND)) {
            sender.sendMessage(Message.NO_PERMISSION, TextReplacement("permission", Permissions.Commands.FIND))
            return
        }

        if (args.isEmpty()) {
            sender.sendMessage(Message.FIND_USAGE)
            return
        }

        val targetPlayer = VRuom.getPlayer(args[0])

        if (targetPlayer == null) {
            sender.sendMessage(Message.FIND_NO_TARGET)
            return
        }

        val vanished = VanishHook.isVanished(targetPlayer.uniqueId)
        val server = targetPlayer.currentServer

        if (!server.isPresent && !vanished) {
            sender.sendMessage(Message.FIND_NO_SERVER)
            return
        }

        if (vanished && !sender.hasPermission(Permissions.Actions.FIND_VANISHED)) {
            sender.sendMessage(Message.FIND_NO_TARGET)
            return
        }

        sender.sendMessage(
            Message.FIND_USE,
            TextReplacement("player", targetPlayer.username),
            TextReplacement("server", server.map { it.serverInfo.name }.orElse("Unknown")),
            TextReplacement("vanished", if (vanished && sender.hasPermission(Permissions.Actions.FIND_VANISHED)) Settings.formatMessage(Message.FIND_VANISHED) else "")
        )
    }

    override fun suggest(invocation: SimpleCommand.Invocation): List<String> {
        val args = invocation.arguments()
        val lastArg = args.lastOrNull()?.lowercase() ?: ""
        return VanishHook.getNonVanishedPlayers()
            .map { it.username }
            .filter { it.lowercase().startsWith(lastArg) }
            .sorted()
    }

    override fun suggestAsync(invocation: SimpleCommand.Invocation): CompletableFuture<List<String>> {
        return CompletableFuture.completedFuture(suggest(invocation))
    }
}