const canvas = document.body.firstElementChild;
const context = canvas.getContext("2d", {alpha: false});

const canvasSize = {};
const updateCanvasSize = () => {
  const {innerWidth, innerHeight} = window;

  Object.assign(canvas.style, {
    width: `${innerWidth}px`,
    height: `${innerHeight}px`
  });

  Object.assign(canvasSize, {
    width: innerWidth * devicePixelRatio,
    height: innerHeight * devicePixelRatio
  });

  canvas.width = canvasSize.width;
  canvas.height = canvasSize.height;
  reset();
};

const random = (min, max) =>
  Math.random() * (max - min) + min;

const arrayRandom = array =>
  array[Math.floor(random(0, array.length))];

const getCurrentValue = ({from, to}, easing) =>
  from + (to - from) * easing;

const getKeyframePositions = size => ({
  from: canvasSize[size] / 2 - particles.size / 2,
  to: Math.round(random(0, canvasSize[size] - particles.size))
});

const particles = {
  size: 8,
  duration: 5000,
  palette: [
    "616AFF",
    "2DBAE7",
    "FFBF00",
    "48DC6B",
    "FFFFFF",
    "FC6E3F"
  ]
};

const createParticles = () => {
  const {size, duration, palette} = particles;
  const total = Math.min(7000, Math.round(canvasSize.width * canvasSize.height / 500));
  const elements = [];

  for (let index = 0; index < total; index++) {
    const color = `#${arrayRandom(palette)}`;
    const keyframes = {
      x: getKeyframePositions("width"),
      y: getKeyframePositions("height"),
      opacity: {
        from: 1,
        to: 0
      }
    };
    const timing = {
      delay: (index / total) * duration
    };
    elements.push({color, keyframes, timing});
  }

  particles.total = total;
  particles.elements = elements;
};

const clearCanvas = () => {
  const {width, height} = canvasSize;
  context.fillStyle = "rgb(5, 0, 15)";
  context.fillRect(0, 0, width, height);
};

const reset = () => {
  clearCanvas();
  createParticles();
};

var lastTick = 0

const tick = now => {
  context.globalAlpha = .1;
  clearCanvas();

  const {elements, size, duration} = particles;
  elements.forEach(({color, timing, keyframes}) => {
    if (!timing.start) timing.start = now;
    const elapsed = now - timing.start;

    if (timing.delay) {
      if (elapsed > timing.delay) {
        timing.delay = 0;
        timing.start = now;
      }
      return;
    }

    const progress = Math.min(elapsed / duration, 1);
    const opacity = getCurrentValue(keyframes.opacity, progress);
    const x = getCurrentValue(keyframes.x, progress);
    const y = getCurrentValue(keyframes.y, progress);
    context.globalAlpha = opacity;
    context.fillStyle = color;
    context.fillRect(x, y, size, size);
    if (progress == 1) timing.start = now;
  });

  const diff = now - lastTick
  document.getElementById("fps").innerHTML = `${1000 / diff} fps`
  lastTick = now

  requestAnimationFrame(tick);
};

document.addEventListener("visibilitychange", () => {
  document.hidden || reset();
});

updateCanvasSize();
window.addEventListener("resize", updateCanvasSize);

requestAnimationFrame(tick);
