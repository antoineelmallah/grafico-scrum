import br.com.grafico.conversor.DadosModelo
import br.com.grafico.conversor.GraficoHelper
import br.com.grafico.utils.Temporizador
import groovy.swing.SwingBuilder

import javax.swing.*
import java.awt.*

class Principal {

    static def graficos
    static JPanel panelGrafico

    public static void main(args) {
        Principal principal = new Principal()

        principal.criarGraficos()
        principal.criarTemporizador()
        principal.imprimir(graficos)
    }

    def imprimir(def graficos) {

        new SwingBuilder().build {
            frame(title: 'BurnUp Chart',
                    defaultCloseOperation: WindowConstants.EXIT_ON_CLOSE,
                    layout: new BorderLayout(),
                    extendedState: JFrame.MAXIMIZED_BOTH,
                    pack: true,
                    visible: true
            ) {
                menuBar {
                    menu(text: 'Grafico') {
                        menuItem() {
                            action(
                                    name: 'Exportar',
                                    closure: {
                                        exportarImagem()
                                    }
                            )
                        }
                        menuItem() {
                            action(
                                    name: 'Atualizar',
                                    closure: {
                                        atualizarGraficos()
                                    }
                            )
                        }
                    }
                }
                panelGrafico = panel(id: 'canvas') {
                    graficos.each {
                        widget(
                                it.getPanel()
                        )
                    }

                }
            }
        }
    }

    private static exportarImagem() {
        GraficoHelper.exportarImagem(graficos[0])
    }

    private void atualizarGraficos() {
        new SwingWorker() {
            @Override
            protected Object doInBackground() throws Exception {
                criarGraficos()
                return 0
            }

            @Override
            protected void done() {
                panelGrafico.removeAll()
                panelGrafico.revalidate()

                graficos.each {
                    panelGrafico.add(
                            it.panel
                    )
                }
            }
        }.execute()

    }

    private void criarTemporizador(JPanel panel) {
        Temporizador t = new Temporizador("atualizando graficos")
        t.tarefa = {
            atualizarGraficos()
        }
        t.iniciar()
    }

    private static void criarGraficos() {
        graficos = []
        new DadosModelo().criarSprints().each {
            graficos << GraficoHelper.criarGrafico(it)
        }
    }

}
