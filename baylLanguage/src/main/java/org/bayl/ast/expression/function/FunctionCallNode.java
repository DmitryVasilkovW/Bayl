package org.bayl.ast.expression.function;

import java.util.ArrayList;
import java.util.List;
import org.bayl.ast.Node;
import org.bayl.ast.expression.variable.VariableNode;
import org.bayl.bytecode.impl.Bytecode;
import static org.bayl.model.BytecodeToken.ARG;
import static org.bayl.model.BytecodeToken.CALL;
import static org.bayl.model.BytecodeToken.CALL_END;
import org.bayl.model.SourcePosition;

public class FunctionCallNode extends Node {

    final static public List<Node> NO_ARGUMENTS = new ArrayList<Node>(0);
    private final Node functionNode;
    private final List<Node> arguments;

    public FunctionCallNode(SourcePosition pos, Node functionNode, List<Node> arguments) {
        super(pos);
        this.functionNode = functionNode;
        this.arguments = arguments;
    }

    private String getFunctionName() {
        if (functionNode instanceof VariableNode) {
            return ((VariableNode) functionNode).getName();
        }
        return null;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append('(');
        String functionName = getFunctionName();
        if (functionName == null) {
            functionName = functionNode.toString();
        }
        sb.append(functionName);
        for (Node arg : arguments) {
            sb.append(' ');
            sb.append(arg);
        }
        sb.append(')');
        return sb.toString();
    }

    @Override
    public void generateCode(Bytecode bytecode) {
        bytecode.add(getStartLine());

        functionNode.generateCode(bytecode);
        for (Node arg : arguments) {
            bytecode.add(ARG.toString());
            arg.generateCode(bytecode);
        }

        bytecode.add(getEndLine());
    }

    private String getStartLine() {
        return getBytecodeLineWithPosition(
                CALL.toString(),
                getFunctionName()
        );
    }

    private String getEndLine() {
        return CALL_END.toString();
    }
}
