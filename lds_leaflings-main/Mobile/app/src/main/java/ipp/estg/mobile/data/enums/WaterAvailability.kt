package ipp.estg.mobile.data.enums

enum class WaterAvailability(val value: String) {
    LOW("Low"),
    MEDIUM("Medium"),
    HIGH("High");

    companion object {
        fun from(value: String): WaterAvailability {
            return when (value) {
                "Low" -> LOW
                "Medium" -> MEDIUM
                "High" -> HIGH
                else -> throw IllegalArgumentException()
            }
        }
    }
}