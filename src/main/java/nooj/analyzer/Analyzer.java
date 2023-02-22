package nooj.analyzer;

import org.objectweb.asm.tree.ClassNode;

import java.util.List;

public interface Analyzer
{
    void analyze(List<ClassNode> projectClasses);
    String getAnalyzeResult();
}
