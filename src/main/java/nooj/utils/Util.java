package nooj.utils;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.MatchResult;
import java.util.regex.Pattern;

public class Util
{
    public static List<String> solveDescriptor(String desc)
    {
        List<String> descTypes = new ArrayList<>();

        String descReg = "L[a-zA-Z/]+;|\\[?[BCDFIJSZ]";
        var allMatch = Pattern.compile(descReg)
                .matcher(desc)
                .results()
                .map(MatchResult::group)
                .toArray(String[]::new);

        for (var arg : allMatch)
        {
            String argType = arg;
            if (arg.startsWith("L") && arg.endsWith(";"))
                argType = argType.substring(1, arg.length()-1);

            descTypes.add(argType);
        }

        return descTypes;
    }
}
