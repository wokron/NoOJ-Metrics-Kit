package nooj.analyzer;

import nooj.result.AnalyzeResult;
import nooj.result.ClassAnalyzeResult;
import nooj.utils.Util;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.MethodInsnNode;
import org.objectweb.asm.tree.MethodNode;

import java.util.*;

public class RFCAnalyzer implements Analyzer
{
    private final ClassAnalyzeResult<Set<String>> result = new ClassAnalyzeResult<>("RFC", "");

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
        var responseMethods = result.getAnalyzeResults();

        if (!responseMethods.containsKey(className))
            result.addResult(className, new TreeSet<>());
        Set<String> methodNames = responseMethods.get(className);

        for (var method : cls.methods)
        {
            addMethodToList(methodNames, cls.name, method.name, method.desc);

            methodNames.addAll(solveMethodCodeLines(method));
        }

        methodNames.removeIf(m -> m.contains("lambda"));
    }

    private void addMethodToList(Collection<String> methodsName,String ownerName, String methodName, String desc)
    {
        String normalizedName = methodSignatureNormalize(ownerName, methodName, Util.solveDescriptor(desc));
        methodsName.add(normalizedName);
    }

    private String methodSignatureNormalize(String ownerName, String methodName, List<String> descList)
    {
        StringBuilder sb = new StringBuilder();
        sb.append(descList.get(descList.size() - 1)).append(" ")
                .append(ownerName).append(".")
                .append(methodName).append("(");
        for (int i = 0; i < descList.size() - 1; i++)
            sb.append(descList.get(i)).append(", ");
        sb.append(")");

        return sb.toString().replace("/", ".");
    }

    private List<String> solveMethodCodeLines(MethodNode method)
    {
        List<String> methods = new ArrayList<>();

        for (var instruction : method.instructions)
        {
            if (instruction instanceof MethodInsnNode methodInstruction)
            {
                if (!methodInstruction.name.equals("<init>"))
                {
                    String ownerName = methodInstruction.owner;
                    String methodName = methodInstruction.name;
                    String methodDesc = methodInstruction.desc;
                    addMethodToList(methods, ownerName, methodName, methodDesc);
                }
            }
        }

        return methods;
    }

    @Override
    public AnalyzeResult getAnalyzeResult()
    {
        return result;
    }
}
