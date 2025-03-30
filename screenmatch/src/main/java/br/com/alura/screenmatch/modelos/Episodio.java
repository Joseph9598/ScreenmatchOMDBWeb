package br.com.alura.screenmatch.modelos;

import jakarta.persistence.*;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.zip.DataFormatException;

@Entity
@Table(name = "episodios")

public class  Episodio {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Serie getSerie() {
        return serie;
    }

    public void setSerie(Serie serie) {
        this.serie = serie;
    }

    private Integer temporada;
    private String titulo;
    private Integer numeroEP;
    private Double avaliacao;
    private LocalDate dataLancamento;


    @JoinColumn(name = "serie_id")
    @ManyToOne
    private Serie serie;

    public Episodio(Integer numeroTemporada, DadosEpisodio dadosEpisodio) {
        this.temporada=numeroTemporada;
        this.titulo=dadosEpisodio.titulo();
        try {
            this.avaliacao = Double.valueOf(Double.valueOf(dadosEpisodio.avaliacao()));
        }catch (NumberFormatException e){
            this.avaliacao=0.0;
        }
        try {
            this.dataLancamento = LocalDate.parse(dadosEpisodio.dataLancamento());
        }catch (DateTimeParseException ex){
            this.dataLancamento=null;
        }
        this.numeroEP=dadosEpisodio.numeroEP();
    }

    public Integer getTemporada() {
        return temporada;
    }

    public void setTemporada(Integer temporada) {
        this.temporada = temporada;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public Integer getNumeroEP() {
        return numeroEP;
    }

    public void setNumeroEP(Integer numeroEP) {
        this.numeroEP = numeroEP;
    }

    public Double getAvaliacao() {
        return avaliacao;
    }

    public void setAvaliacao(Double avaliacao) {
        this.avaliacao = avaliacao;
    }

    public LocalDate getDataLancamento() {
        return dataLancamento;
    }

    public void setDataLancamento(LocalDate dataLancamento) {
        this.dataLancamento = dataLancamento;
    }

    @Override
    public String toString() {
        return "Temporada: "+this.getTemporada()+"|| Episódio: "+this.getTitulo()+ "|| Número do Ep: "+this.getNumeroEP()+"|| Avaliação: "+this.getAvaliacao()+"|| Data de lançamento: "+this.getDataLancamento();
    }
}
