import java.util.*;
import java.math.BigDecimal;
import java.text.NumberFormat;
import java.util.Locale;

public class Main {
    private static final Locale PT_BR = new Locale("pt", "BR");
    private static final NumberFormat MOEDA = NumberFormat.getCurrencyInstance(PT_BR);

    public static void main(String[] args) {
        testarClasseGame();
        System.out.println();

        List<Game> catalogo = criarCatalogoExemplo();
        listarCatalogo(catalogo);
        System.out.println();

        listarCatalogoOrdenadoPorPreco(catalogo);

        // Opcional: total do catálogo (só para validação rápida)
        BigDecimal total = catalogo.stream()
                .map(Game::getPreco)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        System.out.println("\nTotal do catálogo: " + MOEDA.format(total));
    }

    /** Parte 1 — Testes da classe Game, sem usar Cliente */
    private static void testarClasseGame() {
        System.out.println("==== Testes da classe Game ====");
        Game jogo = new Game("Minecraft", 99.99, "Aventura", 10);
        Game.exibirInformacoes(jogo);

        System.out.println("-> Tentando setPreco(-10.00) (não deve alterar):");
        jogo.setPreco(-10.00);
        System.out.println("Preço atual: " + MOEDA.format(jogo.getPreco()));

        System.out.println("-> Atualizando classificação para 12:");
        jogo.setClassificacaoEtaria(12);
        System.out.println("Classificação atual: " + jogo.getClassificacaoEtaria());

        System.out.println("-> Tentando classificação 25 (inválida, não deve alterar):");
        jogo.setClassificacaoEtaria(25);
        System.out.println("Classificação atual: " + jogo.getClassificacaoEtaria());
    }

    /** Parte 2 — Catálogo igual ao do exemplo da GUI */
    private static List<Game> criarCatalogoExemplo() {
        return Arrays.asList(
            new Game("Minecraft", 89.90, "Aventura", 10),
            new Game("FIFA 2023", 199.90, "Esporte", 0),
            new Game("The Sims 4", 129.90, "Simulação", 12),
            new Game("Grand Theft Auto V", 149.90, "Ação", 18),
            new Game("Fortnite", 0.0, "Battle Royale", 12),
            new Game("Hades", 79.90, "Roguelike", 14),
            new Game("Stardew Valley", 24.99, "Simulação", 0),
            new Game("Celeste", 36.90, "Plataforma", 10),
            new Game("Among Us", 19.99, "Party", 7),
            new Game("Cyberpunk 2077", 149.99, "RPG", 18),
            new Game("Valorant", 0.00, "FPS", 14),
            new Game("Rocket League", 39.99, "Esporte", 3)
        );
    }

    private static void listarCatalogo(List<Game> catalogo) {
        System.out.println("==== Catálogo (ordem de criação) ====");
        for (Game g : catalogo) {
            System.out.println(g); // usa o toString() da sua classe Game
        }
    }

    private static void listarCatalogoOrdenadoPorPreco(List<Game> catalogo) {
        System.out.println("==== Catálogo ordenado por preço (crescente) ====");
        List<Game> ordenados = new ArrayList<>(catalogo);
        ordenados.sort(Comparator.comparing(Game::getPreco));
        for (Game g : ordenados) {
            System.out.println(g);
        }
    }
}
