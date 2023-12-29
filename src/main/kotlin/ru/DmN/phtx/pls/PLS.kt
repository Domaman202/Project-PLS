package ru.DmN.phtx.pls

import ru.DmN.phtx.pls.parsers.NPIncLzr
import ru.DmN.phtx.pls.parsers.NPLzr
import ru.DmN.siberia.parsers.INodeParser
import ru.DmN.siberia.utils.Module

// Pihta Lazurite Sublanguage
object PLS : Module("phtx/pls") {
    override fun initParsers() {
        addNP("inc-lzr", NPIncLzr)
        addNP("lzr",     NPLzr)
    }

    private fun addNP(pattern: String, parser: INodeParser) {
        add(pattern.toRegularExpr(), parser)
    }
}