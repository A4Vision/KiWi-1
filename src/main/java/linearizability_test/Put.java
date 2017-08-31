package linearizability_test;

import java.util.Map;

/**
 * Created by bugabuga on 27/08/17.
 */
public class Put implements MapOperation {
    Put(int key, int value){
        this.key = key;
        this.value = value;
    }

    @Override
    public void operate(Map<Integer, Integer> map) {
        map.put(key, value);
    }

    @Override
    public void undo(Map<Integer, Integer> map) {
        map.remove(key);
    }

    @Override
    public boolean isConst()    {
        return false;
    }

    @Override
    public boolean validate() {
        return true;
    }

    private int key;
    private int value;
}
