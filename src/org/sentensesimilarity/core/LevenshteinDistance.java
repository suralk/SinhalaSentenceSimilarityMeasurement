/**
 * 
 */
package org.sentensesimilarity.core;

/**
 * @author Kadupitiya JCS
 *
 */
/*
 */

public class LevenshteinDistance {

  /**
   *
   * <pre>
   * StringUtils.getLevenshteinDistance(null, *)             = IllegalArgumentException
   * StringUtils.getLevenshteinDistance(*, null)             = IllegalArgumentException
   * StringUtils.getLevenshteinDistance("","")               = 0
   * StringUtils.getLevenshteinDistance("","a")              = 1
   * StringUtils.getLevenshteinDistance("aaapppp", "")       = 7
   * StringUtils.getLevenshteinDistance("frog", "fog")       = 1
   * StringUtils.getLevenshteinDistance("fly", "ant")        = 3
   * StringUtils.getLevenshteinDistance("elephant", "hippo") = 7
   * StringUtils.getLevenshteinDistance("hippo", "elephant") = 7
   * StringUtils.getLevenshteinDistance("hippo", "zzzzzzzz") = 8
   * StringUtils.getLevenshteinDistance("hello", "hallo")    = 1
   * </pre>
   *
   * @param s  the first String, must not be null
   * @param t  the second String, must not be null
   * @return result distance
   * @throws IllegalArgumentException if either String input <code>null</code>
   */
  public static int getLevenshteinDistance(String s, String t) {
      if (s == null || t == null) {
          throw new IllegalArgumentException("Strings must not be null");
      }

      /*
         Effectively, the difference between the two implementations is this one does not 
         cause an out of memory condition when calculating the LD over two very large strings.
       */

      int n = s.length(); // length of s
      int m = t.length(); // length of t

      if (n == 0) {
          return m;
      } else if (m == 0) {
          return n;
      }

      if (n > m) {
          // swap the input strings to consume less memory
          String tmp = s;
          s = t;
          t = tmp;
          n = m;
          m = t.length();
      }

      int p[] = new int[n+1]; //'previous' cost array, horizontally
      int d[] = new int[n+1]; // cost array, horizontally
      int _d[]; //placeholder to assist in swapping p and d

      // indexes into strings s and t
      int i; // iterates through s
      int j; // iterates through t

      char t_j; // jth character of t

      int cost; // cost

      for (i = 0; i<=n; i++) {
          p[i] = i;
      }

      for (j = 1; j<=m; j++) {
          t_j = t.charAt(j-1);
          d[0] = j;

          for (i=1; i<=n; i++) {
              cost = s.charAt(i-1)==t_j ? 0 : 1;
              // minimum of cell to the left+1, to the top+1, diagonally left and up +cost
              d[i] = Math.min(Math.min(d[i-1]+1, p[i]+1),  p[i-1]+cost);
          }

          // copy current distance counts to 'previous row' distance counts
          _d = p;
          p = d;
          d = _d;
      }

      // our last action in the above loop was to switch d and p, so p now 
      // actually has the most recent cost counts
      return p[n];
  }


}

  
  