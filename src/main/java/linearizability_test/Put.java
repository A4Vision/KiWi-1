package linearizability_test;

import java.util.Map;

/**
 * Created by bugabuga on 27/08/17.
 */
public class Put implements MapOperation {
    Put(int key, int value){
        _key = key;
        _value = value;
    }

    @Override
    public void operate(Map<Integer, Integer> map) {
        map.put(_key, _value);
    }

    @Override
    public void undo(Map<Integer, Integer> map) {
        map.remove(_value);
    }

    @Override
    public boolean is_const()    {
        return false;
    }

    @Override
    public boolean validate() {
        return true;
    }

    private int _key;
    private int _value;
}
