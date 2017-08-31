package linearizability_test;

import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.Map;

/**
 * Created by bugabuga on 27/08/17.
 */
class Scan extends ConstDeterministicOperation<ArrayList<AbstractMap.SimpleImmutableEntry<Integer, Integer>>> {
    Scan(double start_key, double end_key, ArrayList<AbstractMap.SimpleImmutableEntry<Integer, Integer>> retval) {
        super(retval);
        startKey = start_key;
        _end_key = end_key;
    }

    @Override
    ArrayList<AbstractMap.SimpleImmutableEntry<Integer, Integer>> innerOperate(Map<Integer, Integer> map) {
        ArrayList<AbstractMap.SimpleImmutableEntry<Integer, Integer>> res = new ArrayList<>();
        for (Map.Entry<Integer, Integer> entry : map.entrySet()) {
            if(startKey <= entry.getKey() && entry.getKey() <= _end_key)
                res.add(new AbstractMap.SimpleImmutableEntry<>(
                        entry.getKey(), entry.getValue()
                ));
        }
        return res;
    }
    private double startKey;
    private double _end_key;
}
