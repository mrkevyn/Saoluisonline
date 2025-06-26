package semit.saoluis.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "projetos")
public class Projetos {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String nome;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(columnDefinition = "TEXT")
    private String link;

    @Column(name = "\"to\"", columnDefinition = "TEXT")
    private String to;
    
    @Column(name = "is_visible")
    private Boolean isVisible;

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
    }

    // Getters e setters

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public String getLink() { return link; }
    public void setLink(String link) { this.link = link; }

    public String getTo() { return to; }
    public void setTo(String to) { this.to = to; }
    
    public Boolean getIsVisible() { return isVisible; }

    public void setIsVisible(Boolean isVisible) { this.isVisible = isVisible; }
}
