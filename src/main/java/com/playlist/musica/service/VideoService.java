package com.playlist.musica.service;

import com.playlist.musica.model.Video;
import com.playlist.musica.repository.VideoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class VideoService {

    private final VideoRepository videoRepository;
    private final YouTubeService youTubeService;

    @Autowired
    public VideoService(VideoRepository videoRepository, YouTubeService youTubeService) {
        this.videoRepository = videoRepository;
        this.youTubeService = youTubeService;
    }

    /**
     * Obtiene todos los videos
     */
    public List<Video> obtenerTodos() {
        return videoRepository.findAll();
    }

    /**
     * Obtiene un video por ID
     */
    public Optional<Video> obtenerPorId(Long id) {
        return videoRepository.findById(id);
    }

    /**
     * Guarda un nuevo video
     */
    public Video guardar(Video video) {
        // Validar que la URL sea de YouTube
        if (!youTubeService.esUrlValida(video.getLink())) {
            throw new IllegalArgumentException("La URL proporcionada no es válida para YouTube");
        }

        // Extraer ID de YouTube y generar URL embebida
        String videoId = youTubeService.extraerVideoId(video.getLink());
        video.setYoutubeId(videoId);
        video.setEmbedUrl(youTubeService.generarEmbedUrl(videoId));

        // Si no tiene categoría, asignar "Música" por defecto
        if (video.getCategoria() == null || video.getCategoria().trim().isEmpty()) {
            video.setCategoria("Música");
        }

        return videoRepository.save(video);
    }

    /**
     * Elimina un video por ID
     */
    public void eliminar(Long id) {
        videoRepository.deleteById(id);
    }

    /**
     * Incrementa los likes de un video
     */
    public Video darLike(Long id) {
        Video video = videoRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Video no encontrado"));
        video.setLikes(video.getLikes() + 1);
        return videoRepository.save(video);
    }

    /**
     * Marca/desmarca un video como favorito
     */
    public Video toggleFavorito(Long id) {
        Video video = videoRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Video no encontrado"));
        video.setEsFavorito(!video.getEsFavorito());
        return videoRepository.save(video);
    }

    /**
     * Obtiene solo los videos favoritos
     */
    public List<Video> obtenerFavoritos() {
        return videoRepository.findByEsFavoritoTrue();
    }
}

