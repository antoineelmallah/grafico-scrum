import br.com.grafico.conversor.GraficoHelper
import br.com.grafico.utils.PropertiesUtils
import br.com.grafico.utils.Temporizador
import groovy.swing.SwingBuilder

import javax.swing.*
import java.awt.*

class Principal {

    static def graficos
    static JPanel panelGrafico
    private static final String AUTO_ATUALIZAR = "atualizar.automaticamente"

    private Temporizador temporizador

    private JCheckBoxMenuItem checkItem

    private def propriedades = [:]

    public static void main(args) {
        Principal principal = new Principal()

        principal.carregarPropriedades()
        principal.criarGraficos()
        principal.criarTemporizador()
        principal.atualizarTemporizador()
        principal.init(graficos)
    }

    def init(def graficos) {

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
                                        propriedades.put(AUTO_ATUALIZAR, checkItem.selected)
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
        boolean agendar = false
        if (!checkItem) {
            agendar = propriedades[AUTO_ATUALIZAR]
        } else {
            agendar = checkItem.selected
        }
        return agendar
    }

    private void carregarPropriedades() {
        propriedades.put(
                AUTO_ATUALIZAR,
                PropertiesUtils.getPropriedade(AUTO_ATUALIZAR) as boolean
        )
    }

    private void criarGraficos() {
        graficos = GraficoHelper.criarGraficos()
    }

}
