package br.com.grafico.modelo

class Story implements Serializable {
    String story
    def pontos
    Date inicio
    Date termino

    def validate() {
        campoStoryDeveSerInformado()
        campoDataInicioDeveSerInformado()
        campoDataTerminoNaoPodeSerAnteriorQueDataInicio()
        campoPontosDeveSerInformado()
        campoPontosDeveSerMaiorQueZero()
    }

    private void campoStoryDeveSerInformado() {
        if (!story || story.trim().isEmpty()) {
            throw new IllegalArgumentException("Campo 'story' deve ser informado.")
        }
    }

    private void campoDataInicioDeveSerInformado() {
        if (!inicio) {
            throw new IllegalArgumentException("Campo 'data de início' deve ser informado.")
        }
    }

    private void campoDataTerminoNaoPodeSerAnteriorQueDataInicio() {
        if (termino && termino.before(inicio)) {
            throw new IllegalArgumentException("Data término não pode ser anterior que a data início.")
        }
    }

    private void campoPontosDeveSerInformado() {
        if (!pontos) {
            throw new IllegalArgumentException("Campo 'pontos' deve ser informado.")
        }
    }

    private void campoPontosDeveSerMaiorQueZero() {
        if (pontos <= 0) {
            throw new IllegalArgumentException("Campo 'pontos' deve ser maior que zero.")
        }
    }

}
