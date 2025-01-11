package org.bayl.vm.model;

import org.bayl.vm.model.argument.Argument;

public record Instruction(TypeOfInstruction type, Argument<?>... args) {
}
