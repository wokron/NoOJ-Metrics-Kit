package nooj.utils;

import org.objectweb.asm.tree.MethodInsnNode;
import org.objectweb.asm.tree.MethodNode;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.MatchResult;
import java.util.regex.Pattern;

public class Util
{
    public static List<String> solveDescriptor(String desc)
    {
        List<String> descTypes = new ArrayList<>();

        String descReg = "L[a-zA-Z/]+;|\\[?[BCDFIJSZV]";
        var allMatch = Pattern.compile(descReg)
                .matcher(desc)
                .results()
                .map(MatchResult::group)
                .toArray(String[]::new);

        for (var arg : allMatch)
        {
            String argType = arg;
            if (arg.startsWith("L") && arg.endsWith(";"))
                argType = argType.substring(1, arg.length()-1);

            descTypes.add(argType);
        }

        return descTypes;
    }

    public static String normalizeMethodSignature(String ownerName, String methodName, String methodDesc)
    {
        return normalizeMethodSignatureByDescList(ownerName, methodName, solveDescriptor(methodDesc));
    }

    private static String normalizeMethodSignatureByDescList(String ownerName, String methodName, List<String> descList)
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

    public static List<String> getClassesInMethods(MethodNode method)
    {
        List<String> classes = new ArrayList<>();
        String preInit = null;

        for (var instruction : method.instructions)
        {
            if (instruction instanceof MethodInsnNode methodInstruction)
            {
                if (methodInstruction.name.equals("<init>"))
                    preInit = methodInstruction.owner;
                else if (preInit != null)
                {
                    classes.add(preInit);
                    preInit = null;
                }
            }
        }

        return classes;
    }
}
