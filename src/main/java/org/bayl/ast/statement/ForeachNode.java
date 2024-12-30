package org.bayl.ast.statement;

import java.util.Map;

import org.bayl.Interpreter;
import org.bayl.SourcePosition;
import org.bayl.InvalidTypeException;
import org.bayl.ast.expression.array.DictionaryEntryNode;
import org.bayl.ast.Node;
import org.bayl.ast.expression.variable.VariableNode;
import org.bayl.runtime.Dictionary;
import org.bayl.runtime.ZemArray;
import org.bayl.runtime.ZemObject;

public class ForeachNode extends Node {
    private VariableNode onVariableNode;
    private Node asNode;
    private Node loopBody;

    public ForeachNode(SourcePosition pos, VariableNode onVariableNode, Node asNode, Node loopBody) {
        super(pos);
        this.onVariableNode = onVariableNode;
        this.asNode = asNode;
        this.loopBody = loopBody;
    }

    @Override
    public ZemObject eval(Interpreter interpreter) {
        ZemObject onVariable = interpreter.getVariable(onVariableNode.getName(), onVariableNode.getPosition());
        ZemObject ret = null;
        if (onVariable instanceof ZemArray) {
            String asVariableName = asNode.toString();
            for (ZemObject element : (ZemArray) onVariable) {
                interpreter.setVariable(asVariableName, element);
                ret = loopBody.eval(interpreter);
            }
            return ret;
        } else if (onVariable instanceof Dictionary) {
            DictionaryEntryNode entryNode = (DictionaryEntryNode) asNode;
            String keyName = ((VariableNode) entryNode.getKey()).getName();
            String valueName = ((VariableNode) entryNode.getValue()).getName();
            for (Map.Entry<ZemObject, ZemObject> entry : (Dictionary) onVariable) {
                interpreter.setVariable(keyName, entry.getKey());
                interpreter.setVariable(valueName, entry.getValue());
                ret = loopBody.eval(interpreter);
            }
            return ret;
        }
        throw new InvalidTypeException("foreach expects an array or dictionary.", onVariableNode.getPosition());
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append('(');
        sb.append("foreach ");
        sb.append(onVariableNode);
        sb.append(' ');
        sb.append(asNode);
        sb.append(' ');
        sb.append(loopBody);
        sb.append(')');
        return sb.toString();
    }
}
