package ir.syrent.enhancedvelocity.command

import com.velocitypowered.api.command.SimpleCommand
import com.velocitypowered.api.proxy.Player
import ir.syrent.enhancedvelocity.api.VanishManager
import ir.syrent.enhancedvelocity.storage.Message
import ir.syrent.enhancedvelocity.utils.TextReplacement
import ir.syrent.enhancedvelocity.utils.sendMessage
import ir.syrent.enhancedvelocity.vruom.VRuom
import java.util.concurrent.CompletableFuture

class PingCommand : SimpleCommand {

    init {
        VRuom.registerCommand(this, "ping")
    }

    override fun execute(invocation: SimpleCommand.Invocation) {
        val sender = invocation.source()
        val args = invocation.arguments()

        if (!sender.hasPermission(Permissions.Commands.PING)) {
            sender.sendMessage(Message.NO_PERMISSION, TextReplacement("permission", Permissions.Commands.PING))
            return
        }

        if (args.isNotEmpty()) {
            val target = VRuom.getPlayer(args[0])

            if (target == null || (VanishManager.isVanished(target.uniqueId) && !sender.hasPermission(Permissions.Actions.SEE_VANISHED))) {
                sender.sendMessage(Message.PING_NO_TARGET)
                return
            }

            sender.sendMessage(
                Message.PING_USE_TARGET,
                TextReplacement("player", target.username),
                TextReplacement("ping", target.ping.toString())
            )
            return
        }

        if (sender !is Player) {
            sender.sendMessage(Message.ONLY_PLAYERS)
            return
        }

        sender.sendMessage(Message.PING_USE, TextReplacement("ping", sender.ping.toString()))
    }

    override fun suggest(invocation: SimpleCommand.Invocation): List<String> {
        val lastArg = invocation.arguments().lastOrNull()?.lowercase() ?: ""
        val players = if (invocation.source().hasPermission(Permissions.Actions.SEE_VANISHED)) {
            VRuom.onlinePlayers
        } else {
            VanishManager.nonVanishedPlayers
        }

        return players
            .map { it.username }
            .filter { it.lowercase().startsWith(lastArg) }
            .sorted()
    }

    override fun suggestAsync(invocation: SimpleCommand.Invocation): CompletableFuture<List<String>> {
        return CompletableFuture.completedFuture(suggest(invocation))
    }
}