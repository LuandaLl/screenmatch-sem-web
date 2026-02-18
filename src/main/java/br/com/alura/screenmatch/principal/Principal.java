package br.com.alura.screenmatch.principal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.DoubleSummaryStatistics;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Scanner;
import java.util.stream.Collectors;

import br.com.alura.screenmatch.model.Categoria;
import br.com.alura.screenmatch.model.DadosSerie;
import br.com.alura.screenmatch.model.DadosTemporada;
import br.com.alura.screenmatch.model.Episodio;
import br.com.alura.screenmatch.model.Serie;
import br.com.alura.screenmatch.repository.SerieRepository;
import br.com.alura.screenmatch.service.ConsumoApi;
import br.com.alura.screenmatch.service.ConverteDados;
import io.github.cdimascio.dotenv.Dotenv;





/////////////////////////////////////////////////////////////////////////////////////////////
public class Principal {
    private Dotenv dotenv = Dotenv.load();
    private final String API_KEY = dotenv.get("API_KEY");
    private Scanner leitura = new Scanner(System.in);
    private final String ENDERECO = "https://www.omdbapi.com/?t=";
    private ConsumoApi consumoApi = new ConsumoApi();
    private ConverteDados conversor = new ConverteDados();
    private List<Episodio> episodios = new ArrayList<>();
    private List<Serie> series = new ArrayList<>();
    private List<DadosTemporada> temporadas = new ArrayList<>();
    private SerieRepository repositorio;
    private Optional<Serie> serieBusca;

   

    public Principal(SerieRepository repositorio) {
        this.repositorio = repositorio;
    }

    public void exibeMenu() {
        var opcao = -1;
        var menu = """

                Busca Series:

                1 - Buscar série
                2 - Listar episódios da série
                3 - Listar séries buscadas
                4 - Buscar série por titulo
                5- Buscar Serie por ator
                6- Busca por categoria
                7- Top 5 Serie
                8- Busca Serie Por total de temporada

                Busca :Episodios Da serie
                9- Busca EPisódio Por trecho
                10 - Listar top 5 episodios
                11 - Filtrar episódios por data
                12 - Ver estatísticas
                
                0 - Sair
                """;

        while (opcao != 0) {
            System.out.println(menu);
            opcao = leitura.nextInt();
            leitura.nextLine();

            switch (opcao) {
               case 1:
                    buscarSerieWeb();
                    break;
                case 2:
                    buscarEpisodioPorSerie();
                    break;
                case 3:
                    listarSerieBuscada();
                    break;
                case 4:
                    buscarSeriePorTitulo();
                    break;
                case 5:
                    buscarSeriePorAtor();
                    break;
                case 6:
                    buscarSeriePorCategoria();
                    break;
                case 7:
                    buscarTop5Series();
                    break;
                case 8:
                    buscaSeriePorTotalTemporada();
                     break;
                case 9:
                     buscarEpisodioPorTrecho();
                    break;
                case 10:
                     exibirTop5Episodios();
                    break;
                case 11:
                    filtrarEpisodiosPorData();
                    break;
                case 12:
                    exibirEstatisticas();
                    break;

         
                case 0:
                    System.out.println("Saindo...");
                    break;
                default:
                    System.out.println("Opção inválida");
            }
        }
    }

   

    private void buscarSerieWeb() {
        DadosSerie dados = getDadosSerie();
        Serie serie = new Serie(dados);
        //listaSerie.add(dados);
        repositorio.save(serie);
        System.out.println(dados);
    }

    private DadosSerie getDadosSerie() {
        System.out.println("Digite o nome da série para busca:");
        var nomeSerie = leitura.nextLine();
        var json = consumoApi.obterDados(ENDERECO + nomeSerie.replace(" ", "+") + API_KEY);
        return conversor.obterDados(json, DadosSerie.class);
    }

    private void buscarEpisodioPorSerie() {
        listarSerieBuscada();
        System.out.println("Escolha uma seŕie pelo nome:");
         var nomeSerie = leitura.nextLine();
        
        Optional<Serie> serie = repositorio.findByTituloContainingIgnoreCase(nomeSerie);
        
        if(serie.isPresent()){ 
            var serieEncontrada = serie.get();
            temporadas.clear(); // Limpa busca anterior

            for (int i = 1; i <= serieEncontrada.getTotalTemporadas(); i++) {
                var json = consumoApi.obterDados(ENDERECO + serieEncontrada.getTitulo().replace(" ", "+") + "&season=" + i + API_KEY);
                DadosTemporada dadosTemporada = conversor.obterDados(json, DadosTemporada.class);
                temporadas.add(dadosTemporada);
            }
            
            System.out.println("\nTemporadas da série " + serieEncontrada.getTitulo() + ":");
            
            temporadas.stream()
                .forEach(t -> System.out.println("Temporada " + t.numeroTemporada() + " - " + t.episodios().size() + " episódios"));
           
            episodios = temporadas.stream()
                .flatMap(t -> t.episodios().stream()
                    .map(d -> new Episodio(t.numeroTemporada(), d)))
                .sorted(Comparator.comparing(Episodio::getTemporada)
                    .thenComparing(Episodio::getNumeroEpisodio))
                .collect(Collectors.toList());
            
            serieEncontrada.setEpisodios(episodios);
            repositorio.save(serieEncontrada);
        } else{
            System.out.println("Série não");
        }
    }

    private void listarSerieBuscada() {
        series = repositorio.findAll();        
        series.stream()
                    .sorted(Comparator.comparing(Serie::getGenero))
                    .forEach(System.out::println);
    }

  
    private void buscarSeriePorTitulo(){
    listarSerieBuscada();
        System.out.println("Escolha uma seŕie pelo nome:");
         var nomeSerie = leitura.nextLine();
        
        serieBusca = repositorio.findByTituloContainingIgnoreCase(nomeSerie);

        if(serieBusca.isPresent()){
            System.out.println("Dados da série: " + serieBusca.get());

        } else{
            System.out.println("Série não encontrada");
        }
    }

    private void  buscarSeriePorAtor(){
        System.out.println("Digite nome do ator para busca");
        var nomeAtor = leitura.nextLine();
        System.out.println("Deseja ver series a partir de que nota?");
        var avaliacao = leitura.nextDouble();
        List<Serie> seriesEncontradas = repositorio.findByAtoresContainsIgnoreCaseAndAvaliacaoGreaterThanEqual(nomeAtor, avaliacao);
        System.out.println("Seŕies em que " +nomeAtor + " trabalhou");
        
        seriesEncontradas.forEach(s->
            System.out.println(s.getTitulo() + " avaliação: " + s.getAvaliacao())

        );

    }

     private void buscarSeriePorCategoria(){
        System.out.println("Digite a categoria desejada");
        var nomeGenero = leitura.nextLine();
        Categoria categoria = Categoria.fromPortugues(nomeGenero);
        List<Serie> seriePorCategoria = repositorio.findByGenero(categoria);
        System.out.println("Séries da categoria: " +nomeGenero);
        seriePorCategoria.forEach(System.out::println);
     }


    private void  buscarTop5Series(){
        List <Serie> topSeries = repositorio.findTop5ByOrderByAvaliacaoDesc();
        topSeries.forEach(s->
            System.out.println(s.getTitulo() + " avaliação: " + s.getAvaliacao())
        );
    }

    private void buscaSeriePorTotalTemporada(){
        System.out.println("Digite o total de temporadas desejado: ");
        int totalTemporada = leitura.nextInt();
        System.out.println("Digite a avaliação desejada: ");
        var avaliacao = leitura.nextDouble();
        List<Serie> seriePorTemporada = repositorio.seriesPorTemporadaEAvaliacao(totalTemporada,avaliacao);


        System.out.println("Séries com " + totalTemporada + " temporadas: ");
        seriePorTemporada.forEach(s-> System.out.println(s.getTitulo() + " temporadas: " + s.getTotalTemporadas() + " avaliaão: " + s.getAvaliacao()));
   
    }



    private void buscarEpisodioPorTrecho() {
        System.out.println("Digite o nome do episódio desejado: ");
        var trechoTitulo = leitura.nextLine();
        List<Episodio> episodiosEncontrados = repositorio.episodiosPorTrecho(trechoTitulo);
            if (episodiosEncontrados.isEmpty()) {
        System.out.println("Nenhum episódio encontrado com esse nome.");
    } else {
        episodiosEncontrados.forEach(e -> 
            System.out.printf("Série: %s Temporada: %s Episódio %s - %s%n",
                e.getSerie().getTitulo(), e.getTemporada(), 
                e.getNumeroEpisodio(), e.getTitulo()));
    }
}
    

      private void exibirTop5Episodios() {
        buscarSeriePorTitulo();
        if(serieBusca.isPresent()){
            Serie serie = serieBusca.get();
            List<Episodio> topEpisodio = repositorio.topEpisodioPorSerie(serie);
            topEpisodio.forEach(e->System.out.printf("Série: %s Temporada: %s Episódio %s - Avaliação: %.2f %s%n",
                e.getSerie().getTitulo(), e.getTemporada(), 
                e.getNumeroEpisodio(), e.getAvaliacao(), e.getTitulo()));
        }
    }

    private void filtrarEpisodiosPorData() {
        if (episodios.isEmpty()) {
            System.out.println("Primeiro busque os episódios de uma série (Opção 2)!");
            return;
        }
        System.out.println("A partir de que ano você deseja ver os episódios?");
        var ano = leitura.nextInt();
        leitura.nextLine();

        LocalDate dataBusca = LocalDate.of(ano, 1, 1);
        DateTimeFormatter formatador = DateTimeFormatter.ofPattern("dd/MM/yyyy");

        episodios.stream()
                .filter(e -> e.getDataLancamento() != null && e.getDataLancamento().isAfter(dataBusca))
                .forEach(e -> System.out.println(
                        "Temporada: " + e.getTemporada() +
                                " Episódio: " + e.getNumeroEpisodio() +
                                " Data Lançamento: " + e.getDataLancamento().format(formatador)
                ));
    }

    private void exibirEstatisticas() {
        if (episodios.isEmpty()) {
            System.out.println("Primeiro busque os episódios de uma série (Opção 2)!");
            return;
        }
        Map<Integer, Double> avaliacaoPorTemporada = episodios.stream()
                .filter(e -> e.getAvaliacao() > 0)
                .collect(Collectors.groupingBy(Episodio::getTemporada,
                        Collectors.averagingDouble(Episodio::getAvaliacao)));

        System.out.println("Avaliações por temporada: " + avaliacaoPorTemporada);

        DoubleSummaryStatistics est = episodios.stream()
                .filter(e -> e.getAvaliacao() > 0)
                .collect(Collectors.summarizingDouble(Episodio::getAvaliacao));

        System.out.println("Média: " + est.getAverage());
        System.out.println("Melhor: " + est.getMax());
        System.out.println("Pior: " + est.getMin());
        System.out.println("Quantidade: " + est.getCount());
    }
}