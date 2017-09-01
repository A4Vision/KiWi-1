package linearizability_test;

import kiwi.KiWiMap;

import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.Map;

/**
 * Created by bugabuga on 27/08/17.
 */
public class Scan extends ConstDeterministicOperation<ArrayList<Integer>> {
    public Scan(int start_key, int end_key, ArrayList<Integer> retval) {
        super(retval);
        startKey = start_key;
        endKey = end_key;
    }

    @Override
    ArrayList<Integer> innerOperate(Map<Integer, Integer> map) {
        ArrayList<Integer> res = new ArrayList<>();
        for (Map.Entry<Integer, Integer> entry : map.entrySet()) {
            if(startKey <= entry.getKey() && entry.getKey() <= endKey)
                res.add(entry.getValue());
        }
        return res;
    }
    private int startKey;
    private int endKey;

    @Override
    public void operateKiWi(KiWiMap map) {
        Integer[] result = new Integer[1000];
        int length = map.getRange(result, startKey, endKey);
        retval = new ArrayList<>();
        for(int i = 0; i < length; ++i){
            retval.set(i, result[i]);
        }
    }

    @Override
    public boolean weakEqual(MapOperation other) {
        return other.getClass() == Scan.class && ((Scan)other).startKey == startKey && ((Scan)other).endKey == endKey;
    }
}
