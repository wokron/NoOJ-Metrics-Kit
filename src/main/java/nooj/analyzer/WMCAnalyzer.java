package nooj.analyzer;

import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.*;

import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class WMCAnalyzer implements Analyzer
{
    private final Map<String, Map<String, Integer>> circleComplexities = new TreeMap<>();

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

        if (!circleComplexities.containsKey(className))
            circleComplexities.put(className, new TreeMap<>());
        Map<String, Integer> complexitiesForClass = circleComplexities.get(className);

        for (var method : cls.methods)
        {
            String methodName = method.name + " " + method.desc;
            int methodComplexity = solveMethod(method);
            complexitiesForClass.put(methodName, methodComplexity);
        }
    }

    private int solveMethod(MethodNode method)
    {
        int complexity = 1;

        for (var instruction : method.instructions)
        {
            if (instruction instanceof JumpInsnNode
                    || instruction instanceof TableSwitchInsnNode
                    || instruction instanceof LookupSwitchInsnNode
            )
            {
                int opcode = instruction.getOpcode();
                if (opcode == Opcodes.GOTO || opcode == Opcodes.JSR || opcode == Opcodes.RET)
                    continue;
                complexity++;

            }
        }

        return complexity;
    }

    @Override
    public String getAnalyzeResult()
    {
        int totalComplexity = 0;
        for (var className : circleComplexities.keySet())
        {
            System.out.println(className);
            for (var methodName : circleComplexities.get(className).keySet())
            {
                var complexity = circleComplexities.get(className).get(methodName);
                System.out.println(methodName + " " + complexity);
                totalComplexity += complexity;
            }
        }
        return "WMC: " + totalComplexity;
    }
}