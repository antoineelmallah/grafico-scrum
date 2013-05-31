package br.com.grafico.utils

public enum Propriedades {

    AUTO_ATUALIZAR("atualizar.automaticamente"),
    CAMINHO_HOME("caminho.home"),
    CAMINHO_PLANILHA("caminho.planilha"),
    DIAS_SPRINT("numero.dias.sprint"),
    TIMER_DELAY("timer.delay"),
    TIMER_PERIOD("timer.period")

    private String valor

    private Propriedades(String valor) {
        this.valor = valor
    }

    def getValor() {
        return PropertiesUtils.getPropriedade(this.toString())
    }

    @Override
    String toString() {
        return valor
    }
}