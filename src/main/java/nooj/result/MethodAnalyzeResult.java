package nooj.result;

import nooj.utils.ConstStrings;

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

    @Override
    protected String toString(Map<String, V> value)
    {
        StringBuilder sb = new StringBuilder();
        for (var methodName : value.keySet())
        {
            sb.append("\n\t").append(ConstStrings.METHOD).append(" ").append(methodName).append(": ").append(value.get(methodName));
        }
        return sb.toString();
    }
}
