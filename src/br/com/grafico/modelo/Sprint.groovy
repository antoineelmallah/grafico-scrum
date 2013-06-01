package br.com.grafico.modelo

import br.com.grafico.utils.Propriedades

class Sprint implements Serializable {
    String planilha
    Date inicio
    def stories = []

    def getFeito(Date dia) {
        def pontos = stories.findAll({it.termino && it.termino <= dia})*.pontos.sum()
        return pontos ?: 0
    }

    def getAFazer(Date dia) {
        def pontos = stories.findAll({it.inicio <= dia})*.pontos.sum()
        return pontos ?: 0
    }

    def getPeriodo() {
        Calendar calendar = GregorianCalendar.getInstance()
        calendar.setTime(inicio)
        def numeroDiasSprint = Propriedades.DIAS_SPRINT.getValor() as Integer
        calendar.add(Calendar.DAY_OF_MONTH, numeroDiasSprint)
        return inicio..calendar.time
    }

    def getVelocidade() {
        Date ultimaDataPeriodo = getPeriodo()[-1]
        return getFeito(ultimaDataPeriodo)
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
