package ir.syrent.enhancedvelocity.command

import com.velocitypowered.api.command.SimpleCommand
import ir.syrent.enhancedvelocity.api.VanishManager
import ir.syrent.enhancedvelocity.storage.Message
import ir.syrent.enhancedvelocity.utils.PlayerNotFoundException
import ir.syrent.enhancedvelocity.utils.TextReplacement
import ir.syrent.enhancedvelocity.utils.getPlayer
import ir.syrent.enhancedvelocity.utils.sendMessage
import ir.syrent.enhancedvelocity.vruom.VRuom
import java.util.concurrent.CompletableFuture

class SendCommand : SimpleCommand {

    init {
        VRuom.registerCommand(this, "send")
    }

    override fun execute(invocation: SimpleCommand.Invocation) {
        val sender = invocation.source()
        val args = invocation.arguments()

        if (!sender.hasPermission(Permissions.Commands.SEND)) {
            sender.sendMessage(Message.NO_PERMISSION, TextReplacement("permission", Permissions.Commands.SEND))
            return
        }

        if (args.size < 2) {
            sender.sendMessage(Message.SEND_USAGE)
            return
        }

        val target = try {
            getPlayer(args[0])
        } catch (e: PlayerNotFoundException) {
            sender.sendMessage(Message.PLAYER_NOT_FOUND)
            return
        }

        if (VanishManager.isVanished(target.uniqueId) && !sender.hasPermission(Permissions.Actions.SEE_VANISHED)) {
            sender.sendMessage(Message.PLAYER_NOT_FOUND)
            return
        }

        val server = VRuom.server.getServer(args[1]).orElse(null)
        if (server == null) {
            sender.sendMessage(Message.SERVER_NOT_FOUND)
            return
        }

        target.createConnectionRequest(server).fireAndForget()

        sender.sendMessage(
            Message.SEND_USE,
            TextReplacement("player", target.username),
            TextReplacement("server", server.serverInfo.name)
        )
    }

    override fun suggest(invocation: SimpleCommand.Invocation): List<String> {
        val args = invocation.arguments()
        val lastArg = args.lastOrNull()?.lowercase() ?: ""

        return when (args.size) {
            1 -> { // Suggest player names
                val players = if (invocation.source().hasPermission(Permissions.Actions.SEE_VANISHED)) {
                    VRuom.onlinePlayers
                } else {
                    VanishManager.nonVanishedPlayers
                }
                players
                    .map { it.username }
                    .filter { it.lowercase().startsWith(lastArg) }
                    .sorted()
            }

            2 -> { // Suggest destination servers
                VRuom.server.allServers.map { it.serverInfo.name }
                    .filter { it.lowercase().startsWith(lastArg) }
                    .sorted()
            }

            else -> emptyList()
        }
    }

    override fun suggestAsync(invocation: SimpleCommand.Invocation): CompletableFuture<List<String>> {
        return CompletableFuture.completedFuture(suggest(invocation))
    }
}