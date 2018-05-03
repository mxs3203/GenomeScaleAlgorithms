package builder;

import datastructur.SuffixArray;
import datastructur.SuffixTree;
import util.FileUtil;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class SuffixTreeBuilder {
    public static void save(SuffixTree suffixTree, String name, String id) {
        FileUtil.saveObject(suffixTree, "data/" + name + "/" + id + "/","suffixTree.tmp");
    }

    public static List<SuffixTree> reconstruct(String name){
        List<SuffixTree> suffixTreeList = new ArrayList<>();
        File[] directories = new File("data/" + name + "/").listFiles(File::isDirectory);
        for(File file: directories){
            suffixTreeList.add((SuffixTree) FileUtil.loadObject("data/" + name + "/" + file.getName() + "/suffixTree.tmp"));
        }
        return suffixTreeList;
    }
}
