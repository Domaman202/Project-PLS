package ru.DmN.phtx.pls.utils.convertor

import com.kingmang.lazurite.parser.ast.statements.BlockStatement
import com.kingmang.lazurite.parser.ast.statements.FunctionDefineStatement
import com.kingmang.lazurite.parser.ast.statements.Statement
import ru.DmN.phtx.pls.utils.convertor.Convertor.convertClass
import ru.DmN.phtx.pls.utils.convertor.Convertor.convertMethod
import ru.DmN.phtx.pls.utils.node.nodeLzrDefn
import ru.DmN.phtx.pls.utils.node.nodePrognB
import ru.DmN.siberia.ast.Node
import ru.DmN.siberia.utils.mapMutable
import ru.DmN.siberia.utils.node.INodeInfo

object ConvertClassStmt {
    // Конвертация тела класса
    fun convertClassStmt(stmt: Statement, info: INodeInfo, ctx: ConvertContext): Node =
        when (stmt) {
            is BlockStatement           -> convertBlockStmt(stmt, info, ctx)
            is FunctionDefineStatement  -> convertFunctionDefineStmt(stmt, info, ctx)
            else -> throw UnsupportedOperationException(stmt.javaClass.name)
        }

    fun convertBlockStmt(stmt: BlockStatement, info: INodeInfo, ctx: ConvertContext): Node =
        nodePrognB(info, stmt.statements.mapMutable { convertClass(it, info, ctx) })

    fun convertFunctionDefineStmt(stmt: FunctionDefineStatement, info: INodeInfo, ctx: ConvertContext): Node =
        nodeLzrDefn(info, stmt.name, stmt.arguments.map { it.name }, mutableListOf(convertMethod(stmt.body, info, ctx)))
}