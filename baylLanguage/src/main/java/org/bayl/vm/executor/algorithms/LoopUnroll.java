package org.bayl.vm.executor.algorithms;

import org.bayl.runtime.BaylObject;
import org.bayl.runtime.object.ref.BaylArray;
import org.bayl.runtime.object.ref.BaylDictionary;

import java.util.Iterator;
import java.util.Map;
import java.util.function.BiConsumer;

public class LoopUnroll {
    public interface LoopBody<T> {
        void execute(T element);
    }

    public static void unrollArray(BaylArray array, LoopBody<BaylObject> body) {
        int size = array.size();
        int unrollFactor = 10;

        if (size <= unrollFactor) {
            unrollFactor = size;
        }

        int i = 0;

        for (; i + unrollFactor <= size; i += unrollFactor) {
            for (int j = 0; j < unrollFactor; j++) {
                body.execute(array.get(i + j));
            }
        }

        for (; i < size; i++) {
            body.execute(array.get(i));
        }
    }

    public static void unrollDictionary(
            BaylDictionary dictionary,
            BiConsumer<BaylObject, BaylObject> body
    ) {
        int unrollFactor = 10;
        Iterator<Map.Entry<BaylObject, BaylObject>> iterator = dictionary.iterator();

        while (iterator.hasNext()) {
            for (int i = 0; i < unrollFactor && iterator.hasNext(); i++) {
                Map.Entry<BaylObject, BaylObject> entry = iterator.next();
                body.accept(entry.getKey(), entry.getValue());
            }
        }
    }
}
