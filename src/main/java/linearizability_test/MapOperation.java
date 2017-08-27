package linearizability_test;

import java.util.Map;

/**
 * Created by bugabuga on 27/08/17.
 */
public interface MapOperation {
    void operate(Map<Integer, Integer> map);

    void undo(Map<Integer, Integer> map);

    boolean is_const();

    boolean validate();
};
