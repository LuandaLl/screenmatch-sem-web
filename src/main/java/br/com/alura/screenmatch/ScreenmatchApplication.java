package br.com.alura.screenmatch;

import br.com.alura.screenmatch.principal.Principal;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;


@SpringBootApplication
public class ScreenmatchApplication implements CommandLineRunner {

	public static void main(String[] args) {
		SpringApplication.run(ScreenmatchApplication.class, args);
	}


	@Override
	public void run(String... args) throws Exception {
		Principal principal = new Principal();
        principal.exibeMenu();

		/*List<DadosTemporada> temporadas = new ArrayList<>();
		for (int     i =1 ;  i <= dados.totalTemporadas()

		; i++){
			json = consumoAPi.obterDados("https://www.omdbapi.com/" +
					"?t=gilmore+girls&Season="+ i+"&apikey=4e1b1cd7"
			);
			DadosTemporada dadosTemporada = conversor.obterDados(json, DadosTemporada.class);
			temporadas.add(dadosTemporada);
		}
		temporadas.forEach(System.out::println);*/


	}
}
