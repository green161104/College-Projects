package ipp.estg.mobile.data.enums

enum class CareExperience(val value: String) {
    Beginner("Beginner"),
    Intermediate("intermediate"),
    Expert("Expert");

    companion object {
        fun from(value: String): CareExperience {
            return when (value) {
                "Beginner" -> Beginner
                "intermediate" -> Intermediate
                "Expert" -> Expert
                else -> throw IllegalArgumentException()
            }
        }
    }
}