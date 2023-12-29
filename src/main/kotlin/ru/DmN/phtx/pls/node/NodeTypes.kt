package ru.DmN.phtx.pls.node

import ru.DmN.siberia.node.INodeType

enum class NodeTypes(override val operation: String) : INodeType {
    INC_LZR("inc-lzr"),
    LZR("lzr");

    override val processable: Boolean
        get() = false
    override val compilable: Boolean
        get() = false
}