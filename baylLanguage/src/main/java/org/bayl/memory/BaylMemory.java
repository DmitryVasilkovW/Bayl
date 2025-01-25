package org.bayl.memory;

import java.util.HashMap;
import java.util.Map;
import lombok.Getter;
import org.bayl.model.SourcePosition;
import org.bayl.runtime.ValueType;
import org.bayl.runtime.BaylObject;
import org.bayl.runtime.BaylType;
import org.bayl.runtime.exception.UnsetVariableException;
import org.bayl.runtime.function.impl.collection.PushFunction;
import org.bayl.runtime.function.impl.io.PrintFunction;
import org.bayl.runtime.function.impl.io.PrintLineFunction;
import org.bayl.runtime.function.impl.literal.IsNullFunction;
import org.bayl.runtime.function.impl.LenFunction;
import org.bayl.runtime.function.impl.math.MaxFunction;
import org.bayl.runtime.function.impl.math.MinFunction;
import org.bayl.runtime.object.BaylRef;

@Getter
public class BaylMemory implements Cloneable {

    private final Map<String, BaylType> globalStorage;
    private final Map<BaylRef, BaylObject> heap;

    public BaylMemory(Map<BaylRef, BaylObject> heap, Map<String, BaylType> globalStorage) {
        this.heap = heap;
        this.globalStorage = globalStorage;
        initFunctions();
    }

    public BaylMemory() {
        this.heap = new HashMap<>();
        this.globalStorage = new HashMap<>();
        initFunctions();
    }

    private void initFunctions() {
        setVariable("print", new PrintFunction());
        setVariable("println", new PrintLineFunction());
        setVariable("len", new LenFunction());
        setVariable("push", new PushFunction());
        setVariable("isNull", new IsNullFunction());
        setVariable("max", new MaxFunction());
        setVariable("min", new MinFunction());
    }

    public BaylObject getVariable(String name, SourcePosition pos) {
        BaylType value = globalStorage.get(name);

        if (isValueType(value)) {
            return (BaylObject) value;
        } else if (isRef(value)) {
            return heap.get((BaylRef) value);
        }
        throw new UnsetVariableException(name, pos);
    }

    public void setVariable(String name, BaylObject value) {
        if (isValueType(value)) {
            globalStorage.put(name, value);
        } else {
            BaylRef ref = getNewRef(name);

            globalStorage.put(name, ref);
            heap.put(ref, value);
        }
    }

    private BaylRef getNewRef(String name) {
        return new BaylRef(name);
    }

    private boolean isValueType(Object o) {
        return o instanceof BaylObject
                && o instanceof ValueType;
    }

    private boolean isRef(Object o) {
        return o instanceof BaylRef;
    }

    @Override
    public BaylMemory clone() {
        var cloneGlobal = new HashMap<String, BaylType>();
        globalStorage.forEach((name, obj) -> {
            var cloneObj = obj;

            if (isValueType(obj)) {
                cloneObj = ((BaylObject) obj).clone();
            }

            cloneGlobal.put(name, cloneObj);
        });

        var cloneHeap = new HashMap<BaylRef, BaylObject>();
        heap.forEach((ref, obj) -> {
            cloneHeap.put(ref, obj.clone());
        });

        return new BaylMemory(cloneHeap, cloneGlobal);
    }
}
