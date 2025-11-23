package ir.syrent.enhancedvelocity.command

import com.velocitypowered.api.command.SimpleCommand
import ir.syrent.enhancedvelocity.storage.Message
import ir.syrent.enhancedvelocity.utils.TextReplacement
import ir.syrent.enhancedvelocity.utils.sendMessage
import ir.syrent.enhancedvelocity.vruom.VRuom
import java.util.concurrent.CompletableFuture

class KickAllCommand : SimpleCommand {

    init {
        VRuom.registerCommand(this, "kickall")
    }

    override fun execute(invocation: SimpleCommand.Invocation) {
        val sender = invocation.source()
        val args = invocation.arguments()

        if (!sender.hasPermission(Permissions.Commands.KICKALL)) {
            sender.sendMessage(Message.NO_PERMISSION, TextReplacement("permission", Permissions.Commands.KICKALL))
            return
        }

        if (args.isEmpty()) {
            sender.sendMessage(Message.KICKALL_USAGE)
            return
        }

        val serverName = args[0].lowercase()
        val targetPlayers = if (serverName == "all") {
            VRuom.onlinePlayers
        } else {
            VRuom.server.getServer(serverName).map { it.playersConnected }.orElse(null)
        }

        if (targetPlayers == null) {
            sender.sendMessage(Message.KICKALL_NO_SERVER)
            return
        }

        val lobbyServer = VRuom.server.allServers.first()
        targetPlayers
            .filterNot { it.hasPermission(Permissions.Actions.KICKALL_BYPASS) }
            .forEach { it.createConnectionRequest(lobbyServer).fireAndForget() }

        sender.sendMessage(Message.KICKALL_USE, TextReplacement("server", serverName))
    }

    override fun suggest(invocation: SimpleCommand.Invocation): List<String> {
        val lastArg = invocation.arguments().lastOrNull()?.lowercase() ?: ""
        val servers = VRuom.server.allServers.map { it.serverInfo.name }
        return (servers + "all").filter { it.lowercase().startsWith(lastArg) }.sorted()
    }

    override fun suggestAsync(invocation: SimpleCommand.Invocation): CompletableFuture<List<String>> {
        return CompletableFuture.completedFuture(suggest(invocation))
    }
}