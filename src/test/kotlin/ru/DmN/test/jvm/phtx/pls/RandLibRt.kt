package ru.DmN.test.jvm.phtx.pls

import ru.DmN.test.jvm.TestModule
import kotlin.test.assertTrue

class RandLibRt : TestModule("test/phtx/pls/jvm/rand-lib-rt") {
    override fun compileTest() {
        compile()
        assertTrue(Regex("\\d+\n").containsMatchIn(test()))
    }
}