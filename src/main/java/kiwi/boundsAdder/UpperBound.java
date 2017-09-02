package kiwi.boundsAdder;

import java.util.concurrent.atomic.LongAdder;

/**
 * Created by bugabuga on 02/09/17.
 */
public class UpperBound{
    private LongAdder adder;

    public UpperBound(LongAdder adder){
        this.adder = adder;
    }

    public Integer value(){
        if(adder == null){
            return Integer.MAX_VALUE;
        }
        return adder.intValue();
    }

    public void maybeAboutToAdd(){
        if(adder != null) {
            adder.increment();
        }
    }

    public void certainlyRemoved(){
        if(adder != null){
            adder.decrement();
        }
    }

    public void certainlyNotAdded(){
        if(adder != null){
            adder.decrement();
        }
    }
}
