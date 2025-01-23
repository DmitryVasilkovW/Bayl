package org.bayl.bytecode.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.function.BiFunction;
import java.util.function.Function;
import static org.bayl.model.BytecodeToken.DICT_PAIR;
import org.bayl.model.SourcePosition;
import org.bayl.model.BytecodeToken;
import org.bayl.bytecode.BytecodeParser;
import org.bayl.vm.TriFunction;
import org.bayl.vm.executor.Executor;
import org.bayl.vm.executor.classes.ClassCallExecutor;
import org.bayl.vm.executor.classes.ClassExecutor;
import org.bayl.vm.executor.control.BlockExecutor;
import org.bayl.vm.executor.control.RootExecutor;
import org.bayl.vm.executor.expression.function.TailRecursionExecutor;
import org.bayl.vm.executor.expression.literale.FalseExecutor;
import org.bayl.vm.executor.expression.literale.NullExecutor;
import org.bayl.vm.executor.operator.arithmetic.ModOpExecutor;
import org.bayl.vm.executor.operator.arithmetic.NegateOpExecutor;
import org.bayl.vm.executor.operator.arithmetic.PowerOpExecutor;
import org.bayl.vm.executor.expression.literale.TrueExecutor;
import org.bayl.vm.executor.expression.collection.ArrayExecutor;
import org.bayl.vm.executor.expression.collection.DictionaryEntryExecutor;
import org.bayl.vm.executor.expression.collection.DictionaryExecutor;
import org.bayl.vm.executor.expression.collection.LookupExecutor;
import org.bayl.vm.executor.expression.function.FunctionCallExecutor;
import org.bayl.vm.executor.expression.function.FunctionExecutor;
import org.bayl.vm.executor.expression.literale.NumberExecutor;
import org.bayl.vm.executor.expression.literale.StringExecutor;
import org.bayl.vm.executor.expression.variable.VariableExecutor;
import org.bayl.vm.executor.operator.string.ConcatOpExecutor;
import org.bayl.vm.executor.operator.arithmetic.AddOpExecutor;
import org.bayl.vm.executor.operator.arithmetic.DivideOpExecutor;
import org.bayl.vm.executor.operator.arithmetic.MultiplyOpExecutor;
import org.bayl.vm.executor.operator.arithmetic.SubtractOpExecutor;
import org.bayl.vm.executor.operator.comparison.EqualsOpExecutor;
import org.bayl.vm.executor.operator.comparison.GreaterThanOpExecutor;
import org.bayl.vm.executor.operator.comparison.LessThanOpExecutor;
import org.bayl.vm.executor.operator.comparison.NotEqualsOpExecutor;
import org.bayl.vm.executor.operator.logical.AndOpExecutor;
import org.bayl.vm.executor.operator.comparison.GreaterEqualOpExecutor;
import org.bayl.vm.executor.operator.comparison.LessEqualOpExecutor;
import org.bayl.vm.executor.operator.logical.NotOpExecutor;
import org.bayl.vm.executor.operator.logical.OrOpExecutor;
import org.bayl.vm.executor.statement.AssignExecutor;
import org.bayl.vm.executor.statement.ForeachExecutor;
import org.bayl.vm.executor.statement.IfExecutor;
import org.bayl.vm.executor.expression.function.ReturnExecutor;
import org.bayl.vm.executor.statement.WhileExecutor;
import org.bayl.bytecode.impl.profiler.Profiler;
import static org.bayl.model.BytecodeToken.ARRAY_END;
import static org.bayl.model.BytecodeToken.BLOCK_END;
import static org.bayl.model.BytecodeToken.BODY;
import static org.bayl.model.BytecodeToken.CALL_END;
import static org.bayl.model.BytecodeToken.DICT_END;
import static org.bayl.model.BytecodeToken.ELSE;

public class BytecodeParserImpl implements BytecodeParser {

    private int iterator;
    private List<String> bytecode;
    private Profiler profiler;
    private static final String ARGS_DIVISION = " ";
    private static final String EXCEPTION_MESSAGE = "Unexpected value: ";
    private static final int THRESHOLD = 5;

    @Override
    public RootExecutor parse(List<String> bytecode) {
        init(bytecode);

        BlockExecutor program = parseBlock();
        return new RootExecutor(program.getPosition(), program.getStatements());
    }

    public List<BytecodeToken> getMostFrequentlyUsed() {
        return profiler.getInstructions();
    }

    private void init(List<String> bytecode) {
        this.bytecode = bytecode;
        profiler = new Profiler(THRESHOLD);
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
        profiler.countInstruction(token);
        return switch (token) {
            case PUSH_N, PUSH_S, PUSH_T, PUSH_F, PUSH_NULL -> parseLiteral();
            case CLASS, CLASS_CALL -> parseClasses();
            case IF -> parseIf();
            case FOREACH, WHILE -> parseLoops();
            case SET, NEGATE, MOD, POWER, ADD, DIVIDE, MULTIPLY, SUBTRACT, CONCAT -> parseOperator();
            case EQUALS, GREATER_THAN, LESS_THAN, NOT_EQUALS, AND, GREATER_EQUAL,
                 LESS_EQUAL, NOT, OR -> parseComparator();
            case BLOCK_START -> parseBlock();
            case ARRAY_INIT, DICT_INIT, DICT_PAIR -> parseCollection();
            case LOAD, LOOKUP -> parseVarExecutor();
            case FUNC, TAIL_FUNCTION, RETURN, CALL -> parseFunctions();
            default -> throw new IllegalStateException(EXCEPTION_MESSAGE + peekTokens()[0]);
        };
    }

    private Executor parseLiteral() {
        BytecodeToken token = BytecodeToken.valueOf(peekTokens()[0]);
        return switch (token) {
            case PUSH_N -> parseValue(NumberExecutor::new);
            case PUSH_S -> parseString();
            case PUSH_T -> parseConstant(TrueExecutor::new);
            case PUSH_F -> parseConstant(FalseExecutor::new);
            case PUSH_NULL -> parseConstant(NullExecutor::new);
            default -> throw new IllegalStateException(EXCEPTION_MESSAGE + peekTokens()[0]);
        };
    }

    private Executor parseLoops() {
        BytecodeToken token = BytecodeToken.valueOf(peekTokens()[0]);
        return switch (token) {
            case FOREACH -> parseForeach();
            case WHILE -> parseExecutorWithTwoValues(WhileExecutor::new);
            default -> throw new IllegalStateException(EXCEPTION_MESSAGE + peekTokens()[0]);
        };
    }

    private Executor parseVarExecutor() {
        BytecodeToken token = BytecodeToken.valueOf(peekTokens()[0]);
        return switch (token) {
            case LOAD -> parseVariable();
            case LOOKUP -> parseLookup();
            default -> throw new IllegalStateException(EXCEPTION_MESSAGE + peekTokens()[0]);
        };
    }

    private Executor parseCollection() {
        BytecodeToken token = BytecodeToken.valueOf(peekTokens()[0]);
        return switch (token) {
            case ARRAY_INIT -> parseArray();
            case DICT_INIT -> parseDictionary();
            case DICT_PAIR -> parseDictionaryEntry();
            default -> throw new IllegalStateException(EXCEPTION_MESSAGE + peekTokens()[0]);
        };
    }

    private Executor parseFunctions() {
        BytecodeToken token = BytecodeToken.valueOf(peekTokens()[0]);
        return switch (token) {
            case FUNC -> parseFunction(FunctionExecutor::new);
            case TAIL_FUNCTION -> parseFunction(TailRecursionExecutor::new);
            case CALL -> parseFunctionCall();
            case RETURN -> parseExecutorWithOneValue(ReturnExecutor::new);
            default -> throw new IllegalStateException(EXCEPTION_MESSAGE + peekTokens()[0]);
        };
    }

    private Executor parseComparator() {
        BytecodeToken token = BytecodeToken.valueOf(peekTokens()[0]);
        return switch (token) {
            case EQUALS -> parseExecutorWithTwoValues(EqualsOpExecutor::new);
            case GREATER_THAN -> parseExecutorWithTwoValues(GreaterThanOpExecutor::new);
            case LESS_THAN -> parseExecutorWithTwoValues(LessThanOpExecutor::new);
            case NOT_EQUALS -> parseExecutorWithTwoValues(NotEqualsOpExecutor::new);
            case AND -> parseExecutorWithTwoValues(AndOpExecutor::new);
            case GREATER_EQUAL -> parseExecutorWithTwoValues(GreaterEqualOpExecutor::new);
            case LESS_EQUAL -> parseExecutorWithTwoValues(LessEqualOpExecutor::new);
            case NOT -> parseExecutorWithOneValue(NotOpExecutor::new);
            case OR -> parseExecutorWithTwoValues(OrOpExecutor::new);
            default -> throw new IllegalStateException(EXCEPTION_MESSAGE + token);
        };
    }

    private Executor parseOperator() {
        BytecodeToken token = BytecodeToken.valueOf(peekTokens()[0]);
        return switch (token) {
            case SET -> parseAssign();
            case MOD -> parseExecutorWithTwoValues(ModOpExecutor::new);
            case NEGATE -> parseExecutorWithOneValue(NegateOpExecutor::new);
            case POWER -> parseExecutorWithTwoValues(PowerOpExecutor::new);
            case ADD -> parseExecutorWithTwoValues(AddOpExecutor::new);
            case DIVIDE -> parseExecutorWithTwoValues(DivideOpExecutor::new);
            case MULTIPLY -> parseExecutorWithTwoValues(MultiplyOpExecutor::new);
            case SUBTRACT -> parseExecutorWithTwoValues(SubtractOpExecutor::new);
            case CONCAT -> parseExecutorWithTwoValues(ConcatOpExecutor::new);
            default -> throw new IllegalStateException(EXCEPTION_MESSAGE + token);
        };
    }

    private Executor parseClasses() {
        BytecodeToken token = BytecodeToken.valueOf(peekTokens()[0]);
        return switch (token) {
            case CLASS -> parseClass();
            case CLASS_CALL -> parseClassCall();
            default -> throw new IllegalStateException(EXCEPTION_MESSAGE + token);
        };
    }

    private ClassExecutor parseClass() {
        String[] tokens = getTokens();
        SourcePosition position = parsePosition(tokens);

        BlockExecutor bode = parseBlock();

        return new ClassExecutor(position, bode);
    }

    private ClassCallExecutor parseClassCall() {
        String[] tokens = getTokens();
        SourcePosition position = parsePosition(tokens);

        Executor classExecutor = parseExecutor();
        Executor attribute = parseExecutor();
        String name = tokens[1];

        return new ClassCallExecutor(position, classExecutor, attribute, name);
    }

    private ForeachExecutor parseForeach() {
        String[] tokens = getTokens();
        SourcePosition position = parsePosition(tokens);

        VariableExecutor onVar = parseVariable();

        Executor asVar;
        BytecodeToken token = BytecodeToken.valueOf(peekTokens()[0]);
        if (token.equals(DICT_PAIR)) {
            asVar = parseDictionaryEntry();
        } else {
            asVar = parseVarExecutor();
        }

        Executor body = parseExecutor();

        return new ForeachExecutor(position, onVar, asVar, body);
    }

    private IfExecutor parseIf() {
        String[] tokens = getTokens();
        SourcePosition position = parsePosition(tokens);

        Executor testCondition = parseExecutor();
        Executor thenBlock = parseExecutor();

        Executor elseBlock = null;
        if (peekTokens()[0].equals(ELSE.toString())) {
            move();
            elseBlock = parseExecutor();
        }

        return new IfExecutor(position, testCondition, thenBlock, elseBlock);
    }

    private <T extends Executor> T parseExecutorWithOneValue(BiFunction<SourcePosition, Executor, T> constructor) {
        String[] tokens = getTokens();
        SourcePosition position = parsePosition(tokens);

        Executor expression = parseExecutor();

        return constructor.apply(position, expression);
    }

    private <T extends Executor> T parseExecutorWithTwoValues(TriFunction<SourcePosition, Executor, Executor, T> constructor) {
        String[] tokens = getTokens();
        SourcePosition position = parsePosition(tokens);

        Executor left = parseExecutor();
        Executor right = parseExecutor();

        return constructor.apply(position, left, right);
    }

    private AssignExecutor parseAssign() {
        String[] tokens = getTokens();
        SourcePosition position = parsePosition(tokens);

        Executor right = parseExecutor();
        Executor left = parseVarExecutor();

        return new AssignExecutor(position, left, right);
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

    private<T extends Executor> T parseFunction(TriFunction<SourcePosition, List<Executor>, Executor, T> constructor) {
        String[] tokens = getTokens();
        SourcePosition position = parsePosition(tokens);
        var args = new ArrayList<Executor>();

        while (!getBytecodeLine().equals(BODY.toString())) {
            move();
            args.add(parseExecutor());
        }
        move();

        Executor body = parseExecutor();

        return constructor.apply(position, args, body);
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

    private StringExecutor parseString() {
        String[] tokens = getTokens();
        SourcePosition position = parsePosition(tokens);

        var sb = new StringBuilder();
        for (int i = 1; i < tokens.length - 2; i++) {
            String current = tokens[i];

            if (current.isEmpty()) {
                sb.append(ARGS_DIVISION);
            } else {
                addStringValue(
                        sb,
                        current,
                        i != tokens.length - 3
                );
            }
        }

        return new StringExecutor(position, sb.toString());
    }

    private void addStringValue(StringBuilder sb, String current, boolean expression) {
        if (expression) {
            sb
                    .append(current)
                    .append(ARGS_DIVISION);
            return;
        }

        sb.append(current);
    }

    private Executor parseConstant(Function<SourcePosition, Executor> constructor) {
        String[] tokens = getTokens();
        SourcePosition position = parsePosition(tokens);

        return constructor.apply(position);
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
}
