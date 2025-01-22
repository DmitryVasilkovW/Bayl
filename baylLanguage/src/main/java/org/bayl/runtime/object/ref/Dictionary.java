package org.bayl.runtime.object.ref;

import java.util.Iterator;
import java.util.Map;
import org.bayl.runtime.BaylObject;

public class Dictionary extends BaylObject implements Iterable<Map.Entry<BaylObject, BaylObject>> {

    private final Map<BaylObject, BaylObject> dict;

    public Dictionary(Map<BaylObject, BaylObject> dict) {
        this.dict = dict;
    }

    public BaylObject get(BaylObject key) {
        return dict.get(key);
    }

    public void set(BaylObject key, BaylObject value) {
        dict.put(key, value);
    }

    @Override
    public int compareTo(BaylObject o) {
        throw new UnsupportedOperationException();
    }

    @Override
    public String toString() {
        return dict.toString();
    }

    @Override
    public Iterator<Map.Entry<BaylObject, BaylObject>> iterator() {
        return dict.entrySet().iterator();
    }
}
