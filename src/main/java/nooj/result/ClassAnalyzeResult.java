package nooj.result;

import nooj.utils.ConstStrings;

import java.util.Collection;
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
        if (value instanceof Collection<?> c)
        {
            StringBuilder sb = new StringBuilder();
            for (var elem : c)
            {
                sb.append("\n\t").append(elem);
            }
            return sb.toString();
        }
        return value.toString();
    }

    @Override
    public String toString()
    {
        StringBuilder sb = new StringBuilder(super.toString());
        if (classAnalyzeResults.isEmpty())
            sb.append("\nNo interesting result.");
        for (var className : classAnalyzeResults.keySet())
        {
            sb.append("\n")
                    .append(ConstStrings.IN).append(" ").append(className).append(": ")
                    .append(toString(classAnalyzeResults.get(className)));
        }

        return sb.toString();
    }
}
