package util;

import datastructur.SuffixArray;

import java.io.*;

/**
 * Created by Morten on 19-04-2018.
 */
public class ReadBinary {

    public static void main(String[] args){
        FileInputStream fstream = null;
        try {
            fstream = new FileInputStream("t.tmp");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        try {
            ObjectInputStream ostream = new ObjectInputStream(fstream);
            while (true) {
                SuffixArray array;
                try {
                    array = (SuffixArray) ostream.readObject();
                } catch (EOFException e) {
                    break;
                }
                // do something with obj
                System.out.println(array.toString());
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } finally {
            try {
                fstream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
