package br.com.alura.screenmatch.principal;

import br.com.alura.screenmatch.modelos.*;
import br.com.alura.screenmatch.repository.SerieRepository;
import br.com.alura.screenmatch.service.ConsumoAPI;
import br.com.alura.screenmatch.service.ConverteDados;


import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class PrincipalTw {
    private Scanner leitura = new Scanner(System.in);

    private final String API_KEY = "&apikey=1be31f23";

    private final String ENDEREÇO = "https://www.omdbapi.com/?t=";
    private ConsumoAPI consumo = new ConsumoAPI();
    private List<DadosSerie> dadosSeries = new ArrayList<>();

    private ConverteDados conversor = new ConverteDados();

    private SerieRepository repositorio;

    private List<Serie> series = new ArrayList<>();

    public PrincipalTw(SerieRepository repositorio) {
        this.repositorio = repositorio;
    }

    public void exibeMenu() {
        var opcao = -1;
        while (opcao != 0) {
            var menu = """
                    1 - Buscar séries
                    2 - Buscar episódios
                    3 - Listar séries buscadas
                    
                    0 - Sair                                 
                    """;

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
                    listarSeriesBuscadas();
                    break;
                case 0:
                    System.out.println("Saindo...");
                    break;
                default:
                    System.out.println("Opção inválida");
            }
        }


    }

    private void buscarEpisodioPorSerie() {
        listarSeriesBuscadas();
        System.out.println("Escolha uma série pelo nome: ");
        var nomeSerie = leitura.nextLine();

        Optional<Serie> serie = series.stream()
                .filter(s -> s.getTitulo().toLowerCase().contains(nomeSerie.toLowerCase()))
                .findFirst();

        if (serie.isPresent()) {
            var serieEncontrada = serie.get();
            List<DadosTemporadas> temporadas = new ArrayList<>();

            for (int i = 0; i < serieEncontrada.getTotalTemporadas(); i++) {
                var json = consumo.obterDados(ENDEREÇO + serieEncontrada.getTitulo().replace(" ", "+") + API_KEY);
                DadosTemporadas dadosTemporadas = conversor.obterDados(json, DadosTemporadas.class);

                // Verificar se dadosTemporadas é nulo
                if (dadosTemporadas == null || dadosTemporadas.numero() == null) {
                    System.out.println("Erro: Dados da temporada ou número da temporada estão nulos!");
                    continue; // Pule esta temporada
                }

                temporadas.add(dadosTemporadas);
            }

            temporadas.forEach(System.out::println);

            List<Episodio> episodios = temporadas.stream()
                    .flatMap(d -> {
                        // Verificar se o número da temporada é nulo antes de continuar
                        if (d.numero() == null) {
                            System.out.println("Erro: Número da temporada não pode ser nulo.");
                            return Stream.empty(); // Ignorar temporadas inválidas
                        }
                        int numeroTemporada = d.numero();
                        d.episodios().forEach(e ->
                                System.out.println("Número da temporada: " + numeroTemporada + ", Dados do episódio: " + e));
                        return d.episodios().stream().map(e -> new Episodio(numeroTemporada, e));
                    })
                    .collect(Collectors.toList());

            serieEncontrada.setEpisodios(episodios);
            repositorio.save(serieEncontrada);
        } else {
            System.out.println("Série não encontrada!");
        }
    }

    private void buscarSerieWeb() {
        try {
            DadosSerie dados = getDadosSerie();
//        dadosSeries.add(dados);
            Serie serie = new Serie(dados);
            Optional<Serie> serieExistente = repositorio.findById(serie.getId());

            if (serieExistente.isPresent()) {
                System.out.println("Série já coletada para o banco de dados!");
                return;
            }
            repositorio.save(serie);
            System.out.println(dados);

        } catch (Exception e) {
            System.out.println("Série já coletada para o banco de dados!");
        }
    }

    private DadosSerie getDadosSerie() {
        System.out.println("Digite o nome da série para busca: ");
        var nomeSerie = leitura.nextLine();
        var json = consumo.obterDados(ENDEREÇO + nomeSerie.replace(" ", "+") + API_KEY);
        DadosSerie dados = conversor.obterDados(json, DadosSerie.class);
        return dados;
    }

    private void listarSeriesBuscadas() {
        //O "findALL()" pega todas as séries e leva para o banco de dados, substituindo o metodo antigo de pegar as séries e listá-las
        series = repositorio.findAll();
        //aqui nos atribuimos os dados da série para a lista
//        series = dadosSeries.stream()
//                        .map(d->new Serie(d))
//                                .collect(Collectors.toList());
        //aqui ordenamos as séries por gênero
        series.stream().sorted(Comparator.comparing(Serie::getGenero))
                .forEach(System.out::println);

    }
}