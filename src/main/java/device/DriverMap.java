package device;

import breach.BreachWindow;

import java.io.File;
import java.util.Map;
import java.util.TreeMap;

public class DriverMap<V,K,T> {

    private final Map<V, Map<K, T>> drivers = new TreeMap<>();
}
