package ru.DmN.test.console.commands

import ru.DmN.siberia.console.Console
import ru.DmN.siberia.console.utils.Command
import ru.DmN.test.console.TestCommands.forEachTests
import java.io.File

object TestUpdateUnparse : Command(
    "test-update-unparse",
    "tuu",
    "Тест",
    "Обновление 'де-парсинга'",
    "Обновляет данные тестов 'де-парсинга'.",
    emptyList()
) {
    override fun available(console: Console): Boolean =
        true

    override fun builderAvailable(flags: Map<Any?, Any?>): Boolean =
        true

    override fun action(console: Console, vararg args: Any?) {
        println("Начало обновления тестовых данных 'де-парсинга'.")
        forEachTests { dir, path ->
            val dump = File("dump/$path/unparse")
            if (dump.exists()) {
                val dist = File(dir, "test/unparse")
                dist.deleteOnExit()
                dump.copyRecursively(dist, true)
                dist.listFiles()!!.forEach {
                    it.mkdirs()
                    File(it, "module.pht").writeText("""
                        (module
                            (name "$path/test/unparse/${it.name}")
                            (version "1.0.0")
                            (author "DomamaN202")
                            (deps ["pht"])
                            (src ["unparse.pht"]))
                    """.trimIndent())
                }
                val src = File("src/test/resources/${path}")
                if (src.isDirectory()) {
                    src.listFiles()!!
                        .filter { it.name.endsWith(".lzr") }
                        .forEach { it.copyRecursively(File("${dist}/parsed/${it.name}"), overwrite = true) }
                }
            }
        }
        println("Обновление тестовых данных 'де-парсинга' завершено.")
    }
}