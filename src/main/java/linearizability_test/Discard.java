package linearizability_test;

import java.util.Map;

/**
 * Created by bugabuga on 27/08/17.
 */
class Discard implements MapOperation {
    Discard(int key){
        this.key = key;
        prevValue = null;
    }

    @Override
    public void operate(Map<Integer, Integer> map) {
        if(map.containsKey(key)){
            prevValue = map.get(key);
            map.remove(key);
        }
    }

    @Override
    public void undo(Map<Integer, Integer> map) {
        if(prevValue != null){
            map.put(key, prevValue);
            prevValue = null;
        }
    }

    @Override
    public boolean isConst() {
        return false;
    }

    @Override
    public boolean validate() {
        return true;
    }

    private int key;
    private Integer prevValue;
}
