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

    public static Comparator<TimedOperation> StartTimeComparator
            = (TimedOperation op1, TimedOperation op2) -> Double.compare(op1.interval.start, op2.interval.start);
}
