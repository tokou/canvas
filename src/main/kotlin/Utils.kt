import org.w3c.dom.Document
import org.w3c.dom.RenderingContext
import org.w3c.dom.css.CSSStyleDeclaration
import org.w3c.dom.css.get
import org.w3c.dom.events.Event
import org.w3c.dom.get

var RenderingContext.fillStyle : dynamic
    set(value) { this.asDynamic().fillStyle = value }
    get() = this.asDynamic().fillStyle

var RenderingContext.globalAlpha : Double
    set(value) { this.asDynamic().globalAlpha = value }
    get() = this.asDynamic().globalAlpha as Double

fun RenderingContext.fillRect(x: Double, y: Double, width: Double, height: Double) {
    this.asDynamic().fillRect(x, y, width, height)
}

val Document.isHidden: Boolean get() = this["hidden"] as Boolean

var Document.onvisibilitychange: ((Event) -> dynamic)?
    get() = this["onvisibilitychange"] as? ((Event) -> dynamic)?
    set(value) = addEventListener("visibilitychange", value)

fun interpolate(bounds: Pair<Double, Double>, easing: Double): Double {
    val (from, to) = bounds
    return from + (to - from) * easing
}

