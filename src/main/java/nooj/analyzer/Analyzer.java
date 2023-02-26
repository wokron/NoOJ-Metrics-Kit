package nooj.analyzer;

import nooj.result.AnalyzeResult;
import org.objectweb.asm.tree.ClassNode;

import java.util.List;

public interface Analyzer
{
    void analyze(List<ClassNode> projectClasses);
    AnalyzeResult getAnalyzeResult();
}
