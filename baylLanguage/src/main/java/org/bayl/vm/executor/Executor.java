package org.bayl.vm.executor;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import org.bayl.model.SourcePosition;
import org.bayl.runtime.BaylObject;
import org.bayl.vm.Environment;

@Getter
@EqualsAndHashCode
public abstract class Executor {

    private final SourcePosition position;

    public Executor(SourcePosition position) {
        this.position = position;
    }

    public abstract BaylObject eval(Environment virtualMachine);
}
