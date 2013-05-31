package br.com.grafico.utils

class PropertiesUtils {

    private static Properties props
    private static File propsFile

    static def getPropriedade(String propriedade) {
        def _propriedade = getProps().getProperty(propriedade)
        if (!_propriedade) throw new RuntimeException("A propriedade ${propriedade} não foi configurada.")
        return _propriedade
    }

    private static getProps() {
        if (!props) {
            props = new Properties()
            props.load(getPropsFile().newDataInputStream())
        }
        return props
    }

    private static File getPropsFile() {
        if (!propsFile) {
            String caminho = "${System.getProperty("user.home")}/grafico/conf/conf.properties"
            propsFile = new File(caminho)
            if (!propsFile.exists()) {
                throw new RuntimeException("Arquivo $caminho não encontrado!")
            }
        }
        return propsFile
    }

}
