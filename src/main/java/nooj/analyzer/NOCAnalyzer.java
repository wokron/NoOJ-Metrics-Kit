package nooj.analyzer;

import nooj.result.AnalyzeResult;
import nooj.result.ClassAnalyzeResult;
import org.objectweb.asm.tree.ClassNode;

import java.util.ArrayList;
import java.util.List;

public class NOCAnalyzer implements Analyzer
{
    private final ClassAnalyzeResult<List<String>> result = new ClassAnalyzeResult<>("NOC", "");

    @Override
    public void analyze(List<ClassNode> projectClasses)
    {
        for (var cls : projectClasses)
            result.addResult(cls.name, new ArrayList<>());

        for (var cls : projectClasses)
        {
            String child = cls.name;
            String father = cls.superName;
            if (!result.getAnalyzeResults().containsKey(father))
                continue;
            var children = result.getAnalyzeResults().get(father);
            children.add(child);
        }
    }

    @Override
    public AnalyzeResult getAnalyzeResult()
    {
        return result;
    }
}
