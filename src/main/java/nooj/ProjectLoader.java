package nooj;

import org.objectweb.asm.ClassReader;
import org.objectweb.asm.tree.ClassNode;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class ProjectLoader
{
    private final List<ClassNode> classNodes = new ArrayList<>();

    public ProjectLoader(String projPath) throws IOException
    {
        loadProject(projPath);
    }

    public void loadProject(String projPath) throws IOException
    {
        File projDir = new File(projPath);
        classNodes.clear();
        classNodes.addAll(loadClassesOnFile(projDir));
    }

    private List<ClassNode> loadClassesOnFile(File currentFile) throws IOException
    {
        List<ClassNode> newAcquiredClasses = new ArrayList<>();
        if (currentFile.isDirectory())
        {
            var childrenFile = currentFile.listFiles();
            for (var childFile : Objects.requireNonNull(childrenFile))
            {
                newAcquiredClasses.addAll(loadClassesOnFile(childFile));
            }
        }
        else if (currentFile.getName().endsWith(".class"))
        {
            try (var in = new FileInputStream(currentFile))
            {
                newAcquiredClasses.add(getClassNodeByStream(in));
            }
        }

        return newAcquiredClasses;
    }

    private ClassNode getClassNodeByStream(FileInputStream in) throws IOException
    {
        ClassReader classReader = new ClassReader(in);
        ClassNode classNode = new ClassNode();
        classReader.accept(classNode,
                ClassReader.SKIP_DEBUG|ClassReader.SKIP_FRAMES);
        return classNode;
    }

    public List<ClassNode> getResult()
    {
        return classNodes;
    }
}
