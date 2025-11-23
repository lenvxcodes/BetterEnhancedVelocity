package ir.syrent.enhancedvelocity.command

import com.velocitypowered.api.command.SimpleCommand
import com.velocitypowered.api.proxy.Player
import ir.syrent.enhancedvelocity.storage.Message
import ir.syrent.enhancedvelocity.storage.Settings
import ir.syrent.enhancedvelocity.utils.TextReplacement
import ir.syrent.enhancedvelocity.utils.sendMessage
import ir.syrent.enhancedvelocity.vruom.VRuom
import java.util.concurrent.CompletableFuture

class MoveCommand : SimpleCommand {

    init {
        VRuom.registerCommand(this, Settings.moveCommand, *Settings.moveAliases.toTypedArray())
    }

    override fun execute(invocation: SimpleCommand.Invocation) {
        val sender = invocation.source()
        val args = invocation.arguments()

        if (!sender.hasPermission(Permissions.Commands.MOVE)) {
            sender.sendMessage(Message.NO_PERMISSION, TextReplacement("permission", Permissions.Commands.MOVE))
            return
        }

        if (args.size < 2) {
            sender.sendMessage(Message.MOVE_USAGE)
            return
        }

        val targetSelector = args[0].lowercase()
        val targetServerName = args[1].lowercase()

        val destinationServer = VRuom.server.getServer(targetServerName).orElse(null)
        if (destinationServer == null) {
            sender.sendMessage(Message.SERVER_NOT_FOUND, TextReplacement("server", targetServerName))
            return
        }

        val playersToMove: List<Player> = when {
            targetSelector == "all" -> {
                if (!sender.hasPermission(Permissions.Actions.MOVE_ALL)) {
                    sender.sendMessage(
                        Message.NO_PERMISSION,
                        TextReplacement("permission", Permissions.Actions.MOVE_ALL)
                    )
                    return
                }
                VRuom.onlinePlayers.toList()
            }

            targetSelector.startsWith("server:") -> {
                if (!sender.hasPermission(Permissions.Actions.MOVE_SERVER)) {
                    sender.sendMessage(
                        Message.NO_PERMISSION,
                        TextReplacement("permission", Permissions.Actions.MOVE_SERVER)
                    )
                    return
                }
                val sourceServerName = targetSelector.substringAfter("server:")
                val sourceServer = VRuom.server.getServer(sourceServerName).orElse(null)
                if (sourceServer == null) {
                    sender.sendMessage(Message.SERVER_NOT_FOUND, TextReplacement("server", sourceServerName))
                    return
                }
                sourceServer.playersConnected.toList()
            }

            else -> {
                if (!sender.hasPermission(Permissions.Actions.MOVE_PLAYER)) {
                    sender.sendMessage(
                        Message.NO_PERMISSION,
                        TextReplacement("permission", Permissions.Actions.MOVE_PLAYER)
                    )
                    return
                }
                val player = VRuom.getPlayer(targetSelector)
                if (player == null) {
                    sender.sendMessage(Message.PLAYER_NOT_FOUND, TextReplacement("player", targetSelector))
                    return
                }
                listOf(player)
            }
        }

        if (playersToMove.isEmpty()) {
            sender.sendMessage(Message.MOVE_NO_PLAYERS_FOUND)
            return
        }

        var movedCount = 0
        var alreadyOnServerCount = 0
        var failedCount = 0

        for (player in playersToMove) {
            if (player.currentServer.isPresent && player.currentServer.get().serverInfo.name.equals(
                    destinationServer.serverInfo.name,
                    ignoreCase = true
                )
            ) {
                alreadyOnServerCount++
                continue
            }
            player.createConnectionRequest(destinationServer).connect().thenAccept { result ->
                if (result.isSuccessful) {
                    movedCount++
                } else {
                    failedCount++
                }
            }
        }

        sender.sendMessage(
            Message.MOVE_SUCCESS,
            TextReplacement("count", movedCount.toString()),
            TextReplacement("destination", destinationServer.serverInfo.name)
        )
        if (alreadyOnServerCount > 0) {
            sender.sendMessage(
                Message.MOVE_ALREADY_ON_SERVER,
                TextReplacement("count", alreadyOnServerCount.toString()),
                TextReplacement("server", destinationServer.serverInfo.name)
            )
        }
        if (failedCount > 0) {
            sender.sendMessage(
                Message.MOVE_FAILED,
                TextReplacement("count", failedCount.toString())
            )
        }
    }

    override fun suggest(invocation: SimpleCommand.Invocation): List<String> {
        val args = invocation.arguments()
        val lastArg = args.lastOrNull()?.lowercase() ?: ""

        return when (args.size) {
            1 -> { // Suggest player selectors or server names
                val playerSelectors = mutableListOf("all")
                VRuom.server.allServers.mapTo(playerSelectors) { "server:${it.serverInfo.name}" }
                VRuom.onlinePlayers.mapTo(playerSelectors) { it.username }
                playerSelectors.filter { it.lowercase().startsWith(lastArg) }.sorted()
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