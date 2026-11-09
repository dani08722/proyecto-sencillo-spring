package com.salesianostriana.dam.spring.data.jpa;

import com.salesianostriana.dam.spring.data.jpa.models.Pelicula;
import com.salesianostriana.dam.spring.data.jpa.repos.CriticaRepository;
import com.salesianostriana.dam.spring.data.jpa.repos.GeneroRepository;
import com.salesianostriana.dam.spring.data.jpa.repos.PeliculaRepository;
import jakarta.annotation.PostConstruct;
import org.springframework.context.annotation.DependsOn;
import org.springframework.stereotype.Component;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.support.TransactionTemplate;

@Component
@DependsOn("dataSeed")
public class DataCheck {

    private final PeliculaRepository peliculaRepository;
    private final CriticaRepository criticaRepository;
    private final GeneroRepository generoRepository;
    private final TransactionTemplate transactionTemplate;

    public DataCheck(PeliculaRepository peliculaRepository, CriticaRepository criticaRepository,
                     GeneroRepository generoRepository, PlatformTransactionManager transactionManager) {
        this.peliculaRepository = peliculaRepository;
        this.criticaRepository = criticaRepository;
        this.generoRepository = generoRepository;
        this.transactionTemplate = new TransactionTemplate(transactionManager);
    }

    @PostConstruct
    public void comprobarDatos() {
        transactionTemplate.executeWithoutResult(status -> {
            peliculaRepository.findAll().forEach(pelicula ->
                    System.out.println("Película: " + pelicula.getTitulo()));

            Pelicula primera = peliculaRepository.findAll().stream().findFirst()
                    .flatMap(p -> peliculaRepository.findById(p.getId())).orElseThrow();
            System.out.println("Primera película: " + primera.getTitulo());
            primera.getCriticas().forEach(critica ->
                    System.out.println("Crítica: " + critica.getTexto()));
            primera.getGeneros().forEach(genero ->
                    System.out.println("Género: " + genero.getNombre()));

            System.out.println("Antes del borrado - críticas: " + criticaRepository.count()
                    + ", géneros: " + generoRepository.count());

            Pelicula peliculaConDosCriticas = peliculaRepository.findAll().stream()
                    .filter(p -> p.getCriticas().size() == 2)
                    .findFirst().orElseThrow();
            peliculaRepository.delete(peliculaConDosCriticas);
            peliculaRepository.flush();

            System.out.println("Después del borrado - críticas: " + criticaRepository.count()
                    + ", géneros: " + generoRepository.count());
        });
    }
}
