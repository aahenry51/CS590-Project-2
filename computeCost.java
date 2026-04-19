import java.io.*;

public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new FileReader("input.txt"));
        PrintWriter pw    = new PrintWriter(new FileWriter("output.txt"));

        cost c = new cost();

        int ins = c.insertCost('e', 'e', 't');


        br.close();
        pw.close();
}