package linearizability_test;

import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.Arrays;

import static org.junit.Assert.*;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Created by bugabuga on 30/08/17.
 */
public class HistoryTest {
    @org.junit.Test
    public void is_linearizable() throws Exception {

        ArrayList<AbstractMap.SimpleImmutableEntry<Integer, Integer>> scan1 =
                new ArrayList<>( Arrays.asList(
                        new AbstractMap.SimpleImmutableEntry<>(12, 4),
                        new AbstractMap.SimpleImmutableEntry<>(13, 4)));

        ArrayList<TimedOperation> h0 = new ArrayList<>(Arrays.asList(
                new TimedOperation(new Get(12, 3), new Interval(1, 4)),
                new TimedOperation(new Get(12, 4), new Interval(5, 7)),
                new TimedOperation(new Scan(11.2, 20, scan1), new Interval(8.1, 8.7)),
                new TimedOperation(new Get(12, 3), new Interval(8.8, 10))));


        ArrayList<TimedOperation> h1 = new ArrayList<>(Arrays.asList(
                new TimedOperation(new Put(12, 3), new Interval(1, 5.1)),
                new TimedOperation(new Put(12, 4), new Interval(6, 7.2)),
                new TimedOperation(new Put(10, 4), new Interval(7.3, 7.4)),
                new TimedOperation(new Put(11, 4), new Interval(7.5, 7.6)),
                new TimedOperation(new Put(13, 4), new Interval(8, 8.2)),
                new TimedOperation(new Put(12, 3), new Interval(9.5, 12))));

        History h = new History(new ArrayList<>(Arrays.asList(h0, h1)));
        assertTrue(h.is_linearizable());
        // Scan starts after Put(13, 4)
        h0.get(2).interval.start = 8.3;
        assertFalse(h.is_linearizable());
        h0.get(2).interval.start = 8.1;
        // As before.
        assertTrue(h.is_linearizable());
        // Last get returns a false value.
        ((Get)h0.get(3).operation).set_retval(1);
        assertFalse(h.is_linearizable());

    }

}