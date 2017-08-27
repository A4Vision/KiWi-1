package linearizability_test;

import java.util.Map;

/**
 * Created by bugabuga on 27/08/17.
 */
class Get extends ConstDeterministicOperation<Integer> {
    Get(int key, Integer retval){
        super(retval);
        _key = key;
    }

    @Override
    public Integer inner_operate(Map<Integer, Integer> map) {
        return map.getOrDefault(_key, null);
    }

    private Integer _key;
}
