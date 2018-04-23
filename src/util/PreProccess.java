package util;

import datastructur.SuffixArray;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;

/**
 * Created by Morten on 19-04-2018.
 */
public class PreProccess {
    public static void main(String[] args){
        SuffixArray array = SuffixArray.suffixArrayUsingSort("aefsgfsef");

        FileOutputStream fos = null;


        try {
            fos = new FileOutputStream("t.tmp");
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            oos.writeObject(array);
            oos.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
