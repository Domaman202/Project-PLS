package ru.DmN.phtx.pls.unparsers

import ru.DmN.phtx.pls.ast.NodeLzrGetVar
import ru.DmN.siberia.unparser.Unparser
import ru.DmN.siberia.unparser.ctx.UnparsingContext
import ru.DmN.siberia.unparsers.INodeUnparser
import ru.DmN.siberia.utils.operation

object NULzrGetVar : INodeUnparser<NodeLzrGetVar> {
    override fun unparse(node: NodeLzrGetVar, unparser: Unparser, ctx: UnparsingContext, indent: Int) {
        unparser.out.append('(').append(node.operation).append(' ').append(node.name).append(')')
    }
}