package br.com.alura.screenmatch.repository;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import br.com.alura.screenmatch.model.Categoria;
import br.com.alura.screenmatch.model.Episodio;
import br.com.alura.screenmatch.model.Serie;


public interface  SerieRepository extends JpaRepository<Serie, Long>{
    
    Optional<Serie> findByTituloContainingIgnoreCase(String nomeSerie);

    List<Serie> findByAtoresContainsIgnoreCaseAndAvaliacaoGreaterThanEqual(String nomeAtor, double avaliacao);
    List<Serie> findTop5ByOrderByAvaliacaoDesc();
    List<Serie> findByGenero(Categoria categoria);
    List<Serie> findByTotalTemporadasLessThanEqualAndAvaliacaoGreaterThanEqual(Integer totalTemporada, double avaliacao);

    @Query(value ="select s from Serie s  WHERE s.totalTemporadas <= :totalTemporada and s.avaliacao >= :avaliacao ")
    List<Serie> seriesPorTemporadaEAvaliacao(Integer totalTemporada, double avaliacao);


    @Query(value ="select e from Serie s JOIN s.episodios e WHERE e.titulo ILIKE %:trechoTitulo% ") //ILIKE é semelhante ao ignorecase
    List<Episodio> episodiosPorTrecho(String trechoTitulo);
    
}
