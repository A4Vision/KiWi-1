package linearizability_test;

import kiwi.KiWiMap;
import kiwi.ThreadOperateOnMap;
import org.junit.Test;
import util.Utils;

import java.io.File;
import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

import static org.junit.Assert.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;

/**
 * Created by bugabuga on 01/09/17.
 */


public class KiwiLinearizabilityTest {
    private static final int NUM_THREADS = 4;

    private static ArrayList<MapOperation> randomOperations(int seed, int keyRange,
                                                            int operationsAmount,
                                                            double putPercentange,
                                                            double getPercentage){
        Random generator = new Random(seed);

        ArrayList<MapOperation> res = new ArrayList<>();
        for(int i = 0; i < operationsAmount; ++i){
            double r = generator.nextDouble();
            if(r < putPercentange){
                res.add(new Put(generator.nextInt(keyRange), generator.nextInt(100)));
            }else if(r < putPercentange + getPercentage){
                res.add(new Get(generator.nextInt(keyRange), null));
            }else {
                res.add(new Scan(1, generator.nextInt(keyRange) + 1, null));
            }
        }
        return res;
    }

    @Test
    public void explicitTest1() throws IOException, InterruptedException {
        ArrayList<MapOperation> ops0 = new ArrayList<>(Arrays.asList(
                new Put(1, 2), new Put(1, 3), new Scan(1, 2, null)));
        ArrayList<MapOperation> ops1 = new ArrayList<>(Arrays.asList(
                new Put(3, 4), new Put(1, 4), new Scan(1, 2, null)));
        for(int j = 0; j < 1000; ++j) {
            KiWiIsLinearizableWithSpecificOperations(new ArrayList<>(Arrays.asList(ops0, ops1)));
        }
    }

    @Test
    public void explicitTest3() throws IOException, InterruptedException {
        ArrayList<MapOperation> ops0 = new ArrayList<>(Arrays.asList(
                new Put(1, 3), new Scan(1, 1, null)));
        ArrayList<MapOperation> ops1 = new ArrayList<>(Arrays.asList(
                new Put(1, 5), new Scan(1, 1, null)));
        for(int j = 0; j < 1000; ++j) {
            KiWiIsLinearizableWithSpecificOperations(new ArrayList<>(Arrays.asList(ops0, ops1)));
        }
    }

    @Test
    public void explicitTest5() throws IOException, InterruptedException {
        ArrayList<MapOperation> ops0 = new ArrayList<>(Collections.singletonList(
                (MapOperation) new Put(1, 3)));

        ArrayList<MapOperation> ops1 = new ArrayList<>(Arrays.asList(
                (MapOperation)new Scan(1, 1, null),
                new Get(1, null)));

        for(int j = 0; j < 1000; ++j) {
            KiWiIsLinearizableWithSpecificOperations(new ArrayList<>(Arrays.asList(ops0, ops1)));
        }
    }

    @Test
    public void explicitTest4() throws IOException, InterruptedException {
        ArrayList<MapOperation> ops0 = new ArrayList<>(Arrays.asList(
                new Put(1, 3), new Get(1, null)));
        ArrayList<MapOperation> ops1 = new ArrayList<>(Arrays.asList(
                new Put(1, 5), new Get(1, null)));
        for(int j = 0; j < 1000; ++j) {
            KiWiIsLinearizableWithSpecificOperations(new ArrayList<>(Arrays.asList(ops0, ops1)));
        }
    }

    @Test
    public void explicitTest2() throws IOException, InterruptedException {
        ArrayList<MapOperation> ops0 = new ArrayList<>(Arrays.asList(
                new Put(1, 2), new Put(1, 3), new Scan(1, 1, null)));
        ArrayList<MapOperation> ops1 = new ArrayList<>(Arrays.asList(
                new Put(1, 4), new Put(1, 3), new Put(1, 3), new Scan(1, 1, null)));
        for(int j = 0; j < 1000; ++j) {
            KiWiIsLinearizableWithSpecificOperations(new ArrayList<>(Arrays.asList(ops0, ops1)));
        }
    }

    @Test
    public void explicitTest6() throws IOException, InterruptedException {
        ArrayList<MapOperation> ops0 = new ArrayList<>(Arrays.asList(
                new Put(1, 4), (MapOperation)new Put(1, 2)));
        ArrayList<MapOperation> ops1 = new ArrayList<>(Arrays.asList(
                new Put(2, 7), new Get(1, null), new Put(1, 3), new Scan(1, 1, null)));
        for(int j = 0; j < 1000; ++j) {
            KiWiIsLinearizableWithSpecificOperations(new ArrayList<>(Arrays.asList(ops0, ops1)));
        }
    }

    private void KiWiIsLinearizableWithSpecificOperations(ArrayList<ArrayList<MapOperation>> opsLists) throws InterruptedException, IOException {
        KiWiMap map = new KiWiMap(true);
        ArrayList<Thread> threads = new ArrayList<>();
        String tempFolder = Utils.tempFolder();
        for (ArrayList<MapOperation> ops: opsLists) {
            Thread t = new Thread(new ThreadOperateOnMap(map, ops));
            t.start();
            threads.add(t);
        }
        for (Thread t: threads) {
            t.join();
        }
        map.write(tempFolder);

        HistoryJsonReader reader = new HistoryJsonReader(tempFolder, threads.size());
        History history = reader.read();
        boolean isLinearizable = history.isLinearizable();
        if(!isLinearizable) {
            System.out.println(history);
            System.out.println(tempFolder);
            assertTrue(false);
        }else {
            Utils.clearFolder(tempFolder);
        }
    }

    @Test
    public void KiWiIsLinearizableOperationsFromHistoryFiles() throws InterruptedException, IOException {
        String[] folders = {"/tmp/history_53671261090936", "/tmp/history_52836544755222", "/tmp/history_47649424676670"};
        System.out.println(Utils.tempFolder());
        for(String folder: folders) {
            File f = new File(folder);
            if(!f.exists() || !f.isDirectory())
                continue;
            HistoryJsonReader reader = new HistoryJsonReader(folder, NUM_THREADS);
            History history = reader.read();
            ArrayList<ArrayList<MapOperation>> opsLists = new ArrayList<>();
            for (ArrayList<TimedOperation> timedOps : history.getHistories()) {
                ArrayList<MapOperation> ops = new ArrayList<>();
                for (TimedOperation timedOp : timedOps) {
                    ops.add(timedOp.operation);
                }
                opsLists.add(ops);
            }
            for (int j = 0; j < 100; ++j) {
                KiWiIsLinearizableWithSpecificOperations(opsLists);
            }
        }
    }

    @Test
    public void KiWiIsLinearizableRandomOperations() throws InterruptedException, IOException {
        for(int j = 0; j < 10000; ++j){
            if(j % 100 == 0) {
                System.out.println(j);
            }
            ArrayList<ArrayList<MapOperation>> opsLists = new ArrayList<>();
            for(int i = 0; i < NUM_THREADS; ++i){
                ArrayList<MapOperation> ops = randomOperations(j * NUM_THREADS + i,
                        3, 5, 0.6, 0.2);
                opsLists.add(ops);
            }
            KiWiIsLinearizableWithSpecificOperations(opsLists);
        }
    }
}

