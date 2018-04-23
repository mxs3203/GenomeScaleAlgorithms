import builder.SuffixArrayBuilder;
import datastructur.SuffixArray;

public class Preprocess {
    public static void main(String[] args) {
        if(args.length<1) {
            System.out.println("Missing arguments");
            return;
        }

        String filename = args[0];
        String name = filename.replaceAll(".fa","");

        SuffixArray suffixArray = SuffixArrayBuilder.buildFromFile(filename);
        SuffixArrayBuilder.save(suffixArray, name);
    }
}