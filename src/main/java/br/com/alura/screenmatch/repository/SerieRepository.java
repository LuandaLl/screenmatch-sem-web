package br.com.alura.screenmatch.repository;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.alura.screenmatch.model.Serie;
import br.com.alura.screenmatch.model.Categoria;


public interface  SerieRepository extends JpaRepository<Serie, Long>{
    
    Optional<Serie> findByTituloContainingIgnoreCase(String nomeSerie);

    List<Serie> findByAtoresContainsIgnoreCaseAndAvaliacaoGreaterThanEqual(String nomeAtor, double avaliacao);
    List<Serie> findTop5ByOrderByAvaliacaoDesc();
    List<Serie> findByGenero(Categoria categoria);
    
}
