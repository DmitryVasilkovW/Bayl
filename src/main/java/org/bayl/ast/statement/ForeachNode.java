package org.bayl.ast.statement;

import java.util.Map;

import org.bayl.Interpreter;
import org.bayl.SourcePosition;
import org.bayl.bytecode.Bytecode;
import org.bayl.runtime.exception.InvalidTypeException;
import org.bayl.ast.expression.array.DictionaryEntryNode;
import org.bayl.ast.Node;
import org.bayl.ast.expression.variable.VariableNode;
import org.bayl.runtime.object.Dictionary;
import org.bayl.runtime.object.BaylArray;
import org.bayl.runtime.BaylObject;

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
    public BaylObject eval(Interpreter interpreter) {
        BaylObject onVariable = interpreter.getVariable(onVariableNode.getName(), onVariableNode.getPosition());
        BaylObject ret = null;
        if (onVariable instanceof BaylArray) {
            String asVariableName = asNode.toString();
            for (BaylObject element : (BaylArray) onVariable) {
                interpreter.setVariable(asVariableName, element);
                ret = loopBody.eval(interpreter);
            }
            return ret;
        } else if (onVariable instanceof Dictionary) {
            DictionaryEntryNode entryNode = (DictionaryEntryNode) asNode;
            String keyName = ((VariableNode) entryNode.getKey()).getName();
            String valueName = ((VariableNode) entryNode.getValue()).getName();
            for (Map.Entry<BaylObject, BaylObject> entry : (Dictionary) onVariable) {
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

    @Override
    public void generateCode(Bytecode bytecode) {
        bytecode.add("FOREACH");
        bytecode.add("ONVAR");
        onVariableNode.generateCode(bytecode);
        bytecode.add("AS");
        asNode.generateCode(bytecode);
        bytecode.add("LOOP");
        loopBody.generateCode(bytecode);
    }
}
