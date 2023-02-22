package nooj.analyzer;

import org.objectweb.asm.tree.ClassNode;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class NOCAnalyzer implements Analyzer
{
    private final Map<String, List<String>> childClasses = new TreeMap<>();

    @Override
    public void analyze(List<ClassNode> projectClasses)
    {
        for (var cls : projectClasses)
            childClasses.put(cls.name, new ArrayList<>());

        for (var cls : projectClasses)
        {
            String child = cls.name;
            String father = cls.superName;
            if (!childClasses.containsKey(father))
                continue;
            var children = childClasses.get(father);
            children.add(child);
        }
    }

    @Override
    public String getAnalyzeResult()
    {
        for (var cls : childClasses.keySet())
        {
            System.out.println(cls + ":" + childClasses.get(cls).size());
        }
        return "";
    }
}
