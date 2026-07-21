package com.wictor.service;

import com.wictor.enums.TipoFinanceiro;
import com.wictor.model.Financeiro;
import com.wictor.repository.FinanceiroRepository;
import lombok.RequiredArgsConstructor;
import org.jfree.chart.renderer.category.BarRenderer;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import com.lowagie.text.*;
import com.lowagie.text.pdf.*;
import java.io.ByteArrayOutputStream;
import java.text.NumberFormat;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.data.general.DefaultPieDataset;

import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import org.jfree.chart.labels.StandardPieSectionLabelGenerator;
import java.text.DecimalFormat;
import java.util.Map;
import java.util.stream.Collectors;

import org.jfree.chart.plot.PiePlot;
import org.jfree.data.category.DefaultCategoryDataset;

@Service
@RequiredArgsConstructor
public class FinanceiroRelatorioService {

    private static final DateTimeFormatter DATA_FORMATTER =
            DateTimeFormatter.ofPattern("dd/MM/yyyy");

    private static final NumberFormat MOEDA =
            NumberFormat.getCurrencyInstance(new Locale("pt", "BR"));

    private final FinanceiroRepository financeiroRepository;

    public byte[] gerarRelatorio(LocalDate inicio, LocalDate fim) {

        if (inicio.isAfter(fim)) {

            throw new IllegalArgumentException("A data inicial não pode ser maior que a data final.");
        }

        List<Financeiro> movimentacoes = buscarMovimentacoes(inicio, fim);

        BigDecimal entradas = calcularEntradas(movimentacoes);
        BigDecimal saidas = calcularSaidas(movimentacoes);

        return montarPdf(movimentacoes, inicio, fim, entradas, saidas);
    }

    private Image gerarGraficoPizza(BigDecimal entradas, BigDecimal saidas) {
        try {

            DefaultPieDataset<String> dataset = new DefaultPieDataset<>();

            dataset.setValue("Receitas", entradas.doubleValue());
            dataset.setValue("Despesas", saidas.doubleValue());

            JFreeChart grafico = ChartFactory.createPieChart(
                    "Resumo Financeiro",
                    dataset,
                    true,
                    true,
                    false
            );

            grafico.getTitle().setFont(new java.awt.Font("Helvetica", java.awt.Font.BOLD, 18));

            if (!(grafico.getPlot() instanceof PiePlot<?>)) {
                throw new IllegalStateException("Gráfico gerado não é um PiePlot");
            }

            @SuppressWarnings("unchecked")
            PiePlot<String> plot = (PiePlot<String>) grafico.getPlot();

            plot.setSectionPaint("Receitas", new java.awt.Color(46, 125, 50));

            plot.setSectionPaint("Despesas", new java.awt.Color(198, 40, 40));

            plot.setLabelGenerator(
                    new StandardPieSectionLabelGenerator(
                            "{0}: {2}",
                            new DecimalFormat("0"),
                            new DecimalFormat("0.0%")
                    )
            );

            plot.setLabelFont(new java.awt.Font("Helvetica", java.awt.Font.PLAIN, 12));

            plot.setSimpleLabels(false);

            BufferedImage imagem = grafico.createBufferedImage(500, 300);

            ByteArrayOutputStream baos = new ByteArrayOutputStream();

            ImageIO.write(imagem, "png", baos);

            return Image.getInstance(baos.toByteArray());

        } catch (Exception e) {

            throw new RuntimeException("Erro ao gerar gráfico financeiro", e);
        }
    }

    private Image gerarGraficoBarras(List<Financeiro> movimentacoes) {

        try {

            DefaultCategoryDataset dataset = new DefaultCategoryDataset();

            Map<TipoFinanceiro, BigDecimal> totais =
                    movimentacoes.stream().collect(Collectors.groupingBy(Financeiro::getTipo,
                                    Collectors.mapping(Financeiro::getValor,
                                            Collectors.reducing(BigDecimal.ZERO, BigDecimal::add))));

            totais.forEach((tipo, valor) -> {

                String serie = tipo.isEntrada() ? "Receitas" : "Despesas";

                dataset.addValue(valor.doubleValue(), serie, tipo.getDescricao());
            });

            JFreeChart grafico = ChartFactory.createBarChart(
                    "Movimentações por Tipo",
                    "Tipo",
                    "Valor (R$)",
                    dataset
            );

            grafico.getTitle().setFont(new java.awt.Font("Helvetica", java.awt.Font.BOLD, 18));

            BarRenderer renderer = (BarRenderer) grafico.getCategoryPlot().getRenderer();

            renderer.setSeriesPaint(0, new java.awt.Color(46,125,50));

            renderer.setSeriesPaint(1, new java.awt.Color(198,40,40));

            BufferedImage imagem = grafico.createBufferedImage(600, 350);

            ByteArrayOutputStream baos = new ByteArrayOutputStream();

            ImageIO.write(imagem, "png", baos);

            return Image.getInstance(baos.toByteArray());

        } catch (Exception e) {

            throw new RuntimeException("Erro ao gerar gráfico de barras", e);
        }
    }

    private List<Financeiro> buscarMovimentacoes(LocalDate inicio, LocalDate fim) {
        return financeiroRepository.findByDataBetweenOrderByDataAsc(inicio, fim);
    }

    private BigDecimal calcularEntradas(List<Financeiro> movimentacoes) {

        return movimentacoes.stream()
                .filter(f -> f.getTipo().isEntrada())
                .map(Financeiro::getValor)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    private BigDecimal calcularSaidas(List<Financeiro> movimentacoes) {

        return movimentacoes.stream()
                .filter(f -> f.getTipo().isSaida())
                .map(Financeiro::getValor)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    private BigDecimal calcularSaldo(BigDecimal entradas, BigDecimal saidas) {return entradas.subtract(saidas);}

    private String obterOrigem(Financeiro financeiro) {
        return financeiro.getOrigem() != null
                ? financeiro.getOrigem().name()
                : "-";
    }

    private static final Font TITULO = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 18);

    private static final Font SUBTITULO = FontFactory.getFont(FontFactory.HELVETICA, 12);

    private static final Font CABECALHO = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 11);

    private static final Font TEXTO = FontFactory.getFont(FontFactory.HELVETICA, 10);

    private static final Font RESUMO = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 12);

    private void adicionarCabecalho(PdfPTable tabela, String titulo) {

        PdfPCell cell = new PdfPCell(new Phrase(titulo, CABECALHO));
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cell.setPadding(5);

        tabela.addCell(cell);
    }

    private void adicionarLinha(PdfPTable tabela, Financeiro financeiro) {

        tabela.addCell(new Phrase(financeiro.getData().format(DATA_FORMATTER), TEXTO));

        tabela.addCell(new Phrase(financeiro.getTipo().getDescricao(), TEXTO));

        tabela.addCell(new Phrase(obterOrigem(financeiro), TEXTO));

        tabela.addCell(new Phrase(financeiro.getNome(), TEXTO));

        tabela.addCell(new Phrase(obterResponsavel(financeiro), TEXTO));

        tabela.addCell(new Phrase(obterFormaPagamento(financeiro), TEXTO));

        tabela.addCell(new Phrase(MOEDA.format(financeiro.getValor()), TEXTO));
    }

    private void adicionarResumo(Document document, List<Financeiro> movimentacoes, BigDecimal entradas,
                                 BigDecimal saidas, BigDecimal saldo) throws DocumentException {

        document.add(new Paragraph("Movimentações: " + movimentacoes.size(), RESUMO));

        document.add(new Paragraph("Entradas: " + MOEDA.format(entradas), RESUMO));

        document.add(new Paragraph("Saídas: " + MOEDA.format(saidas), RESUMO));

        document.add(new Paragraph("Saldo: " + MOEDA.format(saldo), RESUMO));
    }

    private String obterResponsavel(Financeiro financeiro) {

        if (financeiro.getFuncionario() == null) {return "Sistema";}

        return financeiro.getFuncionario().getUser().getNome();
    }

    private void adicionarGraficos(Document document, List<Financeiro> movimentacoes, BigDecimal entradas,
                                   BigDecimal saidas) throws DocumentException {

        Image pizza = gerarGraficoPizza(entradas, saidas);
        pizza.scaleToFit(400, 250);
        pizza.setAlignment(Element.ALIGN_CENTER);
        document.add(pizza);

        Image barras = gerarGraficoBarras(movimentacoes);
        barras.scaleToFit(500, 300);
        barras.setAlignment(Element.ALIGN_CENTER);
        document.add(barras);
    }

    private String obterFormaPagamento(Financeiro financeiro) {

        if (financeiro.getPagamento() == null ||
                financeiro.getPagamento().getFormaPagamento() == null) {
            return "-";
        }

        return financeiro.getPagamento()
                .getFormaPagamento()
                .name();
    }

    private byte[] montarPdf(List<Financeiro> movimentacoes, LocalDate inicio, LocalDate fim,
                             BigDecimal entradas, BigDecimal saidas) {

        BigDecimal saldo = calcularSaldo(entradas, saidas);

        try (ByteArrayOutputStream baos = new ByteArrayOutputStream()) {

            Document document = new Document(PageSize.A4, 36, 36, 36, 36);
            PdfWriter.getInstance(document, baos);

            document.open();

            Paragraph tituloPdf = new Paragraph("RELATÓRIO FINANCEIRO", TITULO);
            tituloPdf.setAlignment(Element.ALIGN_CENTER);
            document.add(tituloPdf);

            document.add(Chunk.NEWLINE);

            document.add(new Paragraph(
                    "Período: "
                            + inicio.format(DATA_FORMATTER)
                            + " até "
                            + fim.format(DATA_FORMATTER),
                    SUBTITULO));

            document.add(new Paragraph(
                    "Emitido em: "
                            + LocalDate.now().format(DATA_FORMATTER),
                    SUBTITULO));

            document.add(Chunk.NEWLINE);

            PdfPTable tabela = new PdfPTable(7);
            tabela.setWidthPercentage(100);
            tabela.setWidths(new float[]{2f, 2f, 3f, 4f, 3f, 2f, 2f});

            adicionarCabecalho(tabela, "Data");
            adicionarCabecalho(tabela, "Tipo");
            adicionarCabecalho(tabela, "Origem");
            adicionarCabecalho(tabela, "Descrição");
            adicionarCabecalho(tabela, "Responsável");
            adicionarCabecalho(tabela, "Forma");
            adicionarCabecalho(tabela, "Valor");

            movimentacoes.forEach(financeiro -> adicionarLinha(tabela, financeiro));

            document.add(tabela);

            document.add(Chunk.NEWLINE);

            adicionarResumo(document, movimentacoes, entradas, saidas, saldo);

            document.add(Chunk.NEWLINE);
            document.add(Chunk.NEWLINE);

            adicionarGraficos(document, movimentacoes, entradas, saidas);

            Paragraph rodape = new Paragraph("Relatório gerado automaticamente", TEXTO);

            rodape.setAlignment(Element.ALIGN_CENTER);

            document.add(Chunk.NEWLINE);
            document.add(rodape);

            document.close();

            return baos.toByteArray();

        } catch (Exception e) {

            throw new RuntimeException("Erro ao gerar relatório financeiro.", e);
        }
    }
}