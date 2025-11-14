package com.playlist.musica.controller;

import com.playlist.musica.model.Video;
import com.playlist.musica.service.VideoService;
import com.playlist.musica.service.YouTubeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class HomeController {

    private final VideoService videoService;
    private final YouTubeService youTubeService;

    @Autowired
    public HomeController(VideoService videoService, YouTubeService youTubeService) {
        this.videoService = videoService;
        this.youTubeService = youTubeService;
    }

    @GetMapping("/")
    public String home(Model model) {
        model.addAttribute("videos", videoService.obtenerTodos());
        model.addAttribute("youTubeService", youTubeService);
        // Si no hay un objeto video en el modelo (de flash attributes), crear uno nuevo
        if (!model.containsAttribute("video")) {
            model.addAttribute("video", new Video());
        }
        return "index";
    }

    @PostMapping("/videos/agregar")
    public String agregarVideo(@ModelAttribute Video video, RedirectAttributes redirectAttributes) {
        try {
            videoService.guardar(video);
            redirectAttributes.addFlashAttribute("mensaje", "Video agregado exitosamente");
            redirectAttributes.addFlashAttribute("tipoMensaje", "success");
        } catch (IllegalArgumentException e) {
            redirectAttributes.addFlashAttribute("mensaje", "Error: " + e.getMessage());
            redirectAttributes.addFlashAttribute("tipoMensaje", "danger");
            redirectAttributes.addFlashAttribute("video", video); // Mantener valores del formulario
        }
        return "redirect:/";
    }

    @PostMapping("/videos/eliminar/{id}")
    public String eliminarVideo(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            videoService.eliminar(id);
            redirectAttributes.addFlashAttribute("mensaje", "Video eliminado exitosamente");
            redirectAttributes.addFlashAttribute("tipoMensaje", "success");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("mensaje", "Error al eliminar el video");
            redirectAttributes.addFlashAttribute("tipoMensaje", "danger");
        }
        return "redirect:/";
    }

    @PostMapping("/videos/like/{id}")
    public String darLike(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            videoService.darLike(id);
            redirectAttributes.addFlashAttribute("mensaje", "Like agregado");
            redirectAttributes.addFlashAttribute("tipoMensaje", "success");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("mensaje", "Error al dar like");
            redirectAttributes.addFlashAttribute("tipoMensaje", "danger");
        }
        return "redirect:/";
    }

    @PostMapping("/videos/favorito/{id}")
    public String toggleFavorito(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            Video video = videoService.toggleFavorito(id);
            String mensaje = video.getEsFavorito() ? "Video marcado como favorito" : "Video desmarcado de favoritos";
            redirectAttributes.addFlashAttribute("mensaje", mensaje);
            redirectAttributes.addFlashAttribute("tipoMensaje", "success");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("mensaje", "Error al cambiar estado de favorito");
            redirectAttributes.addFlashAttribute("tipoMensaje", "danger");
        }
        return "redirect:/";
    }
}

