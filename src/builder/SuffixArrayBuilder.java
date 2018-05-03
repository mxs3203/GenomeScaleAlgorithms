package builder;

import datastructur.SuffixArray;
import util.FastaUtil;
import util.FileUtil;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class SuffixArrayBuilder {
    /*public static void save(SuffixArray suffixArray, String name) {
        int[][] oTable = suffixArray.getOTable();
        int[] cTable = suffixArray.getCTable();
        int[] sa = suffixArray.getSuffixArray();
        char[] text = suffixArray.getText();
        //TODO måske også gem alphabetet og alphabetMap
        FileUtil.saveObject(oTable, "data/" + name + "/oTable.tmp");
        FileUtil.saveObject(cTable, "data/" + name + "/cTable.tmp");
        FileUtil.saveObject(sa, "data/" + name + "/suffixArray.tmp");
        FileUtil.saveObject(text, "data/" + name + "/text.tmp");
    }

    public static SuffixArray reconstruct(String name) {
        int[][] oTable = (int[][]) FileUtil.loadObject("data/" + name + "/oTable.tmp");
        int[] cTable = (int[]) FileUtil.loadObject("data/" + name + "/cTable.tmp");
        int[] sa = (int[]) FileUtil.loadObject("data/" + name + "/suffixArray.tmp");
        char[] text = (char[]) FileUtil.loadObject("data/" + name + "/text.tmp");
        //TODO måske også hent alphabetet og alphabetMap
        return new SuffixArray(sa, text, oTable, cTable);
    }*/

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

    public static void save(SuffixArray suffixArray, String name, String id) {
        FileUtil.saveObject(suffixArray, "data/" + name + "/" + id + "/","suffixArray.tmp");
    }
}
