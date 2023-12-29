package ru.DmN.phtx.pls.parsers

import com.kingmang.lazurite.parser.pars.FunctionAdder
import com.kingmang.lazurite.parser.pars.Lexer
import ru.DmN.phtx.pls.node.NodeTypes
import ru.DmN.phtx.pls.utils.convert
import ru.DmN.phtx.pls.utils.nodePrognB
import ru.DmN.siberia.Parser
import ru.DmN.siberia.ast.Node
import ru.DmN.siberia.lexer.Token
import ru.DmN.siberia.lexer.isOperation
import ru.DmN.siberia.node.INodeInfo
import ru.DmN.siberia.parser.ctx.ParsingContext
import ru.DmN.siberia.parsers.INodeParser
import ru.DmN.siberia.processor.utils.module

object NPIncLzr : INodeParser {
    override fun parse(parser: Parser, ctx: ParsingContext, token: Token): Node? {
        val module = ctx.module
        val info = INodeInfo.of(NodeTypes.INC_LZR, ctx, token)
        val nodes = parseFileNames(parser)
            .asSequence()
            .map { module.getModuleFile(it) }
            .map { Lexer.tokenize(it) }
            .map { com.kingmang.lazurite.parser.pars.Parser(it) }
            .map { it.parse().apply { if (it.parseErrors.hasErrors()) throw RuntimeException(it.parseErrors.toString()) } }
            .map { it.apply { accept(FunctionAdder()) } }
            .map { convert(info, it) }
            .toMutableList()
        return nodePrognB(info, nodes)
    }

    private fun parseFileNames(parser: Parser): List<String> {
        val names = ArrayList<String>()
        var tk = parser.nextToken()!!
        while (tk.isOperation()) {
            names += tk.text!!
            tk = parser.nextToken()!!
        }
        parser.tokens.push(tk)
        return names
    }
}