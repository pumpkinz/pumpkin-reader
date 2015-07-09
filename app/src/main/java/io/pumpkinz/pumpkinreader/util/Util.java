package io.pumpkinz.pumpkinreader.util;

import android.content.res.Resources;
import android.util.Log;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;


public class Util {

    public static String getDomainName(String url) {
        try {
            if (url.isEmpty()) {
                throw new URISyntaxException(url, "Empty URL");
            }

            URI uri = new URI(url);
            String domain = uri.getHost();
            return domain.startsWith("www.") ? domain.substring(4) : domain;
        } catch (URISyntaxException ex) {
            return url;
        }
    }

    public static String join(List<Integer> list, Character separator) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0, n = list.size(); i < n; ++i) {
            sb.append(list.get(i).toString());
            if (i < n) { sb.append(separator); }
        }

        return sb.toString();
    }

    public static List<Integer> split(String value, String separator) {
        String[] parsedValues = value.split(Pattern.quote(separator));
        List<Integer> retval = new ArrayList<>();

        for (String parsedValue : parsedValues) {
            retval.add(Integer.valueOf(parsedValue));
        }

        return retval;
    }

    public static CharSequence trim(CharSequence source) {

        if(source == null)
            return "";

        int i = source.length();

        // loop back to the first non-whitespace character
        while(--i >= 0 && Character.isWhitespace(source.charAt(i))) {
        }

        return source.subSequence(0, i + 1);
    }

    public static int dpToPx(int dp)
    {
        return (int) (dp * Resources.getSystem().getDisplayMetrics().density);
    }

    public static int pxToDp(int px)
    {
        return (int) (px / Resources.getSystem().getDisplayMetrics().density);
    }

}
