package linearizability_test;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

import com.google.gson.Gson;

/**
 * Created by bugabuga on 29/08/17.
 */
public class HistoryJsonWriter {
    public static void main(String[] args) {
        Gson gson = new Gson();
        Integer[] intArray = { 1,2,3,4,5 };
        TreeMap<Integer, Integer> m = new TreeMap<>();
        AbstractMap.SimpleImmutableEntry<Integer, Integer> a = new AbstractMap.SimpleImmutableEntry<Integer, Integer>(1, 2);
        AbstractMap.SimpleImmutableEntry<Integer, Integer> b = new AbstractMap.SimpleImmutableEntry<Integer, Integer>(3, 4);
        ArrayList<AbstractMap.SimpleImmutableEntry<Integer, Integer>> pairs = new ArrayList<AbstractMap.SimpleImmutableEntry<Integer, Integer>>(Arrays.asList(a ,b));
        Put p = new Put(12, 3);
        ArrayList<Put> pl = new ArrayList<>(Arrays.asList(p, p));
        Get g = new Get(123, 456);
        Scan s = new Scan(0.4, 5.6, pairs);
        ArrayList<MapOperation> ml = new ArrayList<>(Arrays.asList(p, g, s));
        m.put(12, 124);
        m.put(4, 51);
        ArrayList<Integer> lb = new ArrayList<>(Arrays.asList(intArray));
        try(FileWriter file = new FileWriter("/tmp/a.json")){
//            gson.toJson(lb, file);
//            gson.toJson(m, file);
//            gson.toJson(p, file);
//            gson.toJson(pl, file);
            gson.toJson("list of operations", file);
            gson.toJson(ml, file);
            gson.toJson("scan", file);
            gson.toJson(s, file);
            gson.toJson("list of pairs", file);
            gson.toJson(pairs, file);
            System.out.println("HistoryJsonWriter.main");
            System.out.println("DONE");
            System.out.println(((Scan)ml.get(2))._retval.get(0).getKey());
        }  catch (IOException e){
            System.out.println(e.getStackTrace()[0].toString());
        }

    }
}
