package org.bayl.vm.impl;

import org.bayl.SourcePosition;
import org.bayl.vm.executor.Executor;
import org.bayl.vm.executor.control.BlockExecutor;
import org.bayl.vm.executor.expression.TrueExecutor;
import org.bayl.vm.executor.expression.variable.VariableExecutor;
import org.bayl.vm.executor.statement.AssignExecutor;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class BytecodeParserImpl {

    private int iterator = 0;
    private List<String> bytecode;
    private static final String ARGS_DIVISION = " ";

    public Executor parse(List<String> bytecode) {
        this.bytecode = bytecode;
        return parseBlockExecutor();
    }

    private Executor parseBlockExecutor() {
        var statements = new ArrayList<Executor>();
        String[] tokens = getTokens();
        move();
        SourcePosition position = parsePosition(tokens[1], tokens[2]);

        while (getBytecodeLine() != "BLOCK_END") {
            Executor executor = switch (getTokens()[0]) {
                case "SET" -> parseAssignExecutor();
                default -> throw new IllegalStateException("Unexpected value: " + getTokens()[0]);
            };

            statements.add(executor);
        }

        return new BlockExecutor(position, statements);
    }

    private Executor parseAssignExecutor() {
        String[] tokens = getTokens();
        move();
        SourcePosition position = parsePosition(tokens[1], tokens[2]);

        Executor right = parseTrueExecutor();
        Executor left = parseVariableExecutor();

        return new AssignExecutor(position, left, right);
    }

    private Executor parseTrueExecutor() {
        String[] tokens = getTokens();
        move();
        SourcePosition position = parsePosition(tokens[2], tokens[3]);

        return new TrueExecutor(position);
    }

    private Executor parseVariableExecutor() {
        String[] tokens = getTokens();
        move();

        String name = tokens[1];
        SourcePosition position = parsePosition(tokens[2], tokens[3]);

        return new VariableExecutor(position, name);
    }

    private SourcePosition parsePosition(String line, String column) {
        int lineOn = Integer.parseInt(line);
        int columnNo = Integer.parseInt(column);

        return new SourcePosition(lineOn, columnNo);
    }

    private void move() {
        iterator++;
    }

    private void move(int point) {
        iterator += point;
    }

    private String[] getTokens() {
        String line = getBytecodeLine();
        return split(line);
    }

    private String getBytecodeLine() {
        return bytecode.get(iterator);
    }

    private String[] split(String line) {
        return line.split(ARGS_DIVISION);
    }

    public static void main(String[] args) {
        List<String> bytecode = Arrays.asList(
                "BLOCK_START 1 1",
                "SET 3 8",
                "PUSH true 3 10",
                "STORE a_bool 3 1",
                "BLOCK_END"
        );

        var parser = new BytecodeParserImpl();
        Executor ast = parser.parse(bytecode);
        ast.eval(new VirtualMachineImpl());
    }
}
