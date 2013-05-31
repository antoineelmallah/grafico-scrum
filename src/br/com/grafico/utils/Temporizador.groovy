package br.com.grafico.utils

class Temporizador {

    private Timer timer
    private String nomeTarefa
    Integer delay
    Integer period
    def tarefa = {}

    def Temporizador(String nomeTarefa) {
        this.nomeTarefa = nomeTarefa
        this.delay = PropertiesUtils.getPropriedade("timer.delay") as Integer
        this.period = PropertiesUtils.getPropriedade("timer.period") as Integer
    }

    def iniciar() {
        getTimer()
    }

    def parar() {
        this.timer.cancel()
    }

    private Timer getTimer() {
        timer = new Timer()
        timer.schedule(new TimerTask() {
            @Override
            void run() {
                tarefa()
                println "Executou tarefa $nomeTarefa"
            }
        }, delay, period)
        return timer
    }

}
