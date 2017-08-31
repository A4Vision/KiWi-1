package linearizability_test;

import java.util.*;

/**
 * Created by bugabuga on 27/08/17.
 */


class History {
    History(ArrayList<ArrayList<TimedOperation>> concurrent_history){
        threadsHistories = concurrent_history;
        Comparator<TimedOperation> byStart = new Comparator<TimedOperation>() {
            @Override
            public int compare(TimedOperation o1, TimedOperation o2) {
                return new Double(o1.interval.start).compareTo(o2.interval.start);
            }
        };
        for(ArrayList<TimedOperation> core_history: concurrent_history){
            Collections.sort(core_history, byStart);
            for(int i = 1; i < core_history.size(); ++i){
                if(core_history.get(i - 1).interval.end > core_history.get(i).interval.start){
                    throw new IllegalArgumentException("Core " + (new Integer(i)).toString() + " history" +
                            "is not linear");
                }
            }
        }
    }


    private ArrayList<Double> _all_times_sorted() {
        HashSet<Double> all_times = new HashSet<>();

        for (ArrayList<TimedOperation> timed_ops_list : threadsHistories) {
            for (TimedOperation timed_op : timed_ops_list) {
                all_times.add(timed_op.interval.start);
                all_times.add(timed_op.interval.end);
            }
        }
        ArrayList<Double> list = new ArrayList<>(all_times);
        java.util.Collections.sort(list);
        return list;
    }



//    def __str__(self):
//    times = self._all_times_sorted()
//    time2index = {t: i for i, t in enumerate(times)}
//    LEN = 10
//    res = ""
//            for i in sorted(self._sorted_thread_histories.keys()):
//    prefix = "Core{:3d}: ".format(i)
//    ops_strings = [' ' * LEN] * (len(times) * 2)
//            for timed_op in self._sorted_thread_histories[i]:
//    i1 = time2index[timed_op.interval.start]
//    i2 = time2index[timed_op.interval.end]
//    middle = (str(timed_op.operation) + ' ' * LEN)[:LEN]
//    ops_strings[i1 * 2] = ' ' * (LEN // 2) + '[' + ' ' * ((LEN - 1) // 2)
//    ops_strings[i1 * 2 + 1] = middle
//    ops_strings[i2 * 2] = ' ' * (LEN // 2) + ']' + ' ' * ((LEN - 1) // 2)
//    res += prefix + ''.join(ops_strings) + '\n'
//            return res

    private int n_cores(){
        return threadsHistories.size();
    }

    public boolean isLinearizable(){
        Map<Integer, Integer> map_state = new HashMap<>();
        ArrayList<Integer> indices = new ArrayList<>(Collections.nCopies(n_cores(), 0));
        return recursiveIsLinearizable(map_state, indices);
    }

    // Recursive !!!
    private boolean recursiveIsLinearizable(Map<Integer, Integer> map_state, ArrayList<Integer> indices) {
        // Wing Gong naive test
        TimedOperation[] first_operations = new TimedOperation[n_cores()];
        boolean found_any = false;
        double earliest_end_among_first = Double.MAX_VALUE;
        for (int i = 0; i < n_cores(); ++i) {
            ArrayList<TimedOperation> ops_list = threadsHistories.get(i);
            Integer index = indices.get(i);
            if (index != null && index < ops_list.size()) {
                first_operations[i] = ops_list.get(index);
                found_any = true;
                earliest_end_among_first = Double.min(earliest_end_among_first, ops_list.get(index).interval.end);
            } else {
                first_operations[i] = null;
            }
        }
        if (!found_any) {
            // History left to linearize is empty.
            return true;
        }
        for (int i = 0; i < n_cores(); ++i) {
            TimedOperation timed_op = first_operations[i];
            if (timed_op == null)
                continue;
            // Check whether the operation is minimal
            if (timed_op.interval.start < earliest_end_among_first) {
                indices.set(i, indices.get(i) + 1);
                MapOperation op = timed_op.operation;
                op.operate(map_state);
                if (op.validate() && recursiveIsLinearizable(map_state, indices)) {
                    op.undo(map_state);
                    return true;
                } else {
                    op.undo(map_state);
                    indices.set(i, indices.get(i) - 1);
                }
            }

        }
        return false;
    }

    private ArrayList<ArrayList<TimedOperation>> threadsHistories;
}

