package linearizability_test;

import java.util.Comparator;

/**
 * Created by bugabuga on 27/08/17.
 */
class TimedOperation {
    MapOperation operation;
    Interval interval;

    TimedOperation(MapOperation operation, Interval interval) {
        this.operation = operation;
        this.interval = interval;
    }

    public static Comparator<TimedOperation> StartTimeComparator = new Comparator<TimedOperation>() {
        @Override
        public int compare(TimedOperation o1, TimedOperation o2) {
            return Double.compare(o1.interval.start, o2.interval.start);
        }
    };
}
