import javax.swing.*;
import java.awt.*;
import java.math.BigDecimal;
import java.text.NumberFormat;
import java.util.Locale;
import java.util.Arrays;
import java.util.List;

public class LojaGamesGUI extends JFrame {
    private JList<Game> listaGames;
    private JList<Game> listaComprados;
    private JLabel labelSaldo;
    private Cliente clienteAtual;
    private DefaultListModel<Game> modeloDisponiveis;
    private DefaultListModel<Game> modeloComprados;

    private static final Locale PT_BR = new Locale("pt", "BR");
    private static final NumberFormat MOEDA = NumberFormat.getCurrencyInstance(PT_BR);

    public LojaGamesGUI() {
        // Janela
        setTitle("Loja de Games - Java OOP");
        setSize(900, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout(10, 10));

        // Cliente
        clienteAtual = new Cliente("Thay", "thay@example.com", new BigDecimal("500.00"));

        // Componentes
        inicializarComponentes();
        criarGamesIniciais();

        setLocationRelativeTo(null);
        setVisible(true);
    }

    private void inicializarComponentes() {
        // Topo: info do cliente
        JPanel painelCliente = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 10));
        JLabel labelCliente = new JLabel("Cliente: " + clienteAtual.getNome());
        labelSaldo = new JLabel("Saldo: " + MOEDA.format(clienteAtual.getSaldo()));
        painelCliente.add(labelCliente);
        painelCliente.add(labelSaldo);
        add(painelCliente, BorderLayout.NORTH);

        // Esquerda: lista de disponíveis
        modeloDisponiveis = new DefaultListModel<>();
        listaGames = new JList<>(modeloDisponiveis);
        listaGames.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        JScrollPane scrollLista = new JScrollPane(listaGames);
        scrollLista.setPreferredSize(new Dimension(380, 420));
        JPanel painelEsquerda = new JPanel(new BorderLayout(5, 5));
        painelEsquerda.add(new JLabel("Games disponíveis:"), BorderLayout.NORTH);
        painelEsquerda.add(scrollLista, BorderLayout.CENTER);
        add(painelEsquerda, BorderLayout.WEST);

        // Centro: botões
        JButton btnComprar = new JButton("Comprar selecionado");
        btnComprar.addActionListener(e -> comprarGameSelecionado());

        JButton btnComprarMax = new JButton("Comprar o máximo");
        btnComprarMax.addActionListener(e -> comprarMaximo());

        JPanel painelCentro = new JPanel();
        painelCentro.setLayout(new BoxLayout(painelCentro, BoxLayout.Y_AXIS));
        painelCentro.add(Box.createVerticalStrut(60));
        painelCentro.add(btnComprar);
        painelCentro.add(Box.createVerticalStrut(20));
        painelCentro.add(btnComprarMax);
        add(painelCentro, BorderLayout.CENTER);

        // Direita: lista de comprados
        modeloComprados = new DefaultListModel<>();
        listaComprados = new JList<>(modeloComprados);
        JScrollPane scrollComprados = new JScrollPane(listaComprados);
        scrollComprados.setPreferredSize(new Dimension(380, 420));
        JPanel painelDireita = new JPanel(new BorderLayout(5, 5));
        painelDireita.add(new JLabel("Games comprados:"), BorderLayout.NORTH);
        painelDireita.add(scrollComprados, BorderLayout.CENTER);
        add(painelDireita, BorderLayout.EAST);

        // Dica: duplo clique para comprar
        listaGames.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                if (evt.getClickCount() == 2) comprarGameSelecionado();
            }
        });
    }

    private void criarGamesIniciais() {
        List<Game> iniciais = Arrays.asList(
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
        for (Game g : iniciais) {
            modeloDisponiveis.addElement(g);
        }
    }

    private void comprarGameSelecionado() {
        Game gameSelecionado = listaGames.getSelectedValue();
        if (gameSelecionado == null) {
            JOptionPane.showMessageDialog(this, "Selecione um game para comprar.",
                    "Atenção", JOptionPane.WARNING_MESSAGE);
            return;
        }
        boolean ok = clienteAtual.comprarGame(gameSelecionado);
        if (ok) {
            modeloComprados.addElement(gameSelecionado);
            // opcional: remover dos disponíveis após comprar
            modeloDisponiveis.removeElement(gameSelecionado);
            atualizarSaldo();
        } else {
            JOptionPane.showMessageDialog(this,
                    "Saldo insuficiente para comprar " + gameSelecionado.getNome() +
                            ". Saldo atual: " + MOEDA.format(clienteAtual.getSaldo()),
                    "Saldo insuficiente", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void comprarMaximo() {
        // Compra no cliente; depois sincroniza as listas
        java.util.List<Game> disponiveis = new java.util.ArrayList<>();
        for (int i = 0; i < modeloDisponiveis.size(); i++) {
            disponiveis.add(modeloDisponiveis.getElementAt(i));
        }

        clienteAtual.comprarMaximo(disponiveis);

        // Sincroniza: remove dos disponíveis os que foram comprados
        for (Game g : clienteAtual.getGamesComprados()) {
            if (modeloDisponiveis.contains(g) && !modeloComprados.contains(g)) {
                modeloDisponiveis.removeElement(g);
                modeloComprados.addElement(g);
            }
        }
        atualizarSaldo();
    }

    private void atualizarSaldo() {
        labelSaldo.setText("Saldo: " + MOEDA.format(clienteAtual.getSaldo()));
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(LojaGamesGUI::new);
    }
}
