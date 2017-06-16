/**
 * Created by bugabuga on 16/06/17.
 */


import kiwi.KiWiMap;

import java.util.concurrent.atomic.LongAdder;


class A{
    Integer _t;
    A(Integer t){
        _t = t;
    }
    public void foo(){
        kiwi.KiWiMap a = new kiwi.KiWiMap();
        a.put(10, 123);
        for(Integer i = 0; i < _t; ++i) {
            Integer x = a.get(i);
            if (x == null) {
                System.out.println("value of " + i.toString() + " not found");
            } else {
                System.out.println("value of " + i.toString() + " is " + x.toString());
            }
        }
        System.out.println(a.size());

    }


}


class MyThread implements Runnable {
    Integer _x;
    LongAdder _adder;
    MyThread(LongAdder adder, Integer x){
        _x = x;
        _adder = adder;
    }
    @Override
    public void run() {
        _adder.add(_x);
        for(Integer i = 0; i < 123456; ++i){
            if(i == _x){
                System.out.println(_x);
            }
        }
        System.out.println(_adder.sum());
    }
}


class HelloWorldApp {
    public static void main(String[] args) {
//        A a = new A(15);
//        a.foo();
//        Integer x = 3;
//        System.out.println("Hello World!"); // Display the string.
        LongAdder adder = new LongAdder();
        for(Integer i = 0; i < 10; ++i) {
            Thread t = new Thread(new MyThread(adder, i));
            t.start();
        }
    }

}



