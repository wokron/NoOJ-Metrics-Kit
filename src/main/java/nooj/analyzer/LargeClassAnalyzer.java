package nooj.analyzer;

import nooj.result.AnalyzeResult;
import nooj.result.ClassAnalyzeResult;
import org.objectweb.asm.tree.ClassNode;

import java.util.List;

public class LargeClassAnalyzer implements Analyzer
{
    private final LCOM4Analyzer lcom4Analyzer = new LCOM4Analyzer();
    private final ClassAnalyzeResult<String> result = new ClassAnalyzeResult<>("Code Smell", "Large Class");

    @Override
    public void analyze(List<ClassNode> projectClasses)
    {
        lcom4Analyzer.analyze(projectClasses);
        var lcom4Result = lcom4Analyzer.getAnalyzeResult();
        var lcom4Values = lcom4Result.getAnalyzeResults();

        for (var className : lcom4Values.keySet())
        {
            int lcom = lcom4Values.get(className);
            if (lcom > 1)
            {
                result.addResult(className, String.format("lack of cohesion, can split into %d classes", lcom));
            }
        }
    }

    @Override
    public AnalyzeResult getAnalyzeResult()
    {
        return result;
    }
}
