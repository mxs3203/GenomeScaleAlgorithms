import builder.SuffixArrayBuilder;
import datastructur.SuffixArray;
import model.Fastq;
import util.FastaUtil;
import util.FastqUtil;

import java.util.Map;

public class Run {
    public static void main(String[] args) {
        /*if (args.length < 3) {
            System.out.println("Missing arguments");
            return;
        }

        int d = Integer.parseInt(args[0]);//TODO er måske ikke nødvendig i exact pattern matching
        String referenceFilename = args[1];
        String readsFilename = args[2];

        SuffixArray suffixArray = SuffixArrayBuilder.reconstruct(referenceFilename.replace(".fa", ""));
        Map<String, Fastq> fastqMap = FastqUtil.parseTo(readsFilename);
        for (String key : fastqMap.keySet()) {
            Fastq current = fastqMap.get(key);
            //TODO do some matching here :)
        }*/
    }
}
