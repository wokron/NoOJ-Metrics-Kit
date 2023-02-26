package nooj.analyzer;

import nooj.result.AnalyzeResult;
import nooj.result.ListLikeAnalyzeResult;
import nooj.utils.ConstStrings;
import org.objectweb.asm.tree.ClassNode;

import java.util.*;

public class CyclicDependencyAnalyzer implements Analyzer
{
    private final CBOAnalyzer cboAnalyzer = new CBOAnalyzer();
    private final ListLikeAnalyzeResult<String> result = new ListLikeAnalyzeResult<>("Code Smell", "Cyclic dependency detect");

    private final Stack<String> path = new Stack<>();

    @Override
    public void analyze(List<ClassNode> projectClasses)
    {
        cboAnalyzer.analyze(projectClasses);
        var cboResults = cboAnalyzer.getAnalyzeResult().getAnalyzeResults();

        Map<String, List<String>> cyclesStorage = new TreeMap<>();

        for (var cls : cboResults.keySet())
        {
            dfs(cls, cboResults, cyclesStorage);
        }

        cyclesStorage.keySet().forEach(k -> result.addResult(drawDependencyString(cyclesStorage.get(k))));
    }

    private String drawDependencyString(List<String> classes)
    {
        return String.join(ConstStrings.DEPEND_ON, classes);
    }

    private void dfs(String currentClass, Map<String, Set<String>> dependencyMap, Map<String, List<String>> cyclesStorage)
    {
        if (!dependencyMap.containsKey(currentClass))
            return;
        else if (path.contains(currentClass))
        {
            addCyclicPath(cyclesStorage, new ArrayList<>(path));
            return;
        }

        path.push(currentClass);
        for (var dependentClass : dependencyMap.get(currentClass))
        {
            dfs(dependentClass, dependencyMap, cyclesStorage);
        }
        path.pop();
    }

    private void addCyclicPath(Map<String, List<String>> cyclesStorage, List<String> path)
    {
        String label = getUniqueCyclicPathLabel(path);
        cyclesStorage.put(label, path);
    }

    private String getUniqueCyclicPathLabel(List<String> path)
    {
        path.sort(String::compareTo);
        return path.toString();
    }

    @Override
    public AnalyzeResult getAnalyzeResult()
    {
        return result;
    }
}
