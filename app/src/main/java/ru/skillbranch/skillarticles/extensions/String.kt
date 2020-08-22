package ru.skillbranch.skillarticles.extensions

/**
Реализуй функцию расширения fun String?.indexesOf(substr: String, ignoreCase: Boolean = true): List,
в качестве аргумента принимает подстроку и флаг - учитывать или нет регистр подстроки при поиске по исходной строке.
Возвращает список позиций вхождений подстроки в исходную строку.
Пример: "lorem ipsum sum".indexesOf("sum") // [8, 12]
 */
fun String?.indexesOf(substr: String, ignoreCase: Boolean = true): List<Int> {
    val listOut: MutableList<Int> = mutableListOf()

    if (!this.isNullOrEmpty() && substr.isNotEmpty()) {
        var matchIndex: Int = -1
        do {
            matchIndex = this.indexOf(substr, matchIndex + 1, ignoreCase)
            if (matchIndex != -1) listOut.add(matchIndex)
        } while (matchIndex != -1)
    }

    return listOut
}