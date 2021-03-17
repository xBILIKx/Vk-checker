import org.apache.poi.ss.usermodel.WorkbookFactory
import org.apache.poi.xssf.usermodel.XSSFWorkbook
import org.jsoup.Jsoup
import java.io.FileInputStream
import java.io.FileOutputStream

const val filePath = "outputFiles/Profiles.xlsx"

fun createTable(){
    val profiles = startPars()

    val workBook = XSSFWorkbook()
    val workSheet = workBook.createSheet()

    workSheet.createRow(0).createCell(0).setCellValue("Names")
    workSheet.getRow(0).createCell(1).setCellValue("Refs")

    for (i in 1..profiles.size){

        workSheet.createRow(i).createCell(0).setCellValue(profiles[i-1].name)
        workSheet.getRow(i).createCell(1).setCellValue(profiles[i-1].ref)

    }

    val output = FileOutputStream(filePath)

    workBook.write(output)
    workBook.close()
}

fun readTable(): List<String> {
    val inputStream = FileInputStream(filePath)

    val workBook = WorkbookFactory.create(inputStream)
    val workSheet = workBook.getSheetAt(0)

    return mutableListOf<String>().apply {
        var count = 1

        while (workSheet.getRow(count)?.getCell(1) != null) {
            add(workSheet.getRow(count)?.getCell(1).toString())
            count++
        }
    }.toList()
}

fun adjustTable(refsArr: List<String>){
    val inputStream = FileInputStream(filePath)

    val workBook = WorkbookFactory.create(inputStream)
    val workSheet = workBook.getSheetAt(0)

    workSheet.getRow(0).getCell(2).setCellValue("Can write")

    for (i in 1..refsArr.size){
        val document = Jsoup.connect(refsArr[i-1]).get()

        workSheet.getRow(i).getCell(2).setCellValue(checker(document))

    }

}

fun checker(doc: org.jsoup.nodes.Document): Boolean{
    TODO() //проверить можно ли писать человеку, P.S через количество дочерних объектов
}
