package org.bayl.vm.impl;

import lombok.Getter;
import lombok.Setter;
import org.bayl.ast.control.RootNode;
import org.bayl.bytecode.impl.Bytecode;
import org.bayl.bytecode.impl.BytecodeParserImpl;
import org.bayl.bytecode.impl.profiler.Profiler;
import org.bayl.memory.BaylMemory;
import org.bayl.model.SourcePosition;
import org.bayl.runtime.BaylFunction;
import org.bayl.runtime.BaylObject;
import org.bayl.runtime.exception.InvalidTypeException;
import org.bayl.runtime.exception.TooFewArgumentsException;
import org.bayl.syntax.Lexer;
import org.bayl.syntax.Parser;
import org.bayl.vm.Environment;
import org.bayl.vm.executor.control.RootExecutor;

import java.io.*;
import java.util.List;

public class VirtualMachineImpl implements Environment {

    private BaylMemory memory = new BaylMemory();

    @Getter
    @Setter
    private static boolean jitEnabled = true;

    public VirtualMachineImpl(boolean jitEnabled) {
        VirtualMachineImpl.jitEnabled = jitEnabled;
    }

    public VirtualMachineImpl() {
        this(true);
    }

    @Override
    public BaylObject getVariable(String name, SourcePosition pos) {
        return memory.getVariable(name, pos);
    }

    @Override
    public void setVariable(String name, BaylObject value) {
        memory.setVariable(name, value);
    }

    public void checkFunctionExists(String functionName, SourcePosition pos) {
        BaylObject symbol = getVariable(functionName, pos);
        if (!(symbol instanceof BaylFunction)) {
            throw new InvalidTypeException(functionName + " is not a function", pos);
        }
    }

    @Override
    public BaylObject callFunction(
            BaylFunction function,
            List<BaylObject> args,
            SourcePosition pos,
            String functionName
    ) {
        var memoryClone = memory.clone();

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
        memory = memoryClone;

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
        Profiler profiler = Profiler.getInstance();

        RootExecutor exe = new BytecodeParserImpl(profiler).parse(bytecode);

        return exe.eval(this);
    }
}
