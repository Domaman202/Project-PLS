package ru.DmN.phtx.pls.utils

import com.kingmang.lazurite.parser.ast.*
import com.kingmang.lazurite.parser.ast.ConditionalExpression.Operator.*
import com.kingmang.lazurite.parser.ast.UnaryExpression.Operator.*
import ru.DmN.pht.std.ast.NodeFieldA
import ru.DmN.pht.std.ast.NodeModifierNodesList
import ru.DmN.pht.std.node.NodeParsedTypes.*
import ru.DmN.pht.std.node.NodeParsedTypes.AND
import ru.DmN.pht.std.node.NodeParsedTypes.OR
import ru.DmN.pht.std.node.NodeTypes.*
import ru.DmN.pht.std.processor.utils.*
import ru.DmN.siberia.ast.Node
import ru.DmN.siberia.ast.NodeNodesList
import ru.DmN.siberia.node.INodeInfo
import ru.DmN.siberia.node.NodeTypes.PROGN
import ru.DmN.siberia.processor.utils.nodeProgn
import com.kingmang.lazurite.parser.ast.Node as LNode

// c
fun nodeCycle(info: INodeInfo, cond: Node, body: Node) =
    NodeNodesList(info.withType(CYCLE), mutableListOf(cond, body))
fun nodeCCall(info: INodeInfo) =
    NodeNodesList(info.withType(CCALL))
fun nodeCtor(info: INodeInfo, nodes: List<Node>) =
    NodeNodesList(info.withType(CTOR), mutableListOf<Node>(nodeValn(info, mutableListOf())).apply { addAll(nodes) })
// d
fun nodeDefSet(info: INodeInfo, name: Node, value: Node) =
    NodeNodesList(info.withType(DEF_SET), mutableListOf(name, value))
// f
fun nodeFld(info: INodeInfo, args: List<Pair<String, String>>) =
    NodeFieldA(info.withType(FLD), mutableListOf(nodeValn(info, args.map { (nodeValn(info, mutableListOf(nodeGetOrName(info, it.first), nodeValueClass(info, it.second)))) }.toMutableList())))
fun nodeFor(info: INodeInfo, variable: String, iterable: Node, body: Node) =
    NodeNodesList(info.withType(FOR), mutableListOf(nodeValn(info, mutableListOf(nodeGetOrName(info, variable), iterable)), body))
// p
fun nodePrognB(info: INodeInfo, nodes: MutableList<Node>) =
    NodeModifierNodesList(info.withType(PROGN), nodes)
fun nodePrintln(info: INodeInfo, node: Node) =
    NodeNodesList(info.withType(PRINTLN), mutableListOf(node))
// r
fun nodeRet(info: INodeInfo, value: Node) =
    NodeNodesList(info.withType(RET), mutableListOf(value))

fun convert(info: INodeInfo, stmt: LNode): Node =
    when (stmt) {
        is MStatement -> nodePrognB(info, stmt.statements.asSequence().map { convert(info, it) }.toMutableList())
        is ExprStatement -> convert(info, stmt.expr)
        is PrintStatement -> nodePrintln(info, convert(info, stmt.expression))
        is ValueExpression -> when (stmt.value.type()) {
            1 -> nodeValue(info, stmt.value.asInt())
            2 -> nodeValue(info, stmt.value.asString())
            else -> throw UnsupportedOperationException()
        }

        is BinaryExpression -> NodeNodesList(
            info.withType(when (stmt.operation.toString()) {
                    "+" -> ADD
                    "-" -> SUB
                    "*" -> MUL
                    "/" -> DIV
                    "%" -> REM
                    "&" -> AND
                    "|" -> OR
                    "^" -> XOR
                    "<<" -> SHIFT_LEFT
                    ">>" -> SHIFT_RIGHT
                    else -> throw UnsupportedOperationException()
                }
            ),
            mutableListOf(convert(info, stmt.expr1), convert(info, stmt.expr2))
        )

        is AssignmentExpression -> nodeDefSet(info, convert(info, stmt.target), convert(info, stmt.expression))
        is VariableExpression -> when (stmt.name) {
            "true" -> nodeValue(info, true)
            "false" -> nodeValue(info, false)
            else -> nodeGetOrName(info, stmt.name)
        }

        is FunctionDefineStatement -> nodeDefn(
            info,
            stmt.name,
            "dynamic",
            stmt.arguments.map {
                if (it.valueExpr == null) Pair(it.name, "dynamic") else Pair(
                    it.name,
                    (it.valueExpr as VariableExpression).name
                )
            },
            mutableListOf(convert(info, stmt.body))
        )

        is ReturnStatement -> nodeRet(info, convert(info, stmt.expression))
        is FunctionalExpression -> nodeMCall(
            info,
            nodeGetOrName(info, "."),
            (stmt.functionExpr as VariableExpression).name,
            stmt.arguments.map { convert(info, it) }
        )

        is IfStatement -> nodeIf(
            info,
            sequenceOf(stmt.expression, stmt.ifStatement, stmt.elseStatement)
                .filterNotNull()
                .map { convert(info, it) }
                .toMutableList()
        )

        is WhileStatement -> nodeCycle(info, convert(info, stmt.condition), convert(info, stmt.statement))
        is ConditionalExpression -> NodeNodesList(
            info.withType(
                when (stmt.operation) {
                    EQUALS      -> EQ
                    NOT_EQUALS  -> NOT_EQ
                    LT          -> LESS
                    LTEQ        -> LESS_OR_EQ
                    GT          -> GREAT
                    GTEQ        -> GREAT_OR_EQ
                    else        -> throw UnsupportedOperationException()
                }
            ),
            mutableListOf(convert(info, stmt.expr1), convert(info, stmt.expr2))
        )

        is UnaryExpression -> NodeNodesList(
            info.withType(
                when (stmt.operation) {
                    INCREMENT_PREFIX  -> INC_PRE
                    INCREMENT_POSTFIX -> INC_POST
                    DECREMENT_PREFIX  -> DEC_PRE
                    DECREMENT_POSTFIX -> DEC_POST
                    else -> throw UnsupportedOperationException()
                }
            ),
            mutableListOf(nodeGetOrName(info, (stmt.expr1 as VariableExpression).name))
        )

        is ClassDeclarationStatement -> {
            val nodes = ArrayList<Node>()
            if (stmt.fields.isNotEmpty())
                nodes += nodeFld(
                    info,
                    stmt.fields.map {
                        Pair(
                            (it.target as VariableExpression).name,
                            (it.expression as VariableExpression).name
                        )
                    })
            stmt.methods.forEach {
                if (it.name == stmt.name) {
                    nodes += nodeCtor(info, listOf(nodeCCall(info), convert(info, it.body)))
                } else {
                    nodes += convert(info, it)
                }
            }
            nodeCls(info, stmt.name, listOf("Object"), nodes)
        }

        is ForeachAStatement -> nodeFor(info, stmt.variable, convert(info, stmt.container), convert(info, stmt.body))
        is ForStatement -> nodeProgn(
            info,
            mutableListOf(
                convert(info, stmt.initialization),
                nodeCycle(
                    info,
                    convert(info, stmt.termination),
                    mutableListOf(convert(info, stmt.statement), convert(info, stmt.increment))
                )
            )
        )

        else -> throw UnsupportedOperationException()
    }