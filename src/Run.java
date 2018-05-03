import builder.SuffixArrayBuilder;
import builder.SuffixTreeBuilder;
import com.sun.org.apache.xpath.internal.SourceTree;
import datastructur.SuffixArray;
import datastructur.SuffixTree;
import model.Fastq;
import model.Interval;
import model.Pair;
import util.CigarUtil;
import util.FastaUtil;
import util.FastqUtil;

import java.util.List;
import java.util.Map;

public class Run {
    public static void main(String[] args) {
        if (args.length < 3) {
            System.out.println("Missing arguments");
            return;
        }

        int d = Integer.parseInt(args[0]);//TODO er måske ikke nødvendig i exact pattern matching
        String referenceFilename = args[1];
        String readsFilename = args[2];


        if( d>0) {
            suffixArrayInexact(d, referenceFilename, readsFilename);
        } else {
           suffixArrayExact(referenceFilename, readsFilename);
        }
    }

    private static void suffixArrayInexact(int d, String referenceFilename, String readsFilename) {
        //long startTime = System.nanoTime();
        List<SuffixArray> suffixArrayList = SuffixArrayBuilder.reconstruct(referenceFilename.replace(".fa", ""));
        //long middleTime = System.nanoTime();
        //System.out.println("Done loading!");
        Map<String, Fastq> fastqMap = FastqUtil.parseTo(readsFilename);
        for (SuffixArray suffixArray : suffixArrayList) {
            for (String key : fastqMap.keySet()) {
                Fastq current = fastqMap.get(key);
                String sequens = current.getSeq();
                List<Pair<String,Interval>> positions = suffixArray.matchesOfKDistance(sequens.toCharArray(), d);
                for (Pair<String,Interval> pr : positions) {
                    String cigar = CigarUtil.compress(pr.getFirst());
                    Interval pos = pr.getSecond();
                    for(int i = pos.getL(); i<=pos.getR(); i++ )
                    System.out.println(key + "\t0\t" + suffixArray.getId() + "\t" + (suffixArray.getSuffixArray()[i]+1) + "\t0\t"
                            + cigar + "\t*\t0\t0\t" + sequens + "\t" + current.getQuality());
                }
            }
        }
        //long endTime = System.nanoTime();
        //System.out.println("load pre: " + (middleTime - startTime));
        //System.out.println("matching: " + (endTime - middleTime));
    }

    private static void suffixArrayExact(String referenceFilename, String readsFilename) {
        //long startTime = System.nanoTime();
        List<SuffixArray> suffixArrayList = SuffixArrayBuilder.reconstruct(referenceFilename.replace(".fa", ""));
        //long middleTime = System.nanoTime();
        Map<String, Fastq> fastqMap = FastqUtil.parseTo(readsFilename);
        for (SuffixArray suffixArray : suffixArrayList) {
            for (String key : fastqMap.keySet()) {
                Fastq current = fastqMap.get(key);
                String sequens = current.getSeq();
                List<Integer> positions = suffixArray.bw(sequens.toCharArray());
                for (int pos : positions) {
                    System.out.println(key + "\t0\t" + suffixArray.getId() + "\t" + (pos + 1) + "\t0\t"
                            + sequens.length() + "M\t*\t0\t0\t" + sequens + "\t" + current.getQuality());
                }
            }
        }
        //long endTime = System.nanoTime();
        //System.out.println("load pre: " + (middleTime-startTime));
        //System.out.println("matching: " + (endTime - middleTime));
    }
}
