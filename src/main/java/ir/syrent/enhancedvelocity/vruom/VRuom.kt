package ir.syrent.enhancedvelocity.vruom

import com.velocitypowered.api.command.Command
import com.velocitypowered.api.proxy.Player
import java.util.concurrent.TimeUnit

object VRuom {

    private val plugin by lazy { VRUoMPlugin.instance }
    private val server by lazy { plugin.server }
    private val logger by lazy { plugin.logger }
    private var debug = false

    fun registerCommand(command: Command, name: String, vararg aliases: String) {
        val meta = server.commandManager.metaBuilder(name).aliases(*aliases).build()
        server.commandManager.register(meta, command)
    }

    val onlinePlayers: Collection<Player>
        get() = server.allPlayers

    fun getPlayer(username: String): Player? =
        server.getPlayer(username).orElse(null)

    fun setDebug(debug: Boolean) {
        this.debug = debug
    }

    fun log(message: String) {
        logger.info(message)
    }

    fun debug(message: String) {
        if (debug) {
            log("[Debug] $message")
        }
    }

    fun warn(message: String) {
        logger.warn(message)
    }

    fun error(message: String) {
        logger.error(message)
    }

    fun run(runnable: Runnable) {
        try {
            runnable.run()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun runAsync(runnable: Runnable) =
        server.scheduler.buildTask(plugin, runnable).schedule()

    fun runAsync(runnable: Runnable, delay: Long, delayUnit: TimeUnit) =
        server.scheduler.buildTask(plugin, runnable).delay(delay, delayUnit).schedule()

    fun runAsync(
        runnable: Runnable,
        delay: Long,
        delayUnit: TimeUnit,
        period: Long,
        periodUnit: TimeUnit
    ) = server.scheduler.buildTask(plugin, runnable)
        .delay(delay, delayUnit)
        .repeat(period, periodUnit)
        .schedule()
}