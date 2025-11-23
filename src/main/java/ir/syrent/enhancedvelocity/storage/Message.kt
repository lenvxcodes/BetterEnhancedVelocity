package ir.syrent.enhancedvelocity.storage

import ir.syrent.enhancedvelocity.utils.TextReplacement

enum class Message(val path: String) {
    RAW_PREFIX("general.raw_prefix"),
    PREFIX("general.prefix"),
    CONSOLE_PREFIX("general.console_prefix"),
    SUCCESSFUL_PREFIX("general.successful_prefix"),
    WARN_PREFIX("general.warn_prefix"),
    ERROR_PREFIX("general.error_prefix"),
    ONLY_PLAYERS("general.only_players"),
    VALID_PARAMS("general.valid_parameters"),
    PLAYER_NOT_FOUND("general.player_not_found"),
    SERVER_NOT_FOUND("general.server_not_found"),
    NO_PERMISSION("command.no_permission"),
    GLOBALLIST_HEADER("features.global_list.header"),
    NO_ONE_PLAYING("features.global_list.no_one_playing"),
    GLOBALLIST_SERVER("features.global_list.server"),
    FIND_USAGE("features.find.command.usage"),
    SEND_USAGE("features.send.command.usage"),
    SEND_USE("features.send.command.use"),
    ALERT_USAGE("features.alert.command.usage"),
    ALERT_USE("features.alert.command.use"),
    FIND_USE("features.find.command.use"),
    FIND_VANISHED("features.find.command.vanished"),
    FIND_NO_SERVER("features.find.command.no_target"),
    FIND_NO_TARGET("features.find.command.no_target"),
    PING_USE("features.ping.command.use"),
    PING_NO_TARGET("features.ping.command.no_target"),
    PING_USE_TARGET("features.ping.command.use_target"),
    KICKALL_USAGE("features.kickall.command.usage"),
    KICKALL_USE("features.kickall.command.use"),
    KICKALL_NO_SERVER("features.kickall.command.no_server"),
    MOVE_USAGE("features.move.command.usage"), // New message
    MOVE_SUCCESS("features.move.command.success"), // New message
    MOVE_NO_PLAYERS_FOUND("features.move.command.no_players_found"), // New message
    MOVE_ALREADY_ON_SERVER("features.move.command.already_on_server"), // New message
    MOVE_FAILED("features.move.command.failed"), // New message
    EMPTY("");

    fun get(vararg replacements: TextReplacement): String {
        return Settings.formatMessage(this, *replacements)
    }

    override fun toString(): String {
        return get()
    }
}