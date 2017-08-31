package linearizability_test;

import java.util.Map;

/**
 * Created by bugabuga on 27/08/17.
 */
abstract public class ConstDeterministicOperation<RetType> implements MapOperation {
    ConstDeterministicOperation(RetType retval){
        _retval = retval;
        _actual_retval = null;
    }

    abstract RetType inner_operate(Map<Integer, Integer> map);

    @Override
    public void operate(Map<Integer, Integer> map) {
        if(_actual_retval != null){
            throw new UnsupportedOperationException("");
        }
        _actual_retval = inner_operate(map);
    }

    @Override
    public void undo(Map<Integer, Integer> map) {
        _actual_retval = null;
    }

    @Override
    public boolean is_const() {
        return true;
    }

    @Override
    public boolean validate() {
        if(_retval == null){
            return _actual_retval == null;
        }else {
            return _retval.equals(_actual_retval);
        }
    }

    public void set_retval(RetType retval){
        _retval = retval;
    }

    private RetType _retval;
    private RetType _actual_retval;
}
