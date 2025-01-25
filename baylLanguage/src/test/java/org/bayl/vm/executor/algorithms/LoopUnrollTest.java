package org.bayl.vm.executor.algorithms;

import org.bayl.runtime.BaylObject;
import org.bayl.runtime.object.ref.BaylArray;
import org.bayl.runtime.object.ref.BaylDictionary;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;
import java.util.function.BiConsumer;

import static org.mockito.Mockito.*;

class LoopUnrollTest {

    @Test
    void testUnrollArray() {
        BaylArray mockArray = mock(BaylArray.class);
        var mockBody = mock(LoopUnroll.LoopBody.class);

        int size = 15;
        when(mockArray.size()).thenReturn(size);

        BaylObject[] elements = new BaylObject[size];
        for (int i = 0; i < size; i++) {
            elements[i] = mock(BaylObject.class);
            when(mockArray.get(i)).thenReturn(elements[i]);
        }

        LoopUnroll.unrollArray(mockArray, mockBody);

        for (BaylObject element : elements) {
            verify(mockBody, times(1)).execute(element);
        }

        verifyNoMoreInteractions(mockBody);
    }

    @Test
    void testUnrollDictionary() {
        BaylDictionary mockDictionary = mock(BaylDictionary.class);
        var mockBody = mock(BiConsumer.class);

        Map<BaylObject, BaylObject> map = new HashMap<>();
        for (int i = 0; i < 15; i++) {
            BaylObject key = mock(BaylObject.class);
            BaylObject value = mock(BaylObject.class);
            map.put(key, value);
        }

        when(mockDictionary.iterator()).thenReturn(map.entrySet().iterator());

        LoopUnroll.unrollDictionary(mockDictionary, mockBody);

        for (Map.Entry<BaylObject, BaylObject> entry : map.entrySet()) {
            verify(mockBody, times(1)).accept(entry.getKey(), entry.getValue());
        }

        verifyNoMoreInteractions(mockBody);
    }
}
