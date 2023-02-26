package nooj.result;

import java.util.ArrayList;
import java.util.List;

public class ListLikeAnalyzeResult<V> extends AnalyzeResult
{
    List<V> values = new ArrayList<>();

    public ListLikeAnalyzeResult(String name, String msg)
    {
        super(name, msg);
    }

    public void addResult(V value)
    {
        values.add(value);
    }

    public List<V> getAnalyzeResults()
    {
        return values;
    }

    @Override
    public String toString()
    {
        StringBuilder sb = new StringBuilder(super.toString());
        if (values.isEmpty())
            sb.append("\n\tNo interesting result.");
        for (var value : values)
        {
            sb.append("\n\t").append(value);
        }

        return sb.toString();
    }
}
