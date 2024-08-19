package ru.DmN.test.jvm.phtx.pls

import ru.DmN.test.jvm.TestModule
import kotlin.test.assertEquals

class FuncRefCt : TestModule("test/phtx/pls/jvm/func-ref-ct") {
    override fun compileTest() {
        compile()
        assertEquals(test(), "Simple Reference!\n")
    }
}