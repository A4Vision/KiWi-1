package kiwi;
import org.junit.Test;

import java.util.Arrays;

import static org.junit.Assert.*;

/**
 * Created by bugabuga on 01/09/17.
 */
public class TestKiWiNaive {
    @Test
    public void testPutAndScan(){
        KiWiMap map = new KiWiMap(false);
        map.put(3, 4);
        Integer[] r = new Integer[10];
        int size = map.getRange(r, null, false, 0, 2);
        assertEquals(0, size);
    }

    @Test
    public void testPutTwiceAndScan(){
        KiWiMap map = new KiWiMap(false);
        Integer[] r = new Integer[10];
        map.put(2, 1);
        map.getRange(r, null, false, 0, 2);
        map.put(2, 8);
        map.getRange(r, null, false, 0, 2);
        map.put(2, 0);
        int size = map.getRange(r, null, false, 0, 2);
        System.out.println(Arrays.toString(r));
        assertEquals(1, size);
    }
}
