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

import org.bayl.Lexer;
import org.bayl.Parser;
import org.bayl.SourcePosition;
import org.bayl.ast.control.RootNode;
import org.bayl.runtime.exception.InvalidTypeException;
import org.bayl.runtime.exception.TooFewArgumentsException;
import org.bayl.runtime.exception.UnsetVariableException;
import org.bayl.runtime.function.ArrayLenFunction;
import org.bayl.runtime.function.ArrayPushFunction;
import org.bayl.runtime.Function;
import org.bayl.runtime.function.StringLenFunction;
import org.bayl.runtime.function.PrintFunction;
import org.bayl.runtime.function.PrintLineFunction;
import org.bayl.runtime.BaylObject;

public class VirtualMachineImpl {
    private Map<String, BaylObject> symbolTable = new HashMap<String, BaylObject>();

    public VirtualMachineImpl() {
        symbolTable.put("print", new PrintFunction());
        symbolTable.put("println", new PrintLineFunction());
        symbolTable.put("str_len", new StringLenFunction());
        symbolTable.put("arr_len", new ArrayLenFunction());
        symbolTable.put("array_push", new ArrayPushFunction());
    }

    public BaylObject getVariable(String name, SourcePosition pos) {
        if (!symbolTable.containsKey(name)) {
            throw new UnsetVariableException(name, pos);
        }
        return symbolTable.get(name);
    }

    public void setVariable(String name, BaylObject value) {
        symbolTable.put(name, value);
    }

    public void checkFunctionExists(String functionName, SourcePosition pos) {
        BaylObject symbol = getVariable(functionName, pos);
        if (!(symbol instanceof Function)) {
            throw new InvalidTypeException(functionName + " is not a function", pos);
        }
    }

    public BaylObject callFunction(Function function, List<BaylObject> args, SourcePosition pos, String functionName) {
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
        return program.eval(this);
    }
}
