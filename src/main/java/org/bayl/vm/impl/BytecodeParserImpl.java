package org.bayl.vm.impl;

import org.bayl.SourcePosition;
import org.bayl.model.BytecodeToken;
import org.bayl.vm.executor.Executor;
import org.bayl.vm.executor.control.BlockExecutor;
import org.bayl.vm.executor.control.RootExecutor;
import org.bayl.vm.executor.expression.FalseExecutor;
import org.bayl.vm.executor.expression.TrueExecutor;
import org.bayl.vm.executor.expression.array.ArrayExecutor;
import org.bayl.vm.executor.expression.array.DictionaryEntryExecutor;
import org.bayl.vm.executor.expression.array.DictionaryExecutor;
import org.bayl.vm.executor.expression.array.LookupExecutor;
import org.bayl.vm.executor.expression.function.FunctionCallExecutor;
import org.bayl.vm.executor.expression.function.FunctionExecutor;
import org.bayl.vm.executor.expression.literale.NumberExecutor;
import org.bayl.vm.executor.expression.literale.StringExecutor;
import org.bayl.vm.executor.expression.variable.VariableExecutor;
import org.bayl.vm.executor.statement.AssignExecutor;
import org.bayl.vm.executor.statement.ReturnExecutor;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.BiFunction;
import java.util.function.Function;
import static org.bayl.model.BytecodeToken.ARRAY_END;
import static org.bayl.model.BytecodeToken.BLOCK_END;
import static org.bayl.model.BytecodeToken.BODY;
import static org.bayl.model.BytecodeToken.CALL_END;
import static org.bayl.model.BytecodeToken.DICT_END;

public class BytecodeParserImpl {

    private int iterator;
    private List<String> bytecode;
    private static final String ARGS_DIVISION = " ";

    public RootExecutor parse(List<String> bytecode) {
        init(bytecode);

        BlockExecutor program = parseBlock();
        return new RootExecutor(program.getPosition(), program.getStatements());
    }

    private void init(List<String> bytecode) {
        this.bytecode = bytecode;
        iterator = 0;
    }

    private BlockExecutor parseBlock() {
        var statements = new ArrayList<Executor>();
        String[] tokens = getTokens();
        SourcePosition position = parsePosition(tokens);

        while (!getBytecodeLine().equals(BLOCK_END.toString())) {
            statements.add(parseExecutor());
        }
        move();

        return new BlockExecutor(position, statements);
    }

    private Executor parseExecutor() {
        BytecodeToken token = BytecodeToken.valueOf(peekTokens()[0]);
        return switch (token) {
            case PUSH_N -> parseValue(NumberExecutor::new);
            case PUSH_S -> parseValue(StringExecutor::new);
            case PUSH_T -> parseBoolConstant(TrueExecutor::new);
            case PUSH_F -> parseBoolConstant(FalseExecutor::new);
            case SET -> parseAssign();
            case BLOCK_START -> parseBlock();
            case ARRAY_INIT, DICT_INIT -> parseCollection();
            case LOAD, LOOKUP -> parseVarExecutor();
            case FUNC, RETURN_START, CALL -> parseFunctions();
            default -> throw new IllegalStateException("Unexpected value: " + peekTokens()[0]);
        };
    }

    private Executor parseVarExecutor() {
        BytecodeToken token = BytecodeToken.valueOf(peekTokens()[0]);
        return switch (token) {
            case LOAD -> parseVariable();
            case LOOKUP -> parseLookup();
            default -> throw new IllegalStateException("Unexpected value: " + peekTokens()[0]);
        };
    }

    private Executor parseCollection() {
        BytecodeToken token = BytecodeToken.valueOf(peekTokens()[0]);
        return switch (token) {
            case ARRAY_INIT -> parseArray();
            case DICT_INIT -> parseDictionary();
            default -> throw new IllegalStateException("Unexpected value: " + peekTokens()[0]);
        };
    }

    private Executor parseFunctions() {
        BytecodeToken token = BytecodeToken.valueOf(peekTokens()[0]);
        return switch (token) {
            case FUNC -> parseFunction();
            case CALL -> parseFunctionCall();
            case RETURN_START -> parseReturn();
            default -> throw new IllegalStateException("Unexpected value: " + peekTokens()[0]);
        };
    }

    private FunctionCallExecutor parseFunctionCall() {
        String[] tokens = getTokens();
        SourcePosition position = parsePosition(tokens);
        var args = new ArrayList<Executor>();

        Executor function = parseExecutor();
        while (!getBytecodeLine().equals(CALL_END.toString())) {
            move();
            args.add(parseExecutor());
        }
        move();

        return new FunctionCallExecutor(position, function, args);
    }

    private FunctionExecutor parseFunction() {
        String[] tokens = getTokens();
        SourcePosition position = parsePosition(tokens);
        var args = new ArrayList<Executor>();

        while (!getBytecodeLine().equals(BODY.toString())) {
            move();
            args.add(parseExecutor());
        }
        move();

        Executor body = parseExecutor();

        return new FunctionExecutor(position, args, body);
    }

    private ReturnExecutor parseReturn() {
        String[] tokens = getTokens();
        SourcePosition position = parsePosition(tokens);

        Executor expression = parseExecutor();

        return new ReturnExecutor(position, expression);
    }

    private DictionaryExecutor parseDictionary() {
        String[] tokens = getTokens();
        SourcePosition position = parsePosition(tokens);
        int capacity = Integer.parseInt(tokens[1]);
        var elements = new ArrayList<DictionaryEntryExecutor>(capacity);

        while (!getBytecodeLine().equals(DICT_END.toString())) {
            elements.add(parseDictionaryEntry());
        }
        move();

        return new DictionaryExecutor(position, elements);
    }

    private DictionaryEntryExecutor parseDictionaryEntry() {
        String[] tokens = getTokens();
        SourcePosition position = parsePosition(tokens);

        Executor key = parseExecutor();
        Executor value = parseExecutor();

        return new DictionaryEntryExecutor(position, key, value);
    }

    private ArrayExecutor parseArray() {
        String[] tokens = getTokens();
        SourcePosition position = parsePosition(tokens);
        int capacity = Integer.parseInt(tokens[1]);
        var elements = new ArrayList<Executor>(capacity);

        while (!getBytecodeLine().equals(ARRAY_END.toString())) {
            move();
            elements.add(parseExecutor());
        }
        move();

        return new ArrayExecutor(position, elements);
    }

    private Executor parseValue(BiFunction<SourcePosition, String, Executor> constructor) {
        String[] tokens = getTokens();
        String value = tokens[1];
        SourcePosition position = parsePosition(tokens);

        return constructor.apply(position, value);
    }

    private Executor parseBoolConstant(Function<SourcePosition, Executor> constructor) {
        String[] tokens = getTokens();
        SourcePosition position = parsePosition(tokens);

        return constructor.apply(position);
    }

    private AssignExecutor parseAssign() {
        String[] tokens = getTokens();
        SourcePosition position = parsePosition(tokens);

        Executor right = parseExecutor();
        Executor left = parseVarExecutor();

        return new AssignExecutor(position, left, right);
    }

    private LookupExecutor parseLookup() {
        String[] tokens = getTokens();
        SourcePosition position = parsePosition(tokens);

        VariableExecutor variableExecutor = parseVariable();
        Executor key = parseExecutor();

        return new LookupExecutor(position, variableExecutor, key);
    }

    private VariableExecutor parseVariable() {
        String[] tokens = getTokens();

        String name = tokens[1];
        SourcePosition position = parsePosition(tokens);

        return new VariableExecutor(position, name);
    }

    private SourcePosition parsePosition(String[] tokens) {
        int lineOn = Integer.parseInt(
                tokens[tokens.length - 2]
        );

        int columnNo = Integer.parseInt(
                tokens[tokens.length - 1]
        );

        return new SourcePosition(lineOn, columnNo);
    }

    private String[] peekTokens() {
        String line = getBytecodeLine();
        return split(line);
    }

    private String[] getTokens() {
        String line = getBytecodeLine();
        move();

        return split(line);
    }

    private void move() {
        iterator++;
    }

    private String getBytecodeLine() {
        return bytecode.get(iterator);
    }

    private String[] split(String line) {
        return line.split(ARGS_DIVISION);
    }

    public static void main(String[] args) {
        var test = "BLOCK_START 1 1\n" +
                "SET 1 5\n" +
                "FUNC 1 7\n" +
                "ARG\n" +
                "LOAD a 1 16\n" +
                "ARG\n" +
                "LOAD b 1 19\n" +
                "BODY\n" +
                "BLOCK_START 1 22\n" +
                "RETURN_START 2 5\n" +
                "PUSH_T true 2 12\n" +
                "BLOCK_END\n" +
                "LOAD sum 1 1\n" +
                "CALL sum 5 1\n" +
                "LOAD sum 5 1\n" +
                "ARG\n" +
                "PUSH_N 1 5 5\n" +
                "ARG\n" +
                "PUSH_N 3 5 8\n" +
                "CALL_END\n" +
                "BLOCK_END\n";

        List<String> bytecode = new ArrayList<>(Arrays.asList(test.split("\n")));

        var parser = new BytecodeParserImpl();
        Executor ast = parser.parse(bytecode);
        ast.eval(new VirtualMachineImpl());
    }
}
