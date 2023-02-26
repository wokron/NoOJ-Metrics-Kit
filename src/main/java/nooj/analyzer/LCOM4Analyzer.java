package nooj.analyzer;

import nooj.result.AnalyzeResult;
import nooj.result.ClassAnalyzeResult;
import nooj.utils.Util;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.FieldInsnNode;
import org.objectweb.asm.tree.MethodInsnNode;
import org.objectweb.asm.tree.MethodNode;

import java.util.*;

public class LCOM4Analyzer implements Analyzer
{
    ClassAnalyzeResult<Integer> result = new ClassAnalyzeResult<>("LCOM4", "");

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
        var relationship = getRelationMap(cls);

        Set<String> vertexSet = new TreeSet<>(relationship.keySet());

        Map<String, String> fathers = new TreeMap<>();
        vertexSet.forEach(v -> fathers.put(v, v));

        for (var vertex : relationship.keySet())
        {
            relationship.get(vertex).forEach(u -> fathers.put(getFather(fathers, u), getFather(fathers, vertex)));
        }

        Set<String> sets = new TreeSet<>();

        for (var elem : fathers.keySet())
        {
            sets.add(getFather(fathers, elem));
        }

        result.addResult(cls.name, sets.size());
    }

    private String getFather(Map<String, String> fathers, String elem)
    {
        while (!fathers.get(elem).equals(elem))
        {
            elem = fathers.get(elem);
        }
        return elem;
    }

    private Map<String, Set<String>> getRelationMap(ClassNode cls)
    {
        Map<String, Set<String>> relationMap = new TreeMap<>();
        cls.fields.forEach(f -> relationMap.put(f.name, new TreeSet<>()));

        Set<String> fields = new TreeSet<>();
        cls.fields.forEach(f -> fields.add(f.name));
        Set<String> methods = new TreeSet<>();
        cls.methods.forEach(m -> methods.add(Util.normalizeMethodSignature(cls.name, m.name, m.desc)));

        for (var m : cls.methods)
        {
            String methodSignature = Util.normalizeMethodSignature(cls.name, m.name, m.desc);
            var relatedMethodsAndFields = getRelatedMethodsAndFields(methods, fields, m);
            relationMap.put(methodSignature, relatedMethodsAndFields);
        }

        return relationMap;
    }

    private Set<String> getRelatedMethodsAndFields(Set<String> methods, Set<String> fields, MethodNode method)
    {
        Set<String> relatedNames = new TreeSet<>();
        for (var instruction : method.instructions)
        {
            if (instruction instanceof MethodInsnNode methodInsnNode)
            {
                String owner = methodInsnNode.owner;
                String name = methodInsnNode.name;
                String desc = methodInsnNode.desc;
                String methodSignature = Util.normalizeMethodSignature(owner, name, desc);

                if (methods.contains(methodSignature))
                    relatedNames.add(methodSignature);
            }
            else if (instruction instanceof FieldInsnNode fieldInsnNode)
            {
                if (fields.contains(fieldInsnNode.name))
                    relatedNames.add(fieldInsnNode.name);
            }
        }

        return relatedNames;
    }

    @Override
    public AnalyzeResult getAnalyzeResult()
    {
        return result;
    }
}
