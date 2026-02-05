package br.com.alura.screenmatch.principal;

import br.com.alura.screenmatch.model.DadosEpisodios;
import br.com.alura.screenmatch.model.DadosSerie;
import br.com.alura.screenmatch.model.DadosTemporada;
import br.com.alura.screenmatch.model.Episodio;
import br.com.alura.screenmatch.service.ConsumoApi;
import br.com.alura.screenmatch.service.ConverteDados;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

public class Principal {
    private Scanner leitura = new Scanner(System.in);
    private final String ENDERECO = "https://www.omdbapi.com/?t=";
    private final String API_KEY = "&apikey=4e1b1cd7";
    private ConsumoApi consumoApi = new ConsumoApi();
    private   ConverteDados conversor = new ConverteDados();

    public void exibeMenu(){

        System.out.println("Digite o nome da serie para busca ");
        var nomeSerie = leitura.nextLine();
        var json = consumoApi.obterDados(
                ENDERECO + nomeSerie.replace(" ", "+") + API_KEY);
        DadosSerie dados = conversor.obterDados(json, DadosSerie.class);
        System.out.println(dados);

        List<DadosTemporada> temporadas = new ArrayList<>();
        for (int     i =1 ;  i <= dados.totalTemporadas(); i++){
            json = consumoApi.obterDados(ENDERECO + nomeSerie.replace(" ","+") +
                    "&season="+ i+ API_KEY
            );
            DadosTemporada dadosTemporada = conversor.obterDados(json, DadosTemporada.class);
            temporadas.add(dadosTemporada);
        }
        temporadas.forEach(System.out::println);

        /*for(int i = 0; i < dados.totalTemporadas(); i++){
            List<DadosEpisodios> episodiosTemporadas = temporadas.get(i).episodios()
                    ;
            for( int j = 0; j < episodiosTemporadas.size(); j++){
                System.out.println(episodiosTemporadas.get(j).titulo()

                );
            }
        }*/

        temporadas.forEach(t -> t.episodios().forEach( e -> System.out.println(e.titulo())));

        List<DadosEpisodios> dadosTodosEpisodios = temporadas.stream()
                .flatMap(t -> t.episodios().stream())
                .collect(Collectors.toList());
        // da para usar    .toList(); mas da lista imutavél, se tentar adicionar algo nao da para adicionar

        System.out.println("\nTop 5 Episódios");
        dadosTodosEpisodios.stream()
                .filter(e -> !e.avaliacao().equalsIgnoreCase("N/A"))
                .peek(e-> System.out.println(" Primeiro filtro N/a" + e))
                .sorted(Comparator.comparing(DadosEpisodios::avaliacao).reversed())
                .peek(e-> System.out.println(" Ordenação " + e))
                .limit(5)
                .peek(e-> System.out.println(" Limite " + e))
                .map(e-> e.titulo().toUpperCase())
                .peek(e-> System.out.println(" Mapeamento " + e))
                .forEach(System.out::println);

        List<Episodio> episodio = temporadas.stream()
                .flatMap(t -> t.episodios().stream()
                        .map(d -> new Episodio(t.numeroTemporada(),d)))
                .collect(Collectors.toList());

        episodio.forEach(System.out::println);
        System.out.println("Digite o nome do episódio desejado: ");
        var trechoTitulo = leitura.nextLine();
        Optional<Episodio> episodioBuscado = episodio.stream()
                .filter(e -> e.getTitulo().toUpperCase().contains(trechoTitulo.toUpperCase()))
                .findFirst();
        if(episodioBuscado.isPresent()){
            System.out.println("Episódio encontrado");
            System.out.println("Temporada: " + episodioBuscado.get().getTemporada());
        } else{
            System.out.println("Episódio não encontrado");
        }

       System.out.println("A partir de que ano você deseja ver os episódios");
        var ano = leitura.nextInt();
        leitura.nextLine();

        LocalDate dataBusca = LocalDate.of(ano, 1,1);


        DateTimeFormatter formatador = DateTimeFormatter.ofPattern("dd/MM/yyyy");

        episodio.stream().filter(e ->e.getDataLancamento()!=  null && e.getDataLancamento()
                .isAfter(dataBusca)).forEach(e -> System.out.println("Temporada: "+ e.getTemporada()
                + " Episódio: " + e.getNumeroEpisodio() + " Data Lançamento: " + e.getDataLancamento().format(formatador)));



        Map<Integer, Double> avaliacaoPorTemporada = episodio.stream()
                .filter(e-> e.getAvaliacao() > 0)
                .collect(Collectors.groupingBy(Episodio::getTemporada,
                        Collectors.averagingDouble(Episodio::getAvaliacao)));

        System.out.println(avaliacaoPorTemporada);

        DoubleSummaryStatistics est = episodio.stream()
                .filter(e -> e.getAvaliacao() > 0)
                .collect(Collectors.summarizingDouble(Episodio::getAvaliacao));

        System.out.println("A media de avaliação das temporadas é de: " + est.getAverage());
        System.out.println("Melhor episodio " + est.getMax());
        System.out.println("Pior episódio: " + est.getMin());
        System.out.println("Quantidade de episódios avaliados: " + est.getCount());
        
    }

}

