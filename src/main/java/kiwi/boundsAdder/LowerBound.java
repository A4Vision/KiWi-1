package kiwi.boundsAdder;

import java.util.concurrent.atomic.LongAdder;

/**
 * Created by bugabuga
 * n 02/09/17.
 */

public class LowerBound{
    private LongAdder adder;

    public LowerBound(LongAdder adder){
        this.adder = adder;
    }

    public Integer value(){
        if(adder == null){
            return Integer.MAX_VALUE;
        }
        return adder.intValue();
    }

    public void maybeAboutToRemove(){
        if(adder != null) {
            adder.decrement();
        }
    }

    public void certainlyAdded(){
        if(adder != null){
            adder.increment();
        }
    }

    public void certainlyNotRemoved(){
        if(adder != null){
            adder.increment();
        }
    }
}
