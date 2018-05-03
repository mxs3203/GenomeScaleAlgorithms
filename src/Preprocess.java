import algorithm.SuffixArray12;
import builder.SuffixArrayBuilder;
import builder.SuffixTreeBuilder;
import datastructur.SuffixArray;
import datastructur.SuffixTree;
import util.FastaUtil;
import util.FileUtil;

import java.util.Map;

public class Preprocess {
    public static void main(String[] args) {
        if(args.length<1) {
            System.out.println("Missing arguments");
            return;
        }

        String filename = args[0];
        String name = filename.replaceAll(".fa","");

        if(FileUtil.exist("data/" + name + "/"))
            return;
        Map<String, String> fastaEntries = FastaUtil.parseTo(filename);
        for(String id: fastaEntries.keySet()) {
            SuffixArray suffixArray = SuffixArray12.build(id, fastaEntries.get(id));
            SuffixArrayBuilder.save(suffixArray, name, id);

            //SuffixTree tree = new SuffixTree(fastaEntries.get(id), id);
            //SuffixTreeBuilder.save(tree, name, id);
        }
    }
}