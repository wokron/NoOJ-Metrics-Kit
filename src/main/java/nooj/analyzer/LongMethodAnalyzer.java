package nooj.analyzer;

import nooj.result.ClassAnalyzeResult;
import nooj.result.MethodAnalyzeResult;
import nooj.utils.Util;
import org.objectweb.asm.tree.ClassNode;

import java.util.List;
import java.util.Set;
import java.util.TreeSet;

public class LongMethodAnalyzer implements Analyzer
{
    private final WMCAnalyzer wmcAnalyzer = new WMCAnalyzer();

    public ClassAnalyzeResult<Set<String>> result = new ClassAnalyzeResult<>("Code Smell", "Long Method");

    @Override
    public void analyze(List<ClassNode> projectClasses)
    {
        analyzeMethodComplexity(projectClasses);
        analyzeMethodLength(projectClasses);
    }

    private void analyzeMethodComplexity(List<ClassNode> projectClasses)
    {
        wmcAnalyzer.analyze(projectClasses);
        MethodAnalyzeResult<Integer> wmcResult = wmcAnalyzer.getAnalyzeResult();

        var wmcResultValues = wmcResult.getAnalyzeResults();

        var results = result.getAnalyzeResults();

        for (var clsName : wmcResultValues.keySet())
        {
            var methods = results.getOrDefault(clsName, new TreeSet<>());
            var wmcMethods = wmcResultValues.get(clsName);
            for (var methodName : wmcMethods.keySet())
            {
                int complexity = wmcMethods.get(methodName);
                if (complexity >= 10)
                    methods.add(methodName + " " + complexity);
            }
            if (!methods.isEmpty())
                result.addResult(clsName, methods);
        }
    }

    private void analyzeMethodLength(List<ClassNode> projectClasses)
    {
        var results = result.getAnalyzeResults();
        for (var cls : projectClasses)
        {
            var methods = results.getOrDefault(cls.name, new TreeSet<>());
            for (var method : cls.methods)
            {
                if (method.instructions.size() > 45)
                    methods.add(Util.normalizeMethodSignature(cls.name, method.name, method.desc));
            }
            if (methods.size() > 0)
                result.addResult(cls.name, methods);
        }
    }

    @Override
    public ClassAnalyzeResult<Set<String>> getAnalyzeResult()
    {
        return result;
    }
}
