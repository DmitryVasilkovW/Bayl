package org.bayl.ast.statement;

import java.util.Map;
import org.bayl.model.BytecodeToken;
import static org.bayl.model.BytecodeToken.DICT_PAIR;
import org.bayl.model.SourcePosition;
import org.bayl.ast.Node;
import org.bayl.ast.expression.collection.DictionaryEntryNode;
import org.bayl.ast.expression.variable.VariableNode;
import org.bayl.bytecode.impl.Bytecode;
import static org.bayl.model.BytecodeToken.FOREACH;
import org.bayl.runtime.BaylObject;
import org.bayl.runtime.exception.InvalidTypeException;
import org.bayl.runtime.object.BaylArray;
import org.bayl.runtime.object.Dictionary;
import org.bayl.vm.Environment;

public class ForeachNode extends Node {

    private final VariableNode onVariableNode;
    private final Node asNode;
    private final Node loopBody;

    public ForeachNode(SourcePosition pos, VariableNode onVariableNode, Node asNode, Node loopBody) {
        super(pos);
        this.onVariableNode = onVariableNode;
        this.asNode = asNode;
        this.loopBody = loopBody;
    }

    @Override
    public BaylObject eval(Environment virtualMachine) {
        BaylObject onVariable = virtualMachine.getVariable(onVariableNode.getName(), onVariableNode.getPosition());
        BaylObject ret = null;
        if (onVariable instanceof BaylArray) {
            String asVariableName = asNode.toString();
            for (BaylObject element : (BaylArray) onVariable) {
                virtualMachine.setVariable(asVariableName, element);
                ret = loopBody.eval(virtualMachine);
            }
            return ret;
        } else if (onVariable instanceof Dictionary) {
            DictionaryEntryNode entryNode = (DictionaryEntryNode) asNode;
            String keyName = ((VariableNode) entryNode.getKey()).getName();
            String valueName = ((VariableNode) entryNode.getValue()).getName();
            for (Map.Entry<BaylObject, BaylObject> entry : (Dictionary) onVariable) {
                virtualMachine.setVariable(keyName, entry.getKey());
                virtualMachine.setVariable(valueName, entry.getValue());
                ret = loopBody.eval(virtualMachine);
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
        bytecode.add(getBytecodeLineWithPosition(
                FOREACH.toString())
        );

        onVariableNode.generateCode(bytecode);
        if (asNode instanceof DictionaryEntryNode) {
            bytecode.add(DICT_PAIR + " " + asNode.getPositionForBytecode());
        }
        asNode.generateCode(bytecode);
        loopBody.generateCode(bytecode);
    }
}
