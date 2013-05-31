package br.com.grafico.conversor

import br.com.grafico.modelo.Grafico
import br.com.grafico.modelo.Sprint
import br.com.grafico.utils.PropertiesUtils
import org.jfree.chart.ChartRenderingInfo
import org.jfree.chart.ChartUtilities
import org.jfree.chart.entity.StandardEntityCollection
import org.jfree.data.time.Day
import org.jfree.data.time.TimeSeries
import org.jfree.data.time.TimeSeriesCollection

class GraficoHelper {

    static def criarGraficos() {
        def graficos = []
        new DadosModelo().criarSprints().each {
            graficos << criarGrafico(it)
        }
        return graficos
    }

    static Grafico criarGrafico(Sprint sprint) {
        Grafico grafico = new Grafico(
            titulo: "SPRINT ${sprint.inicio.format('dd/MM/yyyy')} - Velocidade: ${sprint.getVelocidade()} pts",
            tituloEixoX: "Dias",
            tituloEixoY: "Pontos",
            dataset: criarDataset(sprint),
            inicioSprint: sprint.inicio
        )
        return grafico
    }

    static void exportarImagem(Grafico grafico) {
        def tamanho = [600, 400]
        ChartRenderingInfo info = new ChartRenderingInfo(new StandardEntityCollection())
        String caminhoPastaDados = "${PropertiesUtils.getPropriedade('caminho.home')}/dados"
        ChartUtilities.saveChartAsPNG(
                new File("${caminhoPastaDados}BurnUpChart - ${new Date().format('dd-MM-yyyy-hhmmss')}.png"),
                grafico.panel.chart,
                *tamanho,
                info
        )
    }

    private static def criarDataset(Sprint sprint) {
        def dataset = new TimeSeriesCollection()
        dataset.addSeries(createSeries("A Fazer", sprint) {
            day -> sprint.getAFazer(day)
        })
        dataset.addSeries(createSeries("Feito"  , sprint) {
            day -> sprint.getFeito(day)
        })
        return dataset
    }

    private static def createSeries(String name, Sprint sprint, closure) {
        def series = new TimeSeries(name)
        sprint.getPeriodo().each {
            series.add(new Day(it), closure(it) as Number)
        }
        return series
    }

}
