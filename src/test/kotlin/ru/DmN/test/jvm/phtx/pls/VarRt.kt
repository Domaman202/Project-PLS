package ru.DmN.test.jvm.phtx.pls

import ru.DmN.test.jvm.TestModule
import kotlin.test.assertEquals

class VarRt : TestModule("test/phtx/pls/jvm/var-rt") {
    override fun compileTest() {
        compile()
        assertEquals(test(), "12.0\n21.33\n")
    }
}