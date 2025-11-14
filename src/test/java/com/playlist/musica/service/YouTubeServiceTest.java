package com.playlist.musica.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class YouTubeServiceTest {

    @Autowired
    private YouTubeService youTubeService;

    @Test
    void testExtraerVideoIdDesdeUrlCompleta() {
        String url = "https://www.youtube.com/watch?v=dQw4w9WgXcQ";
        String videoId = youTubeService.extraerVideoId(url);
        assertEquals("dQw4w9WgXcQ", videoId);
    }

    @Test
    void testExtraerVideoIdDesdeUrlCorta() {
        String url = "https://youtu.be/dQw4w9WgXcQ";
        String videoId = youTubeService.extraerVideoId(url);
        assertEquals("dQw4w9WgXcQ", videoId);
    }

    @Test
    void testEsUrlValida() {
        assertTrue(youTubeService.esUrlValida("https://www.youtube.com/watch?v=dQw4w9WgXcQ"));
        assertTrue(youTubeService.esUrlValida("https://youtu.be/dQw4w9WgXcQ"));
        assertFalse(youTubeService.esUrlValida("https://www.google.com"));
        assertFalse(youTubeService.esUrlValida("no es una url"));
    }

    @Test
    void testGenerarEmbedUrl() {
        String videoId = "dQw4w9WgXcQ";
        String embedUrl = youTubeService.generarEmbedUrl(videoId);
        assertEquals("https://www.youtube.com/embed/dQw4w9WgXcQ", embedUrl);
    }
}

