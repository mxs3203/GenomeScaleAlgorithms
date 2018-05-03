package util;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

public class FastaUtil {
    public static Map<String, String> parseTo(String filename){
        Map<String, String> result = new HashMap<>();

        FileReader file = null;
        try {
            file = new FileReader(filename);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        BufferedReader bufferedReader = new BufferedReader(file);
        String id = null;
        StringBuilder seq = new StringBuilder();
        for(String line: bufferedReader.lines().collect(Collectors.toList())){
            if(line.charAt(0) == '>'){
                if(id != null){
                    result.put(id, seq.toString());
                    seq = new StringBuilder();
                }
                id = line.substring(1, line.length()).trim();
            } else {
                seq.append(line.trim());
            }
        }
        
        if(id != null){
            result.put(id, seq.toString());
        }

        return result;
    }

    public static String getFirst(String filename) {

        FileReader file = null;
        try {
            file = new FileReader(filename);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        BufferedReader bufferedReader = new BufferedReader(file);
        String id = null;
        StringBuilder seq = new StringBuilder();
        for(String line: bufferedReader.lines().collect(Collectors.toList())){
            if(line.charAt(0) == '>'){
                if(id != null){
                    return seq.toString();
                }
                id = line.substring(1, line.length()).trim();
            } else {
                seq.append(line.trim());
            }
        }

        return seq.toString();
    }
}
