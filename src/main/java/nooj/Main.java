package nooj;

import nooj.analyzer.*;

import java.io.IOException;
import java.util.Arrays;

public class Main
{
    public static void main(String[] args)
    {
        if (args.length < 1)
            return;

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
                String path = args[0];

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
