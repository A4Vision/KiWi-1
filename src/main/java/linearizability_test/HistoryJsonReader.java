package linearizability_test;

import com.google.gson.Gson;
import com.google.gson.stream.JsonReader;

import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;

public class HistoryJsonReader{
    public HistoryJsonReader(String dir, int max_cores){
        directory = dir;
        maxCores = max_cores;
    }

    public History read(){
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
