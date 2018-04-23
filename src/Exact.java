import builder.SuffixArrayBuilder;
import datastructur.SuffixArray;
import datastructur.SuffixTree;
import model.Fastq;
import util.FastaUtil;
import util.FastqUtil;

import java.util.List;
import java.util.Map;

public class Exact {

    public static void main(String[] args) {
        if (args.length < 3) {
            System.out.println("Missing arguments");
            return;
        }

        int d = Integer.parseInt(args[0]);//TODO er måske ikke nødvendig i exact pattern matching
        String referenceFilename = args[1];
        String readsFilename = args[2];

        //SuffixArray suffixArray = SuffixArrayBuilder.buildFromFile(referenceFilename);
        SuffixTree suffixTree = new SuffixTree(FastaUtil.getFirst(referenceFilename));

        Map<String, Fastq> fastqMap = FastqUtil.parseTo(readsFilename);
        int counter = 0;
        for (String key : fastqMap.keySet()) {
            Fastq current = fastqMap.get(key);
            String sequens = current.getSeq();
            List<Integer> positions = suffixTree.match(sequens.toCharArray());
            for (int pos : positions) {
                System.out.println(key + "\t0\tchr1\t" + (pos+1) + "\t0\t"
                        + sequens.length() + "M\t*\t0\t0\t" + sequens + "\t" + current.getQuality());
            }
            counter++;
        }
    }
}
