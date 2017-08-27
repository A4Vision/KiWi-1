package linearizability_test;

import java.util.Map;

/**
 * Created by bugabuga on 27/08/17.
 */
class Size extends ConstDeterministicOperation<Integer> {
    Size(Integer retval) {
        super(retval);
    }

    @Override
    Integer inner_operate(Map<Integer, Integer> map) {
        return _retval;
    }


}
