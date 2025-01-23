package org.bayl.runtime.function.impl.collection;

import org.bayl.model.SourcePosition;
import org.bayl.runtime.BaylObject;
import org.bayl.runtime.BaylFunction;
import org.bayl.runtime.object.ref.BaylArray;
import org.bayl.runtime.object.ref.Dictionary;
import org.bayl.runtime.object.ref.DictionaryEntry;
import org.bayl.vm.Environment;

public class PushFunction extends BaylFunction {

    private static final String COLLECTION = "`collection_push";
    private static final String VALUE = "`element_push";
    private final String[] parameters = {COLLECTION, VALUE};

    @Override
    public BaylObject eval(Environment interpreter, SourcePosition pos) {
        BaylObject value = interpreter.getVariable(VALUE, pos);
        BaylObject collection = interpreter.getVariable(COLLECTION, pos);

        if (collection instanceof BaylArray) {
            ((BaylArray) collection).push(value);
        } else if (collection instanceof Dictionary) {
            ((Dictionary) collection).push(((DictionaryEntry) value));
        }

        return collection;
    }

    @Override
    public BaylObject getDefaultValue(int index) {
        return null;
    }

    @Override
    public int getParameterCount() {
        return 2;
    }

    @Override
    public String getParameterName(int index) {
        return parameters[index];
    }

}
