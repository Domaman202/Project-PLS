package ru.DmN.phtx.pls.parsers

import com.kingmang.lazurite.parser.pars.FunctionAdder
import ru.DmN.phtx.pls.node.NodeTypes
import ru.DmN.phtx.pls.utils.Lexer
import ru.DmN.phtx.pls.utils.convert
import ru.DmN.siberia.Parser
import ru.DmN.siberia.ast.Node
import ru.DmN.siberia.lexer.Token
import ru.DmN.siberia.node.INodeInfo
import ru.DmN.siberia.parser.ctx.ParsingContext
import ru.DmN.siberia.parsers.INodeParser

object NPLzr : INodeParser {
    override fun parse(parser: Parser, ctx: ParsingContext, token: Token): Node {
        val lexer = Lexer(parser.lexer.input, parser.lexer.ptr)
        val lparser = com.kingmang.lazurite.parser.pars.Parser(lexer.tokenize())
        parser.lexer.ptr = lexer.pos
        val stmt = lparser.parse()
        if (lparser.parseErrors.hasErrors())
            throw RuntimeException(lparser.parseErrors.toString())
        stmt.accept(FunctionAdder())
        return convert(INodeInfo.of(NodeTypes.LZR, ctx, token), stmt)
    }
}