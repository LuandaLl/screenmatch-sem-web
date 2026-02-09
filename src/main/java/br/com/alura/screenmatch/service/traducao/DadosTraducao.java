package br.com.alura.screenmatch.service.traducao;
import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.alura.screenmatch.service.traducao.DadosRespostas.java;
@JsonIgnoreProperties(ignoreUnknown = true)
public record DadosTraducao(@JsonAlias(value = "responseData") DadosRespostas dadosResposta) {
}
