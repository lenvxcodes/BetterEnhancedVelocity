package ir.syrent.enhancedvelocity.storage

import ir.syrent.enhancedvelocity.core.ServerData
import ir.syrent.enhancedvelocity.utils.TextReplacement
import ir.syrent.enhancedvelocity.vruom.VRuom
import ir.syrent.enhancedvelocity.vruom.string.ResourceUtils
import org.spongepowered.configurate.CommentedConfigurationNode
import org.spongepowered.configurate.yaml.YamlConfigurationLoader
import java.io.File
import java.nio.file.Path

object Settings {

    private val messages = mutableMapOf<Message, String>()
    val servers = mutableMapOf<String, ServerData>()

    lateinit var defaultLanguage: String
    lateinit var globalListCommand: String
    lateinit var globalListAliases: List<String>
    var globalListMaxServers: Int = 9
    lateinit var progressComplete: String
    lateinit var progressNotComplete: String
    lateinit var playerVanishDecoration: String
    lateinit var serverVanishDecoration: String

    lateinit var findCommand: String
    lateinit var findAliases: List<String>

    lateinit var moveCommand: String // New property for move command
    lateinit var moveAliases: List<String> // New property for move command aliases

    lateinit var startupCommands: List<String>

    fun load() {
        val settingsConfig = YamlConfig("settings.yml")
        val settingsRoot = settingsConfig.load()

        defaultLanguage = settingsRoot.node("default_language").getString("en_US")

        val features = settingsRoot.node("features")
        val globalList = features.node("global_list")
        globalListCommand = globalList.node("command").getString("glist")
        globalListAliases = globalList.node("aliases").getList(String::class.java) ?: emptyList()
        globalListMaxServers = globalList.node("max-servers").getInt(9)

        val progress = globalList.node("progress")
        progressCount = progress.node("count").getInt(45)
        progressComplete = progress.node("complete").getString("■")
        progressNotComplete = progress.node("not_complete").getString("□")

        val vanish = globalList.node("vanish", "decoration")
        playerVanishDecoration = vanish.node("player").getString("&7[&fV&7] &f\$player")
        serverVanishDecoration = vanish.node("server").getString("&7[&fV&7] &f\$server")

        servers.clear()
        val serverSection = globalList.node("server")
        for ((key, value) in serverSection.childrenMap()) {
            val serverData = ServerData(
                value.node("displayname").string,
                value.node("sum").getList(String::class.java),
                value.node("hidden").getBoolean(false),
            )
            servers[key.toString()] = serverData
        }

        val find = features.node("find")
        findCommand = find.node("command").getString("find")
        findAliases = find.node("aliases").getList(String::class.java) ?: emptyList()

        val move = features.node("move") // Load move command settings
        moveCommand = move.node("command").getString("move")
        moveAliases = move.node("aliases").getList(String::class.java) ?: emptyList()

        startupCommands = settingsRoot.node("startup_commands").getList(String::class.java) ?: emptyList()

        val languageConfig = YamlConfig("languages/$defaultLanguage.yml")
        val languageRoot = languageConfig.load()

        messages.clear()
        for (message in Message.values()) {
            if (message == Message.EMPTY) {
                messages[message] = ""
                continue
            }
            messages[message] = languageRoot.node(message.path.split(".")).string ?: "Missing message: ${message.name}"
        }
    }

    fun formatMessage(message: String, vararg replacements: TextReplacement): String {
        var formattedMessage = message
            .replace("\$prefix", getMessage(Message.PREFIX))
            .replace("\$successful_prefix", getMessage(Message.SUCCESSFUL_PREFIX))
            .replace("\$warn_prefix", getMessage(Message.WARN_PREFIX))
            .replace("\$error_prefix", getMessage(Message.ERROR_PREFIX))
        for (replacement in replacements) {
            formattedMessage = formattedMessage.replace("\$${replacement.from}", replacement.to)
        }
        return formattedMessage
    }

    fun formatMessage(message: Message, vararg replacements: TextReplacement): String {
        return formatMessage(getMessage(message), *replacements)
    }

    fun formatMessage(messages: List<String>, vararg replacements: TextReplacement): List<String> {
        return messages.map { formatMessage(it, *replacements) }
    }

    private fun getMessage(message: Message): String {
        return messages[message] ?: "Unknown message ($message)"
    }

    private class YamlConfig(private val filePath: String) {
        private val file: File = VRuom.dataDirectory.resolve(filePath).toFile()
        private val loader: YamlConfigurationLoader = YamlConfigurationLoader.builder().file(file).build()

        fun load(): CommentedConfigurationNode {
            if (!file.exists()) {
                file.parentFile.mkdirs()
                ResourceUtils.copyResource(filePath, file)
            }
            return loader.load()
        }
    }
}