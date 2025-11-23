package ir.syrent.enhancedvelocity.command

import com.velocitypowered.api.command.SimpleCommand
import com.velocitypowered.api.proxy.Player
import ir.syrent.enhancedvelocity.api.VanishManager
import ir.syrent.enhancedvelocity.storage.Message
import ir.syrent.enhancedvelocity.storage.Settings
import ir.syrent.enhancedvelocity.utils.TextReplacement
import ir.syrent.enhancedvelocity.utils.sendMessage
import ir.syrent.enhancedvelocity.vruom.VRuom
import ir.syrent.enhancedvelocity.vruom.string.ProgressBar

class GListCommand : SimpleCommand {

    init {
        VRuom.registerCommand(this, Settings.globalListCommand, *Settings.globalListAliases.toTypedArray())
    }

    override fun execute(invocation: SimpleCommand.Invocation) {
        val sender = invocation.source()

        if (!sender.hasPermission(Permissions.Commands.GLIST)) {
            sender.sendMessage(Message.NO_PERMISSION, TextReplacement("permission", Permissions.Commands.GLIST))
            return
        }

        val canSeeVanished = sender.hasPermission(Permissions.Actions.SEE_VANISHED)
        val onlinePlayers = if (canSeeVanished) VRuom.onlinePlayers else VanishManager.nonVanishedPlayers

        sender.sendMessage(Message.GLOBALLIST_HEADER, TextReplacement("count", onlinePlayers.size.toString()))

        val serverPlayerCounts = VRuom.server.allServers
            .map { server ->
                val players = server.playersConnected.filter { canSeeVanished || !VanishManager.isVanished(it.uniqueId) }
                server to players
            }
            .filterNot { (server, _) -> Settings.servers[server.serverInfo.name]?.hidden == true }
            .sortedByDescending { (_, players) -> players.size }
            .take(Settings.globalListMaxServers)

        for ((server, players) in serverPlayerCounts) {
            val serverName = Settings.servers[server.serverInfo.name]?.displayname ?: server.serverInfo.name
            val progress = ProgressBar.progressBar(
                players.size,
                onlinePlayers.size,
                Settings.progressCount,
                Settings.progressComplete,
                Settings.progressNotComplete
            )
            val playersString = if (players.isEmpty()) {
                Settings.formatMessage(Message.NO_ONE_PLAYING)
            } else {
                formatPlayerList(players, canSeeVanished)
            }

            sender.sendMessage(
                Message.GLOBALLIST_SERVER,
                TextReplacement("players", playersString),
                TextReplacement("progress", progress),
                TextReplacement("count", players.size.toString()),
                TextReplacement("server", serverName)
            )
        }
    }

    private fun formatPlayerList(players: Collection<Player>, canSeeVanished: Boolean): String {
        return players.joinToString(", ") { player ->
            if (canSeeVanished && VanishManager.isVanished(player.uniqueId)) {
                Settings.formatMessage(Settings.playerVanishDecoration.replace("\$player", player.username))
            } else {
                player.username
            }
        }
    }
}