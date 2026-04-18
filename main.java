import java.io.*;
import java.util.*;

public class Main {
    public static void main(String[] args) throws Exception {
        List<String[]> cases = readInput("input.txt");
        StringBuilder output = new StringBuilder();

        CostModel costModel = new ComputeCost();

        for (int caseNum = 0; caseNum < cases.size(); caseNum++) {
            String target = cases.get(caseNum)[0];
            String typo = cases.get(caseNum)[1];

            TypoSolver solver = new TypoSolver(target, typo, costModel);
            TypoSolver.Result result = solver.solve();

            output.append(result.minCost).append('\n');
            for (String op : result.operations) {
                output.append(op).append('\n');
            }

            if (caseNum + 1 < cases.size()) {
                output.append('\n');
            }
        }

        try (BufferedWriter bw = new BufferedWriter(new FileWriter("output.txt"))) {
            bw.write(output.toString());
        }
    }

    private static List<String[]> readInput(String filename) throws Exception {
        List<String> lines = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new FileReader(filename))) {
            String line;
            while ((line = br.readLine()) != null) {
                lines.add(line);
            }
        }

        int idx = 0;
        while (idx < lines.size() && lines.get(idx).trim().isEmpty()) {
            idx++;
        }

        int x = Integer.parseInt(lines.get(idx).trim());
        idx++;

        List<String[]> result = new ArrayList<>();

        for (int k = 0; k < x; k++) {
            while (idx < lines.size() && lines.get(idx).trim().isEmpty()) {
                idx++;
            }
            String target = lines.get(idx++);

            while (idx < lines.size() && lines.get(idx).trim().isEmpty()) {
                idx++;
            }
            String typo = lines.get(idx++);

            result.add(new String[]{target, typo});
        }

        return result;
    }
}