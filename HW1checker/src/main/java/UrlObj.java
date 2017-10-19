import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by shikhar on 10/18/17.
 */
public class UrlObj {
    String originalUrl;
    boolean hasColonOrHash;
    boolean isExternalLink;
    boolean isRedirectOrNonExistant; // Non-existent links: -5. Not handling redirects: -3


    public UrlObj(String url) {
        originalUrl = url;
        hasColonOrHash = checkColonAndHash(url);
        isExternalLink = checkIfExtLink(url);
        isRedirectOrNonExistant = checkForRedirect(url, 1);
    }

    private boolean checkForRedirect(String url, int attemptNum) {
        HttpURLConnection.setFollowRedirects(false);
        int responseCode = 0;
        try {
            HttpURLConnection httpURLConnection =
                    (HttpURLConnection) (new URL(url)).openConnection();
            // HttpURLConnection.setFollowRedirects(false);
            httpURLConnection.setInstanceFollowRedirects(false);
            responseCode = httpURLConnection.getResponseCode();
            if (responseCode!=200)
                System.out.println(originalUrl+", responseCode:"+responseCode);
        } catch (IOException e) {
            // try till 3 attempts for this url
            if (attemptNum <= 3)
                return checkForRedirect(url, ++attemptNum);

            // if all attempts fail, throw exception and go to next url
            System.out.println("Exception in connecting to the url "+ url);
            e.printStackTrace();
            // System.exit(1);
        }

        return responseCode!=200;
    }

    private boolean checkIfExtLink(String url) {
        return (! url.startsWith("https://en.wikipedia.org/wiki/"));
    }

    private boolean checkColonAndHash(String url) {
        String newUrl = null;
        if (url.startsWith("http"))
            newUrl = url.replaceFirst("https?:", "");
        if (newUrl.contains(":") || newUrl.contains("#"))
            return true;
        else {
            return false;
        }
    }

}
