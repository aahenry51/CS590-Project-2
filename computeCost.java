import java.io.*;
import java.util.*;

public class computeCost {
public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new FileReader("input.txt"));
        PrintWriter pw    = new PrintWriter(new FileWriter("output.txt"));
       
        String target = br.readLine();
        String typo = br.readLine();


        System.out.println("target = [" + target + "]");
        System.out.println("typo = [" + typo + "]");


        br.close();
        pw.close();
}
}