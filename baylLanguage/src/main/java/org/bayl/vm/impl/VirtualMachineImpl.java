package org.bayl.vm.impl;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.bayl.bytecode.impl.Bytecode;
import org.bayl.bytecode.impl.BytecodeParserImpl;
import org.bayl.runtime.function.impl.literal.IsNullFunction;
import org.bayl.syntax.Lexer;
import org.bayl.syntax.Parser;
import org.bayl.model.SourcePosition;
import org.bayl.ast.control.RootNode;
import org.bayl.runtime.BaylObject;
import org.bayl.runtime.BaylFunction;
import org.bayl.runtime.exception.InvalidTypeException;
import org.bayl.runtime.exception.TooFewArgumentsException;
import org.bayl.runtime.exception.UnsetVariableException;
import org.bayl.runtime.function.impl.collection.array.ArrayLenFunction;
import org.bayl.runtime.function.impl.collection.array.ArrayPushFunction;
import org.bayl.runtime.function.impl.io.PrintFunction;
import org.bayl.runtime.function.impl.io.PrintLineFunction;
import org.bayl.runtime.function.impl.literal.string.StringLenFunction;
import org.bayl.vm.Environment;
import org.bayl.vm.executor.control.RootExecutor;

public class VirtualMachineImpl implements Environment {

    private Map<String, BaylObject> symbolTable = new HashMap<>();

    public VirtualMachineImpl() {
        symbolTable.put("print", new PrintFunction());
        symbolTable.put("println", new PrintLineFunction());
        symbolTable.put("str_len", new StringLenFunction());
        symbolTable.put("arr_len", new ArrayLenFunction());
        symbolTable.put("array_push", new ArrayPushFunction());
        symbolTable.put("is_null", new IsNullFunction());
    }

    @Override
    public BaylObject getVariable(String name, SourcePosition pos) {
        if (!symbolTable.containsKey(name)) {
            throw new UnsetVariableException(name, pos);
        }
        return symbolTable.get(name);
    }

    @Override
    public void setVariable(String name, BaylObject value) {
        symbolTable.put(name, value);
    }

    public void checkFunctionExists(String functionName, SourcePosition pos) {
        BaylObject symbol = getVariable(functionName, pos);
        if (!(symbol instanceof BaylFunction)) {
            throw new InvalidTypeException(functionName + " is not a function", pos);
        }
    }

    @Override
    public BaylObject callFunction(BaylFunction function, List<BaylObject> args, SourcePosition pos, String functionName) {
        Map<String, BaylObject> savedSymbolTable =
                new HashMap<String, BaylObject>(symbolTable);
        int noMissingArgs = 0;
        int noRequiredArgs = 0;
        for (int paramIndex = 0;
             paramIndex < function.getParameterCount(); paramIndex++) {
            String parameterName = function.getParameterName(paramIndex);
            BaylObject value = function.getDefaultValue(paramIndex);
            if (value == null) {
                noRequiredArgs++;
            }
            if (paramIndex < args.size()) {
                value = args.get(paramIndex);
            }
            if (value == null) {
                noMissingArgs++;
            }
            setVariable(parameterName, value);
        }
        if (noMissingArgs > 0) {
            throw new TooFewArgumentsException(functionName, noRequiredArgs,
                                               args.size(), pos);
        }
        BaylObject ret = function.eval(this, pos);
        symbolTable = savedSymbolTable;

        return ret;
    }

    public BaylObject eval(String script) throws IOException {
        return eval(new StringReader(script));
    }

    public BaylObject eval(File file) throws IOException {
        return eval(new BufferedReader(new FileReader(file)));
    }

    public BaylObject eval(Reader reader) throws IOException {
        Lexer lexer = new Lexer(reader);
        Parser parser = new Parser(lexer);
        RootNode program = parser.program();
        List<String> bytecode = new Bytecode().getInstructions(program);

        RootExecutor exe = new BytecodeParserImpl().parse(bytecode);

        return exe.eval(this);
    }
}
