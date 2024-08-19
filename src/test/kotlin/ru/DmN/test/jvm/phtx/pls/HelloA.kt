package ru.DmN.test.jvm.phtx.pls

import ru.DmN.test.jvm.TestModule
import kotlin.test.assertEquals

class HelloA : TestModule("test/phtx/pls/jvm/hello-a") {
    override fun compileTest() {
        compile()
        assertEquals(test(), "Hello, Pihta!\n")
    }
}