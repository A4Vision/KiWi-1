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
        getTimes = new ArrayList<>();
        put = new ArrayList<>();
        putTimes = new ArrayList<>();
        discard = new ArrayList<>();
        discardTimes = new ArrayList<>();
        for(TimedOperation timed: list){
            if(timed.operation.getClass() == Get.class){
                get.add((Get)(timed.operation));
                getTimes.add(timed.interval);
            } else if(timed.operation.getClass() == Put.class){
                put.add((Put)(timed.operation));
                putTimes.add(timed.interval);
            } else if(timed.operation.getClass() == Discard.class){
                discard.add((Discard)(timed.operation));
                discardTimes.add(timed.interval);
            }
        }
    }

    ArrayList<TimedOperation> getSortedList(){
        System.out.format("#get %d #put %d #discard %d", get.size(), put.size(), discard.size());
        ArrayList<TimedOperation> res = new ArrayList<>();
        for(int i = 0; i < get.size(); ++i){
            res.add(new TimedOperation(get.get(i), getTimes.get(i)));
        }
        for(int i = 0; i < put.size(); ++i){
            res.add(new TimedOperation(put.get(i), putTimes.get(i)));
        }
        for(int i = 0; i < discard.size(); ++i){
            res.add(new TimedOperation(discard.get(i), discardTimes.get(i)));
        }
        res.sort(TimedOperation.StartTimeComparator);
        return res;
    }

    private ArrayList<Get> get;
    private ArrayList<Interval> getTimes;
    private ArrayList<Put> put;
    private ArrayList<Interval> putTimes;
    private ArrayList<Discard> discard;
    private ArrayList<Interval> discardTimes;
}


/**
 * Created by bugabuga on 29/08/17.
 * Writes a Json file for the operations logged by this core.
 */
public class HistoryJsonWriter {
    HistoryJsonWriter(String dir, int core){
        this.core = core;
        filepath = Paths.get(dir, Integer.toString(core) + ".json");
        operations = new ArrayList<>();
        directory = dir;
    }

    void addOperation(TimedOperation op){
        operations.add(op);
    }

    void write() throws IOException{
        if(!Files.exists(Paths.get(directory))){
            System.out.println("creating directory...");
            Files.createDirectories(Paths.get(directory));
        }
        Gson gson = new Gson();
        try(FileWriter file = new FileWriter(filepath.toString())){
            SerializableOperationsList obj = new SerializableOperationsList(operations);
            gson.toJson(obj, file);
            System.out.println("Done saving");
        }  catch (IOException e){
            System.out.println("Error saving");
            e.printStackTrace();
        }
    }

    private int core;
    private Path filepath;
    private String directory;
    private ArrayList<TimedOperation> operations;
}



class HistoryJsonReader{
    HistoryJsonReader(String dir, int max_cores){
        directory = dir;
        maxCores = max_cores;
    }

    History read(){
        ArrayList<ArrayList<TimedOperation>> concurrent_history = new ArrayList<>();
        for(int i = 0; i < maxCores; ++i){
            Path filepath = Paths.get(directory, Integer.toString(i) + ".json");
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

    private String directory;
    private int maxCores;
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
