package ir.syrent.enhancedvelocity.command

class Permissions {

    object Commands {
        const val GLIST = "enhancedvelocity.commands.glist"
        const val FIND = "enhancedvelocity.commands.find"
        const val SEND = "enhancedvelocity.commands.send"
        const val ALERT = "enhancedvelocity.commands.alert"
        const val PING = "enhancedvelocity.commands.ping"
        const val KICKALL = "enhancedvelocity.commands.kickall"
        const val MOVE = "enhancedvelocity.commands.move" // New permission for the move command
    }

    object Actions {
        const val SEE_VANISHED = "enhancedvelocity.actions.seevanished"
        const val FIND_VANISHED = "enhancedvelocity.actions.findvanished"
        const val KICKALL_BYPASS = "enhancedvelocity.actions.kickall.bypass"
        const val MOVE_ALL = "enhancedvelocity.actions.move.all" // New permission for moving all players
        const val MOVE_SERVER = "enhancedvelocity.actions.move.server" // New permission for moving players from a server
        const val MOVE_PLAYER = "enhancedvelocity.actions.move.player" // New permission for moving a specific player
    }
}