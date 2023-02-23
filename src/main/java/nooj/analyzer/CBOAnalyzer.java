package nooj.analyzer;

import nooj.utils.Util;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.MethodInsnNode;
import org.objectweb.asm.tree.MethodNode;

import java.util.*;
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
            coupling.addAll(Util.solveDescriptor(field.desc));
        }

        for (var method : cls.methods)
        {
            coupling.addAll(solveMethodCodeLines(method));
            coupling.addAll(Util.solveDescriptor(method.desc));
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
