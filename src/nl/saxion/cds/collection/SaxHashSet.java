package nl.saxion.cds.collection;



public class SaxHashSet<V> implements SaxSet<V> {

    private final SaxHashMap<V, Boolean> map = new SaxHashMap<>();

    @Override
    public void add(V value) {
        if (!contains(value)) {
            map.put(value, true);
        }
    }

    @Override
    public boolean contains(V value) {
        return map.containsKey(value);
    }

    @Override
    public int getSize() {
        return map.getSize();
    }

    @Override
    public void clear() {
        map.clear();
    }

    @Override
    public String graphViz(String name) {
        return map.graphViz(name);
    }

    @Override
    public String toString() {
        return map.toString();
    }

    @Override
    public java.util.Iterator<V> iterator() {
        return map.getKeys().iterator();
    }
}
