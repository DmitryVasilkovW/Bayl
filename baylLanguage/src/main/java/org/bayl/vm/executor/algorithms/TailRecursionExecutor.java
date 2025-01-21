package org.bayl.vm.executor.algorithms;

import org.bayl.model.SourcePosition;
import org.bayl.runtime.BaylObject;
import org.bayl.vm.executor.Executor;
import org.bayl.vm.impl.VirtualMachineImpl;

import java.util.List;

public class TailRecursionExecutor extends Executor {
    private final Executor functionExecutor;
    private final List<Executor> arguments;

    public TailRecursionExecutor(SourcePosition position, Executor functionExecutor, List<Executor> arguments) {
        super(position);
        this.functionExecutor = functionExecutor;
        this.arguments = arguments;
    }

    @Override
    public BaylObject eval(VirtualMachineImpl virtualMachine) {
        // логика обработки хвостовой рекурсии

        return null;
    }
}
