package com.krishan.composePractice.ui

/*
 * Copyright 2025 Kyriakos Georgiopoulos
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
import android.annotation.SuppressLint
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.animateIntAsState
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.gestures.detectVerticalDragGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Timeline
import androidx.compose.material.icons.filled.WaterDrop
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.clipPath
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.lerp
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.launch
import kotlin.math.abs
import kotlin.math.roundToInt

private val TrackTopInset = 32.dp
private val TrackBottomInset = 32.dp
private val PrimaryBlue = Color(0xFF2930FF)

private val DryRed = Color(0xFFFF3366)
private val ComfortBlue = Color(0xFF2F7BFF)

/**
 * Maps a humidity percentage to a vivid color with smooth transitions:
 *
 * - 10–30%  : red
 * - 30–40%  : red → blue
 * - 40–65%  : blue
 * - 65–75%  : blue → red
 * - 75–100% : red
 *
 * @param humidity Humidity percentage in the range [0, 100].
 * @return A color representing the qualitative humidity state.
 */
private fun humidityToColor(humidity: Float): Color {
    val clamped = humidity.coerceIn(0f, 100f)

    val lowRedEnd = 30f
    val blueStart = 40f
    val blueEnd = 65f
    val highRedStart = 75f

    fun smoothStep(t: Float): Float {
        val x = t.coerceIn(0f, 1f)
        return x * x * (3f - 2f * x)
    }

    return when {
        clamped <= lowRedEnd -> DryRed
        clamped < blueStart -> {
            val tRaw = (clamped - lowRedEnd) / (blueStart - lowRedEnd)
            val t = smoothStep(tRaw)
            lerp(DryRed, ComfortBlue, t)
        }

        clamped <= blueEnd -> ComfortBlue
        clamped < highRedStart -> {
            val tRaw = (clamped - blueEnd) / (highRedStart - blueEnd)
            val t = smoothStep(tRaw)
            lerp(ComfortBlue, DryRed, t)
        }

        else -> DryRed
    }
}

/**
 * Top-level humidity control screen.
 *
 * Composes the humidity control, top fade overlay and bottom navigation bar.
 */
@Composable
fun HumidityControlScreen() {
    var targetHumidity by remember { mutableStateOf(60f) }
    val roomHumidity = 84f
    val background = Color(0xFFF5F5FA)

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(background)
    ) {
        Surface(
            modifier = Modifier
                .fillMaxSize()
                .padding(
                    start = 24.dp,
                    end = 24.dp,
                    top = 24.dp,
                    bottom = 88.dp
                ),
            color = Color.Transparent
        ) {
            HumidityControl(
                humiditySetPoint = targetHumidity,
                roomHumidity = roomHumidity,
                onHumidityChange = { targetHumidity = it.coerceIn(10f, 100f) }
            )
        }

        TopStatusBarFade(
            modifier = Modifier.align(Alignment.TopCenter),
            background = background
        )

        BottomNavBar(
            modifier = Modifier.align(Alignment.BottomCenter)
        )
    }
}

/**
 * Faded overlay behind the status bar area.
 *
 * @param modifier Modifier applied to the container.
 * @param background Base background color to fade from.
 */
@Composable
private fun TopStatusBarFade(
    modifier: Modifier = Modifier,
    background: Color
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(56.dp)
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(
                        background,
                        background.copy(alpha = 0f)
                    )
                )
            )
    )
}

/**
 * Main humidity control layout tying together scale, track and info panel.
 *
 * @param humiditySetPoint Current target humidity level.
 * @param roomHumidity Current measured room humidity.
 * @param onHumidityChange Callback when the target humidity changes.
 * @param minHumidity Minimum allowed humidity value.
 * @param maxHumidity Maximum allowed humidity value.
 */
@SuppressLint("UnusedBoxWithConstraintsScope")
@Composable
fun HumidityControl(
    humiditySetPoint: Float,
    roomHumidity: Float,
    onHumidityChange: (Float) -> Unit,
    minHumidity: Float = 10f,
    maxHumidity: Float = 100f
) {
    val animatedHumidityForScale by animateFloatAsState(
        targetValue = humiditySetPoint,
        animationSpec = spring(
            stiffness = Spring.StiffnessLow,
            dampingRatio = 0.9f
        ),
        label = "humiditySpringScale"
    )

    val displayedSetPoint by animateIntAsState(
        targetValue = humiditySetPoint.roundToInt(),
        animationSpec = tween(durationMillis = 380, easing = FastOutSlowInEasing),
        label = "humidityNumber"
    )

    BoxWithConstraints(
        modifier = Modifier.fillMaxSize()
    ) {
        val scaleWidth = 72.dp
        val spacerBetweenScaleAndTrack = 16.dp
        val trackWidth = 110.dp

        Row(
            modifier = Modifier.fillMaxSize(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            VerticalHumidityScale(
                modifier = Modifier
                    .width(scaleWidth)
                    .fillMaxHeight(),
                humidity = animatedHumidityForScale,
                minHumidity = minHumidity,
                maxHumidity = maxHumidity
            )

            Spacer(Modifier.width(spacerBetweenScaleAndTrack))

            CurvedTrackWithThumb(
                modifier = Modifier
                    .width(trackWidth)
                    .fillMaxHeight(),
                humidity = humiditySetPoint,
                onHumidityChange = onHumidityChange,
                minHumidity = minHumidity,
                maxHumidity = maxHumidity
            )

            Spacer(Modifier.width(24.dp))

            RightInfoPanel(
                modifier = Modifier.weight(1.3f),
                roomHumidity = roomHumidity.roundToInt(),
                setPointHumidity = displayedSetPoint
            )
        }
    }
}

/**
 * Vertical humidity scale with tick labels and emphasis around the current value.
 *
 * @param modifier Modifier applied to the scale container.
 * @param humidity Current humidity used to highlight nearby ticks.
 * @param minHumidity Minimum scale value.
 * @param maxHumidity Maximum scale value.
 */
@SuppressLint("UnusedBoxWithConstraintsScope")
@Composable
private fun VerticalHumidityScale(
    modifier: Modifier,
    humidity: Float,
    minHumidity: Float,
    maxHumidity: Float
) {
    val ticks = (minHumidity.toInt()..maxHumidity.toInt() step 10).toList()

    BoxWithConstraints(
        modifier = modifier,
        contentAlignment = Alignment.TopStart
    ) {
        val density = LocalDensity.current
        val topInsetPx = with(density) { TrackTopInset.toPx() }
        val bottomInsetPx = with(density) { TrackBottomInset.toPx() }
        val range = maxHumidity - minHumidity

        Layout(
            content = {
                ticks.forEach { value ->
                    val distance = abs(humidity - value)
                    val influence = (1f - distance / 8f).coerceIn(0f, 1f)

                    val baseColor = Color(0xFF9EA3B5)
                    val selectedColor = Color(0xFF1C4CFF)
                    val color = lerp(baseColor, selectedColor, influence)

                    val baseSize = 21.sp
                    val selectedExtra = 12.sp
                    val fontSize = (baseSize.value + selectedExtra.value * influence).sp
                    val fontWeight =
                        if (influence > 0.55f) FontWeight.Bold else FontWeight.Medium

                    Text(
                        text = "$value%",
                        color = color,
                        fontSize = fontSize,
                        fontWeight = fontWeight,
                        modifier = Modifier.padding(vertical = 2.dp)
                    )
                }
            },
            measurePolicy = { measurables, constraints ->
                val placeables = measurables.map { measurable ->
                    measurable.measure(constraints.copy(minHeight = 0))
                }

                val width = placeables.maxOfOrNull { it.width } ?: 0
                val height = constraints.maxHeight
                val usableHeightPx =
                    (height.toFloat() - topInsetPx - bottomInsetPx).coerceAtLeast(1f)

                layout(width, height) {
                    ticks.indices.forEach { index ->
                        val value = ticks[index]
                        val placeable = placeables[index]

                        val fraction =
                            ((value - minHumidity) / range).coerceIn(0f, 1f)
                        val yCenter =
                            topInsetPx + usableHeightPx * (1f - fraction)

                        val y = (yCenter - placeable.height / 2f)
                            .roundToInt()
                            .coerceIn(0, height - placeable.height)

                        placeable.placeRelative(0, y)
                    }
                }
            }
        )
    }
}

/**
 * Curved vertical track with an interactive thumb that controls the humidity set-point.
 *
 * @param modifier Modifier applied to the track container.
 * @param humidity Current humidity set-point.
 * @param onHumidityChange Callback when humidity changes due to user interaction.
 * @param minHumidity Minimum allowed humidity value.
 * @param maxHumidity Maximum allowed humidity value.
 */
@Composable
private fun CurvedTrackWithThumb(
    modifier: Modifier,
    humidity: Float,
    onHumidityChange: (Float) -> Unit,
    minHumidity: Float,
    maxHumidity: Float
) {
    BoxWithConstraints(
        modifier = modifier,
        contentAlignment = Alignment.Center
    ) {
        val density = LocalDensity.current
        val heightPx = with(density) { maxHeight.toPx() }
        val widthPx = with(density) { maxWidth.toPx() }

        val range = maxHumidity - minHumidity
        val thumbRadiusPx = with(density) { 24.dp.toPx() }

        val topInsetPx = with(density) { TrackTopInset.toPx() }
        val bottomInsetPx = with(density) { TrackBottomInset.toPx() }
        val usableHeightPx = (heightPx - topInsetPx - bottomInsetPx).coerceAtLeast(1f)

        val thumbCenterX = widthPx / 2f
        val lineOffset = with(density) { 8.dp.toPx() }
        val borderX = thumbCenterX - lineOffset

        fun humidityToY(hum: Float): Float {
            val fraction = ((hum - minHumidity) / range).coerceIn(0f, 1f)
            return topInsetPx + usableHeightPx * (1f - fraction)
        }

        fun yToHumidity(y: Float): Float {
            val clampedY = y.coerceIn(topInsetPx, heightPx - bottomInsetPx)
            val frac = 1f - (clampedY - topInsetPx) / usableHeightPx
            return (minHumidity + frac * range).coerceIn(minHumidity, maxHumidity)
        }

        val scope = rememberCoroutineScope()
        var dragging by remember { mutableStateOf(false) }

        val thumbY = remember {
            Animatable(humidityToY(humidity.coerceIn(minHumidity, maxHumidity)))
        }

        LaunchedEffect(humidity) {
            if (!dragging) {
                thumbY.animateTo(
                    targetValue = humidityToY(humidity),
                    animationSpec = spring(
                        stiffness = Spring.StiffnessMediumLow,
                        dampingRatio = 0.9f
                    )
                )
            }
        }

        val thumbColor = humidityToColor(humidity)

        Box(
            modifier = Modifier
                .fillMaxSize()
                .pointerInput(heightPx, minHumidity, maxHumidity, range) {
                    detectTapGestures { offset ->
                        val newY = offset.y.coerceIn(topInsetPx, heightPx - bottomInsetPx)
                        val newHumidity = yToHumidity(newY)
                        scope.launch { thumbY.snapTo(newY) }
                        onHumidityChange(newHumidity)
                    }
                }
                .pointerInput(heightPx, minHumidity, maxHumidity, range) {
                    detectVerticalDragGestures(
                        onDragStart = {
                            dragging = true
                            scope.launch { thumbY.stop() }
                        },
                        onVerticalDrag = { change, dragAmount ->
                            val newY = (thumbY.value + dragAmount)
                                .coerceIn(topInsetPx, heightPx - bottomInsetPx)

                            scope.launch { thumbY.snapTo(newY) }

                            val newHumidity = yToHumidity(newY)
                            onHumidityChange(newHumidity)

                            change.consume()
                        },
                        onDragEnd = { dragging = false },
                        onDragCancel = { dragging = false }
                    )
                }
        ) {
            val thumbCenterYPx = thumbY.value

            TrackBackground(
                modifier = Modifier.fillMaxSize(),
                thumbCenterYPx = thumbCenterYPx,
                borderX = borderX,
                minHumidity = minHumidity,
                maxHumidity = maxHumidity
            )

            Thumb(
                modifier = Modifier
                    .align(Alignment.TopStart)
                    .graphicsLayer {
                        translationY = thumbCenterYPx - thumbRadiusPx
                        translationX = thumbCenterX - thumbRadiusPx
                    },
                color = thumbColor
            )
        }
    }
}

/**
 * Painted background for the curved track, including the red/blue comfort band and border line.
 *
 * @param modifier Modifier applied to the canvas.
 * @param thumbCenterYPx Current thumb center Y in pixels, used as curve anchor.
 * @param borderX X coordinate of the main track border.
 * @param minHumidity Minimum humidity represented by the track.
 * @param maxHumidity Maximum humidity represented by the track.
 */
@Composable
private fun TrackBackground(
    modifier: Modifier,
    thumbCenterYPx: Float,
    borderX: Float,
    minHumidity: Float,
    maxHumidity: Float
) {
    Canvas(modifier = modifier) {
        val w = size.width
        val h = size.height

        val totalSpan = minOf(h * 0.85f, 320f)
        val yTop = (thumbCenterYPx - totalSpan / 2f).coerceIn(0f, h)
        val yBottom = (thumbCenterYPx + totalSpan / 2f).coerceIn(0f, h)
        val span = yBottom - yTop

        val rightCurveDepth = minOf(w * 0.35f, 80f)
        val bandWidth = minOf(w * 0.55f, borderX)
        val leftBaseX = borderX - bandWidth
        val lineWidth = 3.5.dp.toPx()

        val bandRightX = (borderX - lineWidth / 2f)
            .coerceAtLeast(leftBaseX + 4f)

        val backgroundColor = Color(0xFFF5F5FA)

        val linePath = Path().apply {
            moveTo(borderX, 0f)
            lineTo(borderX, yTop)
            cubicTo(
                borderX, yTop + span * 0.25f,
                borderX - rightCurveDepth, thumbCenterYPx - span * 0.25f,
                borderX - rightCurveDepth, thumbCenterYPx
            )
            cubicTo(
                borderX - rightCurveDepth, thumbCenterYPx + span * 0.25f,
                borderX, yBottom - span * 0.25f,
                borderX, yBottom
            )
            lineTo(borderX, h)
        }

        val bandPath = Path().apply {
            moveTo(leftBaseX, 0f)
            lineTo(bandRightX, 0f)
            lineTo(bandRightX, yTop)
            cubicTo(
                bandRightX, yTop + span * 0.25f,
                bandRightX - rightCurveDepth, thumbCenterYPx - span * 0.25f,
                bandRightX - rightCurveDepth, thumbCenterYPx
            )
            cubicTo(
                bandRightX - rightCurveDepth, thumbCenterYPx + span * 0.25f,
                bandRightX, yBottom - span * 0.25f,
                bandRightX, yBottom
            )
            lineTo(bandRightX, h)
            lineTo(leftBaseX, h)
            lineTo(leftBaseX, 0f)
            close()
        }

        fun humidityToPos(hum: Float): Float {
            val frac = ((hum - minHumidity) / (maxHumidity - minHumidity))
                .coerceIn(0f, 1f)
            return 1f - frac
        }

        val blueTopHum = 65f
        val blueBottomHum = 40f

        val blueTop = humidityToPos(blueTopHum)
        val blueBottom = humidityToPos(blueBottomHum)

        val blendSpan = 0.10f

        val blueTopStart = (blueTop - blendSpan).coerceIn(0f, 1f)
        val blueBottomEnd = (blueBottom + blendSpan).coerceIn(0f, 1f)

        val red = Color(0xFFFF5B6C)
        val blue = Color(0xFF4A6CFF)

        val pastelRed = lerp(Color.White, red, 0.28f)
        val pastelBlue = lerp(Color.White, blue, 0.28f)

        val centerFadeAlpha = 0.44f
        val edgeAlpha = 0.72f
        val outerAlpha = 0.45f

        val fillStops = arrayOf(
            0f to pastelRed.copy(alpha = outerAlpha),
            blueTopStart to pastelRed.copy(alpha = edgeAlpha),
            blueTop to pastelBlue.copy(alpha = centerFadeAlpha),
            blueBottom to pastelBlue.copy(alpha = centerFadeAlpha),
            blueBottomEnd to pastelRed.copy(alpha = edgeAlpha),
            1f to pastelRed.copy(alpha = outerAlpha)
        )

        drawPath(
            path = bandPath,
            brush = Brush.verticalGradient(colorStops = fillStops)
        )

        clipPath(bandPath) {
            drawRect(
                brush = Brush.horizontalGradient(
                    colors = listOf(
                        backgroundColor,
                        backgroundColor.copy(alpha = 0.15f),
                        Color.Transparent
                    ),
                    startX = leftBaseX,
                    endX = bandRightX
                ),
                topLeft = Offset(leftBaseX, 0f),
                size = Size(bandRightX - leftBaseX, h)
            )
        }

        clipPath(bandPath) {
            val topFadeHeight = h * 0.14f
            drawRect(
                brush = Brush.verticalGradient(
                    colors = listOf(
                        backgroundColor,
                        Color.Transparent
                    ),
                    startY = 0f,
                    endY = topFadeHeight
                ),
                topLeft = Offset(leftBaseX, 0f),
                size = Size(bandRightX - leftBaseX, topFadeHeight)
            )

            val bottomFadeHeight = h * 0.16f
            drawRect(
                brush = Brush.verticalGradient(
                    colors = listOf(
                        Color.Transparent,
                        backgroundColor
                    ),
                    startY = h - bottomFadeHeight,
                    endY = h
                ),
                topLeft = Offset(leftBaseX, h - bottomFadeHeight),
                size = Size(bandRightX - leftBaseX, bottomFadeHeight)
            )
        }

        drawRect(
            color = backgroundColor,
            topLeft = Offset(borderX - lineWidth / 2f, 0f),
            size = Size(lineWidth, h)
        )

        val lineStops = arrayOf(
            0f to red,
            blueTopStart to red,
            blueTop to blue,
            blueBottom to blue,
            blueBottomEnd to red,
            1f to red
        )

        val lineBrush = Brush.verticalGradient(colorStops = lineStops)

        drawPath(
            path = linePath,
            brush = lineBrush,
            style = Stroke(width = lineWidth)
        )
    }
}

/**
 * Circular thumb used as the draggable control on the curved track.
 *
 * @param modifier Modifier applied to the thumb container.
 * @param color Base color for the thumb and its glow.
 */
@Composable
private fun Thumb(
    modifier: Modifier,
    color: Color
) {
    Box(
        modifier = modifier
            .size(48.dp)
            .shadow(elevation = 8.dp, shape = CircleShape, clip = false)
            .clip(CircleShape)
            .background(
                brush = Brush.radialGradient(
                    colors = listOf(
                        color,
                        lerp(color, Color.Black, 0.35f)
                    )
                )
            )
            .drawBehind {
                drawCircle(
                    color = color,
                    radius = size.minDimension / 2f + 10.dp.toPx(),
                    center = center,
                    alpha = 0.25f
                )
            },
        contentAlignment = Alignment.Center
    ) {
        Canvas(Modifier.size(18.dp)) {
            val w = size.width
            val h = size.height
            val cx = w / 2f
            val cy = h / 2f

            val gap = h * 0.22f
            val arrowHeight = (h - gap) / 2f
            val arrowWidth = w * 0.5f

            val upBaseY = cy - gap / 2f
            val upTipY = upBaseY - arrowHeight

            val downBaseY = cy + gap / 2f
            val downTipY = downBaseY + arrowHeight

            val upPath = Path().apply {
                moveTo(cx, upTipY)
                lineTo(cx - arrowWidth / 2f, upBaseY)
                lineTo(cx + arrowWidth / 2f, upBaseY)
                close()
            }
            drawPath(upPath, color = Color.White)

            val downPath = Path().apply {
                moveTo(cx, downTipY)
                lineTo(cx - arrowWidth / 2f, downBaseY)
                lineTo(cx + arrowWidth / 2f, downBaseY)
                close()
            }
            drawPath(downPath, color = Color.White)
        }
    }
}

/**
 * Right-side panel with textual metrics and contextual guidance for humidity.
 *
 * @param modifier Modifier applied to the container.
 * @param roomHumidity Current measured room humidity.
 * @param setPointHumidity Current target humidity set-point.
 */
@Composable
private fun RightInfoPanel(
    modifier: Modifier,
    roomHumidity: Int,
    setPointHumidity: Int
) {
    val textMuted = Color(0xFFB0B3C5)

    Column(
        modifier = modifier.padding(horizontal = 8.dp),
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "Environment",
            fontSize = 12.sp,
            color = textMuted,
            fontWeight = FontWeight.Medium
        )

        Spacer(Modifier.height(16.dp))

        MetricRow(
            label = "Return temperature",
            value = "20",
            unit = "°C",
            emphasize = false,
            highlight = false
        )

        Spacer(Modifier.height(24.dp))

        PrimaryHumidityBlock(
            roomHumidity = roomHumidity,
            setPointHumidity = setPointHumidity
        )

        Spacer(Modifier.height(24.dp))

        MetricRow(
            label = "Absolute humidity",
            value = "4",
            unit = "gr/ft³",
            emphasize = false,
            highlight = false
        )

        Spacer(Modifier.height(24.dp))

        ComfortNote(roomHumidity = roomHumidity)

        Spacer(Modifier.height(16.dp))

        WarningBlock()
    }
}

/**
 * Primary humidity section showing current humidity, set-point and a comfort indicator.
 *
 * @param roomHumidity Current measured room humidity.
 * @param setPointHumidity Current target humidity set-point.
 */
@Composable
private fun PrimaryHumidityBlock(
    roomHumidity: Int,
    setPointHumidity: Int
) {
    val textMuted = Color(0xFFB0B3C5)
    val valueColor = humidityToColor(roomHumidity.toFloat())

    Text(
        text = "Current humidity",
        fontSize = 12.sp,
        color = textMuted,
        fontWeight = FontWeight.Medium
    )

    Row(
        verticalAlignment = Alignment.Bottom
    ) {
        Text(
            text = roomHumidity.toString(),
            fontSize = 62.sp,
            color = valueColor,
            fontWeight = FontWeight.ExtraBold
        )
        Spacer(Modifier.width(2.dp))
        Text(
            text = "%",
            fontSize = 22.sp,
            color = valueColor,
            fontWeight = FontWeight.SemiBold,
            modifier = Modifier.offset(y = (-6).dp)
        )
    }

    Spacer(Modifier.height(4.dp))

    Text(
        text = "Set-point $setPointHumidity%",
        fontSize = 13.sp,
        color = textMuted,
        fontWeight = FontWeight.Medium
    )

    Spacer(Modifier.height(12.dp))

    ComfortChip(roomHumidity = roomHumidity)
}

/**
 * Simple metric row showing a labeled value with a unit.
 *
 * @param label Metric label text.
 * @param value Metric value text.
 * @param unit Metric unit text.
 * @param emphasize Whether the value should be visually emphasized.
 * @param highlight Whether the value should use the primary highlight color.
 */
@Composable
private fun MetricRow(
    label: String,
    value: String,
    unit: String,
    emphasize: Boolean,
    highlight: Boolean
) {
    val textMuted = Color(0xFFB0B3C5)
    val textDark = Color(0xFF232849)
    val primaryBlue = Color(0xFF2930FF)

    Text(
        text = label,
        fontSize = 12.sp,
        color = textMuted,
        fontWeight = FontWeight.Medium
    )
    Row(verticalAlignment = Alignment.Bottom) {
        Text(
            text = value,
            fontSize = if (emphasize) 32.sp else 22.sp,
            color = if (highlight) primaryBlue else textDark,
            fontWeight = if (emphasize) FontWeight.SemiBold else FontWeight.Medium
        )
        Spacer(Modifier.width(4.dp))
        Text(
            text = unit,
            fontSize = if (emphasize) 16.sp else 14.sp,
            color = if (highlight) primaryBlue else textDark,
            fontWeight = FontWeight.Medium
        )
    }
}

/**
 * Chip indicating whether the current humidity is within the comfort range.
 *
 * @param roomHumidity Current measured room humidity.
 */
@Composable
private fun ComfortChip(roomHumidity: Int) {
    val primaryBlue = Color(0xFF2930FF)
    val textMuted = Color(0xFFB0B3C5)

    val inRange = roomHumidity in 40..65
    val label = if (inRange) "Within comfort range" else "Outside comfort range"
    val bgColor =
        if (inRange) primaryBlue.copy(alpha = 0.10f) else Color(0xFFFFC107).copy(alpha = 0.12f)
    val textColor = if (inRange) primaryBlue else Color(0xFFB26A00)

    Row(
        modifier = Modifier
            .clip(RoundedCornerShape(999.dp))
            .background(bgColor)
            .padding(horizontal = 10.dp, vertical = 4.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(6.dp)
                .clip(CircleShape)
                .background(textColor)
        )
        Spacer(Modifier.width(6.dp))
        Text(
            text = label,
            fontSize = 11.sp,
            color = textColor,
            fontWeight = FontWeight.Medium
        )
    }

    Spacer(Modifier.height(6.dp))

    Text(
        text = "Comfort range 40–65%",
        fontSize = 11.sp,
        color = textMuted,
        fontWeight = FontWeight.Medium
    )
}

/**
 * Short explanatory note suggesting actions based on the current humidity.
 *
 * @param roomHumidity Current measured room humidity.
 */
@Composable
private fun ComfortNote(roomHumidity: Int) {
    val textMuted = Color(0xFFB0B3C5)
    val note = if (roomHumidity in 40..65) {
        "Humidity is within the recommended comfort range."
    } else if (roomHumidity < 40) {
        "Air is on the dry side. Consider increasing the set-point."
    } else {
        "Air is quite humid. Consider lowering the set-point."
    }

    Text(
        text = note,
        fontSize = 11.sp,
        color = textMuted,
        lineHeight = 14.sp
    )
}

/**
 * Warning block describing safe humidity set-point ranges.
 */
@Composable
private fun WarningBlock() {
    val textMuted = Color(0xFFB0B3C5)

    Row(
        verticalAlignment = Alignment.Top
    ) {
        Text(
            text = "⚠",
            fontSize = 14.sp,
            color = textMuted,
            modifier = Modifier.padding(top = 2.dp, end = 6.dp)
        )
        Text(
            text = "Extreme humidity levels.\n" +
                    "Use precaution for set-points\n" +
                    "outside of 20–55%.",
            fontSize = 11.sp,
            color = textMuted,
            lineHeight = 14.sp
        )
    }
}

/**
 * Bottom navigation bar for the humidity control screen.
 *
 * @param modifier Modifier applied to the navigation bar container.
 */
@Composable
private fun BottomNavBar(
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 32.dp, vertical = 16.dp),
        color = Color.White,
        shadowElevation = 12.dp,
        shape = RoundedCornerShape(26.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 28.dp, vertical = 12.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            BottomNavItem(
                icon = Icons.Filled.Timeline,
                contentDescription = "History",
                active = false
            )
            BottomNavItem(
                icon = Icons.Filled.WaterDrop,
                contentDescription = "Humidity",
                active = true
            )
            BottomNavItem(
                icon = Icons.Filled.Home,
                contentDescription = "Home",
                active = false
            )
        }
    }
}

/**
 * Single bottom navigation item.
 *
 * @param icon Icon displayed for this item.
 * @param contentDescription Accessibility description for the icon.
 * @param active Whether this item is currently selected.
 */
@Composable
private fun BottomNavItem(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    contentDescription: String,
    active: Boolean
) {
    val bgColor = if (active) PrimaryBlue else Color.Transparent
    val iconColor = if (active) Color.White else PrimaryBlue.copy(alpha = 0.55f)

    Box(
        modifier = Modifier
            .size(36.dp)
            .clip(CircleShape)
            .background(bgColor),
        contentAlignment = Alignment.Center
    ) {
        Icon(
            imageVector = icon,
            contentDescription = contentDescription,
            tint = iconColor,
            modifier = Modifier.size(22.dp)
        )
    }
}

/**
 * Design-time preview of [HumidityControlScreen].
 */
@Preview(showBackground = true, backgroundColor = 0xFFF5F5FA)
@Composable
private fun HumidityControlPreview() {
    MaterialTheme {
        HumidityControlScreen()
    }
}