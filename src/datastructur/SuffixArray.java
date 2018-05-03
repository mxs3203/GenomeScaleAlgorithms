package datastructur;

import model.Interval;
import model.Pair;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

public class SuffixArray implements Serializable {
    private String id;
    private int[] suffixArray;
    private char[] text;
    private int n;

    private List<Character> alphabet;
    private HashMap<Character, Integer> alphabetMap;
    private int[] C;
    private int[][] O;
    private boolean[][] B;

    public SuffixArray(int[] suffixArray, char[] text) {
        this(suffixArray, text, "chr1");
    }

    public SuffixArray(int[] suffixArray, char[] text, String id) {
        this.id = id;
        this.suffixArray = suffixArray;
        this.text = text;
        this.n = text.length;
        buildAlphabet();
        buildCTable();
        buildOTable();
        buildBTable();
    }

    public List<Integer> binarySearch(char[] pattern) {
        int j = -1, L = 0, R = n - 1, M;
        do {
            M = (int) Math.ceil((R + L) / 2);
            if (equals(pattern, suffixArray[M])) {
                j = M;
            } else if (after(pattern, suffixArray[M])) {
                L = M;
            } else {
                R = M;
            }
        } while (!(L == R || L == R - 1 || j != -1));

        if (j == -1)
            return Collections.EMPTY_LIST;

        List<Integer> result = report(
                findLeft(pattern, L, M),
                findRight(pattern, M, R));
        return result;
    }

    private int findLeft(char[] pattern, int L, int R) {
        int j = -1, M = 0;
        do {
            M = (int) Math.ceil((R + L) / 2);
            if ((M == 0 || !equals(pattern, suffixArray[M - 1])) && equals(pattern, suffixArray[M])) {
                j = M;
            } else if (!equals(pattern, suffixArray[M])) {
                L = M;
            } else {
                R = M;
            }
        } while (!(L == R || L == R - 1 || j != -1));

        if (j == -1) return R;
        return j;
    }

    private int findRight(char[] pattern, int L, int R) {
        int j = -1, M;
        do {
            M = (int) Math.ceil((R + L) / 2);
            if ((M == suffixArray.length - 1 || !equals(pattern, suffixArray[M + 1])) && equals(pattern, suffixArray[M])) {
                j = M;
            } else if (equals(pattern, suffixArray[M])) {
                L = M;
            } else {
                R = M;
            }
        } while (!(L == R || L == R - 1 || j != -1));

        if (j == -1) {
            if (L != suffixArray.length - 1 && equals(pattern, suffixArray[M + 1])) {
                return L + 1;
            } else {
                return L;
            }
        }
        return j;
    }

    public List<Integer> bw(char[] w) {
        int m = w.length;
        int L = 0, R = n - 1, i = m - 1;
        while (i >= 0 && L <= R) {
            Integer wi = alphabetMap.get(w[i]);
            if (wi == null)
                return Collections.EMPTY_LIST;

            L = C[wi] + (L == 0 ? 0 : O[wi][L - 1]) + 1;
            R = C[wi] + O[wi][R];
            i--;
        }

        if (i < 0 && L <= R) {
            return report(L, R);
        } else {
            return Collections.EMPTY_LIST;
        }
    }

    public List<Pair<String,Interval>> matchesOfKDistance(char[] P, int k){
        return k_differences(P, P.length-1, k, 0, text.length-1, "");
    }

    private List<Pair<String,Interval>> k_differences(char[] P, int j, int d, int lb, int rb, String cigar){
        List<Pair<String,Interval>> I = new ArrayList<>();
        if(d < 0){
            return Collections.EMPTY_LIST;
        }
        if (j == -1){
            I.add(new Pair<>(cigar, new Interval(lb,rb)));
            return I;
        }
        if(d>0)
            I.addAll(k_differences(P, j-1, d-1, lb, rb, "D" + cigar)); //Deletion
        List<Pair<Integer, Interval>> list = getIntervals(lb, rb);
        for (Pair<Integer, Interval> pair: list){
            int c = pair.getFirst();
            Interval lbrb = pair.getSecond();
            lb = lbrb.getL();
            rb = lbrb.getR();
            if(d>0)
                I.addAll(k_differences(P, j, d-1,lb, rb, "I" + cigar)); //insertion
            if (alphabetMap.get(P[j]) == c){
                I.addAll(k_differences(P, j-1, d, lb, rb, "=" + cigar)); //match
            } else {
                if(d>0)
                    I.addAll(k_differences(P, j-1, d-1, lb, rb, "X" + cigar)); //substation
            }
        }
        return I;
    }

    private List<Integer> report(int L, int R) {
        List<Integer> result = new ArrayList<>();
        for (int i = L; i <= R; i++) {
            result.add(suffixArray[i]);
        }
        return result;
    }

    private boolean after(char[] pattern, int start) {
        for (int i = 0; i < pattern.length; i++) {
            if (i + start > text.length - 1)
                return true;
            if (pattern[i] > text[i + start])
                return true;
            if (pattern[i] < text[i + start])
                return false;
        }
        return false;
    }

    private boolean equals(char[] pattern, int start) {
        if (start + pattern.length > text.length) return false;

        for (int i = 0; i < pattern.length; i++) {
            if (pattern[i] != text[i + start])
                return false;
        }
        return true;
    }

    private void buildAlphabet() {
        alphabet = new ArrayList<>();
        alphabetMap = new HashMap<>();
        alphabet.add('$');
        alphabetMap.put('$', 0);
        alphabet.add('A');
        alphabetMap.put('A', 1);
        alphabet.add('C');
        alphabetMap.put('C', 2);
        alphabet.add('G');
        alphabetMap.put('G', 3);
        alphabet.add('T');
        alphabetMap.put('T', 4);
    }

    private void buildBTable(){
        List<List<Boolean>> b = new ArrayList<>();
        b.add(new ArrayList<>());
        b.add(new ArrayList<>());
        b.add(new ArrayList<>());
        b.add(new ArrayList<>());
        for(int i: suffixArray){
            int c = alphabetMap.get(text[i==0? text.length-1: i-1]);
            switch (c){
                case 0:
                    b.get(0).add(false);
                    b.get(1).add(false);
                    b.get(2).add(false);
                    break;
                case 1:
                    b.get(0).add(true);
                    b.get(1).add(false);
                    b.get(2).add(false);
                    break;
                case 2:
                    b.get(1).add(true);
                    b.get(2).add(false);
                    break;
                case 3:
                    b.get(2).add(true);
                    b.get(3).add(false);
                    break;
                case 4:
                    b.get(2).add(true);
                    b.get(3).add(true);
                    break;
            }
        }


        B = new boolean[][]{toBooleanArray(b.get(0)),
                toBooleanArray(b.get(1)),
                toBooleanArray(b.get(2)),
                toBooleanArray(b.get(3))};
    }

    private boolean[] toBooleanArray(List<Boolean> booleans) {
        boolean[] b = new boolean[booleans.size()];
        for (int i = 0; i < booleans.size(); i++) {
            b[i] = booleans.get(i);
        }
        return b;
    }

    private void buildCTable() {
        C = new int[alphabet.size()];
        C[0] = 0;
        int index = 0;
        for (int i = 1; i < suffixArray.length; i++) {
            if (text[suffixArray[i]] != text[suffixArray[i - 1]]) {
                index++;
                C[index] = i - 1;
            }
        }
    }

    private void buildOTable() {
        O = new int[alphabet.size()][suffixArray.length];
        for (int i = 0; i < suffixArray.length; i++) {
            for (int j = 0; j < alphabet.size(); j++) {
                int SAi = suffixArray[i];
                SAi = SAi == 0 ? text.length : SAi;
                if (i == 0) {
                    if (text[SAi - 1] == alphabet.get(j))
                        O[j][i] = 1;
                    else
                        O[j][i] = 0;
                } else {
                    if (text[SAi - 1]
                            == alphabet.get(j))
                        O[j][i] = O[j][i - 1] + 1;
                    else
                        O[j][i] = O[j][i - 1];
                }
            }
        }
    }

    public static SuffixArray suffixArrayUsingSort(String t) {

        //init
        char[] text = (t + "$").toCharArray();
        List<Integer> list = new ArrayList<>();
        int n = text.length;
        for (int i = 0; i < text.length; i++) {
            list.add(i);
        }

        //sort
        list.sort((i, j) -> {
            while (i < text.length && j < text.length && text[i] == text[j]) {
                i++;
                j++;
            }
            return text[i] - text[j];
        });


        //change format
        int[] suffixArray = new int[n];
        for (int i = 0; i < n; i++) {
            suffixArray[i] = list.get(i);
        }
        return new SuffixArray(suffixArray, text);
    }

    private List<Pair<Integer, Interval>> getIntervals(int i, int j) {
        ArrayList<Pair<Integer, Interval>> list = new ArrayList<>();
        getIntervals_(i+1, j+1, 0, alphabet.size() - 1, list);
        return list;
    }

    private void getIntervals_(int i, int j, int l, int r, List<Pair<Integer, Interval>> list) {
        if (l == r) {
            int c = l;
            if (c != 0){
                list.add(new Pair<>(c, new Interval(C[c]+i, C[c] + j)));
            }
        } else {
            int a0 = rank0(B(l,r), i - 1-1);
            int b0 = rank0(B(l,r), j-1);
            int a1 = i - 1 - a0;
            int b1 = j - b0;
            int m = (int) ((l + r) / 2.0);
            if (b0 > a0){
                getIntervals_(a0+1, b0, l, m, list);
            }
            if (b1 > a1){
                getIntervals_(a1+1, b1, m + 1, r, list);
            }
        }
    }

    private boolean[] B(int l, int r) {
        if(l == 0 && r == 1){
            return B[0];
        }
        if(l == 0 && r == 2){
            return B[1];
        }
        if(l == 0 && r == 4){
            return B[2];
        }
        return B[3];
    }

    private int rank0(boolean[] B, int x) {
        int count = 0;
        for (int i = 0; i <= Math.min(x, B.length - 1); i++) {
            if (!B[i]) {
                count++;
            }
        }
        return count;
    }

    @Override
    public String toString() {
        StringBuilder str = new StringBuilder();
        for (int i : suffixArray) {
            str.append(i).append(":\t");
            for (int j = i; j < text.length; j++) {
                str.append(text[j]);
            }
            str.append("\n");
        }
        return str.toString();
    }

    public int[][] getOTable() {
        return O;
    }

    public int[] getCTable() {
        return C;
    }

    public int[] getSuffixArray() {
        return suffixArray;
    }

    public char[] getText() {
        return text;
    }

    public String getId() {
        return id;
    }
}
