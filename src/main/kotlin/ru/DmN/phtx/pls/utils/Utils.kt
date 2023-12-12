package ru.DmN.phtx.pls.utils

import com.kingmang.lazurite.parser.ast.*
import ru.DmN.pht.std.ast.NodeModifierNodesList
import ru.DmN.pht.std.processor.utils.*
import ru.DmN.siberia.ast.Node
import ru.DmN.siberia.ast.NodeNodesList
import ru.DmN.siberia.lexer.Token
import ru.DmN.siberia.processor.utils.nodeProgn
import com.kingmang.lazurite.parser.ast.Node as LNode

fun nodePrognB(line: Int, nodes: MutableList<Node>) =
    NodeModifierNodesList(Token.operation(line, "progn-"), nodes)

fun convert(line: Int, stmt: LNode): Node =
    when (stmt) {
        is MStatement -> nodePrognB(line, stmt.statements.asSequence().map { convert(line, it) }.toMutableList())
        is ExprStatement -> convert(line, stmt.expr)
        is PrintStatement -> nodePrintln(line, convert(line, stmt.expression))
        is ValueExpression -> when (stmt.value.type()) {
            1 -> nodeValue(line, stmt.value.asInt())
            2 -> nodeValue(line, stmt.value.asString())
            else -> throw UnsupportedOperationException()
        }

        is BinaryExpression -> NodeNodesList(
            Token.operation(
                line, when (stmt.operation.toString()) {
                    "+" -> "add"
                    "-" -> "sub"
                    "*" -> "mul"
                    "/" -> "div"
                    "%" -> "rem"
                    "&" -> "and"
                    "|" -> "or"
                    "^" -> "xor"
                    "<< " -> "shift-left"
                    ">>" -> "shift-right"
                    else -> throw UnsupportedOperationException()
                }
            ),
            mutableListOf(convert(line, stmt.expr1), convert(line, stmt.expr2))
        )

        is AssignmentExpression -> nodeDefSet(line, convert(line, stmt.target), convert(line, stmt.expression))
        is VariableExpression -> when (stmt.name) {
            "true" -> nodeValue(line, true)
            "false" -> nodeValue(line, false)
            else -> nodeGetOrName(line, stmt.name)
        }

        is FunctionDefineStatement -> nodeDefn(
            line,
            stmt.name,
            "dynamic",
            stmt.arguments.map {
                if (it.valueExpr == null) Pair(it.name, "dynamic") else Pair(
                    it.name,
                    (it.valueExpr as VariableExpression).name
                )
            },
            mutableListOf(convert(line, stmt.body))
        )

        is ReturnStatement -> nodeRet(line, convert(line, stmt.expression))
        is FunctionalExpression -> nodeMCall(
            line,
            nodeGetOrName(line, "."),
            (stmt.functionExpr as VariableExpression).name,
            stmt.arguments.map { convert(line, it) }
        )

        is IfStatement -> nodeIf(
            line,
            sequenceOf(stmt.expression, stmt.ifStatement, stmt.elseStatement)
                .filterNotNull()
                .map { convert(line, it) }
                .toMutableList()
        )

        is WhileStatement -> nodeCycle(line, convert(line, stmt.condition), convert(line, stmt.statement))
        is ConditionalExpression -> NodeNodesList(
            Token.operation(
                line,
                when (stmt.operation) {
                    ConditionalExpression.Operator.EQUALS -> "eq"
                    ConditionalExpression.Operator.NOT_EQUALS -> "not-eq"
                    ConditionalExpression.Operator.LT -> "less"
                    ConditionalExpression.Operator.LTEQ -> "less-or-eq"
                    ConditionalExpression.Operator.GT -> "great"
                    ConditionalExpression.Operator.GTEQ -> "great-or-eq"
                    else -> throw UnsupportedOperationException()
                }
            ),
            mutableListOf(convert(line, stmt.expr1), convert(line, stmt.expr2))
        )

        is UnaryExpression -> NodeNodesList(
            Token.operation(
                line,
                when (stmt.operation) {
                    UnaryExpression.Operator.INCREMENT_PREFIX -> "inc"
                    UnaryExpression.Operator.INCREMENT_POSTFIX -> "inc-"
                    UnaryExpression.Operator.DECREMENT_PREFIX -> "dec"
                    UnaryExpression.Operator.DECREMENT_POSTFIX -> "dec-"
                    else -> throw UnsupportedOperationException()
                }
            ),
            mutableListOf(nodeGetOrName(line, (stmt.expr1 as VariableExpression).name))
        )

        is ClassDeclarationStatement -> {
            val nodes = ArrayList<Node>()
            if (stmt.fields.isNotEmpty())
                nodes += nodeFld(
                    line,
                    stmt.fields.map {
                        Pair(
                            (it.target as VariableExpression).name,
                            (it.expression as VariableExpression).name
                        )
                    })
            stmt.methods.forEach {
                if (it.name == stmt.name) {
                    nodes += nodeCtor(line, listOf(nodeCCall(line), convert(line, it.body)))
                } else {
                    nodes += convert(line, it)
                }
            }
            nodeCls(line, stmt.name, listOf("Object"), nodes)
        }

        is ForeachAStatement -> nodeFor(line, stmt.variable, convert(line, stmt.container), convert(line, stmt.body))
        is ForStatement -> nodeProgn(
            line,
            mutableListOf(
                convert(line, stmt.initialization),
                nodeCycle(
                    line,
                    convert(line, stmt.termination),
                    mutableListOf(convert(line, stmt.statement), convert(line, stmt.increment))
                )
            )
        )

        else -> throw UnsupportedOperationException()
    }