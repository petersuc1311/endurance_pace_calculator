package dev.psuchanek.endurancepacecalculator.models

data class Split(
    val splitDistance: Float,
    val splitTime: String,
    val splitTotalDistance: Float,
    val totalTime: String
) {
    val getSplitDistance: String
        get() = "$splitDistance"

    val getSplitTime: String
        get() = splitTime

    val getTotalDistance: String
        get() = "$splitTotalDistance"

    val getTotalTime: String
        get() = totalTime
}
