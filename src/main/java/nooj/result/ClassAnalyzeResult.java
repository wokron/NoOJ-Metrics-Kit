package nooj.result;

import java.util.Map;
import java.util.TreeMap;

public class ClassAnalyzeResult<V> extends AnalyzeResult
{
    private Map<String, V> classAnalyzeResults = new TreeMap<>();

    public ClassAnalyzeResult(String name, String msg)
    {
        super(name, msg);
    }

    public ClassAnalyzeResult(String name, String msg, Map<String, V> initResults)
    {
        super(name, msg);
        classAnalyzeResults = new TreeMap<>(initResults);
    }

    public void addResult(String className, V resultValue)
    {
        classAnalyzeResults.put(className, resultValue);
    }

    public Map<String, V> getAnalyzeResults()
    {
        return classAnalyzeResults;
    }
}
