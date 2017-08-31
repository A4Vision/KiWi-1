package linearizability_test;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

import com.google.gson.Gson;
import com.google.gson.stream.JsonReader;


class SerializableOperationsList{
    SerializableOperationsList(ArrayList<TimedOperation> list){
        get = new ArrayList<>();
        get_times = new ArrayList<>();
        put = new ArrayList<>();
        put_times  = new ArrayList<>();
        discard = new ArrayList<>();
        discard_times= new ArrayList<>();
        for(TimedOperation timed: list){
            if(timed.operation.getClass() == Get.class){
                get.add((Get)(timed.operation));
                get_times.add(timed.interval);
            } else if(timed.operation.getClass() == Put.class){
                put.add((Put)(timed.operation));
                put_times.add(timed.interval);
            } else if(timed.operation.getClass() == Discard.class){
                discard.add((Discard)(timed.operation));
                discard_times.add(timed.interval);
            }
        }
    }

    ArrayList<TimedOperation> getSortedList(){
        System.out.format("#get %d #put %d #discard %d", get.size(), put.size(), discard.size());
        ArrayList<TimedOperation> res = new ArrayList<>();
        for(int i = 0; i < get.size(); ++i){
            res.add(new TimedOperation(get.get(i), get_times.get(i)));
        }
        for(int i = 0; i < put.size(); ++i){
            res.add(new TimedOperation(put.get(i), put_times.get(i)));
        }
        for(int i = 0; i < discard.size(); ++i){
            res.add(new TimedOperation(discard.get(i), discard_times.get(i)));
        }
        res.sort(TimedOperation.StartTimeComparator);
        return res;
    }

    private ArrayList<Get> get;
    private ArrayList<Interval> get_times;
    private ArrayList<Put> put;
    private ArrayList<Interval> put_times;
    private ArrayList<Discard> discard;
    private ArrayList<Interval> discard_times;
}


/**
 * Created by bugabuga on 29/08/17.
 * Writes a Json file for the operations logged by this core.
 */
public class HistoryJsonWriter {
    HistoryJsonWriter(String dir, int core){
        _core = core;
        _filepath = Paths.get(dir, Integer.toString(core) + ".json");
        _operations = new ArrayList<>();
        _dir = dir;
    }

    void addOperation(TimedOperation op){
        _operations.add(op);
    }

    void write() throws IOException{
        if(!Files.exists(Paths.get(_dir))){
            System.out.println("creating directory...");
            Files.createDirectories(Paths.get(_dir));
        }
        Gson gson = new Gson();
        try(FileWriter file = new FileWriter(_filepath.toString())){
            SerializableOperationsList obj = new SerializableOperationsList(_operations);
            gson.toJson(obj, file);
            System.out.println("Done saving");
        }  catch (IOException e){
            System.out.println("Error saving");
            e.printStackTrace();
        }
    }

    private int _core;
    private Path _filepath;
    private String _dir;
    private ArrayList<TimedOperation> _operations;
}



class HistoryJsonReader{
    HistoryJsonReader(String dir, int max_cores){
        _dir = dir;
        _max_cores = max_cores;
    }

    History read(){
        ArrayList<ArrayList<TimedOperation>> concurrent_history = new ArrayList<>();
        for(int i = 0; i < _max_cores; ++i){
            Path filepath = Paths.get(_dir, Integer.toString(i) + ".json");
            Gson gson = new Gson();
            try(FileReader file = new FileReader(filepath.toFile())){
                JsonReader reader = new JsonReader(file);
                SerializableOperationsList obj = gson.fromJson(reader, SerializableOperationsList.class);
                concurrent_history.add(obj.getSortedList());
                System.out.println("Done loading");
            }  catch (IOException e){
                System.out.println("Error loading");
                e.printStackTrace();
                concurrent_history.add(new ArrayList<TimedOperation>());
            }
        }
        return new History(concurrent_history);
    }

    private String _dir;
    private int _max_cores;
}

class WriteReadExample {
    public static void main(String[] args) throws IOException {
        String dir = Paths.get(System.getProperty("user.home"),
                "histories", "tmp1").toString();
        for(int i = 0; i < 4; ++i) {
            HistoryJsonWriter writer = new HistoryJsonWriter(dir, i);
            if(i == 0){
                writer.addOperation(new TimedOperation(new Put(10, 20), new Interval(3, 4)));
            }
            writer.addOperation(new TimedOperation(new Get(10, 20), new Interval(5, 20)));

            writer.write();
        }

        HistoryJsonReader reader = new HistoryJsonReader(dir, 4);
        History history = reader.read();
        System.out.println(history.is_linearizable());
    }
}
