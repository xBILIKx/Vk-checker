import java.io.File

fun main(){
    if (!File(filePath).exists())
        startCreating()

    createSheetToTrueWrite()

//    adjustTable(readTable())
}

fun startCreating(){
    createTable()
    adjustTable(readTable())
    createSheetToTrueWrite()
}