package dev.psuchanek.endurancepacecalculator.models

data class Split(
    val splitNumber: Int,
    val splitTime: String,
    val splitTotalDistance: Float,
    val totalTime: String
) {
    val getSplitNumber: String
        get() = "$splitNumber"

    val getSplitTime: String
        get() = splitTime

    val getTotalDistance: String
        get() = "$splitTotalDistance"

    val getTotalTime: String
        get() = totalTime
}
