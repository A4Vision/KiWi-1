package linearizability_test;

import java.util.Map;

/**
 * Created by bugabuga on 27/08/17.
 */
class Discard implements MapOperation {
    Discard(int key){
        _key = key;
        _prev_value = null;
    }

    @Override
    public void operate(Map<Integer, Integer> map) {
        if(map.containsKey(_key)){
            _prev_value = map.get(_key);
            map.remove(_key);
        }
    }

    @Override
    public void undo(Map<Integer, Integer> map) {
        if(_prev_value != null){
            map.put(_key, _prev_value);
            _prev_value = null;
        }
    }

    @Override
    public boolean is_const() {
        return false;
    }

    @Override
    public boolean validate() {
        return true;
    }

    private int _key;
    private Integer _prev_value;
}
