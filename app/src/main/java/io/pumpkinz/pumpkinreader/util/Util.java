package io.pumpkinz.pumpkinreader.util;

import java.net.URI;
import java.net.URISyntaxException;


public class Util {

    public static String getDomainName(String url) {
        try {
            if (url.equals("")) {
                throw new URISyntaxException(url, "Empty URL");
            }

            URI uri = new URI(url);
            String domain = uri.getHost();
            return domain.startsWith("www.") ? domain.substring(4) : domain;
        } catch (URISyntaxException ex) {
            return url;
        }
    }

}
