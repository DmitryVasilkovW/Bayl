package org.bayl.vm.executor.control;

import lombok.EqualsAndHashCode;
import org.bayl.SourcePosition;
import org.bayl.vm.executor.Executor;
import java.util.List;

@EqualsAndHashCode(callSuper = true)
public class RootExecutor extends BlockExecutor {

    public RootExecutor(SourcePosition pos, List<Executor> statements) {
        super(pos, statements);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (Executor executor : getStatements()) {
            sb.append(executor.toString());
        }
        return sb.toString();
    }
}
