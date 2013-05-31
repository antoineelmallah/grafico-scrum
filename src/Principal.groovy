import br.com.grafico.conversor.GraficoHelper
import br.com.grafico.utils.Propriedades
import br.com.grafico.utils.Temporizador
import groovy.swing.SwingBuilder

import javax.swing.*
import java.awt.*

class Principal {

    private static JPanel panelGrafico
    private JCheckBoxMenuItem checkItem

    private static def graficos
    private Temporizador temporizador
    private boolean autoAtualizar

    public static void main(args) {
        new Principal().with {
            criarGraficos()
            criarTemporizador()
            atualizarTemporizador()
            init()
        }
    }

    def init() {

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
                        checkItem = checkBoxMenuItem(
                                selected: deveAgendarAtualizacao()) {
                            action(
                                    name: 'Teste',
                                    closure: {
                                        atualizarTemporizador()
                                        autoAtualizar = checkItem.selected
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

    private void criarTemporizador() {
        if (!temporizador) {
            temporizador = new Temporizador("atualizando graficos")
            temporizador.tarefa = {
                atualizarGraficos()
            }
        }
    }

    private void atualizarTemporizador() {
        if (deveAgendarAtualizacao())
            temporizador.iniciar()
        else
            temporizador.parar()
    }

    private boolean deveAgendarAtualizacao() {
        if (!checkItem) {
            autoAtualizar = Propriedades.AUTO_ATUALIZAR.getValor() as boolean
        } else {
            autoAtualizar = checkItem.selected
        }
        return autoAtualizar
    }

    private void criarGraficos() {
        graficos = GraficoHelper.criarGraficos()
    }

}
