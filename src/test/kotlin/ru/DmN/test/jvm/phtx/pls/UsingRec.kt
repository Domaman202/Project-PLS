package ru.DmN.test.jvm.phtx.pls

import ru.DmN.test.jvm.TestModule
import kotlin.test.assertEquals

class UsingRec : TestModule("test/phtx/pls/jvm/using-rec") {
    override fun compileTest() {
        compile()
        assertEquals(test(), "A!\nB!\nC!\n")
    }
}