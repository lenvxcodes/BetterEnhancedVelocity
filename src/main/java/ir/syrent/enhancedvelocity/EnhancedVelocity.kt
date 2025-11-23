package ir.syrent.enhancedvelocity

import com.google.inject.Inject
import com.velocitypowered.api.event.Subscribe
import com.velocitypowered.api.event.proxy.ProxyInitializeEvent
import com.velocitypowered.api.plugin.annotation.DataDirectory
import com.velocitypowered.api.proxy.ProxyServer
import ir.syrent.enhancedvelocity.command.*
import ir.syrent.enhancedvelocity.storage.Settings
import ir.syrent.enhancedvelocity.vruom.VRuom
import org.bstats.velocity.Metrics
import org.slf4j.Logger
import java.nio.file.Path

class EnhancedVelocity @Inject constructor(
    private val server: ProxyServer,
    private val logger: Logger,
    private val metricsFactory: Metrics.Factory,
    @DataDirectory private val dataDirectory: Path
) {

    @Subscribe
    fun onProxyInitialization(event: ProxyInitializeEvent) {
        VRuom.initialize(this, this.server, this.logger, this.dataDirectory)
        Settings.load()

        enableMetrics()
        registerCommands()
        executeStartupCommands()
    }

    private fun enableMetrics() {
        val pluginID = 16753
        metricsFactory.make(this, pluginID)
    }

    private fun registerCommands() {
        GListCommand()
        FindCommand()
        SendCommand()
        AlertCommand()
        PingCommand()
        KickAllCommand()
        MoveCommand()
    }

    private fun executeStartupCommands() {
        for (command in Settings.startupCommands) {
            VRuom.server.commandManager.executeAsync(VRuom.server.consoleCommandSource, command)
            logger.info("Executed startup command: /$command")
        }
    }
}