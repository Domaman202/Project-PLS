package ru.DmN.test.jvm.phtx.pls

import ru.DmN.test.jvm.TestModule
import kotlin.test.assertEquals

class FuncRefCastCt : TestModule("test/phtx/pls/jvm/func-ref-cast-ct") {
    override fun compileTest() {
        compile()
        assertEquals(test(), "Casted Reference!\n")
    }
}