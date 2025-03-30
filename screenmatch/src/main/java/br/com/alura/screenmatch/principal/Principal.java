package br.com.alura.screenmatch.principal;

import br.com.alura.screenmatch.modelos.DadosEpisodio;
import br.com.alura.screenmatch.modelos.DadosSerie;
import br.com.alura.screenmatch.modelos.DadosTemporadas;
import br.com.alura.screenmatch.modelos.Episodio;
import br.com.alura.screenmatch.service.ConsumoAPI;
import br.com.alura.screenmatch.service.ConverteDados;
import java.util.*;
import java.util.stream.Collectors;

public class Principal {
    private Scanner leitra = new Scanner(System.in);

    private final String API_KEY = "&apikey=1be31f23";

    private final String ENDEREÇO = "https://www.omdbapi.com/?t=";
    private ConsumoAPI consumo = new ConsumoAPI();

    private ConverteDados conversor = new ConverteDados();

    public void exibeMenu() {
            System.out.println("Digite o nome de uma séria: ");
            var leituraSerie = leitra.nextLine();
            var json = consumo.obterDados(ENDEREÇO + leituraSerie.replace(" ", "+") + API_KEY);
            DadosSerie dados = conversor.obterDados(json, DadosSerie.class);
            System.out.println(dados);

            List<DadosTemporadas> temporadas = new ArrayList<>();

            for (int i = 1; i <= dados.totalTemporadas(); i++) {
                json = consumo.obterDados(ENDEREÇO + leituraSerie.replace(" ", "+") + "&season=" + i + API_KEY);
                DadosTemporadas dadosTemp = conversor.obterDados(json, DadosTemporadas.class);
                temporadas.add(dadosTemp);
            }


            //aqui se faz a mesma coisa no "foreach"
//        for (int i = 0; i < dados.totalTemporadas();i++){
//            List<DadosEpisodio> episodiosTemporada = temporadas.get(i).episodios();
//            for (int j=0; j<episodiosTemporada.size();j++){
//                System.out.println(episodiosTemporada.get(j).titulo());
//            }
//        }
            temporadas.forEach(t -> t.episodios().forEach(e -> System.out.println(e.titulo())));
            temporadas.forEach(System.out::println);

//        List<String> listaDeNomes = Arrays.asList("Rodrigo","Nico","Pedro","João");
//        listaDeNomes.stream()
//                .sorted()
//                .limit(3)
//                .filter(n->n.startsWith("N"))
//                .map(n->n.toUpperCase())
//                .forEach(System.out::println);

            List<DadosEpisodio> dadosEpisodios = temporadas.stream()
                    .flatMap(t -> t.episodios().stream())
                    .collect(Collectors.toList());

            dadosEpisodios.forEach(System.out::println);


//        System.out.println("\nTop 10 melhores episódios: ");
//        dadosEpisodios.stream()
//                .filter(e -> !e.avaliacao().equalsIgnoreCase("N/A"))
//                .peek(e-> System.out.println("Primeiro Filtro N/A "+e))
//                .sorted(Comparator.comparing(DadosEpisodio::avaliacao).reversed())
//                .peek(e-> System.out.println("Ordenação: "+e))
//                .limit(10)
//                .peek(e-> System.out.println("Limite: "+e))
//                .map(e->e.titulo().toUpperCase())
//                .peek(e-> System.out.println("Mapeamento: "+e))
//                .forEach(System.out::println);
//
            List<Episodio> episodios = temporadas.stream()
                    .flatMap(t -> t.episodios().stream()
                            .map(d -> new Episodio(t.numero(), d))).collect(Collectors.toList());
            episodios.forEach(System.out::println);


            System.out.println("Digite um trecho do titulo do ep: ");
            var trechoTitulo = leitra.nextLine();

            Optional<Episodio> episodioBuscado = episodios.stream().filter(e -> e.getTitulo().toUpperCase().contains(trechoTitulo.toUpperCase()))
                    .findFirst();
            if (episodioBuscado.isPresent()) {
                //o "get()" será buscado apenas o episódio, e o getTemporada() busca a temporada
                System.out.println("Temporada do episódio procurado: " + episodioBuscado.get().getTemporada());
            } else {
                System.out.println("Episódio não encontrado!");
            }

            Map<Integer, Double> avaliacoesPorTemporada = episodios.stream().filter(e -> e.getAvaliacao() > 0.0).collect(Collectors.groupingBy(Episodio::getTemporada,
                    //aqui se compara a temporada com as avaliações agrupando os dois com o groupingby
                    Collectors.averagingDouble(Episodio::getAvaliacao)));
            System.out.println(avaliacoesPorTemporada);

            DoubleSummaryStatistics estat = episodios.stream().filter(e -> e.getAvaliacao() > 0.0).collect(Collectors.summarizingDouble(Episodio::getAvaliacao));
            System.out.println("Média: " + estat.getAverage());
            System.out.println("Melhor episódio: " + estat.getMax());
            System.out.println("Pior episódio: " + estat.getMin());
            System.out.println("Quantidade de episódios: " + estat.getCount())
            ;

//
//        System.out.println("A partir de qual ano você quer os episódios? ");
//        var ano = leitra.nextInt();
//        //Usado o segundo Scanner para evitar erro de leitura do ano
//        leitra.nextLine();
//
//        DateTimeFormatter formatador = DateTimeFormatter.ofPattern("dd/MM/yyyy");
//
//        LocalDate busca = LocalDate.of(ano, 1, 1);
//
//        episodios.stream()
//                .filter(e -> e.getDataLancamento() != null && e.getDataLancamento().isAfter(busca))
//                .forEach(e -> System.out.println("Temporada: " + e.getTemporada() +
//                        " | Episódio: " + e.getTitulo() +
//                        " | Número do Ep: " + e.getNumeroEP() +
//                        " | Data de lançamento: " + e.getDataLancamento().format(formatador)));
//    }
        }
    }
