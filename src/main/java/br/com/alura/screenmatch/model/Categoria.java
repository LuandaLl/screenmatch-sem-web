package br.com.alura.screenmatch.model;

public enum Categoria {
    ACAO("Action", "Ação"),
    ROMANCE("Romance", "romance"),
    COMEDIA("Comedy", "comédia"),
    CRIME("Crime", "crime"),
    DRAMA("Drama", "drama"),
    AVENTURA("Adventure", "aventura");

    private String categoriaOmdb;
    private String categoriaPortugues;

    private Categoria(String categoriaOmdb, String categoriaPortgues) {
        this.categoriaOmdb = categoriaOmdb;
        this.categoriaPortugues = categoriaPortgues;
    }

    public static Categoria fromString(String text) {
        for (Categoria categoria : Categoria.values()) {
            if (categoria.categoriaOmdb.equalsIgnoreCase(text)) {
                return categoria;
            }
        }
        throw new IllegalArgumentException("Nenhuma categoria encontrada para a string fornecida: " + text);
    }

    public static Categoria fromPortugues(String text) {
        for (Categoria categoria : Categoria.values()) {
            if (categoria.categoriaPortugues.equalsIgnoreCase(text)) {
                return categoria;
            }
        }
        throw new IllegalArgumentException("Nenhuma categoria encontrada para a string fornecida: " + text);
    }



    


}
