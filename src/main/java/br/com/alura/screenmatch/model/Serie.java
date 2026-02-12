package br.com.alura.screenmatch.model;
import java.util.Optional;

import br.com.alura.screenmatch.service.ConsultaMyMemory;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "series")
public class Serie {

    @Id
    @GeneratedValue(strategy =GenerationType.IDENTITY)
    private Long id;
    @Column(unique = true)
    private  String titulo;
    private Double avaliacao;
    private int totalTemporadas;
    @Enumerated(EnumType.STRING)
    private Categoria genero;
    private  String atores;
    private String poster;
    private String sinopse;
     
    public Serie(DadosSerie dadosSeries) {
        this.atores = dadosSeries.atores();
        this.avaliacao = Optional.ofNullable(Double.valueOf(dadosSeries.avaliacao())).orElse(0.0);        this.genero = Categoria.fromString(dadosSeries.genero().split(",")[0].trim());
        this.poster = dadosSeries.poster();
        this.sinopse = ConsultaMyMemory.obterTraducao(dadosSeries.sinopse()).trim();  
        this.titulo = dadosSeries.titulo();
        this.totalTemporadas = dadosSeries.totalTemporadas();

    }

     public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
    

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public Double getAvaliacao() {
        return avaliacao;
    }

    public void setAvaliacao(Double avaliacao) {
        this.avaliacao = avaliacao;
    }

    public int getTotalTemporadas() {
        return totalTemporadas;
    }


    public void setTotalTemporadas(int totalTemporadas) {
        this.totalTemporadas = totalTemporadas;
    }

    public Categoria getGenero() {
        return genero;
    }


    public void setGenero(Categoria genero) {
        this.genero = genero;
    }

    public String getAtores() {
        return atores;
    }

  public void setAtores(String atores) {
        this.atores = atores;
    }

  public String getPoster() {
        return poster;
    }

    public void setPoster(String poster) {
        this.poster = poster;
    }

    public String getSinopse() {
        return sinopse;
    }

    public void setSinopse(String sinopse) {
        this.sinopse = sinopse;
    }


    @Override
    public final String toString() {
        // TODO Auto-generated method stub
        return   "Gênero: " + genero + "\n" +
                "Titulo Série: " + titulo +"\n" +
                "Avaliação: " + avaliacao + "\n" +
                "Total de Temporadas: " + totalTemporadas + "\n"+
                "Atores: " + atores + "\n" +
                "Poster: " + poster + "\n" +
                "Sinopese: " + sinopse + "\n"
            
                ;
    }

    



        
}
