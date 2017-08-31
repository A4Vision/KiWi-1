package linearizability_test;

import java.util.Map;

/**
 * Created by bugabuga on 27/08/17.
 */
class Get extends ConstDeterministicOperation<Integer> {
    Get(int key, Integer retval){
        super(retval);
        this.key = key;
    }

    @Override
    public Integer innerOperate(Map<Integer, Integer> map) {
        return map.getOrDefault(key, null);
    }

    private Integer key;
}
