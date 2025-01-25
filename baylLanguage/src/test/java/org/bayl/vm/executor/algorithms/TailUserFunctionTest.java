package org.bayl.vm.executor.algorithms;

import org.bayl.model.SourcePosition;
import org.bayl.runtime.BaylObject;
import org.bayl.runtime.exception.ReturnException;
import org.bayl.runtime.function.TailUserFunction;
import org.bayl.vm.Environment;
import org.bayl.vm.executor.Executor;
import org.bayl.vm.executor.control.BlockExecutor;
import org.bayl.vm.executor.expression.function.FunctionCallExecutor;
import org.bayl.vm.executor.expression.function.ReturnExecutor;
import org.bayl.vm.impl.VirtualMachineImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Collections;
import java.util.LinkedList;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

class TailUserFunctionTest {

    private TailUserFunction tailUserFunction;
    private Executor bodyMock;
    private Environment environmentMock;
    private SourcePosition sourcePositionMock;

    @BeforeEach
    void setUp() {
        bodyMock = mock(Executor.class);
        environmentMock = mock(Environment.class);
        sourcePositionMock = mock(SourcePosition.class);
        tailUserFunction = new TailUserFunction(Collections.emptyList(), bodyMock);
    }

    @Test
    void testEvalWithJitDisabled() {
        VirtualMachineImpl.setJitEnabled(false);
        BaylObject expected = mock(BaylObject.class);
        when(bodyMock.eval(environmentMock)).thenReturn(expected);

        BaylObject result = tailUserFunction.eval(environmentMock, sourcePositionMock);

        assertEquals(expected, result);
        verify(bodyMock, times(1)).eval(environmentMock);
    }

    @Test
    void testEvalWithJitDisabledAndReturnException() {
        VirtualMachineImpl.setJitEnabled(false);
        BaylObject returnObject = mock(BaylObject.class);
        doThrow(new ReturnException(returnObject)).when(bodyMock).eval(environmentMock);

        // Act
        BaylObject result = tailUserFunction.eval(environmentMock, sourcePositionMock);

        // Assert
        assertEquals(returnObject, result);
        verify(bodyMock, times(1)).eval(environmentMock);
    }

    @Test
    void testEvalWithJitEnabledInfiniteLoopSimulation() {
        VirtualMachineImpl.setJitEnabled(false);
        BlockExecutor blockExecutorMock = mock(BlockExecutor.class);
        ReturnExecutor returnExecutorMock = mock(ReturnExecutor.class);
        FunctionCallExecutor functionCallExecutorMock = mock(FunctionCallExecutor.class);

        LinkedList<Executor> statements = new LinkedList<>();
        statements.add(returnExecutorMock);
        when(blockExecutorMock.getStatements()).thenReturn(statements);
        when(returnExecutorMock.getExpression()).thenReturn(functionCallExecutorMock);

        doThrow(new ReturnException(mock(BaylObject.class))).when(blockExecutorMock).eval(environmentMock);
        tailUserFunction = new TailUserFunction(Collections.emptyList(), blockExecutorMock);

        BaylObject result = tailUserFunction.eval(environmentMock, sourcePositionMock);

        assertNotNull(result);
    }
}
