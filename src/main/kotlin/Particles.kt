import org.w3c.dom.HTMLCanvasElement
import kotlin.browser.document
import kotlin.browser.window
import kotlin.math.min
import kotlin.math.roundToInt
import kotlin.random.Random

val canvas : HTMLCanvasElement = document.body?.firstElementChild!! as HTMLCanvasElement
val params : Any = js("{alpha: true}") as Any
val context = canvas.getContext("2d", params)!!

lateinit var canvasSize: Size

var lastTick: Double = 0.0

val particles = Particles(
    size = 8,
    duration = 5000,
    palette = listOf(
        "616AFF",
        "2DBAE7",
        "FFBF00",
        "48DC6B",
        "FFFFFF",
        "FC6E3F"
    )
)

fun updateCanvasSize() {
    val innerWidth = window.innerWidth
    val innerHeight = window.innerHeight

    canvas.style.width = "${innerWidth}px"
    canvas.style.height = "${innerHeight}px"

    canvasSize = Size(innerWidth, innerHeight) * window.devicePixelRatio

    canvas.width = canvasSize.width.toInt()
    canvas.height = canvasSize.height.toInt()

    reset()
}

fun updatePosition(canvasDimension: Double) =
    canvasDimension / 2 - particles.size.toDouble() / 2 to Random.nextFloat() * (canvasDimension - particles.size)

fun createParticles() {
    val (_, duration, palette) = particles
    val limit = canvasSize.width * canvasSize.height / 500
    val total = min(7000, limit.roundToInt())
    val elements = mutableListOf<Element>()

    for (index in 0..total) {
        val color = "#${palette.random()}"
        val keyframes = KeyFrame(
            x = updatePosition(canvasSize.width),
            y = updatePosition(canvasSize.height),
            opacity = 1.0 to 0.0
        )
        val timing = Timing(delay = (index / total.toDouble()) * duration)
        elements.add(Element(color, keyframes, timing))
    }

    particles.total = total
    particles.elements = elements
}

fun clearCanvas() {
    val width = canvasSize.width
    val height = canvasSize.height
    context.fillStyle = "rgb(5, 0, 15)"
    context.fillRect(0.0, 0.0, width, height)
}

fun reset() {
    clearCanvas()
    createParticles()
}

fun tick(now: Double) {
    context.globalAlpha = .1
    clearCanvas()

    val (size, duration, _, _, elements) = particles
    for ((color, keyframes, timing) in elements) {
        if (timing.start == 0.0) timing.start = now
        val elapsed = now - timing.start
        if (timing.delay > 0) {
            if (elapsed > timing.delay) {
                timing.delay = 0.0
                timing.start = now
            }
            continue
        }

        val progress = min(elapsed / duration.toDouble(), 1.0)
        val opacity = interpolate(keyframes.opacity, progress)
        val x = interpolate(keyframes.x, progress)
        val y = interpolate(keyframes.y, progress)
        context.globalAlpha = opacity
        context.fillStyle = color
        context.fillRect(x, y, size.toDouble(), size.toDouble())
        if (progress == 1.0) timing.start = now
    }

    updateFps(now - lastTick)
    lastTick = now

    window.requestAnimationFrame(::tick)
}

fun updateFps(delta: Double) {
    val fps = 1000 / delta
    document.getElementById("fps")?.textContent = "$fps fps"
}

fun main(args: Array<String>) {
    document.onvisibilitychange = { if (!document.isHidden) reset() }
    updateCanvasSize()
    window.onresize = { updateCanvasSize() }
    window.requestAnimationFrame(::tick)
}
