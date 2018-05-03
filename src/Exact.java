import algorithm.SuffixArray12;
import datastructur.SuffixArray;
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

        SuffixArray suffixArray = SuffixArray12.build(FastaUtil.getFirst(referenceFilename), "chr1");
        Map<String, Fastq> fastqMap = FastqUtil.parseTo(readsFilename);
        for (String key : fastqMap.keySet()) {
            Fastq current = fastqMap.get(key);
            String sequences = current.getSeq();
            List<Integer> positions = suffixArray.bw(sequences.toCharArray());
            for (int pos : positions) {
                System.out.println(key + "\t0\t" + suffixArray.getId() + "\t" + (pos+1) + "\t0\t"
                        + sequences.length() + "M\t*\t0\t0\t" + sequences + "\t" + current.getQuality());
            }
        }
    }
}
