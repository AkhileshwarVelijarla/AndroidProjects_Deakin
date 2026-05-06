package com.example.istreampersonalvideoplaylistapp_51c.utils;

import android.net.Uri;
import android.text.TextUtils;

public class YouTubeUtils {
    private YouTubeUtils() {
    }

    public static String extractVideoId(String url) {
        if (TextUtils.isEmpty(url)) {
            return null;
        }

        try {
            Uri uri = Uri.parse(url.trim());
            String host = uri.getHost() == null ? "" : uri.getHost().toLowerCase();

            if (host.contains("youtube.com")) {
                String fromQuery = uri.getQueryParameter("v");
                if (isValidVideoId(fromQuery)) {
                    return fromQuery;
                }

                String path = uri.getPath() == null ? "" : uri.getPath();
                String[] parts = path.split("/");
                for (int i = 0; i < parts.length - 1; i++) {
                    if (parts[i].equals("embed") || parts[i].equals("shorts") || parts[i].equals("live")) {
                        String possibleId = parts[i + 1];
                        if (isValidVideoId(possibleId)) {
                            return possibleId;
                        }
                    }
                }
            }

            if (host.contains("youtu.be")) {
                String path = uri.getPath();
                if (path != null && path.length() > 1) {
                    String possibleId = path.substring(1);
                    if (isValidVideoId(possibleId)) {
                        return possibleId;
                    }
                }
            }
        } catch (Exception ignored) {
            return null;
        }

        return null;
    }

    public static boolean isValidYouTubeUrl(String url) {
        return extractVideoId(url) != null;
    }

    public static String buildEmbedHtml(String videoId) {
        return "<html><body style=\"margin:0;background:#000;\">"
                + "<iframe width=\"100%\" height=\"100%\" "
                + "src=\"https://www.youtube.com/embed/" + videoId + "?autoplay=1\" "
                + "title=\"YouTube video player\" frameborder=\"0\" "
                + "allow=\"accelerometer; autoplay; clipboard-write; encrypted-media; gyroscope; picture-in-picture; web-share\" "
                + "allowfullscreen></iframe></body></html>";
    }

    private static boolean isValidVideoId(String videoId) {
        return videoId != null && videoId.matches("^[A-Za-z0-9_-]{11}$");
    }
}
