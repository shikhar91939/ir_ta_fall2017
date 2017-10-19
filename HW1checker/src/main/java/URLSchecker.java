import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Created by shikhar on 10/18/17.
 */

/**
 * this code does not checkEveryUrl for:
 *      Not following crawling directions/ not realizing Task1 is basically BFS: -8
 *      No politeness: -4
 */
public class URLSchecker {
    private static final int MAX_URLS = 1000;
    private static String filePath = "/Users/shikhar/Downloads/irCorrection_hw1/Yuqing_Zhong_Tu_HW1/Task-2-urls.txt";
    private static String report = "";
    private static List<String> colonHashUrls = new LinkedList<>();
    private static List<String> nonWikiUrls = new LinkedList<>();
    private static List<String> redirectsNonExistant = new LinkedList<>();
    private static int spinnerInt = 1;


    public static void main(String[] args) {

        Set<String> urls = readToSet(filePath);
        System.out.println();

        checkUrlCount(urls);


        List<UrlObj> urlObjs =
                        urls
                        .stream()

                        .map(url -> new UrlObj(url))
                        .peek(ignored -> System.out.print("\r"+getSpinnerChar()))
                        .collect(Collectors.toList());
        System.out.println();

        spinnerInt=1;
        urlObjs.stream()
                //.peek(ignored -> System.out.print(getSpinnerChar()))
                .peek(urlObj -> System.out.println(urlObj.originalUrl))
                .forEach(urlObj -> checkEveryUrl(urlObj));
        System.out.println();

        updateReport();

        if (report.isEmpty())
            System.out.println("everything ok");// todo: improve string
        else
            System.out.println(report);

    }

    private static String getSpinnerChar() {
        incrementSpinnerInt();
        switch (spinnerInt) {
            case 1: return "/";
            case 2: return "-";
            case 3: return "\\";
            case 4: return "|";
            default:
                return "?"; // this statement prevents compilation error. should never be executed.
        }
    }

    private static void incrementSpinnerInt() {
        spinnerInt++;
        if (spinnerInt == 5)
            spinnerInt=1;
    }

    private static void updateReport() {
        if (colonHashUrls.size() != 0) {
            report += "\n\nurls that have colon or Hash:\n";
            for (String url : colonHashUrls)
                report += "\t" + url + "\n";
        }
        if (nonWikiUrls.size() != 0) {
            report += "\n\nurls that are not en.wikipedia links:\n";
            for (String url : nonWikiUrls)
                report += "\t" + url + "\n";
        }
        if (redirectsNonExistant.size() != 0) {
            report += "\n\nurls that are either redirects or do not exist:\n";
            for (String url : redirectsNonExistant)
                report += "\t" + url + "\n";
        }
    }

    private static void checkEveryUrl(UrlObj urlObj) {
        if(urlObj.hasColonOrHash){
            colonHashUrls.add(urlObj.originalUrl);
        }
        if(urlObj.isExternalLink){
            nonWikiUrls.add(urlObj.originalUrl);
        }
        if(urlObj.isRedirectOrNonExistant){
            redirectsNonExistant.add(urlObj.originalUrl);
        }
    }

    private static void checkUrlCount(Set<String> urls) {
        int count = urls.size();
        if (count != MAX_URLS) {
            if (count > MAX_URLS) {
                report += "count > 1000. " + " - 3";
            }else
                report += "count <1000 " + count + ", checkEveryUrl if depth 6 reached";
        }

    }

    private static Set<String> readToSet(String path) {
        try {
            return Files.lines(Paths.get(filePath))
                    .flatMap(line -> Arrays.stream(line.split("\\s+"))) // split on any whitespaces
                    .collect(Collectors.toSet());
            // used flatMap because some files have 1000 urls in one line
            // and some files have 1k urls in 1k line. FlatMap handles both.
        } catch (IOException e) {
            System.out.println("exception in reading file");
            e.printStackTrace();
            System.exit(1);
        }
        return null; // unreachable statement
    }
}
