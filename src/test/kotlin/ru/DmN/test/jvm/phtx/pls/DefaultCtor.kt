package ru.DmN.test.jvm.phtx.pls

import ru.DmN.test.jvm.TestModule
import kotlin.test.assertNotNull

class DefaultCtor : TestModule("test/phtx/pls/jvm/default-ctor") {
    override fun compileTest() {
        compile()
        assertNotNull(test())
    }
}