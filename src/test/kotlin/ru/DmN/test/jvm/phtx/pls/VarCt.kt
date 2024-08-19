package ru.DmN.test.jvm.phtx.pls

import ru.DmN.test.jvm.TestModule
import kotlin.test.assertEquals

class VarCt : TestModule("test/phtx/pls/jvm/var-ct") {
    override fun compileTest() {
        compile()
        assertEquals(test(), "12.0\n21.33\n")
    }
}