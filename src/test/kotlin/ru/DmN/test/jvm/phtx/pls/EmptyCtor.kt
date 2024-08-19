package ru.DmN.test.jvm.phtx.pls

import ru.DmN.test.jvm.TestModule
import kotlin.test.assertEquals

class EmptyCtor : TestModule("test/phtx/pls/jvm/empty-ctor") {
    override fun compileTest() {
        compile()
        assertEquals(test(), "Hi!\n")
    }
}