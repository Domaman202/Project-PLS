package ru.DmN.phtx.pls.unparsers

import ru.DmN.pht.utils.vtype.nameWithGenerics
import ru.DmN.phtx.pls.ast.NodeLzrCast
import ru.DmN.siberia.unparser.Unparser
import ru.DmN.siberia.unparser.ctx.UnparsingContext
import ru.DmN.siberia.unparsers.INodeUnparser
import ru.DmN.siberia.utils.operation

object NULzrCast : INodeUnparser<NodeLzrCast> {
    override fun unparse(node: NodeLzrCast, unparser: Unparser, ctx: UnparsingContext, indent: Int) {
        unparser.out.apply {
            append('(').append(node.operation).append(' ').append(node.type.nameWithGenerics).append('\n')
            append("\t".repeat(indent + 1))
            unparser.unparse(node.value, ctx, indent + 1)
            append(')')
        }
    }
}