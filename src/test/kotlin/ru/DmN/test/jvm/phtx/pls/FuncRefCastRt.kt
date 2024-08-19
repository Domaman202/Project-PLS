package ru.DmN.test.jvm.phtx.pls

import ru.DmN.test.jvm.TestModule
import kotlin.test.assertEquals

class FuncRefCastRt : TestModule("test/phtx/pls/jvm/func-ref-cast-rt") {
    override fun compileTest() {
        compile()
        assertEquals(test(), "Casted Reference!\n")
    }
}