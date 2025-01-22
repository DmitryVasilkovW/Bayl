package org.bayl.ast.expression.function;

import java.util.List;
import org.bayl.ast.Node;
import org.bayl.ast.expression.variable.VariableNode;
import org.bayl.bytecode.impl.Bytecode;
import static org.bayl.model.BytecodeToken.ARG;
import static org.bayl.model.BytecodeToken.BODY;
import static org.bayl.model.BytecodeToken.TAIL_FUNCTION;
import org.bayl.model.SourcePosition;

public class TailRecursionNode extends Node {

    private final List<Node> parameters;
    private final Node body;

    public TailRecursionNode(SourcePosition pos, List<Node> parameters, Node body) {
        super(pos);
        this.parameters = parameters;
        this.body = body;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("(recursion (");
        boolean first = true;
        for (Node node : parameters) {
            if (first) {
                first = false;
            } else {
                sb.append(' ');
            }
            sb.append(node);
        }
        sb.append(") ");
        sb.append(body);
        sb.append(')');
        return sb.toString();
    }

    @Override
    public void generateCode(Bytecode bytecode) {
        bytecode.add(getBytecodeLineWithPosition(
                TAIL_FUNCTION.toString()
        ));

        for (Node argument : parameters) {
            if (argument instanceof VariableNode) {
                bytecode.add(ARG.toString());
                argument.generateCode(bytecode);
            }
        }

        bytecode.add(BODY.toString());
        body.generateCode(bytecode);
    }
}
