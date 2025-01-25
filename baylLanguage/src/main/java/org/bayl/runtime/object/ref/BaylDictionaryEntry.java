package org.bayl.runtime.object.ref;

import java.util.List;
import org.bayl.runtime.BaylObject;
import org.bayl.runtime.ContainedTypes;

public class BaylDictionaryEntry extends BaylObject implements ContainedTypes {

    private final BaylObject key;
    private BaylObject value;

    public BaylDictionaryEntry(BaylObject key, BaylObject value) {
        this.key = key;
        this.value = value;
    }

    public BaylObject getKey() {
        return key;
    }

    public BaylObject getValue() {
        return value;
    }

    public void setValue(BaylObject value) {
        this.value = value;
    }

    @Override
    public int compareTo(BaylObject o) {
        BaylDictionaryEntry entry = (BaylDictionaryEntry) o;
        return value.compareTo(entry.value);
    }

    @Override
    public String toString() {
        return key.toString() + "=" + value.toString();
    }

    @Override
    public BaylObject clone() {
        return new BaylDictionaryEntry(
                key.clone(),
                value.clone()
        );
    }

    @Override
    public List<BaylObject> getAllTypes() {
        return List.of(key, value);
    }
}
