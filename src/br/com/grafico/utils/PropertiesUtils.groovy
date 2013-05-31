package br.com.grafico.utils

class PropertiesUtils {

    static Properties props

    static def getPropriedade(String propriedade) {
        return getProps().getProperty(propriedade)
    }

    private static getProps() {
        if (!props) {
            props = new Properties()
            String diretorioCorrente = System.getProperty("user.home")
            File propsFile = new File("${diretorioCorrente}/grafico/conf/conf.properties")
            if (!propsFile || !propsFile.exists())
                throw new RuntimeException("Arquivo $diretorioCorrente/grafico/conf/conf.properties não encontrado!")
            props.load(propsFile.newDataInputStream())
        }
        return props
    }

}
