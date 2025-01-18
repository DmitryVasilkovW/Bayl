package org.bayl.ast.classes;

import org.bayl.SourcePosition;
import org.bayl.ast.Node;
import org.bayl.ast.expression.function.FunctionCallNode;
import org.bayl.ast.expression.variable.VariableNode;
import org.bayl.bytecode.Bytecode;
import org.bayl.runtime.BaylObject;
import org.bayl.runtime.classes.UserClass;
import org.bayl.vm.Environment;

public class ClassCallNode extends Node {

    private final Node classNode;
    private final Node attribute;
    private final String name;

    public ClassCallNode(SourcePosition position, Node classNode, Node attribute, String name) {
        super(position);
        this.classNode = classNode;
        this.attribute = attribute;
        this.name = name;
    }

    @Override
    public BaylObject eval(Environment virtualMachine) {
        String className = ((VariableNode) classNode).getName();
        BaylObject userClass = virtualMachine.getVariable(className, classNode.getPosition());

        if (attribute instanceof FunctionCallNode) {
            ((UserClass) userClass).setCall(((FunctionCallNode) attribute), virtualMachine);
        } else {
            ((UserClass) userClass).setCall(name, virtualMachine, attribute.getPosition());
        }

        return ((UserClass) userClass).eval(virtualMachine, classNode.getPosition());
    }

    @Override
    public void generateCode(Bytecode bytecode) {

    }
}
