package com.example.todoapp.ui.compose

import com.example.todoapp.data.Importance
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.todoapp.R
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChangeTodoItemImportanceComponent(
    importance: Importance,
    onAction: (ChangeTodoItemActions) -> Unit
) {
    var showBottomSheet by remember { mutableStateOf(false) }
    val bottomSheetState = rememberModalBottomSheetState()
    val scope = rememberCoroutineScope()

    if (showBottomSheet) {
        ImportanceBottomSheet(
            hide = {
                scope.launch {
                    bottomSheetState.hide()
                    showBottomSheet = false
                }
            },
            onAction = onAction
        )
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clickable {
                scope.launch {
                    showBottomSheet = true
                    bottomSheetState.show()
                }
            }
            .padding(16.dp)
    ) {
        val color by animateColorAsState(
            targetValue = when (importance) {
                Importance.IMPORTANT -> AppTheme.colors.colorRed
                else -> AppTheme.colors.labelSecondary
            },
            animationSpec = tween(200, easing = LinearEasing),
            label = "color"
        )
        Text(
            text = stringResource(id = R.string.title_importance),
            style = AppTheme.typography.body,
            color = AppTheme.colors.labelPrimary
        )
        Text(
            text = stringResource(
                id = when (importance) {
                    Importance.IMPORTANT -> R.string.importance_urgent
                    Importance.LOW -> R.string.importance_low
                    else -> R.string.importance_normal
                }
            ),
            style = AppTheme.typography.subhead,
            color = color
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ImportanceBottomSheet(
    hide: () -> Unit,
    onAction: (ChangeTodoItemActions) -> Unit
) {
    ModalBottomSheet(
        onDismissRequest = { hide() },
        containerColor = AppTheme.colors.backSecondary
    ) {
        Column(Modifier.padding(bottom = 48.dp)) {
            PriorityItem(
                text = stringResource(R.string.importance_normal),
                color = AppTheme.colors.labelPrimary
            ) {
                onAction(ChangeTodoItemActions.UpdateImportance(Importance.BASIC))
                hide()
            }

            PriorityItem(
                text = stringResource(R.string.importance_low),
                color = AppTheme.colors.labelPrimary
            ) {
                onAction(ChangeTodoItemActions.UpdateImportance(Importance.LOW))
                hide()
            }

            PriorityItem(
                text = stringResource(R.string.importance_urgent),
                color = AppTheme.colors.colorRed
            ) {
                onAction(ChangeTodoItemActions.UpdateImportance(Importance.IMPORTANT))
                hide()
            }
        }
    }
}

@Composable
fun PriorityItem(text: String, color: Color, onClick: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .padding(16.dp)
    ) {
        Text(text = text, style = AppTheme.typography.body, color = color)
    }
}


@Preview
@Composable
fun PreviewChangeTodoItemImportanceComponent() {
    AppTheme {
        ChangeTodoItemImportanceComponent(importance = Importance.IMPORTANT) {}
    }
}