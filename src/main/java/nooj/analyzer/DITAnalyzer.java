package nooj.analyzer;

import nooj.result.AnalyzeResult;
import nooj.result.ClassAnalyzeResult;
import org.objectweb.asm.tree.ClassNode;

import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class DITAnalyzer implements Analyzer
{
    private final Map<String, String> inheritTree = new TreeMap<>();

    private final ClassAnalyzeResult<Integer> result = new ClassAnalyzeResult<>("DIT", "");

    @Override
    public void analyze(List<ClassNode> projectClasses)
    {
        for (var cls : projectClasses)
        {
            inheritTree.put(cls.name, cls.superName);
        }
        for (var cls : projectClasses)
        {
            result.addResult(cls.name, calculateInheritDepth(cls.name));
        }
    }

    private int calculateInheritDepth(String className)
    {
        var inheritDepth = result.getAnalyzeResults();
        if (className.equals("java/lang/Object"))
            return 1;
        else if (!inheritTree.containsKey(className))
        {
            try {
                String fullClassName = className.replace("/", ".");
                return calculateInheritDepth(Class.forName(fullClassName));
            } catch (ClassNotFoundException e) {
                return 1;
            }
        }
        else if (inheritDepth.containsKey(className))
            return inheritDepth.get(className);

        int superClassDepth = calculateInheritDepth(inheritTree.get(className));
        int currentClassDepth = superClassDepth + 1;
        inheritDepth.put(className, currentClassDepth);
        return currentClassDepth;
    }

    private int calculateInheritDepth(Class<?> cls)
    {
        int depth = 1;
        while (cls != null)
        {
            cls = cls.getSuperclass();
            depth++;
        }
        return depth;
    }

    @Override
    public AnalyzeResult getAnalyzeResult()
    {
        return result;
    }
}
