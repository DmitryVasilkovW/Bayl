package org.bayl.runtime.object.ref;

import org.bayl.runtime.BaylObject;

public class DictionaryEntry extends BaylObject {

    private final BaylObject key;
    private BaylObject value;

    public DictionaryEntry(BaylObject key, BaylObject value) {
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
        DictionaryEntry entry = (DictionaryEntry) o;
        return value.compareTo(entry.value);
    }

    @Override
    public String toString() {
        return key.toString() + "=" + value.toString();
    }
}
