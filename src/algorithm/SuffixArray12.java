package algorithm;

public class SuffixArray12 {
    static boolean leq(int a1, int a2,   int b1, int b2) { // lexic. order for pairs
        return(a1 < b1 || a1 == b1 && a2 <= b2);
    }                                                   // and triples
    static boolean leq(int a1, int a2, int a3,   int b1, int b2, int b3) {
        return(a1 < b1 || a1 == b1 && leq(a2,a3, b2,b3));
    }

    static void radixPass(int[] a, int[] b, int[] r, int r_offset, int n, int K)
    { // count occurrences
        int[] c = new int[K + 1]; // counter array
        for (int i = 0; i <= K; i++) c[i] = 0; // reset counters
        for (int i = 0; i < n; i++) c[r[a[i] + r_offset]]++; // count occurrences
        for (int i = 0, sum = 0; i <= K; i++) // exclusive prefix sums
        { int t = c[i]; c[i] = sum; sum += t; }
        for (int i = 0; i < n; i++) b[c[r[a[i] + r_offset]]++] = a[i]; // sort
    }

    public static int[] suffixArray(String text){
        int n = text.length();
        int[] T = new int[n+3];
        int[] SA = new int[n];
        for(int i = 0; i<n; i++) SA[i] = i;
        return suffixArray( T, SA, n, 4);
    }

    static int[] suffixArray(int[] T, int[] SA, int n, int K){
        int n0=(n+2)/3, n1=(n+1)/3, n2=n/3, n02=n0+n2;
        int[] R = new int[n02 + 3]; R[n02]= R[n02+1]= R[n02+2]=0;
        int[] SA12 = new int[n02 + 3]; SA12[n02]=SA12[n02+1]=SA12[n02+2]=0;
        int[] R0 = new int[n0];
        int[] SA0 = new int[n0];

        //Step 0
        for (int i=0, j=0; i < n+(n0-n1); i++) if (i%3 != 0) R[j++] = i;

        //Step 1
        //Construct the suffix array of the suffixes starting at positions i mod 3 ≠ 0.
        //... done recursively by reducing the problem to a string of 2/3 size ...
        // lsb radix sort the mod 1 and mod 2 triples
        radixPass(R , SA12, T, 2, n02, K);
        radixPass(SA12, R , T, 1, n02, K);
        radixPass(R, SA12, T, 0, n02, K);

        // find lexicographic names of triples and
        // write them to correct places in R
        int name = 0, c0 = -1, c1 = -1, c2 = -1;
        for (int i = 0; i < n02; i++) {
            if (T[SA12[i]] != c0 || T[SA12[i]+1] != c1 || T[SA12[i]+2] != c2)
            { name++; c0 = T[SA12[i]]; c1 = T[SA12[i]+1]; c2 = T[SA12[i]+2]; }
            if (SA12[i] % 3 == 1) { R[SA12[i]/3] = name; } // write to R1
            else { R[SA12[i]/3 + n0] = name; } // write to R2
        }

        // recurse if names are not yet unique
        if (name < n02) {
            suffixArray(R, SA12, n02, name);
            // store unique names in R using the suffix array
            for (int i = 0; i < n02; i++) R[SA12[i]] = i + 1;
        } else // generate the suffix array of R directly
            for (int i = 0; i < n02; i++) SA12[R[i] - 1] = i;


        //Step 2
        //Construct the suffix array of the remaining suffixes.
        //... done using the suffix array constructed in step 1 ...
        // stably sort the mod 0 suffixes from SA12 by their first character
        for (int i=0, j=0; i < n02; i++) if (SA12[i] < n0) R0[j++] = 3*SA12[i];
        radixPass(R0, SA0, T, 0, n0, K);

        //Step 3
        //Merge the two suffix arrays into one
        for (int p=0, t=n0-n1, k=0; k < n; k++) {
            //#define GetI() (SA12[t] < n0 ? SA12[t] *3+1: (SA12[t] - n0) * 3 + 2)
            int i = GetI(SA12, t, n0); // pos of current offset 12 suffix
            int j = SA0[p]; // pos of current offset 0 suffix
            if (SA12[t] < n0 ? // different compares for mod 1 and mod 2 suffixes
                    leq(T[i], R[SA12[t] + n0], T[j], R[j/3]) :
                    leq(T[i],T[i+1],R[SA12[t]-n0+1], T[j],T[j+1],R[j/3+n0]))
            { // suffix from SA12 is smaller
                SA[k] = i; t++;
                if (t == n02) // done --- only SA0 suffixes left
                    for (k++; p < n0; p++, k++) SA[k] = SA0[p];
            } else { // suffix from SA0 is smaller
                SA[k] = j; p++;
                if (p == n0) // done --- only SA12 suffixes left
                    for (k++; t < n02; t++, k++) SA[k] = GetI(SA12, t, n0);
            }
        }
        return SA;
    }

    private static int GetI(int[] SA12, int t, int n0){
        return (SA12[t] < n0 ? SA12[t] *3+1: (SA12[t] - n0) * 3 + 2);
    }
}
