
public class cost {

    public class KeyPos{
        public int x = 0;
        public int y = 0;
    }

    private String[] rowKeys = {
        "1234567890",
        "qwertyuiop",
        "asdfghjkl;",
        "zxcvbnm,.'"  
    };

    private String leftKeys = "12345qwertasdfgzxcvb";
    private String rightKeys = "67890yuiophjkl;nm,./";


    public int [] getKeyPos(char c) {
        for (int i = 0; i < rowKeys.length; i++) {
            int col = rowKeys[i].indexOf(c);
            if (col != -1){
                return new int[]{i, col};
            }

        }
        return null; 
    }

    //d(k1, k2)
    public int getKeyDist(char a, char b) {
        int[] loc1 = getKeyPos(a);
        int[] loc2 = getKeyPos(b);
        return Math.max(Math.abs(loc1[0] - loc2[0]), Math.abs(loc1[1] - loc2[1]));
    }

    public boolean isSpace(char c) {
        return c == ' ';
    }

    public boolean sameHand(char a, char b) {
        return (leftKeys.indexOf(a) != -1 && leftKeys.indexOf(b) != -1)
            || (rightKeys.indexOf(a) != -1 && rightKeys.indexOf(b) != -1);
    }

    public boolean sameFinger(char a, char b) {
        int[] loc1 = getKeyPos(a);
        int[] loc2 = getKeyPos(b);
        if (loc1 == null || loc2 == null) return false;
        
        return loc1[1] + loc2[1] == 9 && !sameHand(a, b);
    }

    public boolean isBottomRow(char c) {
        return rowKeys[3].indexOf(c) != -1;
    }

    public int insertCostOneNeighbor(char ins, char neighbor) {
        if (ins == neighbor) return 1;                  // repeated character
        if (isSpace(ins)) {
            // inserting a space
            if (isBottomRow(neighbor)) return 2;
            else return 6;
        }
        if (isSpace(neighbor)) return 6;                // non-space next to space
        if (sameHand(ins, neighbor)) return getKeyDist(ins, neighbor);
        return 5;                                       // opposite hand
    }
    

    public int insertCost(char ins, char prev, char next) {
        int cost = Integer.MAX_VALUE;
        if (prev != 0) cost = Math.min(cost, insertCostOneNeighbor(ins, prev));
        if (next != 0) cost = Math.min(cost, insertCostOneNeighbor(ins, next));
        return cost;
    }

    public int deleteCost(char del, char prev) {
        if (prev == 0) return 6;                        // first character
        if (del == prev) return 1;                      // repeated character
        if (isSpace(del)) return 3;                     // deleting a space
        if (isSpace(prev)) return 6;                    // after a space
        if (sameHand(del, prev)) return 2;              // same hand
        return 6;                                       // different hand
    }

    public int substituteCost(char orig, char sub) {
        if (isSpace(orig) || isSpace(sub)) return 6;
        if (sameHand(orig, sub)) return getKeyDist(orig, sub);
        if (sameFinger(orig, sub)) return 1;
        return 5;                                      
    }

    public int transposeCost(char a, char b) {
        if (isSpace(a) || isSpace(b)) return 3;
        if (sameHand(a, b)) return 2;
        return 1;                                   
    }

}
s