package nooj.result;

import java.util.Map;
import java.util.TreeMap;

public class MethodAnalyzeResult<V> extends ClassAnalyzeResult<Map<String, V>>
{
    public MethodAnalyzeResult(String name, String msg)
    {
        super(name, msg);
    }

    public void addResult(String className, String methodName, V resultValue)
    {
        Map<String, V> methodResults = getAnalyzeResults().getOrDefault(className, new TreeMap<>());
        methodResults.put(methodName, resultValue);
        super.addResult(className, methodResults);
    }
}
