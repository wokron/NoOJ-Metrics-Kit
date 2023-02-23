package nooj.analyzer;

import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.MethodInsnNode;
import org.objectweb.asm.tree.MethodNode;

import java.util.*;
import java.util.regex.MatchResult;
import java.util.regex.Pattern;

public class CBOAnalyzer implements Analyzer
{
    private final Map<String, Set<String>> couplingClasses = new TreeMap<>();
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

        if (!couplingClasses.containsKey(className))
            couplingClasses.put(className, new TreeSet<>());
        Set<String> coupling = couplingClasses.get(className);

        coupling.add(cls.superName);
        coupling.addAll(cls.interfaces);

        for (var field : cls.fields)
        {
            coupling.addAll(solveDescriptor(field.desc));
        }

        for (var method : cls.methods)
        {
            coupling.addAll(solveMethodCodeLines(method));
            coupling.addAll(solveDescriptor(method.desc));
        }

        coupling.remove(cls.name);
        coupling.removeIf(c -> Pattern.matches("^(java.*|\\[?[BCDFIJSZ])$", c));
    }

    private List<String> solveMethodCodeLines(MethodNode method)
    {
        List<String> coupling = new ArrayList<>();
        String preInit = null;

        for (var instruction : method.instructions)
        {
            if (instruction instanceof MethodInsnNode methodInstruction)
            {
                if (methodInstruction.name.equals("<init>"))
                    preInit = methodInstruction.owner;
                else if (preInit != null)
                {
                    coupling.add(preInit);
                    preInit = null;
                }
            }
        }

        return coupling;
    }

    private List<String> solveDescriptor(String desc)
    {
        List<String> coupling = new ArrayList<>();

        String descReg = "L[a-zA-Z/]+;|\\[?[BCDFIJSZ]";
        var allMatch = Pattern.compile(descReg)
                .matcher(desc)
                .results()
                .map(MatchResult::group)
                .toArray(String[]::new);

        for (var arg : allMatch)
        {
            if (arg.startsWith("L") && arg.endsWith(";"))
            {
                coupling.add(arg.substring(1, arg.length()-1));
            }
        }

        return coupling;
    }

    @Override
    public String getAnalyzeResult()
    {
        for (var cls : couplingClasses.keySet())
        {
            System.out.println(cls + ":" + couplingClasses.get(cls).size());
            couplingClasses.get(cls).forEach(c -> System.out.print(c + " "));
            System.out.println();
        }

        return "";
    }
}
