package builder;

import datastructur.SuffixArray;
import util.FastaUtil;
import util.FileUtil;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class SuffixArrayBuilder {
    public static void save(SuffixArray suffixArray, String name, String id) {
        int[][] oTable = suffixArray.getOTable();
        int[][] o_Table = suffixArray.getO_Table();
        int[] cTable = suffixArray.getCTable();
        int[] sa = suffixArray.getSuffixArray();
        char[] text = suffixArray.getText();
        //TODO måske også gem alphabetet og alphabetMap
        FileUtil.saveObject(oTable, "data/" + name + "/" + id + "/","oTable.tmp");
        FileUtil.saveObject(o_Table, "data/" + name + "/" + id + "/","o-Table.tmp");
        FileUtil.saveObject(cTable, "data/" + name + "/" + id + "/","cTable.tmp");
        FileUtil.saveObject(sa, "data/" + name + "/" + id + "/","suffixArray.tmp");
        FileUtil.saveObject(text, "data/" + name + "/" + id + "/","text.tmp");
    }

    public static List<SuffixArray> reconstructExact(String name) {
        List<SuffixArray> suffixArrayList = new ArrayList<>();
        File[] directories = new File("data/" + name + "/").listFiles(File::isDirectory);
        for(File file: directories){
            String id = file.getName();
            int[][] oTable = (int[][]) FileUtil.loadObject("data/" + name + "/" + id + "/oTable.tmp");
            int[] cTable = (int[]) FileUtil.loadObject("data/" + name + "/" + id + "/cTable.tmp");
            int[] sa = (int[]) FileUtil.loadObject("data/" + name + "/" + id + "/suffixArray.tmp");
            char[] text = (char[]) FileUtil.loadObject("data/" + name + "/" + id + "/text.tmp");
            suffixArrayList.add(new SuffixArray(text, sa, id, oTable, cTable));
        }
        return suffixArrayList;
    }

    public static List<SuffixArray> reconstructInExact(String name) {
        List<SuffixArray> suffixArrayList = new ArrayList<>();
        File[] directories = new File("data/" + name + "/").listFiles(File::isDirectory);
        for(File file: directories){
            String id = file.getName();
            int[][] oTable = (int[][]) FileUtil.loadObject("data/" + name + "/" + id + "/oTable.tmp");
            int[][] o_Table = (int[][]) FileUtil.loadObject("data/" + name + "/" + id + "/o-Table.tmp");
            int[] cTable = (int[]) FileUtil.loadObject("data/" + name + "/" + id + "/cTable.tmp");
            int[] sa = (int[]) FileUtil.loadObject("data/" + name + "/" + id + "/suffixArray.tmp");
            char[] text = (char[]) FileUtil.loadObject("data/" + name + "/" + id + "/text.tmp");
            suffixArrayList.add(new SuffixArray(text, sa, id, oTable, o_Table, cTable));
        }
        return suffixArrayList;
    }

    public static List<SuffixArray> reconstruct(String name){
        List<SuffixArray> suffixArrayList = new ArrayList<>();
        File[] directories = new File("data/" + name + "/").listFiles(File::isDirectory);
        for(File file: directories){
            suffixArrayList.add((SuffixArray) FileUtil.loadObject("data/" + name + "/" + file.getName() + "/suffixArray.tmp"));
        }
        return suffixArrayList;
    }

    public static SuffixArray buildFromFile(String filename) {
        SuffixArray suffixArray = SuffixArray.suffixArrayUsingSort(FastaUtil.getFirst(filename));
        return suffixArray;
    }

    /*public static void save(SuffixArray suffixArray, String name, String id) {
        FileUtil.saveObject(suffixArray, "data/" + name + "/" + id + "/","suffixArray.tmp");
    }*/
}
