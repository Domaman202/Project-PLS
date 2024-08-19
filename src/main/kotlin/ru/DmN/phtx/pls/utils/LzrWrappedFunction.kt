package ru.DmN.phtx.pls.utils

import com.kingmang.lazurite.core.Function
import com.kingmang.lazurite.runtime.values.LzrValue
import ru.DmN.phtx.pls.utils.LzrObjectUtils.castToLzrValue
import java.lang.reflect.Method

class LzrWrappedFunction(val instance: Any?, val method: Method) : Function {
    override fun execute(vararg args: LzrValue): LzrValue =
        castToLzrValue(method.invoke(instance, *args))
}