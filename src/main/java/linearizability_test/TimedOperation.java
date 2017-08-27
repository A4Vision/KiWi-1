package linearizability_test;

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
}
