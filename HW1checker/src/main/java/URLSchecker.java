import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Created by shikhar on 10/18/17.
 */
public class URLSchecker {
    private static String filePath = "/Users/shikhar/Downloads/irCorrection_hw1/Yuqing_Zhong_Tu_HW1/Tast-1-urls.txt";

    public static void main(String[] args) {
        Set<String> urls = readToSet(filePath);

        urls.stream()
                .limit(8)
                .forEach(System.out::println);
    }

    private static Set<String> readToSet(String path) {
        try {
            return Files.lines(Paths.get(filePath))
                    .map()
                    .map(String::trim)
                    .collect(Collectors.toSet());
        } catch (IOException e) {
            System.out.println("exception in reading file");
            e.printStackTrace();
            System.exit(1);
        }
        return null; // unreachable statement
    }
}
