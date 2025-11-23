package ir.syrent.enhancedvelocity.vruom

import com.velocitypowered.api.proxy.ProxyServer
import org.slf4j.Logger
import java.nio.file.Path

open class VRUoMPlugin(
    val server: ProxyServer,
    val logger: Logger,
    val dataDirectory: Path? = null
) {
    init {
        instance = this
    }

    companion object {
        lateinit var instance: VRUoMPlugin
            private set
    }
}