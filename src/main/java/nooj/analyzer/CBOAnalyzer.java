package nooj.analyzer;

import nooj.result.ClassAnalyzeResult;
import nooj.utils.Util;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.MethodNode;

import java.util.List;
import java.util.Set;
import java.util.TreeSet;
import java.util.regex.Pattern;

public class CBOAnalyzer implements Analyzer
{
    private final ClassAnalyzeResult<Set<String>> result = new ClassAnalyzeResult<>("CBO", "");

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
        String className = cls.name;

        Set<String> coupling = new TreeSet<>();

        for (var field : cls.fields)
        {
            coupling.addAll(Util.solveDescriptor(field.desc));
        }

        for (var method : cls.methods)
        {
            coupling.addAll(solveMethodCodeLines(method));
            coupling.addAll(Util.solveDescriptor(method.desc));
        }

        coupling.remove(cls.name);
        coupling.removeIf(c -> Pattern.matches("^(java.*|\\[?[BCDFIJSZV])$", c));

        result.addResult(className, coupling);
    }

    private List<String> solveMethodCodeLines(MethodNode method)
    {
        return Util.getClassesInMethods(method);
    }

    @Override
    public ClassAnalyzeResult<Set<String>> getAnalyzeResult()
    {
        return result;
    }
}
