package com.salesianostriana.dam.spring.data.jpa;

import com.salesianostriana.dam.spring.data.jpa.models.Critica;
import com.salesianostriana.dam.spring.data.jpa.models.Genero;
import com.salesianostriana.dam.spring.data.jpa.models.Pelicula;
import com.salesianostriana.dam.spring.data.jpa.repos.GeneroRepository;
import com.salesianostriana.dam.spring.data.jpa.repos.PeliculaRepository;
import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Component;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.support.TransactionTemplate;

@Component
public class DataSeed {

    private final GeneroRepository generoRepository;
    private final PeliculaRepository peliculaRepository;
    private final TransactionTemplate transactionTemplate;

    public DataSeed(GeneroRepository generoRepository, PeliculaRepository peliculaRepository,
                    PlatformTransactionManager transactionManager) {
        this.generoRepository = generoRepository;
        this.peliculaRepository = peliculaRepository;
        this.transactionTemplate = new TransactionTemplate(transactionManager);
    }

    @PostConstruct
    public void cargarDatos() {
        transactionTemplate.executeWithoutResult(status -> {
            Genero cienciaFiccion = generoRepository.save(new Genero(null, "Ciencia ficción"));
            Genero magia = generoRepository.save(new Genero(null, "Magia"));

            Pelicula pelicula1 = new Pelicula();
            pelicula1.setTitulo("Interestelar");
            pelicula1.setAnioEstreno("2014");
            pelicula1.getGeneros().add(cienciaFiccion);
            pelicula1.getGeneros().add(magia);
            pelicula1.addCritica(new Critica(null, "Pepe", "Locura", 9.0, null));
            pelicula1.addCritica(new Critica(null, "Paco", "Guapísimo", 8.5, null));

            Pelicula pelicula2 = new Pelicula();
            pelicula2.setTitulo("Ahora me ves");
            pelicula2.setAnioEstreno("2016");
            pelicula2.getGeneros().add(magia);
            pelicula2.addCritica(new Critica(null, "Marta", "Me encanta la magia", 10, null));

            peliculaRepository.save(pelicula1);
            peliculaRepository.save(pelicula2);
        });
    }
}
