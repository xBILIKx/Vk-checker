import java.io.File
import org.jsoup.Jsoup

fun main(){
    if (!File(filePath).exists())
        createTable ()

    adjustTable(readTable())
}