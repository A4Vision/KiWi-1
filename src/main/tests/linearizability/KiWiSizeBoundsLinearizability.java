package linearizability;

import kiwi.KiWiMap;
import kiwi.ThreadOperateOnMap;
import linearizability.*;
import org.junit.Test;
import util.Utils;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Random;

import static org.junit.Assert.assertTrue;

/**
 * Created by bugabuga on 01/09/17.
 */


public class KiWiSizeBoundsLinearizability {
    final static int NUM_ITERATIONS = 100;

    @Test
    public void explicitTest1() throws IOException, InterruptedException {
        ArrayList<MapOperation> ops0 = new ArrayList<>(Arrays.asList(
                new Put(1, 2), new SizeLowerBound(null), new SizeUpperBound(null)));
        ArrayList<MapOperation> ops1 = new ArrayList<>(Arrays.asList(
                new Put(2, 3), new SizeUpperBound(null), new SizeLowerBound(null)));
        for(int j = 0; j < NUM_ITERATIONS; ++j) {
            KiWiLinearizabilityUtils.KiWiIsLinearizableWithSpecificOperations(new ArrayList<>(Arrays.asList(ops0, ops1)));
        }
    }
}

