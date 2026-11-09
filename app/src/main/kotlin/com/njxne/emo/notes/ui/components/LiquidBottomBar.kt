package com.njxne.emo.notes.ui.components

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.BasicText
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.kyant.backdrop.Backdrop
import com.kyant.backdrop.drawBackdrop
import com.kyant.backdrop.effects.blur
import com.kyant.backdrop.effects.lens
import com.kyant.backdrop.effects.vibrancy
import com.kyant.shapes.Capsule
import kotlin.math.roundToInt

data class BottomTabItem(
    val icon: ImageVector,
    val label: String
)

@Composable
fun LiquidBottomBar(
    items: List<BottomTabItem>,
    selectedTabIndex: () -> Int,
    onTabSelected: (index: Int) -> Unit,
    backdrop: Backdrop,
    modifier: Modifier = Modifier
) {
    val tabsCount = items.size
    val isLight = !isSystemInDarkTheme()
    val accent = if (isLight) Color(0xFF0088FF) else Color(0xFF3FA9FF)
    val containerColor =
        if (isLight) Color(0xFFFAFAFA).copy(0.4f) else Color(0xFF121212).copy(0.4f)
    val contentColor = if (isLight) Color(0xFF1A1C20) else Color(0xFFF5F5F5)

    val density = LocalDensity.current
    val indicatorPos = remember { Animatable(0f) }

    LaunchedEffect(selectedTabIndex()) {
        indicatorPos.animateTo(selectedTabIndex().toFloat(), spring(1f, 350f, 0.5f))
    }

    BoxWithConstraints(modifier, contentAlignment = Alignment.CenterStart) {
        val tabWidthDp =
            with(density) { ((constraints.maxWidth - 8.dp.toPx()) / tabsCount).toDp() }
        val indicatorX = with(density) {
            (4.dp + tabWidthDp * indicatorPos.value).toPx().roundToInt()
        }

        Box(
            Modifier
                .drawBackdrop(
                    backdrop = backdrop,
                    shape = { Capsule() },
                    effects = {
                        vibrancy()
                        blur(8f.dp.toPx())
                        lens(24f.dp.toPx(), 24f.dp.toPx())
                    },
                    onDrawSurface = { drawRect(containerColor) }
                )
                .height(64f.dp)
                .fillMaxWidth()
                .padding(4f.dp)
        )

        Box(
            Modifier
                .offset { IntOffset(indicatorX, 0) }
                .drawBackdrop(
                    backdrop = backdrop,
                    shape = { Capsule() },
                    effects = {
                        vibrancy()
                        blur(4f.dp.toPx())
                        lens(10f.dp.toPx(), 14f.dp.toPx(), chromaticAberration = true)
                    },
                    onDrawSurface = { drawRect(accent.copy(alpha = 0.16f)) }
                )
                .height(56f.dp)
                .width(tabWidthDp)
        )

        Row(
            Modifier
                .height(64f.dp)
                .fillMaxWidth()
                .padding(4f.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            repeat(tabsCount) { index ->
                Box(
                    Modifier
                        .weight(1f)
                        .fillMaxHeight()
                        .clickable(onClick = { onTabSelected(index) })
                ) {
                    LiquidTab(
                        item = items[index],
                        selected = selectedTabIndex() == index,
                        contentColor = contentColor,
                        accent = accent,
                    )
                }
            }
        }
    }
}

@Composable
private fun LiquidTab(
    item: BottomTabItem,
    selected: Boolean,
    contentColor: Color,
    accent: Color,
) {
    val scale by animateFloatAsState(
        targetValue = if (selected) 1.15f else 1f,
        animationSpec = spring(0.5f, 300f, 0.01f),
        label = "tabScale"
    )
    val color by animateColorAsState(
        targetValue = if (selected) accent else contentColor,
        animationSpec = spring<Color>(dampingRatio = 0.5f, stiffness = 300f),
        label = "tabColor"
    )
    Column(
        Modifier
            .scale(scale)
            .padding(vertical = 8.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        Image(
            item.icon,
            null,
            Modifier.size(26.dp),
            colorFilter = ColorFilter.tint(color)
        )
        BasicText(
            text = item.label,
            style = TextStyle(
                fontSize = 11.sp,
                color = color,
                textAlign = TextAlign.Center
            ),
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
    }
}
