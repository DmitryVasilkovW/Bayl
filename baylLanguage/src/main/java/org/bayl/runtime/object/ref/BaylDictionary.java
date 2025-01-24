package org.bayl.runtime.object.ref;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import org.bayl.runtime.BaylObject;
import org.bayl.runtime.ContainedTypes;

public class BaylDictionary extends BaylObject implements Iterable<Map.Entry<BaylObject, BaylObject>>, ContainedTypes {

    private final Map<BaylObject, BaylObject> dict;

    public BaylDictionary(Map<BaylObject, BaylObject> dict) {
        this.dict = dict;
    }

    public BaylObject get(BaylObject key) {
        return dict.get(key);
    }

    public void set(BaylObject key, BaylObject value) {
        dict.put(key, value);
    }

    public int size() {
        return dict.size();
    }

    public void push(BaylDictionaryEntry entry) {
        dict.put(entry.getKey(), entry.getValue());
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

    @Override
    public BaylObject clone() {
        var cloneDict = new HashMap<BaylObject, BaylObject>();
        dict.forEach((key, value) -> {
            cloneDict.put(key.clone(), value.clone());
        });

        return new BaylDictionary(cloneDict);
    }

    @Override
    public List<BaylObject> getAllTypes() {
        var refs = new ArrayList<BaylObject>();

        refs.addAll(dict.keySet());
        refs.addAll(dict.values());

        return refs;
    }
}
