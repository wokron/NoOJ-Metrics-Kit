package nooj.analyzer;

import nooj.result.MethodAnalyzeResult;
import nooj.utils.Util;
import org.objectweb.asm.tree.ClassNode;

import java.util.List;

public class LongParameterListAnalyzer implements Analyzer
{
    private final MethodAnalyzeResult<String> result = new MethodAnalyzeResult<>("Code Smell", "Long Parameter List");

    @Override
    public void analyze(List<ClassNode> projectClasses)
    {
        for (var cls : projectClasses)
        {
            solveClass(cls);
        }
    }

    private void solveClass(ClassNode cls)
    {
        for (var method : cls.methods)
        {
            var descList = Util.solveDescriptor(method.desc);
            int size = descList.size() - 1;
            if (size > 3)
                result.addResult(cls.name,
                        Util.normalizeMethodSignature(cls.name, method.name, method.desc),
                        String.format("Too long parameter list, %s parameters now", size));
        }
    }

    @Override
    public MethodAnalyzeResult<String> getAnalyzeResult()
    {
        return result;
    }
}
