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

import br.com.alura.screenmatch.model.DadosEpisodios;
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

   

    public Principal(SerieRepository repositorio) {
        this.repositorio = repositorio;
    }

    public void exibeMenu() {
        var opcao = -1;
        var menu = """

                1 - Buscar série
                2 - Buscar episódios da série
                3 - Listar séries buscadas
                4 - Buscar série por titulo
                5- Buscar episódio por trecho
                6 - Listar top 5 episodios
                7 - Filtrar episódios por data
                8 - Ver estatísticas
                
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
                    buscarEpisodioPorTrecho();
                    break;
                case 6:
                     exibirTop5Episodios();
                     break;
                case 7:
                    filtrarEpisodiosPorData();
                    break;
                case 8:
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
        
        Optional<Serie> serieBuscada = repositorio.findByTituloContainingIgnoreCase(nomeSerie);

        if(serieBuscada.isPresent()){
            System.out.println("Dados da série: " + serieBuscada.get());

        } else{
            System.out.println("Série não encontrada");
        }
    }

    private void buscarEpisodioPorTrecho() {
        if (episodios.isEmpty()) {
            System.out.println("Primeiro busque os episódios de uma série (Opção 2)!");
            return;
        }
        System.out.println("Digite o nome do episódio desejado: ");
        var trechoTitulo = leitura.nextLine();
        Optional<Episodio> episodioBuscado = episodios.stream()
                .filter(e -> e.getTitulo().toUpperCase().contains(trechoTitulo.toUpperCase()))
                .findFirst();

        if (episodioBuscado.isPresent()) {
            System.out.println("Episódio encontrado");
            System.out.println("Temporada: " + episodioBuscado.get().getTemporada() + 
                               " - " + episodioBuscado.get().getTitulo());
        } else {
            System.out.println("Episódio não encontrado");
        }
    }

      private void exibirTop5Episodios() {
        if (temporadas.isEmpty()) {
            System.out.println("Primeiro busque os episódios de uma série (Opção 2)!");
            return;
        }
        System.out.println("\nTop 5 Episódios:");
        List<DadosEpisodios> dadosTodosEpisodios = temporadas.stream()
                .flatMap(t -> t.episodios().stream())
                .collect(Collectors.toList());

        dadosTodosEpisodios.stream()
                .filter(e -> !e.avaliacao().equalsIgnoreCase("N/A"))
                .sorted(Comparator.comparing(DadosEpisodios::avaliacao).reversed())
                .limit(5)
                .map(e -> e.titulo().toUpperCase())
                .forEach(System.out::println);
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