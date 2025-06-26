package semit.saoluis.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "banners")
public class Banner {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nome;

    private String link;

    @Column(name = "\"to\"")
    private String to;

    @Column(name = "created_at")
    private LocalDateTime createdAt = LocalDateTime.now();
    
    @Column(name = "is_visible")
    private Boolean isVisible;

    // Getters e Setters

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }

    public String getLink() { return link; }
    public void setLink(String link) { this.link = link; }

    public String getTo() { return to; }
    public void setTo(String to) { this.to = to; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
    
    public Boolean getIsVisible() { return isVisible; }

    public void setIsVisible(Boolean isVisible) { this.isVisible = isVisible; }
}
