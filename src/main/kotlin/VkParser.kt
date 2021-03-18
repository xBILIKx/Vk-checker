import org.jsoup.Jsoup
import java.io.File
import java.nio.charset.Charset

fun startPars(): List<Profile>{
    val pattern = Regex("([А-Яа-яёЁ]{2,}) ?.*")

    val lines = File("inputFiles/Dictionary.txt").readText(Charset.forName("windows-1251"))

//    println(lines)

    val searchers = mutableListOf<String>().apply {

        pattern.findAll(lines).map { it.groupValues[1] }.forEach { add(it) }

//        val arr = pattern.findAll(lines).map { it.groupValues[1] }.toList()
//
//        for (i in 0..5)
//            add(arr[i])

    }.toList()

//    println(searchers.size)

    val profiles = mutableListOf<Profile>()

    for (searcher in searchers){
        val document = Jsoup.connect("https://vk.com/search?c%5Bq%5D=$searcher%D0%B0&%5Bsection%5D=people").get()

        val profilesElement = document.getElementById("results")

        for (profileCounter in 0 until profilesElement.childrenSize()){
            try {
                val info = profilesElement.child(profileCounter).child(2).child(0).child(0)

                val ref = info.attr("href")
                val name = info.text()

                profiles.add(Profile(name, "https://vk.com$ref"))
            }catch (ex: Exception){
                continue
            }
        }

        println(searchers.indexOf(searcher))
        Thread.sleep(5000)
    }

    return profiles

}

data class Profile(val name: String = "err", val ref: String)