package br.com.grafico.modelo

import br.com.grafico.utils.Propriedades

class Sprint implements Serializable {
    String planilha
    Date inicio
    def stories = []

    Sprint proxima

    def getFeito(Date dia) {
        def pontos = stories.findAll({it.termino && it.termino <= dia})*.pontos.sum()
        pontos = pontos ?: 0
        pontos -= getResiduo(dia)
        return pontos
    }

    private boolean isUltimoDiaPeriodo(Date dia) {
        getPeriodo()[-1] == dia
    }

    def getAFazer(Date dia) {
        def pontos = stories.findAll({it.inicio <= dia})*.pontos.sum()
        return pontos ?: 0
    }

    def getPeriodo() {
        Date fimPeriodo = inicio + (Propriedades.DIAS_SPRINT.getValor() as Integer)
        Date maiorDataTermino = getMaiorDataStorys()
        fimPeriodo = fimPeriodo > maiorDataTermino? fimPeriodo: maiorDataTermino
        return inicio..fimPeriodo
    }

    private Date getMaiorDataStorys() {
        Date maiorDataInicio = stories*.inicio.max()
        Date maiorDataTermino = stories*.termino.max()
        return (maiorDataTermino && maiorDataTermino > maiorDataInicio)? maiorDataTermino: maiorDataInicio
    }

    def getVelocidade() {
        Date ultimaDataPeriodo = getPeriodo()[-1]
        return getFeito(ultimaDataPeriodo)
    }

    void setProxima(Sprint proxima) {
        this.proxima = null
        if (!proxima || proxima.inicio > inicio) {
            this.proxima = proxima
        }
    }

    // Calcula a diferença de stories entre sprints consecutivas
    float getResiduo(Date data) {
        float residuo = 0
        if (proxima && isUltimoDiaPeriodo(data)) {
            residuo = proxima.stories.collect { s2 ->
                def s1 = stories.find { s -> s.story.trim() == s2.story.trim() }
                s1? s2.pontos - s1.pontos: 0
            }.sum()
        }
        return residuo
    }

    def validate() {
        campoPlanilhaDeveSerInformado()
        campoDataInicioDeveSerInformado()
        stories.each {
            it.validate()
            dataInicioStoryNaoPodeSerAnteriorQueDataSprint(inicio, it)
        }

    }

    private void campoPlanilhaDeveSerInformado() {
        if (!planilha || planilha.trim().isEmpty()) {
            throw new IllegalArgumentException("Campo 'planilha' deve ser informado.")
        }
    }

    private void campoDataInicioDeveSerInformado() {
        if (!inicio) {
            throw new IllegalArgumentException("Campo 'data de início' deve ser informado.")
        }
    }

    private void dataInicioStoryNaoPodeSerAnteriorQueDataSprint(Date inicioSprint, Story story) {
        if (story && story.inicio.before(inicioSprint)) {
            throw new IllegalArgumentException("A data de início da story não pode ser anterior que a data de início da sprint.")
        }
    }
}
