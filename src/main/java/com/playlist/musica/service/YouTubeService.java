package com.playlist.musica.service;

import org.springframework.stereotype.Service;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class YouTubeService {

    private static final String YOUTUBE_REGEX = "(?:youtube\\.com\\/(?:[^\\/]+\\/.+\\/|(?:v|e(?:mbed)?)\\/|.*[?&]v=)|youtu\\.be\\/)([^\"&?\\/\\s]{11})";
    private static final Pattern YOUTUBE_PATTERN = Pattern.compile(YOUTUBE_REGEX);
    private static final String EMBED_URL_TEMPLATE = "https://www.youtube.com/embed/%s";

    /**
     * Extrae el ID del video de YouTube desde una URL
     * @param url URL del video de YouTube
     * @return ID del video o null si no es una URL válida de YouTube
     */
    public String extraerVideoId(String url) {
        if (url == null || url.trim().isEmpty()) {
            return null;
        }

        Matcher matcher = YOUTUBE_PATTERN.matcher(url);
        if (matcher.find()) {
            return matcher.group(1);
        }

        return null;
    }

    /**
     * Valida si una URL es de YouTube
     * @param url URL a validar
     * @return true si es una URL válida de YouTube
     */
    public boolean esUrlValida(String url) {
        return extraerVideoId(url) != null;
    }

    /**
     * Genera la URL embebida de YouTube
     * @param videoId ID del video de YouTube
     * @return URL embebida
     */
    public String generarEmbedUrl(String videoId) {
        if (videoId == null || videoId.trim().isEmpty()) {
            return null;
        }
        return String.format(EMBED_URL_TEMPLATE, videoId);
    }
}

