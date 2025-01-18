package org.bayl.ast.operator.logical;

import org.bayl.model.SourcePosition;
import org.bayl.ast.BooleanOpNode;
import org.bayl.ast.Node;
import org.bayl.ast.UnaryOpNode;
import org.bayl.bytecode.impl.Bytecode;
import org.bayl.runtime.BaylObject;
import org.bayl.runtime.object.BaylBoolean;
import static org.bayl.model.BytecodeToken.NOT;
import org.bayl.vm.Environment;

public class NotOpNode extends UnaryOpNode implements BooleanOpNode {

    public NotOpNode(SourcePosition pos, Node operand) {
        super(pos, "not", operand);
    }

    @Override
    public BaylObject eval(Environment virtualMachine) {
        BaylBoolean operand = getOperand().eval(virtualMachine).toBoolean(getOperand().getPosition());
        return operand.not();
    }

    @Override
    public void generateCode(Bytecode bytecode) {
        bytecode.add(getBytecodeLineWithPosition(
                NOT.toString()
        ));

        getOperand().generateCode(bytecode);
    }
}
