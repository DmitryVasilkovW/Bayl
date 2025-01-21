package org.bayl.vm.executor.control;

import lombok.EqualsAndHashCode;
import org.bayl.model.SourcePosition;
import org.bayl.runtime.BaylObject;
import org.bayl.vm.executor.Executor;
import org.bayl.vm.impl.VirtualMachineImpl;
import java.util.List;

@EqualsAndHashCode(callSuper = true)
public class BlockExecutor extends Executor {

    private final List<Executor> statements;

    public BlockExecutor(SourcePosition pos, List<Executor> statements) {
        super(pos);
        this.statements = statements;
    }

    public Executor get(int index) {
        return statements.get(index);
    }

    public List<Executor> getStatements() {
        return statements;
    }

    @Override
    public BaylObject eval(VirtualMachineImpl virtualMachine) {
        BaylObject ret = null;
        for (Executor statement : statements) {
            ret = statement.eval(virtualMachine);
        }
        return ret;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append('(');
        for (Executor Executor : statements) {
            sb.append(Executor.toString());
        }
        sb.append(')');
        return sb.toString();
    }
}
