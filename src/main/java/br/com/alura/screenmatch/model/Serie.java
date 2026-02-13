package br.com.alura.screenmatch.model;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import br.com.alura.screenmatch.service.ConsultaMyMemory;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

@Entity
@Table(name = "series") //nomeia a tabela no banco de dados
public class Serie {

    @Id //identifica a chave padrão
    @GeneratedValue(strategy =GenerationType.IDENTITY) //Define a estrateǵia de geração de id
    private Long id;
    @Column(unique = true) //define que não pode haver duplicação de dados
    private  String titulo;
    private Double avaliacao;
    private int totalTemporadas;
    @Enumerated(EnumType.STRING) //escolhe o modo de numeração do enum categoria
    private Categoria genero;
    private  String atores;
    private String poster;
    private String sinopse;
    //faz mapeamento com base no nome do atributo de Episodios
    // Cascade ajuda com a persistenia, se salvar, deletar ou atualizar uma serie ela fara o mesmo para todos os episodios
    // Fetch força trazer os episoidios imediatamente
    @OneToMany(mappedBy = "serie", cascade=CascadeType.ALL, fetch= FetchType.EAGER) 
    private List<Episodio> episodios = new ArrayList<>();
    

    public Serie(){}
    public Serie(DadosSerie dadosSeries) {
        this.atores = dadosSeries.atores();
        this.avaliacao = Optional.ofNullable(Double.valueOf(dadosSeries.avaliacao())).orElse(0.0);        
        this.poster = dadosSeries.poster(); 
        this.titulo = dadosSeries.titulo();
        this.totalTemporadas = dadosSeries.totalTemporadas();
        try {
           this.genero = Categoria.fromString(dadosSeries.genero().split(",")[0].trim());

        } catch (Exception e) {
            System.out.println("Categoria não encontrada. " + e);
        }
        try{
            String traducao = ConsultaMyMemory.obterTraducao(dadosSeries.sinopse());

        } catch(Exception e) {
            System.out.println("Não foi possivel realizar a tradução. " + e);
        }

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

    public List<Episodio> getEpisodios() {
        return episodios;
    }

    public void setEpisodios(List<Episodio> episodios) {
        //adicionando chave estrangeira de episodios para chave
        episodios.forEach(e-> e.setSerie(this));
        this.episodios = episodios;
    }


    @Override
    public final String toString() {
        return   "Gênero: " + genero + "\n" +
                "Titulo Série: " + titulo +"\n" +
                "Avaliação: " + avaliacao + "\n" +
                "Total de Temporadas: " + totalTemporadas + "\n"+
                "Atores: " + atores + "\n" +
                "Poster: " + poster + "\n" +
                "Sinopese: " + sinopse + "\n" +
                "Episódios: " + episodios + "\n"
                ;
    }

        
}
