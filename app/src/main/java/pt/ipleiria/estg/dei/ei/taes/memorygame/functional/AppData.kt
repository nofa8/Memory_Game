package pt.ipleiria.estg.dei.ei.taes.memorygame.functional

import pt.ipleiria.estg.dei.ei.taes.memorygame.R

object AppData {
    private val numbers: List<Int> = (1..10).shuffled().map { if (it > 7) it + 3 else it }
    private val types: List<Char> = listOf('c', 'o', 'e', 'p')

    fun getPairsOfCards(numberPairs: Int): List<String> {
        // Create unique pairs by combining numbers and types
        val uniquePairs = mutableSetOf<String>()

        // Ensure that we only add unique pairs
        while (uniquePairs.size < numberPairs) {
            val number = numbers.random()
            val type = types.random()
            uniquePairs.add("$type$number")
        }

        // Duplicate each unique pair to form pairs and shuffle the final list
        val cards = (uniquePairs.toList() + uniquePairs.toList()).shuffled()

        return cards
    }
}
// Using enum with the R functionality improves speed :)
enum class CardFile(val value: String, val drawableRes: Int) {
    // Hearts (Copas)
    C1("c1", R.drawable.c1), C2("c2", R.drawable.c2), C3("c3", R.drawable.c3),
    C4("c4", R.drawable.c4), C5("c5", R.drawable.c5), C6("c6", R.drawable.c6),
    C7("c7", R.drawable.c7), C11("c11", R.drawable.c11),
    C12("c12", R.drawable.c12), C13("c13", R.drawable.c13),

    // Diamonds (Ouros)
    O1("o1", R.drawable.o1), O2("o2", R.drawable.o2), O3("o3", R.drawable.o3),
    O4("o4", R.drawable.o4), O5("o5", R.drawable.o5), O6("o6", R.drawable.o6),
    O7("o7", R.drawable.o7), O11("o11", R.drawable.o11),
    O12("o12", R.drawable.o12), O13("o13", R.drawable.o13),

    // Spades (Espadas)
    E1("e1", R.drawable.e1), E2("e2", R.drawable.e2), E3("e3", R.drawable.e3),
    E4("e4", R.drawable.e4), E5("e5", R.drawable.e5), E6("e6", R.drawable.e6),
    E7("e7", R.drawable.e7), E11("e11", R.drawable.e11),
    E12("e12", R.drawable.e12), E13("e13", R.drawable.e13),

    // Clubs (Paus)
    P1("p1", R.drawable.p1), P2("p2", R.drawable.p2), P3("p3", R.drawable.p3),
    P4("p4", R.drawable.p4), P5("p5", R.drawable.p5), P6("p6", R.drawable.p6),
    P7("p7", R.drawable.p7), P11("p11", R.drawable.p11),
    P12("p12", R.drawable.p12), P13("p13", R.drawable.p13);

    companion object {
        // Retrieve a card by its string value, e.g., "c1", "o2", etc.
        fun fromValue(value: String): CardFile? {
            return CardFile.entries.find { it.value == value }
        }
    }
}


