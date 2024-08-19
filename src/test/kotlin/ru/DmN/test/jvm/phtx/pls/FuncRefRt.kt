package ru.DmN.test.jvm.phtx.pls

import ru.DmN.test.jvm.TestModule
import kotlin.test.assertEquals

class FuncRefRt : TestModule("test/phtx/pls/jvm/func-ref-rt") {
    override fun compileTest() {
        compile()
        assertEquals(test(), "Simple Reference!\n")
    }
}