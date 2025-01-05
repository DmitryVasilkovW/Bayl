package org.bayl.vm.model.argument.impl;

import org.bayl.vm.model.argument.Argument;

public class IntegerArgument implements Argument<Integer> {
    private final int value;

    IntegerArgument(int value) {
        this.value = value;
    }

    @Override
    public Integer getArg() {
        return value;
    }
}
