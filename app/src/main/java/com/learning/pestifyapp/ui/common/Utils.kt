package com.learning.pestifyapp.ui.common

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix
import android.os.Build
import android.util.Base64
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.grid.LazyGridState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.State
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.unit.IntSize
import com.learning.pestifyapp.ui.components.BottomBarState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.io.ByteArrayOutputStream
import java.text.ParseException
import java.text.SimpleDateFormat
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter
import java.util.Date
import java.util.Locale

fun truncateText(text: String, maxLength: Int): String {
    return if (text.length <= maxLength) text else text.substring(0, maxLength) + "..."
}

enum class ScrollDirection {
    UP, DOWN, NONE
}

@Composable
fun RememberScrollDirection(
    listState: LazyListState,
    bottomBarState: BottomBarState,
    scope: CoroutineScope
) {
    val previousScrollOffset by remember { derivedStateOf { mutableIntStateOf(0) } }
    val previousFirstVisibleItemIndex by remember { derivedStateOf { mutableIntStateOf(0) } }
    var lastScrollDirection by remember { mutableStateOf(ScrollDirection.NONE) }

    LaunchedEffect(listState) {
        scope.launch {
            snapshotFlow {
                listState.firstVisibleItemIndex to listState.firstVisibleItemScrollOffset
            }.collect { (index, offset) ->
                Log.e("ScrollDirection", "index: $index, offset: $offset")
                Log.e(
                    "ScrollDirection",
                    "previousScrollOffset: $previousScrollOffset, previousFirstVisibleItemIndex: $previousFirstVisibleItemIndex"
                )
                Log.e("ScrollDirection", "lastScrollDirection: $lastScrollDirection")

                val currentScrollDirection = when {
                    index > previousFirstVisibleItemIndex.intValue -> ScrollDirection.DOWN
                    index < previousFirstVisibleItemIndex.intValue -> ScrollDirection.UP
                    offset > previousScrollOffset.intValue -> ScrollDirection.DOWN
                    offset < previousScrollOffset.intValue -> ScrollDirection.UP
                    else -> ScrollDirection.NONE
                }

                if (currentScrollDirection != lastScrollDirection && currentScrollDirection != ScrollDirection.NONE) {
                    scope.launch {
                        bottomBarState.setBottomAppBarState(currentScrollDirection == ScrollDirection.UP)
                    }
                }

                previousFirstVisibleItemIndex.intValue = index
                previousScrollOffset.intValue = offset
                lastScrollDirection = currentScrollDirection
            }
        }
    }
}

@Composable
fun RememberScrollDirection(
    listState: LazyGridState,
    bottomBarState: BottomBarState,
    scope: CoroutineScope
) {
    val previousScrollOffset by remember { derivedStateOf { mutableIntStateOf(0) } }
    val previousFirstVisibleItemIndex by remember { derivedStateOf { mutableIntStateOf(0) } }
    var lastScrollDirection by remember { mutableStateOf(ScrollDirection.NONE) }

    LaunchedEffect(listState) {
        scope.launch {
            snapshotFlow {
                listState.firstVisibleItemIndex to listState.firstVisibleItemScrollOffset
            }.collect { (index, offset) ->
                Log.e("ScrollDirection", "index: $index, offset: $offset")
                Log.e(
                    "ScrollDirection",
                    "previousScrollOffset: $previousScrollOffset, previousFirstVisibleItemIndex: $previousFirstVisibleItemIndex"
                )
                Log.e("ScrollDirection", "lastScrollDirection: $lastScrollDirection")

                val currentScrollDirection = when {
                    index > previousFirstVisibleItemIndex.intValue -> ScrollDirection.DOWN
                    index < previousFirstVisibleItemIndex.intValue -> ScrollDirection.UP
                    offset > previousScrollOffset.intValue -> ScrollDirection.DOWN
                    offset < previousScrollOffset.intValue -> ScrollDirection.UP
                    else -> ScrollDirection.NONE
                }

                if (currentScrollDirection != lastScrollDirection && currentScrollDirection != ScrollDirection.NONE) {
                    scope.launch {
                        bottomBarState.setBottomAppBarState(currentScrollDirection == ScrollDirection.UP)
                    }
                }

                previousFirstVisibleItemIndex.intValue = index
                previousScrollOffset.intValue = offset
                lastScrollDirection = currentScrollDirection
            }
        }
    }
}

@Composable
fun rememberScrollOffset(
    listState: LazyListState,
    scope: CoroutineScope
): State<ScrollDirection> {
    val previousScrollOffset = remember { mutableIntStateOf(0) }
    val previousFirstVisibleItemIndex = remember { mutableIntStateOf(0) }
    val scrollDirection = remember { mutableStateOf(ScrollDirection.NONE) }

    LaunchedEffect(listState) {
        scope.launch {
            snapshotFlow {
                listState.firstVisibleItemIndex to listState.firstVisibleItemScrollOffset
            }.collect { (index, offset) ->
                Log.e("ScrollDirection", "index: $index, offset: $offset")
                Log.e(
                    "ScrollDirection",
                    "previousScrollOffset: ${previousScrollOffset.intValue}, previousFirstVisibleItemIndex: ${previousFirstVisibleItemIndex.value}"
                )
                Log.e("ScrollDirection", "lastScrollDirection: ${scrollDirection.value}")

                scrollDirection.value = when {
                    index > previousFirstVisibleItemIndex.intValue -> ScrollDirection.DOWN
                    index < previousFirstVisibleItemIndex.intValue -> ScrollDirection.UP
                    offset > previousScrollOffset.intValue -> ScrollDirection.DOWN
                    offset < previousScrollOffset.intValue -> ScrollDirection.UP
                    else -> ScrollDirection.NONE
                }

                previousFirstVisibleItemIndex.intValue = index
                previousScrollOffset.intValue = offset
            }
        }

    }
    return scrollDirection
}

fun Modifier.loadingFx(): Modifier = composed {
    var size by remember {
        mutableStateOf(IntSize.Zero)
    }

    val transition = rememberInfiniteTransition(label = "")

    val startOffsetX by transition.animateFloat(
        initialValue = -2 * size.width.toFloat(),
        targetValue = 2 * size.width.toFloat(),
        animationSpec = infiniteRepeatable(
            animation = tween(1000),
        ), label = ""
    )

    background(
        brush = Brush.linearGradient(
            colors = listOf(
                Color(0xFFE0E0E0),
                Color(0xFFB9B2B2),
                Color(0xFFE0E0E0),
            ),
            start = Offset(startOffsetX, 0f),
            end = Offset(startOffsetX + size.width.toFloat(), size.height.toFloat())
        )
    )
        .onGloballyPositioned {
            size = it.size
        }
}

fun formatDateToUSLongFormat(isoDate: String): String {
    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        val inputFormatter = DateTimeFormatter.ISO_DATE_TIME
        val zonedDateTime = ZonedDateTime.parse(isoDate, inputFormatter)
        val outputFormatter = DateTimeFormatter.ofPattern("MMMM dd, yyyy", Locale.US)
        zonedDateTime.format(outputFormatter)
    } else {
        val inputFormatter = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.US)
        inputFormatter.timeZone = java.util.TimeZone.getTimeZone("UTC")
        val outputFormatter = SimpleDateFormat("MMMM dd, yyyy", Locale.US)
        try {
            val date: Date = inputFormatter.parse(isoDate)
            outputFormatter.format(date)
        } catch (e: ParseException) {
            e.printStackTrace()
            ""
        }
    }
}



//BITMAPS UTILS
fun converterBitmapToString(bitmap: Bitmap): String {
    val baos = ByteArrayOutputStream()
    bitmap.compress(Bitmap.CompressFormat.JPEG, 70, baos)
    val byteArray = baos.toByteArray()
    return Base64.encodeToString(byteArray, Base64.DEFAULT)
}
fun converterStringToBitmap(encodedString: String, degrees: Float): Bitmap? {
    return try {
        val encodeByte = Base64.decode(encodedString, Base64.DEFAULT)
        val bitmap = BitmapFactory.decodeByteArray(encodeByte, 0, encodeByte.size)
        rotateBitmapManually(bitmap, degrees)
    } catch (e: Exception) {
        e.printStackTrace()
        null
    }
}
fun compressBitmap(bitmap: Bitmap, quality: Int): Bitmap {
    val stream = ByteArrayOutputStream()
    bitmap.compress(Bitmap.CompressFormat.JPEG, quality, stream)
    val byteArray = stream.toByteArray()
    return BitmapFactory.decodeByteArray(byteArray, 0, byteArray.size)
}
fun rotateBitmapManually(bitmap: Bitmap, degrees: Float): Bitmap {
    val matrix = Matrix()
    matrix.postRotate(degrees)
    return Bitmap.createBitmap(bitmap, 0, 0, bitmap.width, bitmap.height, matrix, true)
}

