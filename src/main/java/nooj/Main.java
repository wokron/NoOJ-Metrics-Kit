package nooj;

import nooj.analyzer.*;

import java.io.IOException;
import java.util.Arrays;
import java.util.Scanner;

public class Main
{
    public static void main(String[] args)
    {
        Scanner in = new Scanner(System.in);

        Analyzer[] analyzers = new Analyzer[]{
                new LargeClassAnalyzer(),
                new LongMethodAnalyzer(),
                new LongParameterListAnalyzer(),
                new CyclicDependencyAnalyzer(),
                new LayerExposureAnalyzer(),
        };

        while (true)
        {
            try {
                System.out.print("enter project path:");
                String path = in.nextLine();

                var loader = new ProjectLoader(path);
                Arrays.stream(analyzers).forEach(a -> a.analyze(loader.getResult()));
                Arrays.stream(analyzers).forEach(a -> System.out.println(a.getAnalyzeResult() + "\n"));

                break;
            } catch (IOException e) {
                System.out.println("path not found");
            }
        }
    }
}
