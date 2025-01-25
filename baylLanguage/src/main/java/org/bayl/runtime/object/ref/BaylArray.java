package org.bayl.runtime.object.ref;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.bayl.runtime.BaylObject;
import org.bayl.runtime.ContainedTypes;

public class BaylArray extends BaylObject implements Iterable<BaylObject>, ContainedTypes {

    private final List<BaylObject> elements;

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

    @Override
    public BaylObject clone() {
        var cloneElements = new ArrayList<BaylObject>();
        elements.forEach((element) -> {
            cloneElements.add(element.clone());
        });

        return new BaylArray(cloneElements);
    }

    @Override
    public List<BaylObject> getAllTypes() {
        return new ArrayList<>(elements);
    }
}
