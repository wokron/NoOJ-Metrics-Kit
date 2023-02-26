package nooj.analyzer;

import nooj.result.AnalyzeResult;
import nooj.result.ClassAnalyzeResult;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.FieldInsnNode;
import org.objectweb.asm.tree.MethodNode;

import java.util.*;

public class LCOMAnalyzer implements Analyzer
{
    private final ClassAnalyzeResult<Integer> result = new ClassAnalyzeResult<>("LCOM1", "");

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

        Map<String, Set<String>> paramsForMethods = new TreeMap<>();

        for (var method : cls.methods)
        {
            if (method.name.contains("lambda"))
                continue;

            Set<String> params = new TreeSet<>();
            paramsForMethods.put(method.name, params);

            params.addAll(solveMethodCodeLines(method));
        }

        int emptyCnt = 0, notEmptyCnt = 0;
        var methodNames = paramsForMethods.keySet().toArray(new String[0]);

        for (int i = 0; i < methodNames.length - 1; i++)
        {
            var set1 = paramsForMethods.get(methodNames[i]);
            for (int j = i + 1; j < methodNames.length; j++)
            {
                var set2 = paramsForMethods.get(methodNames[j]);
                var intersectionSet = new TreeSet<>(set1);
                intersectionSet.retainAll(set2);

                if (intersectionSet.isEmpty())
                    emptyCnt++;
                else
                    notEmptyCnt++;
            }
        }

        int lackOfCohesionValue = Math.max(emptyCnt - notEmptyCnt, 0);

        result.addResult(className, lackOfCohesionValue);
    }

    private Collection<String> solveMethodCodeLines(MethodNode method)
    {
        Set<String> fieldUse = new TreeSet<>();
        for (var instruction : method.instructions)
        {
            if (instruction instanceof FieldInsnNode fieldInstruction)
            {
                fieldUse.add(fieldInstruction.name);
            }
        }

        return fieldUse;
    }

    @Override
    public AnalyzeResult getAnalyzeResult()
    {
        return result;
    }
}
