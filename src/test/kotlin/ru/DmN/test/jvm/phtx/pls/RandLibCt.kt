package ru.DmN.test.jvm.phtx.pls

import ru.DmN.test.jvm.TestModule
import kotlin.test.assertTrue

class RandLibCt : TestModule("test/phtx/pls/jvm/rand-lib-ct") {
    override fun compileTest() {
        compile()
        assertTrue(Regex("\\d+\n").containsMatchIn(test()))
    }
}