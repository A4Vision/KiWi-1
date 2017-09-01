/**
 * Created by bugabuga on 16/06/17.
 */


//import java.util.concurrent.atomic.LongAdder;
import kiwi.KiWiMap;
import linearizability_test.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;
//import java.util.concurrent.atomic.LongAdder;


//class A{
//    Integer _t;
//    A(Integer t){
//        _t = t;
//    }
//
//    public void foo(){
//        kiwi.KiWiMap a = new kiwi.KiWiMap();
//        a.put(10, 123);
//        for(Integer i = 0; i < _t; ++i) {
//            Integer x = a.get(i);
//            if (x == null) {
//                System.out.println("value of " + i.toString() + " not found");
//            } else {
//                System.out.println("value of " + i.toString() + " is " + x.toString());
//            }
//        }
//        System.out.println(a.size());
//
//    }
//
//
//}


class MyThread implements Runnable {
    private KiWiMap map;
    private Random generator;
    private Integer[] tmpRange;

    MyThread(KiWiMap map, long seed){
        this.map = map;
        generator = new Random(seed);
        tmpRange = new Integer[1000];

    }

    @Override
    public void run() {
        for(int i = 0; i < 50; ++i){
            double r = generator.nextDouble();
            if(r < 0.3){
                map.put(generator.nextInt(100), generator.nextInt(10));
            }else if(r < 0.95){
                map.get(generator.nextInt(100));
            }else {
                map.getRange(tmpRange, 10, 20);
            }
        }
    }
}


class HelloWorldApp {
    static final int NUM_THREADS = 3;
    static final String path = "/home/bugabuga/histories/tmp3";

    static void createData() throws InterruptedException, IOException {
        KiWiMap map = new KiWiMap();
        ArrayList<Thread> threads = new ArrayList<>();

        for(Integer i = 0; i < NUM_THREADS ; ++i) {
            Thread t = new Thread(new MyThread(map, i));
            t.start();
            threads.add(t);
        }
        for(int i = 0; i < NUM_THREADS; ++i){
            threads.get(i).join();
        }
        map.write(path);
    }

    static void loadData(){
        HistoryJsonReader reader = new HistoryJsonReader(path, NUM_THREADS);
        History history = reader.read();
        System.out.println(history.isLinearizable());
    }

    public static void main(String[] args) throws InterruptedException, IOException {
        createData();
        loadData();
    }
}



