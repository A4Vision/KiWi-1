package linearizability_test;

import java.util.ArrayList;
import java.util.Map;

/**
 * Created by bugabuga on 27/08/17.
 */
class Scan extends ConstDeterministicOperation<ArrayList<Map.Entry<Integer, Integer>>> {
    Scan(double start_key, double end_key, ArrayList<Map.Entry<Integer, Integer>> retval) {
        super(retval);
        _start_key = start_key;
        _end_key = end_key;
    }

    @Override
    ArrayList<Map.Entry<Integer, Integer>> inner_operate(Map<Integer, Integer> map) {
        ArrayList<Map.Entry<Integer, Integer>> res = new ArrayList<>();
        for (Map.Entry<Integer, Integer> entry : map.entrySet()) {
            if(_start_key <= entry.getKey() && entry.getKey() <= _end_key)
                res.add(entry);
        }
        return res;
    }

    private double _start_key;
    private double _end_key;
}
