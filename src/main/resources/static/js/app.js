// Estadísticas
function updateStats() {
    const videoCards = document.querySelectorAll('.video-item');
    let favoritos = 0;
    let totalLikes = 0;
    
    videoCards.forEach(card => {
        if (card.getAttribute('data-is-favorite') === 'true') favoritos++;
        totalLikes += parseInt(card.getAttribute('data-likes') || 0);
    });
    
    document.getElementById('totalFavoritos').textContent = favoritos;
    document.getElementById('totalLikes').textContent = totalLikes;
}

// Tabs
document.querySelectorAll('.tab-btn').forEach(btn => {
    btn.addEventListener('click', function() {
        const section = this.getAttribute('data-section');
        
        document.querySelectorAll('.tab-btn').forEach(b => b.classList.remove('active'));
        this.classList.add('active');
        
        document.querySelectorAll('.section-content').forEach(s => s.classList.remove('active'));
        document.getElementById(`section-${section}`).classList.add('active');
        
        updateSectionContent(section);
    });
});

// Actualizar contenido de secciones
function updateSectionContent(section) {
    const allCards = Array.from(document.querySelectorAll('#videosContainer-all .video-item'));
    const container = document.getElementById(`videosContainer-${section}`);
    
    if (!container) return;
    
    container.innerHTML = '';
    
    let filteredCards = [];
    
    if (section === 'favorites') {
        filteredCards = allCards.filter(card => card.getAttribute('data-is-favorite') === 'true');
    } else if (section === 'popular') {
        filteredCards = [...allCards]
            .filter(card => parseInt(card.getAttribute('data-likes')) > 0)
            .sort((a, b) => parseInt(b.getAttribute('data-likes')) - parseInt(a.getAttribute('data-likes')));
    } else {
        filteredCards = allCards;
    }
    
    filteredCards.forEach(card => {
        const clone = card.cloneNode(true);
        container.appendChild(clone);
    });
    
    // Aplicar filtros después de cambiar de sección
    setTimeout(filterVideos, 100);
}

// Función para filtrar videos
function filterVideos() {
    const searchTerm = document.getElementById('searchInput')?.value.toLowerCase() || '';
    const categoryFilter = document.getElementById('categoryFilter')?.value || '';
    const activeSection = document.querySelector('.section-content.active');
    const videoCards = activeSection.querySelectorAll('.video-item');
    
    videoCards.forEach(card => {
        const videoName = card.getAttribute('data-video-name').toLowerCase();
        const videoCategory = card.getAttribute('data-categoria') || '';
        
        const matchesSearch = videoName.includes(searchTerm);
        const matchesCategory = !categoryFilter || videoCategory === categoryFilter;
        
        card.style.display = (matchesSearch && matchesCategory) ? '' : 'none';
    });
}

// Búsqueda por nombre
document.getElementById('searchInput')?.addEventListener('input', filterVideos);

// Filtro por categoría
document.getElementById('categoryFilter')?.addEventListener('change', filterVideos);

// Ordenamiento
document.getElementById('sortSelect')?.addEventListener('change', function(e) {
    const sortBy = e.target.value;
    const activeSection = document.querySelector('.section-content.active');
    const container = activeSection.querySelector('.videos-grid');
    const cards = Array.from(container.querySelectorAll('.video-item'));
    
    cards.sort((a, b) => {
        if (sortBy === 'recent') {
            return new Date(b.getAttribute('data-date')) - new Date(a.getAttribute('data-date'));
        } else if (sortBy === 'oldest') {
            return new Date(a.getAttribute('data-date')) - new Date(b.getAttribute('data-date'));
        } else if (sortBy === 'likes') {
            return parseInt(b.getAttribute('data-likes')) - parseInt(a.getAttribute('data-likes'));
        } else if (sortBy === 'name') {
            return a.getAttribute('data-video-name').localeCompare(b.getAttribute('data-video-name'));
        }
        return 0;
    });
    
    cards.forEach(card => container.appendChild(card));
});

// Form submit
document.getElementById('addVideoForm')?.addEventListener('submit', function() {
    const btn = document.getElementById('submitBtn');
    btn.innerHTML = '<span class="spinner-border spinner-border-sm"></span>';
    btn.disabled = true;
});

// Auto-hide alerts
setTimeout(() => {
    document.querySelectorAll('.alert').forEach(alert => {
        alert.style.opacity = '0';
        setTimeout(() => alert.remove(), 300);
    });
}, 5000);

// Modal de video
function openVideoModal(embedUrl, videoName) {
    const modal = document.getElementById('videoModal');
    const modalVideo = document.getElementById('modalVideo');
    const modalTitle = document.getElementById('modalTitle');
    
    modalVideo.src = embedUrl;
    modalTitle.textContent = videoName;
    modal.classList.add('active');
    document.body.style.overflow = 'hidden';
}

function closeVideoModal() {
    const modal = document.getElementById('videoModal');
    const modalVideo = document.getElementById('modalVideo');
    
    modal.classList.remove('active');
    modalVideo.src = '';
    document.body.style.overflow = '';
}

// Cerrar modal al hacer clic fuera
document.getElementById('videoModal')?.addEventListener('click', function(e) {
    if (e.target === this) {
        closeVideoModal();
    }
});

// Cerrar modal con ESC
document.addEventListener('keydown', function(e) {
    if (e.key === 'Escape') {
        closeVideoModal();
    }
});

// Copiar link del video
function copyVideoLink(link) {
    navigator.clipboard.writeText(link).then(function() {
        // Mostrar toast de confirmación
        showToast('Link copiado al portapapeles');
        
        // Cambiar temporalmente el botón
        const buttons = document.querySelectorAll('.btn-copy');
        buttons.forEach(btn => {
            if (btn.getAttribute('onclick')?.includes(link)) {
                const originalHTML = btn.innerHTML;
                btn.classList.add('copied');
                btn.innerHTML = '<i class="bi bi-check"></i>';
                setTimeout(() => {
                    btn.classList.remove('copied');
                    btn.innerHTML = originalHTML;
                }, 2000);
            }
        });
    }).catch(function(err) {
        console.error('Error al copiar:', err);
        showToast('Error al copiar el link', 'error');
    });
}

// Mostrar toast
function showToast(message, type = 'success') {
    const toast = document.createElement('div');
    toast.className = 'toast';
    toast.style.background = type === 'error' ? 'var(--danger)' : 'var(--success)';
    toast.innerHTML = `
        <i class="bi ${type === 'error' ? 'bi-exclamation-triangle-fill' : 'bi-check-circle-fill'}"></i>
        <span>${message}</span>
    `;
    document.body.appendChild(toast);
    
    setTimeout(() => {
        toast.style.opacity = '0';
        setTimeout(() => toast.remove(), 300);
    }, 3000);
}

// Modal de confirmación para eliminar
let formToSubmit = null;

function showConfirmDelete(button) {
    const form = button.closest('.delete-form');
    const videoName = form.getAttribute('data-video-name') || 'este video';
    
    formToSubmit = form;
    
    const modal = document.getElementById('confirmModal');
    const message = document.getElementById('confirmMessage');
    message.textContent = `¿Estás seguro de que deseas eliminar "${videoName}"? Esta acción no se puede deshacer.`;
    
    modal.classList.add('active');
    document.body.style.overflow = 'hidden';
}

function closeConfirmModal() {
    const modal = document.getElementById('confirmModal');
    modal.classList.remove('active');
    document.body.style.overflow = '';
    formToSubmit = null;
}

// Confirmar eliminación
document.getElementById('confirmDeleteBtn')?.addEventListener('click', function() {
    if (formToSubmit) {
        formToSubmit.submit();
    }
});

// Cerrar modal al hacer clic fuera
document.getElementById('confirmModal')?.addEventListener('click', function(e) {
    if (e.target === this) {
        closeConfirmModal();
    }
});

// Cerrar modal con ESC
document.addEventListener('keydown', function(e) {
    if (e.key === 'Escape') {
        const confirmModal = document.getElementById('confirmModal');
        if (confirmModal.classList.contains('active')) {
            closeConfirmModal();
        }
    }
});

// Inicializar
updateStats();
updateSectionContent('favorites');
updateSectionContent('popular');

