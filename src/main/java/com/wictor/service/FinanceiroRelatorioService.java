package com.wictor.service;

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

        List<Financeiro> movimentacoes = buscarMovimentacoes(inicio, fim);

        BigDecimal entradas = calcularEntradas(movimentacoes);
        BigDecimal saidas = calcularSaidas(movimentacoes);

        return montarPdf(movimentacoes, inicio, fim, entradas, saidas);
    }

    private Image gerarGraficoPizza(
            BigDecimal entradas,
            BigDecimal saidas
    ) {
        try {

            DefaultPieDataset dataset = new DefaultPieDataset();

            dataset.setValue("Receitas", entradas.doubleValue());
            dataset.setValue("Despesas", saidas.doubleValue());

            JFreeChart grafico = ChartFactory.createPieChart("Resumo Financeiro", dataset, true, true, false);

            grafico.getTitle().setFont(new java.awt.Font("Helvetica", java.awt.Font.BOLD, 18));

            PiePlot plot = (PiePlot) grafico.getPlot();
            plot.setSectionPaint("Receitas", new java.awt.Color(46, 125, 50));
            plot.setSectionPaint("Despesas", new java.awt.Color(198, 40, 40));

            plot.setLabelGenerator(new StandardPieSectionLabelGenerator("{0}: {2}",
                    new DecimalFormat("0"), new DecimalFormat("0.0%")));

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

            for (Financeiro financeiro : movimentacoes) {

                String tipo = financeiro.getTipo().getDescricao();

                String serie = financeiro.getTipo().isEntrada() ? "Receitas" : "Despesas";

                dataset.addValue(financeiro.getValor().doubleValue(), serie, tipo);
            }

            JFreeChart grafico = ChartFactory.createBarChart("Movimentações por Tipo", "Tipo", "Valor (R$)", dataset);

            grafico.getTitle().setFont(new java.awt.Font("Helvetica", java.awt.Font.BOLD, 18));

            BarRenderer renderer = (BarRenderer) grafico.getCategoryPlot().getRenderer();

            renderer.setSeriesPaint(0, new java.awt.Color(46,125,50));
            renderer.setSeriesPaint(1, new java.awt.Color(198,40,40));

            BufferedImage imagem = grafico.createBufferedImage(600,350);

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

    private BigDecimal calcularSaldo(BigDecimal entradas, BigDecimal saidas) {
        return entradas.subtract(saidas);
    }

    private byte[] montarPdf(
            List<Financeiro> movimentacoes,
            LocalDate inicio,
            LocalDate fim,
            BigDecimal entradas,
            BigDecimal saidas
    ) {

        BigDecimal saldo = calcularSaldo(entradas, saidas);

        try (ByteArrayOutputStream baos = new ByteArrayOutputStream()) {

            Document document = new Document(PageSize.A4, 36, 36, 36, 36);
            PdfWriter.getInstance(document, baos);

            document.open();

            Font titulo = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 18);
            Font subtitulo = FontFactory.getFont(FontFactory.HELVETICA, 12);
            Font cabecalhoTabela = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 11);
            Font texto = FontFactory.getFont(FontFactory.HELVETICA, 10);
            Font resumo = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 12);

            Paragraph tituloPdf = new Paragraph("RELATÓRIO FINANCEIRO", titulo);
            tituloPdf.setAlignment(Element.ALIGN_CENTER);
            document.add(tituloPdf);

            document.add(Chunk.NEWLINE);

            document.add(new Paragraph(
                    "Período: "
                            + inicio.format(DATA_FORMATTER)
                            + " até "
                            + fim.format(DATA_FORMATTER),
                    subtitulo));

            document.add(new Paragraph(
                    "Emitido em: "
                            + LocalDate.now().format(DATA_FORMATTER),
                    subtitulo));

            document.add(Chunk.NEWLINE);

            PdfPTable tabela = new PdfPTable(5);
            tabela.setWidthPercentage(100);
            tabela.setWidths(new float[]{2, 3, 5, 3, 2});

            PdfPCell cell;

            cell = new PdfPCell(new Phrase("Data", cabecalhoTabela));
            tabela.addCell(cell);

            cell = new PdfPCell(new Phrase("Tipo", cabecalhoTabela));
            tabela.addCell(cell);

            cell = new PdfPCell(new Phrase("Descrição", cabecalhoTabela));
            tabela.addCell(cell);

            cell = new PdfPCell(new Phrase("Responsável", cabecalhoTabela));
            tabela.addCell(cell);

            cell = new PdfPCell(new Phrase("Valor", cabecalhoTabela));
            tabela.addCell(cell);

            for (Financeiro financeiro : movimentacoes) {

                tabela.addCell(new Phrase(
                        financeiro.getData().format(DATA_FORMATTER),
                        texto));

                tabela.addCell(new Phrase(
                        financeiro.getTipo().getDescricao(),
                        texto));

                tabela.addCell(new Phrase(
                        financeiro.getNome(),
                        texto));

                tabela.addCell(new Phrase(
                        financeiro.getFuncionario().getUser().getNome(),
                        texto));

                tabela.addCell(new Phrase(
                        MOEDA.format(financeiro.getValor()),
                        texto));
            }

            document.add(tabela);

            document.add(Chunk.NEWLINE);

            document.add(new Paragraph(
                    "Total de entradas: " + MOEDA.format(entradas),
                    resumo));

            document.add(new Paragraph(
                    "Total de saídas: " + MOEDA.format(saidas),
                    resumo));

            document.add(new Paragraph(
                    "Saldo: " + MOEDA.format(saldo),
                    resumo));

            document.add(Chunk.NEWLINE);
            document.add(Chunk.NEWLINE);

            Image pizza = gerarGraficoPizza(entradas, saidas);

            pizza.scaleToFit(400, 250);
            pizza.setAlignment(Element.ALIGN_CENTER);
            document.add(pizza);

            Image barras = gerarGraficoBarras(movimentacoes);

            barras.scaleToFit(500,300);
            barras.setAlignment(Element.ALIGN_CENTER);

            document.add(barras);

            document.close();

            return baos.toByteArray();

        } catch (Exception e) {
            throw new RuntimeException("Erro ao gerar relatório financeiro.", e);
        }
    }


}