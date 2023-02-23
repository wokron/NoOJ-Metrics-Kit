package nooj.analyzer;

import nooj.ProjectLoader;

import java.io.IOException;

public class AnalyzerTest
{
    public static void main(String[] args) throws IOException
    {
        String path = "E:\\桌面文档\\临时整理\\整理\\大二上整理\\电子版作业\\面向对象\\MySCS\\out\\production\\MySCS";
        Analyzer analyzer = new LCOMAnalyzer();
        test(path, analyzer);
    }

    public static void test(String path, Analyzer analyzer) throws IOException
    {
        var loader = new ProjectLoader();
        loader.loadProject(path);
        analyzer.analyze(loader.getResult());
        analyzer.getAnalyzeResult();
    }
}
