package ru.DmN.phtx.pls.parsers

import ru.DmN.phtx.pls.ast.NodeLzrGetVar
import ru.DmN.phtx.pls.utils.node.NodeTypes.LZR_GET_VAR
import ru.DmN.siberia.ast.Node
import ru.DmN.siberia.lexer.Token
import ru.DmN.siberia.parser.Parser
import ru.DmN.siberia.parser.ctx.ParsingContext
import ru.DmN.siberia.parsers.INodeParser
import ru.DmN.siberia.utils.node.INodeInfo

object NPLzrGetVar : INodeParser {
    override fun parse(parser: Parser, ctx: ParsingContext, token: Token): Node =
        NodeLzrGetVar(INodeInfo.of(LZR_GET_VAR, ctx, token), parser.nextToken()!!.text!!)
}