package com.njxne.emo.notes.ui.theme

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.PathParser
import androidx.compose.ui.unit.dp

private fun buildIcon(name: String, vararg paths: Pair<String, Color>): ImageVector {
    val builder = ImageVector.Builder(
        name = name,
        defaultWidth = 24.dp,
        defaultHeight = 24.dp,
        viewportWidth = 24f,
        viewportHeight = 24f
    )
    paths.forEach { (pathData, color) ->
        builder.addPath(
            pathData = PathParser().parsePathString(pathData).toNodes(),
            fill = SolidColor(color)
        )
    }
    return builder.build()
}

object Icons {

    val Notes: ImageVector by lazy {
        buildIcon(
            "Notes",
            "M14 2H6c-1.1 0-1.99.9-1.99 2L4 20c0 1.1.89 2 1.99 2H18c1.1 0 2-.9 2-2V8l-6-6z" to Color.Black,
            "M18.5 9H13V3.5L18.5 9z" to Color.White,
            "M14 16H8v-2h6v2zm0-4H8v-2h6v2z" to Color.White
        )
    }

    val Star: ImageVector by lazy {
        buildIcon(
            "Star",
            "M12 17.27L18.18 21l-1.64-7.03L22 9.24l-7.19-.61L12 2 9.19 8.63 2 9.24l5.46 4.73L5.82 21z" to Color.Black
        )
    }

    val StarOutline: ImageVector by lazy {
        buildIcon(
            "StarOutline",
            "M22 9.24l-7.19-.62L12 2 9.19 8.63 2 9.24l5.46 4.73L5.82 21 12 17.27 18.18 21l-1.63-7.03L22 9.24zM12 15.4l-3.76 2.27 1-4.28-3.32-2.88 4.38-.38L12 6.1l1.71 4.04 4.38.38-3.32 2.88 1 4.28L12 15.4z" to Color.Black
        )
    }

    val Settings: ImageVector by lazy {
        buildIcon(
            "Settings",
            "M3 17v2h6v-2H3zM3 5v2h10V5H3zm10 16v-2h8v-2h-8v-2h-2v6h2zM7 9v2H3v2h4v2h2V9H7zm14 4v-2H11v2h10zm-6-4h2V7h4V5h-4V3h-2v6z" to Color.Black
        )
    }

    val Add: ImageVector by lazy {
        buildIcon(
            "Add",
            "M19 13h-6v6h-2v-6H5v-2h6V5h2v6h6v2z" to Color.Black
        )
    }

    val ArrowBack: ImageVector by lazy {
        buildIcon(
            "ArrowBack",
            "M20 11H7.83l5.59-5.59L12 4l-8 8 8 8 1.41-1.41L7.83 13H20v-2z" to Color.Black
        )
    }

    val Delete: ImageVector by lazy {
        buildIcon(
            "Delete",
            "M6 19c0 1.1.9 2 2 2h8c1.1 0 2-.9 2-2V7H6v12zM19 4h-3.5l-1-1h-5l-1 1H5v2h14V4z" to Color.Black
        )
    }
}
