package com.example.todoapp.ui.compose

import com.example.todoapp.data.Importance
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Done
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.SheetState
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
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
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.todoapp.R
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@Preview
@Composable
fun PreviewChangeTodoImportanceComponent() {
    AppTheme {
        ChangeTodoImportanceComponent(importance = Importance.IMPORTANT) {}
    }
}

@Composable
fun ChangeTodoImportanceComponent(importance: Importance, uiAction: (ChangeTodoItemActions) -> Unit) {
    val isImportant = remember(importance) { importance == Importance.IMPORTANT }
    var showModalBottomSheet by remember { mutableStateOf(false) }
    Column(
        modifier = Modifier
            .padding(horizontal = 15.dp)
            .padding(top = 20.dp, bottom = 15.dp)
            .clip(RoundedCornerShape(5.dp))
            .clickable { showModalBottomSheet = !showModalBottomSheet }
    ) {
        Text(
            text = stringResource(id = R.string.title_importance),
            color = AppTheme.colors.labelPrimary
        )
        Text(
            text = stringResource(id = importance.toStringResource()),
            modifier = Modifier.padding(top = 5.dp),
            color = if (isImportant) AppTheme.colors.colorRed else AppTheme.colors.labelTertiary
        )
        ImportanceModalBottomSheet(
            uiAction = uiAction,
            closeBottomSheet = { showModalBottomSheet = false },
            oldImportance = importance,
            showModalBottomSheet = showModalBottomSheet
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ImportanceModalBottomSheet(
    uiAction: (ChangeTodoItemActions) -> Unit,
    closeBottomSheet: () -> Unit,
    oldImportance: Importance,
    showModalBottomSheet: Boolean
) {
    val scope = rememberCoroutineScope()
    val skipPartially by remember { mutableStateOf(false) }
    val bottomSheetState = rememberModalBottomSheetState(skipPartiallyExpanded = skipPartially)
    var newImportance by remember { mutableStateOf(oldImportance) }
    if (showModalBottomSheet) {
        ModalBottomSheet(
            onDismissRequest = { closeBottomSheet() },
            sheetState = bottomSheetState,
            containerColor = AppTheme.colors.backPrimary,
            modifier = Modifier
                .fillMaxHeight(0.25f)
                .fillMaxWidth()
        ) {
            Column(
                modifier = Modifier
                    .padding(horizontal = 20.dp)
                    .fillMaxSize(),
                verticalArrangement = Arrangement.Top
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    IconButtonForBottomSheet(
                        closeBottomSheet = { closeBottomSheet() },
                        updateImportance =
                        { uiAction(ChangeTodoItemActions.UpdateImportance(newImportance)) },
                        scope = scope,
                        bottomSheetState = bottomSheetState,
                        imageVector = Icons.Default.Close,
                        contentDescription = stringResource(id = R.string.close_button),
                        save = false
                    )
                    IconButtonForBottomSheet(
                        closeBottomSheet = { closeBottomSheet() },
                        updateImportance =
                        { uiAction(ChangeTodoItemActions.UpdateImportance(newImportance)) },
                        scope = scope,
                        bottomSheetState = bottomSheetState,
                        imageVector = Icons.Default.Done,
                        contentDescription = stringResource(id = R.string.save_button),
                        save = true
                    )
                }
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 10.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    for (importance in Importance.values()) {
                        ImportanceItem(
                            changeImportance = { newImportance = importance },
                            importance = importance,
                            selected = newImportance == importance
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun ImportanceItem(changeImportance: () -> Unit, importance: Importance, selected: Boolean) {
    val color = when {
        selected && importance == Importance.IMPORTANT -> AppTheme.colors.colorRed
        selected -> AppTheme.colors.labelPrimary
        else -> AppTheme.colors.labelTertiary
    }
    val scale = remember { Animatable(initialValue = 1f) }
    val clickEnabled = remember { mutableStateOf(true) }
    LaunchedEffect(key1 = selected) {
        if (selected) {
            clickEnabled.value = false
            val job = launch {
                scale.animateTo(
                    targetValue = 0.9f,
                    animationSpec = tween(
                        durationMillis = 50
                    )
                )
                scale.animateTo(
                    targetValue = 1f,
                    animationSpec = spring(
                        dampingRatio = Spring.DampingRatioLowBouncy,
                        stiffness = Spring.StiffnessLow
                    )
                )
            }
            job.join()
            clickEnabled.value = true
        }
    }
    Box(
        modifier = Modifier
            .scale(scale = scale.value)
            .clickable { changeImportance() }
    ) {
        Text(
            text = stringResource(id = importance.toStringResource()),
            modifier = Modifier.padding(all = 5.dp),
            color = color,
            style = AppTheme.typography.title
        )
    }
    if (importance != Importance.IMPORTANT) Spacer(modifier = Modifier.size(10.dp))
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun IconButtonForBottomSheet(
    closeBottomSheet: () -> Unit,
    updateImportance: () -> Unit,
    scope: CoroutineScope,
    bottomSheetState: SheetState,
    imageVector: ImageVector,
    contentDescription: String,
    save: Boolean
) {
    IconButton(
        onClick = {
            scope.launch { bottomSheetState.hide() }.invokeOnCompletion {
                if (!bottomSheetState.isVisible) {
                    if (save) updateImportance()
                    closeBottomSheet()
                }
            }
        }
    ) {
        Icon(
            imageVector = imageVector,
            contentDescription = contentDescription,
            tint = AppTheme.colors.labelPrimary
        )
    }
}


