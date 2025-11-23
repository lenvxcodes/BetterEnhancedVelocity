package ir.syrent.enhancedvelocity.utils

import com.velocitypowered.api.command.CommandSource
import ir.syrent.enhancedvelocity.storage.Message
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.minimessage.MiniMessage
import net.kyori.adventure.text.minimessage.tag.resolver.TagResolver
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer

fun CommandSource.sendMessage(message: Message, vararg replacements: TextReplacement) {
    this.sendMessage(message.get(*replacements))
}

fun CommandSource.sendMessage(message: String) {
    this.sendMessage(message.component())
}

fun String.component(vararg tags: TagResolver): Component {
    return MiniMessage.miniMessage().deserialize(this, *tags)
}

fun String.legacyComponent(): Component {
    return LegacyComponentSerializer.legacy('&').deserialize(this)
}