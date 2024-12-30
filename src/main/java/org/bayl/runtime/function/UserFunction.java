package org.bayl.runtime.function;

import java.util.List;

import org.bayl.Interpreter;
import org.bayl.SourcePosition;
import org.bayl.ast.Node;
import org.bayl.runtime.Function;
import org.bayl.runtime.Parameter;
import org.bayl.runtime.exception.ReturnException;
import org.bayl.runtime.ZemObject;

public class UserFunction extends Function {
    private List<Parameter> parameters;
    private Node body;

    public UserFunction(List<Parameter> parameters, Node body) {
        this.parameters = parameters;
        this.body = body;
    }

    public Node getBody() {
        return body;
    }

    @Override
    public int getParameterCount() {
        return parameters.size();
    }

    @Override
    public String getParameterName(int index) {
        return parameters.get(index).getName();
    }

    @Override
    public ZemObject getDefaultValue(int index) {
        return parameters.get(index).getDefaultValue();
    }

    @Override
    public ZemObject eval(Interpreter interpreter, SourcePosition pos) {
        try {
            return body.eval(interpreter);
        } catch (ReturnException e) {
            return e.getReturn();
        }
    }
}
