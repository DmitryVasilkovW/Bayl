package org.bayl.ast.expression.function;

import org.bayl.model.SourcePosition;
import org.bayl.ast.Node;
import org.bayl.bytecode.impl.Bytecode;
import org.bayl.runtime.BaylObject;
import org.bayl.runtime.exception.ReturnException;
import static org.bayl.model.BytecodeToken.RETURN;
import org.bayl.vm.Environment;

public class ReturnNode extends Node {

    private final Node expression;

    public ReturnNode(SourcePosition pos, Node expression) {
        super(pos);
        this.expression = expression;
    }

    @Override
    public BaylObject eval(Environment virtualMachine) {
        throw new ReturnException(expression.eval(virtualMachine));
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("(return ");
        sb.append(expression);
        sb.append(')');
        return sb.toString();
    }

    @Override
    public void generateCode(Bytecode bytecode) {
        bytecode.add(getBytecodeLineWithPosition(
                RETURN.toString()
        ));

        expression.generateCode(bytecode);
    }
}
