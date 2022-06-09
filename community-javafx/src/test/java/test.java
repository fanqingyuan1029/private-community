
import com.fqy.entity.entityutils.RegexUtils;

import java.util.regex.Pattern;

public class test {
    public static void main(String[] args) {
        String a ="啊啊啊啊啊啊啊啊啊啊啊啊啊啊";
        System.out.println(Pattern.matches(RegexUtils.trueName,a));
    }
}
