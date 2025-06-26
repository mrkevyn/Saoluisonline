package semit.saoluis.model;

import jakarta.persistence.*;

@Entity
@Table(name = "portal")

public class Portal {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nome;

    private String link;
    
    @Column(name = "is_visible")
    private Boolean isVisible = true;
    
    public Long getId() {return id;}
    public void setId(Long id) {this.id = id;}
    
    public String getNome() {return nome;}
    public void setNome(String nome) {this.nome = nome;}
    
    public String getLink() {return link;}
    public void setLink(String link) {this.link = link;}
    
    public Boolean getIsVisible() {return isVisible;}
    public void setIsVisible(Boolean isVisible) {this.isVisible = isVisible;}
}
