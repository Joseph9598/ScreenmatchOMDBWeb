package br.com.alura.screenmatch.modelos;

import br.com.alura.screenmatch.service.traducao.ConsultaMyMemory;
import jakarta.persistence.*;


import java.util.ArrayList;
import java.util.List;
import java.util.OptionalDouble;
@Entity
@Table(name = "series")
public class Serie {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String titulo;

    private Integer totalTemporadas;

    private double avaliacao;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    @Enumerated(EnumType.STRING)
    private Categoria genero;

    private String atores;

    private String banner;

    private String sinopse;


    @OneToMany(mappedBy = "serie", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<Episodio> episodios = new ArrayList<>();

    public void setEpisodios(List<Episodio> episodios) {
        episodios.forEach(e->e.setSerie(this));
        this.episodios = episodios;
    }
    public Serie(){}



    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public Integer getTotalTemporadas() {
        return totalTemporadas;
    }

    public void setTotalTemporadas(Integer totalTemporadas) {
        this.totalTemporadas = totalTemporadas;
    }

    public double getAvaliacao() {
        return avaliacao;
    }

    public void setAvaliacao(double avaliacao) {
        this.avaliacao = avaliacao;
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

    public String getBanner() {
        return banner;
    }

    public void setBanner(String banner) {
        this.banner = banner;
    }

    public String getSinopse() {
        return sinopse;
    }

    public void setSinopse(String sinopse) {
        this.sinopse = sinopse;
    }


    @Override
    public String toString() {
        return "Serie{" +
                "genero=" + genero +
                ", titulo='" + titulo + '\'' +
                ", totalTemporadas=" + totalTemporadas +
                ", avaliacao=" + avaliacao +
                ", atores='" + atores + '\'' +
                ", banner='" + banner + '\'' +
                ", sinopse='" + sinopse + '\'' +
                ", episodios='" + sinopse + '\''+
                '}';
    }
    //aqui foi criado um construtor padrão, pois se nao fosse criado a IDE resultaria em erro


    public Serie(DadosSerie dadosSerie){
        this.titulo= dadosSerie.titulo();
        this.totalTemporadas= dadosSerie.totalTemporadas();
        //Se nao encontrar nenhum valor, considere "avaliação" como "0"
        this.avaliacao= OptionalDouble.of(Double.valueOf(dadosSerie.avaliacao())).orElse(0);
        //atribui os generos e traduz para o usuario. O "splir" pega apenas o primeiro genero atribuido a série, e o "trim" apaga os espaços em branco
        this.genero=Categoria.fromString(dadosSerie.genero().split(",")[0].trim());
        this.atores= dadosSerie.atores();
        this.banner= dadosSerie.banner();
        this.sinopse = ConsultaMyMemory.obterTraducao(dadosSerie.sinopse()).trim();


    }
}
