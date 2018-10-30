
data class Size(val width: Double, val height: Double) {
    constructor(width: Int, height: Int) : this(width.toDouble(), height.toDouble())
    operator fun times(multiplier: Double): Size = Size(width * multiplier, height * multiplier)
}

data class Particles(
    val size: Int,
    val duration: Int,
    val palette: List<String>,
    var total: Int = 0,
    var elements: List<Element> = emptyList()
)

data class Timing(var delay: Double = 0.0, var start: Double = 0.0)
data class KeyFrame(val x: Pair<Double, Double>, val y: Pair<Double, Double>, val opacity: Pair<Double, Double>)
data class Element(val color: String, val keyframes: KeyFrame, val timing: Timing)

