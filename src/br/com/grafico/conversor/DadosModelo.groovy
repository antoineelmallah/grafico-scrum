package br.com.grafico.conversor

import br.com.grafico.modelo.Sprint
import br.com.grafico.modelo.Story
import br.com.grafico.utils.Propriedades
import org.apache.poi.hssf.usermodel.HSSFCell
import org.apache.poi.hssf.usermodel.HSSFDateUtil
import org.apache.poi.hssf.usermodel.HSSFWorkbook

class DadosModelo {

    def planilha
    def wb
    def sprints = []

    def DadosModelo() {
        planilha = new File(Propriedades.CAMINHO_PLANILHA.getValor())
        wb = new HSSFWorkbook(planilha.newInputStream())
    }

    def criarSprints() {
        sprints.clear()

        wb.getNumberOfSheets().times {
            def nomePlanilha = wb.getSheetName(it)
            sprints << new Sprint(
                    planilha: nomePlanilha,
                    inicio: new Date().parse("ddMMyyyy", (nomePlanilha =~ /\d+/)[0] as String)
            )
        }

        sprints = sprints.sort({x,y-> y.inicio <=> x.inicio})

        if (sprints?.size()) {
            if (sprints.size() > 3) {
                // Pega as ultimas 4 sprints
                sprints = sprints[0..3]
            }

            sprints.each {sprint->
                def sheet = wb.getSheet(sprint.planilha)
                def rowIt = sheet.rowIterator()
                if (rowIt.hasNext()) rowIt.next()
                while (rowIt.hasNext()) {
                    sprint.stories << createStory(rowIt.next())
                }
            }
        }

        return sprints
    }

    private def createStory(row) {
        new Story(
                story:   getValue(row.getCell(1)),
                pontos:  getValue(row.getCell(2)),
                inicio:  getValue(row.getCell(3)),
                termino: getValue(row.getCell(4))
        )
    }

    private def getValue(cell) {
        if (!cell) {
            return null
        }
        def value
        switch (cell.cellType) {
            case HSSFCell.CELL_TYPE_NUMERIC:
                if(HSSFDateUtil.isCellDateFormatted(cell)) {
                    value = cell.dateCellValue
                } else {
                    value = cell.numericCellValue
                }
                break
            case HSSFCell.CELL_TYPE_BOOLEAN:
                value = cell.booleanCellValue
                break
            default:
                value = cell.stringCellValue
                if (!value) value = null
                break
        }
        return value
    }
}
