package br.com.grafico.utils

class Temporizador {

    private Timer timer
    private String nomeTarefa
    Integer delay
    Integer period
    def tarefa = {}

    def Temporizador(String nomeTarefa) {
        this.nomeTarefa = nomeTarefa
        this.delay = Propriedades.TIMER_DELAY.getValor() as Integer
        this.period = Propriedades.TIMER_PERIOD.getValor() as Integer
    }

    def iniciar() {
        getTimer()
    }

    def parar() {
        if (timer) {
            timer.cancel()
            timer.purge()
        }
    }

    private Timer getTimer() {
        timer = new Timer()
        timer.schedule(new TimerTask() {
            @Override
            void run() {
                tarefa()
                println "Tarefa '${nomeTarefa}' foi executada."
            }
        }, delay, period)
        return timer
    }

}
