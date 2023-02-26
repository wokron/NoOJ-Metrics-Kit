package nooj.result;

import nooj.utils.ConstStrings;

import java.util.Map;
import java.util.TreeMap;

public class ClassAnalyzeResult<V> extends AnalyzeResult
{
    private final Map<String, V> classAnalyzeResults = new TreeMap<>();

    public ClassAnalyzeResult(String name, String msg)
    {
        super(name, msg);
    }

    public void addResult(String className, V resultValue)
    {
        classAnalyzeResults.put(className, resultValue);
    }

    public Map<String, V> getAnalyzeResults()
    {
        return classAnalyzeResults;
    }

    protected String toString(V value)
    {
        return value.toString();
    }

    @Override
    public String toString()
    {
        StringBuilder sb = new StringBuilder(super.toString());
        for (var className : classAnalyzeResults.keySet())
        {
            sb.append("\n")
                    .append(ConstStrings.IN).append(" ").append(className).append(": ")
                    .append(toString(classAnalyzeResults.get(className)));
        }

        return sb.toString();
    }
}
