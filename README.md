## User Info
Este projeto foi desenvolvido como parte do programa Oracle Next Education em parceria com a Alura.

### Descrição
Este projeto é uma aplicação **console** (sem interface web) desenvolvida em Java com Spring Boot, projetada para rodar via linha de comando.  
Ele consome a **OMDb API** (Open Movie Database) para buscar informações sobre séries de TV em tempo real, permitindo que o usuário pesquise séries, visualize temporadas, episódios, avaliações e estatísticas (médias, top 5 episódios, filtro por ano, etc.).  

O objetivo principal foi praticar:  
- Parsing e conversão de JSON  
- Uso de Streams e Lambdas do Java moderno  
- Injeção de dependências com Spring Boot em aplicações console  
- Manipulação de dados e exibição formatada no terminal  

🚀 **Funcionalidades**

Busca de séries por nome (ex: "Gilmore Girls", "Breaking Bad")
Exibição de dados gerais: título, ano, avaliação IMDb, total de temporadas
Listagem completa de temporadas e episódios
Top 5 episódios melhor avaliados
Busca de episódios por trecho do título
Filtro de episódios a partir de um determinado ano
Média de avaliações por temporada
Estatísticas gerais: média total, melhor/pior episódio, quantidade avaliada


**Tecnologias Utilizadas**

Java 17+
Spring Boot (aplicação console via CommandLineRunner)
Jackson Databind (para parsing de JSON)
java.net.http (cliente HTTP nativo do Java 11+)
Maven (gerenciamento de dependências)
OMDb API (fonte de dados de séries e episódios)


**Como Funciona**

A aplicação inicia e exibe um menu interativo no console.
O usuário digita o nome da série desejada.
O programa faz requisições à OMDb API para obter:
→ Dados gerais da série
→ Informações detalhadas de todas as temporadas
Os dados são convertidos em objetos Java (records e classes)
São processadas estatísticas e exibidas opções adicionais (top 5, filtros, etc.)
O usuário interage até escolher sair.


**Pré-requisitos**

JDK 17 ou superior instalado
Maven instalado (ou use o wrapper mvnw presente no projeto)
Chave de API da OMDb- Para obter cadastre-se gratuitamente em https://www.omdbapi.com/


**Como Executar**

Clone o repositório:

git clone https://github.com/LuandaLl/screenmatch-sem-web.git

Navegue até a pasta do projeto:

cd screenmatch-sem-web

Execute com o Maven Wrapper:

./mvnw spring-boot:run
(no Windows use: mvnw.cmd spring-boot:run)
Ou importe o projeto na sua IDE favorita (IntelliJ, Eclipse, VS Code) e execute a classe:
br.com.alura.screenmatch.ScreenmatchApplication


**Exemplo de Uso**

Digite o nome para busca:

Breaking Bad

Informações da série:

Título: Breaking Bad

Ano: 2008–2013

Avaliação: 9.5

Total Temporadas: 5

Temporada 1:

Episódio 1 - Pilot ... Nota: 9.0

Episódio 2 - Cat's in the Bag... ... Nota: 8.7

Qualquer dúvida ou sugestão, é só abrir uma issue! 🚀
