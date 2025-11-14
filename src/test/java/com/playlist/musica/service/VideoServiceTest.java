package com.playlist.musica.service;

import com.playlist.musica.model.Video;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class VideoServiceTest {

    @Autowired
    private VideoService videoService;

    @Test
    void testGuardarVideo() {
        Video video = new Video("Test Video", "https://www.youtube.com/watch?v=dQw4w9WgXcQ");
        Video guardado = videoService.guardar(video);
        
        assertNotNull(guardado.getId());
        assertEquals("Test Video", guardado.getNombre());
        assertNotNull(guardado.getYoutubeId());
        assertNotNull(guardado.getEmbedUrl());
        assertEquals(0, guardado.getLikes());
        assertFalse(guardado.getEsFavorito());
    }

    @Test
    void testGuardarVideoConUrlInvalida() {
        Video video = new Video("Test Video", "https://www.google.com");
        
        assertThrows(IllegalArgumentException.class, () -> {
            videoService.guardar(video);
        });
    }

    @Test
    void testDarLike() {
        Video video = new Video("Test Video", "https://www.youtube.com/watch?v=dQw4w9WgXcQ");
        Video guardado = videoService.guardar(video);
        
        Video conLike = videoService.darLike(guardado.getId());
        assertEquals(1, conLike.getLikes());
    }

    @Test
    void testToggleFavorito() {
        Video video = new Video("Test Video", "https://www.youtube.com/watch?v=dQw4w9WgXcQ");
        Video guardado = videoService.guardar(video);
        
        assertFalse(guardado.getEsFavorito());
        
        Video favorito = videoService.toggleFavorito(guardado.getId());
        assertTrue(favorito.getEsFavorito());
        
        Video noFavorito = videoService.toggleFavorito(guardado.getId());
        assertFalse(noFavorito.getEsFavorito());
    }

    @Test
    void testEliminarVideo() {
        Video video = new Video("Test Video", "https://www.youtube.com/watch?v=dQw4w9WgXcQ");
        Video guardado = videoService.guardar(video);
        
        videoService.eliminar(guardado.getId());
        
        assertTrue(videoService.obtenerPorId(guardado.getId()).isEmpty());
    }
}

