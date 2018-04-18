package util;

import model.Fastq;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

public class FastqUtil {
    public static Map<String, Fastq> parseTo(String filename){
        Map<String, Fastq> result = new HashMap<>();

        FileReader file = null;
        try {
            file = new FileReader(filename);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        BufferedReader bufferedReader = new BufferedReader(file);
        String id = null;
        String seq = "";
        String quality = "";
        boolean readingSeq = false;
        for(String line: bufferedReader.lines().collect(Collectors.toList())){
            if(line.charAt(0) == '@') {
                if (id != null) {
                    result.put(id, new Fastq(seq, quality));
                    seq = "";
                    quality = "";
                    readingSeq = true;
                }
                id = line.substring(1, line.length()).trim();
            } else if(line.charAt(0) == '+'){
                readingSeq = false;
            } else if(readingSeq){
                seq += line.trim();
            } else {
                quality += line.trim();
            }
        }

        if(id != null){
            result.put(id, new Fastq(seq, quality));
        }

        return result;
    }
}
