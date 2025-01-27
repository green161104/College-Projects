package ipp.estg.mobile.data.enums

enum class MatchTypes(val value: String) {
    PERFECT("PerfectMatch"),
    AVERAGE("AverageMatch"),
    WEAK("WeakMatch");

    companion object {
        fun from(value: String): MatchTypes {
            return when (value) {
                "PerfectMatch" -> PERFECT
                "AverageMatch" -> AVERAGE
                "WeakMatch" -> WEAK
                else -> throw IllegalArgumentException()
            }
        }
    }
}