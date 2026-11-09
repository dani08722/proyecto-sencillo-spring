package com.salesianostriana.dam.spring.data.jpa.models;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.proxy.HibernateProxy;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Builder
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Pelicula {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(nullable = false)
    private Long id;

    private String titulo;

    private String anioEstreno;

    @OneToMany(mappedBy = "pelicula",
            cascade = CascadeType.ALL)
    private List<Critica> criticas= new ArrayList<>();

    @ManyToMany
    private List<Genero> generos= new ArrayList<>();

    public void addCritica(Critica c){
        criticas.add(c);
        c.setPelicula(this);
    }

    public void removeCritica(Critica c){
        criticas.remove(c);
        c.setPelicula(null);
    }

    @Override
    public final boolean equals(Object o) {
        if (this == o) return true;
        if (o == null) return false;
        Class<?> oEffectiveClass = o instanceof HibernateProxy ? ((HibernateProxy) o).getHibernateLazyInitializer().getPersistentClass() : o.getClass();
        Class<?> thisEffectiveClass = this instanceof HibernateProxy ? ((HibernateProxy) this).getHibernateLazyInitializer().getPersistentClass() : this.getClass();
        if (thisEffectiveClass != oEffectiveClass) return false;
        Pelicula pelicula = (Pelicula) o;
        return getId() != null && Objects.equals(getId(), pelicula.getId());
    }

    @Override
    public final int hashCode() {
        return this instanceof HibernateProxy ? ((HibernateProxy) this).getHibernateLazyInitializer().getPersistentClass().hashCode() : getClass().hashCode();
    }
}
