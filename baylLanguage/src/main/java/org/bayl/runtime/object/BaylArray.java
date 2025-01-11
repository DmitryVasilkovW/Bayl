package org.bayl.runtime.object;

import org.bayl.runtime.BaylObject;
import java.util.Iterator;
import java.util.List;

public class BaylArray extends BaylObject implements Iterable<BaylObject> {
    private List<BaylObject> elements;

    public BaylArray(List<BaylObject> elements) {
        this.elements = elements;
    }

    public BaylObject get(int index) {
        return elements.get(index);
    }

    public void set(int index, BaylObject element) {
        elements.set(index, element);
    }

    public int size() {
        return elements.size();
    }

    public void push(BaylObject element) {
        elements.add(element);
    }

    @Override
    public Iterator<BaylObject> iterator() {
        return elements.iterator();
    }

    @Override
    public int compareTo(BaylObject o) {
        throw new UnsupportedOperationException();
    }

    @Override
    public String toString() {
        return elements.toString();
    }
}
