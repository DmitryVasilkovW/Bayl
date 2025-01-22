package org.bayl.vm.executor.expression.literale;

import lombok.EqualsAndHashCode;
import org.bayl.model.SourcePosition;
import org.bayl.runtime.BaylObject;
import org.bayl.runtime.object.value.BaylString;
import org.bayl.vm.Environment;
import org.bayl.vm.executor.Executor;

@EqualsAndHashCode(callSuper = true)
public class StringExecutor extends Executor {

    private final BaylString literal;

    public StringExecutor(SourcePosition pos, String literal) {
        super(pos);
        this.literal = new BaylString(literal);
    }

    @Override
    public BaylObject eval(Environment virtualMachine) {
        return literal;
    }

    @Override
    public String toString() {
        return '"' + literal.toString() + '"';
    }
}
