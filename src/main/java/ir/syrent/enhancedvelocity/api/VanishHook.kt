package ir.syrent.enhancedvelocity.api

import com.velocitypowered.api.proxy.Player
import com.velocitypowered.api.proxy.server.RegisteredServer
import ir.syrent.enhancedvelocity.vruom.VRuom
import java.util.UUID

interface VanishHook {
    fun isVanished(uuid: UUID): Boolean
    fun vanish(uuid: UUID)
    fun unvanish(uuid: UUID)
}

object VanishManager {
    private val hooks = mutableListOf<VanishHook>()

    fun register(hook: VanishHook) {
        hooks.add(hook)
    }

    fun unregister(hook: VanishHook) {
        hooks.remove(hook)
    }

    fun isVanished(uuid: UUID): Boolean {
        return hooks.any { it.isVanished(uuid) }
    }

    fun hasVanishedPlayer(server: RegisteredServer): Boolean {
        return server.playersConnected.any { isVanished(it.uniqueId) }
    }

    val vanishedPlayers: List<Player>
        get() = VRuom.onlinePlayers.filter { isVanished(it.uniqueId) }

    val nonVanishedPlayers: List<Player>
        get() = VRuom.onlinePlayers.filterNot { isVanished(it.uniqueId) }

    fun getNonVanishedPlayers(server: RegisteredServer): List<Player> {
        return server.playersConnected.filterNot { isVanished(it.uniqueId) }
    }
}