package br.com.grafico.modelo

import org.jfree.chart.ChartFactory
import org.jfree.chart.ChartPanel
import org.jfree.chart.plot.XYPlot
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer
import org.jfree.data.time.TimeSeriesCollection

class Grafico {

    String titulo = "Título gráfico"
    String tituloEixoX = "Eixo X"
    String tituloEixoY = "Eixo Y"

    Date inicioSprint

    boolean realizarRefresh = true

    TimeSeriesCollection dataset

    ChartPanel panel

    ChartPanel getPanel() {
        if (!panel) {
            def options = [true, true, true]
            def chart = ChartFactory.createTimeSeriesChart(
                *gerarTitulos(),
                dataset,
                *options
            )
            XYPlot plot = chart.getXYPlot()
            XYLineAndShapeRenderer renderer = plot.getRenderer() as XYLineAndShapeRenderer
            renderer.setBaseShapesVisible(true)
            panel = new ChartPanel(chart)
        }
        return panel
    }

    private String[] gerarTitulos() {
        return [titulo, tituloEixoX, tituloEixoY]
    }

}
