package br.com.alura.screenmatch.model;

import com.fasterxml.jackson.annotation.JsonAlias;

public class Serie {
    private  String titulo;
    private Double avaliacao;
    private int totalTemporadas;
    private Categoria genero;
    private  String atores;
    private String poster;
    private String sinopse; 

    public Serie(DadosSerie dadosSeries) {
        this.atores = atores;
        this.avaliacao = OptionalDouble.valueOf(Double.valueOf(avaliacao)).orElse(0.0);
        this.genero = Categoria.fromString(dadosSeries.genero().split(",",[0].trim()));
        this.poster = poster;
        this.sinopse = sinopse;
        this.titulo = titulo;
        this.totalTemporadas = totalTemporadas;

    }




    @Override
    public final String toString() {
        // TODO Auto-generated method stub
        return "Titulo Série: " + titulo +"\n" +
                "Avaliação: " + avaliacao + "\n" +
                "Gênero: " + genero + "\n" +
                "Total de Temporadas: " + totalTemporadas + "\n"+
                "Atores: " + atores + "\n" +
                "Poster: " + poster + "\n" +
                "Sinopese: " + sinopse + "\n"
            
                ;
    }
    



        
}
