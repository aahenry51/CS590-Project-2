import java.util.ArrayList;
import java.util.List;

public class TypoSolver {
    private static final int INF = 1_000_000_000;

    private final String target;
    private final String typo;
    private final int m;
    private final int n;
    private final CostModel costModel;

    // memo[i][j] = minimum cost to transform target[i..] into typo[j..]
    private final int[][] memo;
    private final boolean[][] seen;
    private final Choice[][] choice;

    public TypoSolver(String target, String typo, CostModel costModel) {
        this.target = target;
        this.typo = typo;
        this.m = target.length();
        this.n = typo.length();
        this.costModel = costModel;

        this.memo = new int[m + 1][n + 1];
        this.seen = new boolean[m + 1][n + 1];
        this.choice = new Choice[m + 1][n + 1];
    }

    public Result solve() {
        int minCost = dp(0, 0);
        List<String> operations = reconstruct();
        return new Result(minCost, operations);
    }

    private int dp(int i, int j) {
        if (seen[i][j]) {
            return memo[i][j];
        }
        seen[i][j] = true;

        // Both strings finished
        if (i == m && j == n) {
            memo[i][j] = 0;
            choice[i][j] = new Choice(Op.END, '\0');
            return 0;
        }

        int best = INF;
        Choice bestChoice = null;

        // 1. Match
        if (i < m && j < n && target.charAt(i) == typo.charAt(j)) {
            int cand = dp(i + 1, j + 1);
            if (cand < best) {
                best = cand;
                bestChoice = new Choice(Op.MATCH, '\0');
            }
        }

        // 2. Insert typo[j]
        if (j < n) {
            int cand = costModel.insertCost(target, typo, i, j) + dp(i, j + 1);
            if (cand < best) {
                best = cand;
                bestChoice = new Choice(Op.INSERT, typo.charAt(j));
            }
        }

        // 3. Delete target[i]
        if (i < m) {
            int cand = costModel.deleteCost(target, typo, i, j) + dp(i + 1, j);
            if (cand < best) {
                best = cand;
                bestChoice = new Choice(Op.DELETE, '\0');
            }
        }

        // 4. Substitute
        if (i < m && j < n && target.charAt(i) != typo.charAt(j)) {
            int cand = costModel.substituteCost(target.charAt(i), typo.charAt(j))
                    + dp(i + 1, j + 1);
            if (cand < best) {
                best = cand;
                bestChoice = new Choice(Op.SUBSTITUTE, typo.charAt(j));
            }
        }

        // 5. Transpose adjacent characters
        if (i + 1 < m && j + 1 < n
                && target.charAt(i) == typo.charAt(j + 1)
                && target.charAt(i + 1) == typo.charAt(j)) {
            int cand = costModel.transposeCost(target.charAt(i), target.charAt(i + 1))
                    + dp(i + 2, j + 2);
            if (cand < best) {
                best = cand;
                bestChoice = new Choice(Op.TRANSPOSE, '\0');
            }
        }

        memo[i][j] = best;
        choice[i][j] = bestChoice;
        return best;
    }

    private List<String> reconstruct() {
        List<String> ops = new ArrayList<>();

        int i = 0;
        int j = 0;
        int pos = 0; // current index in the evolving string

        while (true) {
            Choice ch = choice[i][j];
            if (ch == null) {
                throw new IllegalStateException("Missing choice at state (" + i + ", " + j + ")");
            }

            switch (ch.op) {
                case END:
                    return ops;

                case MATCH:
                    i++;
                    j++;
                    pos++;
                    break;

                case INSERT:
                    ops.add("Insert " + ch.c + " before " + pos);
                    j++;
                    pos++;
                    break;

                case DELETE:
                    ops.add("Delete " + pos);
                    i++;
                    break;

                case SUBSTITUTE:
                    ops.add("Substitute " + ch.c + " at " + pos);
                    i++;
                    j++;
                    pos++;
                    break;

                case TRANSPOSE:
                    ops.add("Transpose " + pos + "-" + (pos + 1));
                    i += 2;
                    j += 2;
                    pos += 2;
                    break;

                default:
                    throw new IllegalStateException("Unknown operation at state (" + i + ", " + j + ")");
            }
        }
    }

    public static class Result {
        public final int minCost;
        public final List<String> operations;

        public Result(int minCost, List<String> operations) {
            this.minCost = minCost;
            this.operations = operations;
        }
    }

    private static class Choice {
        final Op op;
        final char c;

        Choice(Op op, char c) {
            this.op = op;
            this.c = c;
        }
    }

    private enum Op {
        MATCH,
        INSERT,
        DELETE,
        SUBSTITUTE,
        TRANSPOSE,
        END
    }
}