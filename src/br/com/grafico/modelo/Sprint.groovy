package br.com.grafico.modelo

import br.com.grafico.utils.PropertiesUtils

class Sprint {
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
        def numeroDiasSprint = PropertiesUtils.getPropriedade("numero.dias.sprint") as Integer
        calendar.add(Calendar.DAY_OF_MONTH, numeroDiasSprint)
        return inicio..calendar.time
    }

    def getVelocidade() {
        Date ultimaDataPeriodo = getPeriodo()[-1]
        return getFeito(ultimaDataPeriodo)
    }
}
