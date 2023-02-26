package nooj.analyzer;

import nooj.result.AnalyzeResult;
import nooj.result.ListLikeAnalyzeResult;
import nooj.utils.Util;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.MethodNode;

import java.util.*;
import java.util.regex.Pattern;

public class LayerExposureAnalyzer implements Analyzer
{
    private final ListLikeAnalyzeResult<String> result = new ListLikeAnalyzeResult<>("Code Smell", "Irrational layer structure detect");
    private final Map<String, int[]> depthRecord = new TreeMap<>();

    @Override
    public void analyze(List<ClassNode> projectClasses)
    {
        Map<String, Set<String>> structure = getLayerStructure(projectClasses);

        List<String> entranceClasses = getEntranceClasses(projectClasses);

        for (var entranceClass : entranceClasses)
        {
            bfs(entranceClass, structure, 1);
        }

        for (var className : depthRecord.keySet())
        {
            var minmax = depthRecord.get(className);
            int gap = minmax[1] - minmax[0];
            if (gap > 1)
                result.addResult(String.format("class %s access different layer depth, gap:%d(%d-%d)",
                        className, gap, minmax[0], minmax[1]));
        }
    }

    private Map<String, Set<String>> getLayerStructure(List<ClassNode> projectClasses)
    {
        Map<String, Set<String>> structureMap = new TreeMap<>();

        for (var cls : projectClasses)
        {
            structureMap.put(cls.name, getRelatedLayers(cls));
        }

        return structureMap;
    }

    private Set<String> getRelatedLayers(ClassNode cls)
    {
        Set<String> layers = new TreeSet<>();
        cls.fields.forEach(f -> layers.add(Util.solveDescriptor(f.desc).get(0)));

        layers.removeIf(elem -> Pattern.matches("^(java.*|\\[?[BCDFIJSZV])$", elem));

        return layers;
    }

    private List<String> getEntranceClasses(List<ClassNode> projectClasses)
    {
        for (var cls : projectClasses)
        {
            var mainFounded = cls.methods.stream().filter(this::isMainFunction).findAny();
            if (mainFounded.isPresent())
            {
                return Util.getClassesInMethods(mainFounded.get());
            }
        }
        return new ArrayList<>();
    }

    private boolean isMainFunction(MethodNode method)
    {
        return method.name.equals("Main") && method.desc.equals("([Ljava/lang/String;)V");
    }

    private void bfs(String currentClass, Map<String, Set<String>> structureMap, int depth)
    {
        if (!structureMap.containsKey(currentClass))
            return;
        if (depthRecord.containsKey(currentClass))
        {
            int[] minmax = depthRecord.get(currentClass);
            minmax[0] = Math.min(minmax[0], depth);
            minmax[1] = Math.max(minmax[1], depth);
            return;
        }

        int[] minmax = new int[]{depth, depth};
        depthRecord.put(currentClass, minmax);

        for (var deeperLayer : structureMap.get(currentClass))
        {
            bfs(deeperLayer, structureMap, depth+1);
        }
    }

    @Override
    public AnalyzeResult getAnalyzeResult()
    {
        return result;
    }
}
