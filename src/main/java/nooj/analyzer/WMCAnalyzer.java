package nooj.analyzer;

import nooj.result.MethodAnalyzeResult;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.*;

import java.util.List;

public class WMCAnalyzer implements Analyzer
{
    private final MethodAnalyzeResult<Integer> result = new MethodAnalyzeResult<>("WMC", "");

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

        for (var method : cls.methods)
        {
            String methodName = method.name + " " + method.desc;
            int methodComplexity = solveMethod(method);
            result.addResult(className, methodName, methodComplexity);
        }
    }

    private int solveMethod(MethodNode method)
    {
        int complexity = 1;

        complexity += method.tryCatchBlocks.size();

        for (var instruction : method.instructions)
        {
            if (instruction instanceof JumpInsnNode)
            {
                int opcode = instruction.getOpcode();
                if (opcode != Opcodes.GOTO && opcode != Opcodes.JSR && opcode != Opcodes.RET)
                    complexity++;
            }
            else if (instruction instanceof TableSwitchInsnNode tableSwitchInsnNode)
            {
                complexity += tableSwitchInsnNode.labels.size();
            }
            else if (instruction instanceof LookupSwitchInsnNode lookupSwitchInsnNode)
            {
                complexity += lookupSwitchInsnNode.labels.size();
            }
        }

        return complexity;
    }

    @Override
    public MethodAnalyzeResult<Integer> getAnalyzeResult()
    {
        return result;
    }
}
