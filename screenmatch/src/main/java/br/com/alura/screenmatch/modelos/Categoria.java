package br.com.alura.screenmatch.modelos;

public enum Categoria {
    ACAO("Action"),
    ROMANCE("Romance"),
    COMEDIA("Comedy"),
    DRAMA("Drama"),
    CRIMNE("Crime"),
    AVENTURA("Adventure");

    private String categoriaOMDB;

    Categoria (String categoriaOMDB){
        this.categoriaOMDB = categoriaOMDB;
    }
    public static Categoria fromString(String text) {
        for (Categoria categoria : Categoria.values()) {
            if (categoria.categoriaOMDB.equalsIgnoreCase(text)) {
                return categoria;
            }
        }
        throw new IllegalArgumentException("Nenhuma categoria encontrada para a string fornecida: " + text);
    }
}
