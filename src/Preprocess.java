import builder.SuffixArrayBuilder;
import datastructur.SuffixArray;

public class Preproccess {
    public static void main(String[] args) {
        if(args.length<1) {
            System.out.println("Missing arguments");
            return;
        }

        String filename = args[0];

        SuffixArray suffixArray = SuffixArrayBuilder.buildFromFile(filename);
        SuffixArrayBuilder.save(suffixArray, filename.replaceAll(".fa",""));
    }
}
