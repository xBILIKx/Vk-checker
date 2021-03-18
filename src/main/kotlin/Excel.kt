import org.apache.poi.ss.usermodel.Row
import org.apache.poi.ss.usermodel.WorkbookFactory
import org.apache.poi.xssf.usermodel.XSSFWorkbook
import org.jsoup.HttpStatusException
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import java.io.FileInputStream
import java.io.FileOutputStream
import java.util.ArrayList

import java.util.HashMap




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

    workSheet.getRow(0).createCell(2).setCellValue("Can write")

    for (i in 1..refsArr.size.also(::println)){
        val document = try {
            Jsoup.connect(refsArr[i-1]).get()
        }catch (ex: HttpStatusException){
            workSheet.getRow(i).createCell(2).setCellValue(false)
            continue
        }

        workSheet.getRow(i).createCell(2).setCellValue(checker(document))

        println(i-1)
    }

    val outputStream = FileOutputStream(filePath)
    workBook.write(outputStream)

    workBook.close()
}

fun createSheetToTrueWrite(){
    val inputStream = FileInputStream(filePath)

    val workBook = WorkbookFactory.create(inputStream)
    val readingSheet = workBook.getSheetAt(0)

    val rowList = mutableListOf<Row?>().apply {
        readingSheet.rowIterator().apply {
            var rowIndex = 0
            while (hasNext()) {
                val row = next()
                add(row)
                rowIndex++
            }
        }
        this[0] = null
    }.toList()

    workBook.createSheet().apply {
        var counter = 0
        for (row in rowList){
            if (row?.getCell(2)?.booleanCellValue == true){
                createRow(counter).createCell(0).setCellValue(row.getCell(0).toString())
                getRow(counter).createCell(1).setCellValue(row.getCell(1).toString())

                counter++
            }

        }
    }

    val outputStream = FileOutputStream(filePath)
    workBook.write(outputStream)

    workBook.close()
}

private fun checker(doc: Document) =
    try {
        doc.getElementsByClass("profile_action_btn").size >= 2
    }catch (ex: Exception){
        false
    }

