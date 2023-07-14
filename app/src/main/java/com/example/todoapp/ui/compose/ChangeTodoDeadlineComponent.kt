package com.example.todoapp.ui.compose

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Switch
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TimePicker
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.material3.rememberTimePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.example.todoapp.R
import com.example.todoapp.utils.DAY
import com.example.todoapp.utils.HOUR
import com.example.todoapp.utils.MINUTE
import com.example.todoapp.utils.dateToUnix
import com.example.todoapp.utils.formatDate
import com.example.todoapp.utils.formatDateToDatePattern
import java.util.Calendar
import java.util.Date
import java.util.TimeZone

@Preview
@Composable
fun PreviewChangeTodoDeadlineComponentSet() {
    AppTheme {
        ChangeTodoItemDeadlineComponent(Date(System.currentTimeMillis()), true, {})
    }
}

@Preview
@Composable
fun PreviewChangeTodoDeadlineComponentUnset() {
    AppTheme {
        ChangeTodoItemDeadlineComponent(Date(System.currentTimeMillis()), false, {})
    }
}

@Preview
@Composable
fun PreviewDatePicker() {
    AppTheme {
        DeadlineDatePicker(
            isDialogOpen = true,
            deadline = Date(System.currentTimeMillis()),
            uiAction = { },
            closeDialog = { }
        )
    }
}

@Composable
fun ChangeTodoItemDeadlineComponent(
    deadline: Date,
    isDateVisible: Boolean,
    uiAction: (ChangeTodoItemActions) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(all = 15.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        val dateText = remember(deadline) { formatDateToDatePattern(deadline) }
        var isDialogOpen by remember { mutableStateOf(false) }

        Column {
            Text(
                text = stringResource(id = R.string.deadline),
                modifier = Modifier.padding(start = 5.dp),
                color = AppTheme.colors.labelPrimary
            )
            AnimatedVisibility(visible = isDateVisible) {
                Box(
                    modifier = Modifier
                        .padding(5.dp)
                ) {
                    Text(
                        text = dateText,
                        color = AppTheme.colors.colorBlue)
                }
            }
        }
        Switch(
            checked = isDateVisible,
            onCheckedChange = { checked ->
                if (checked) {
                    isDialogOpen = true
                } else {
                    uiAction(ChangeTodoItemActions.UpdateDeadlineSet(false))
                }
            },
            colors = SwitchDefaults.colors(
                checkedThumbColor = AppTheme.colors.colorBlue,
                checkedTrackColor = AppTheme.colors.colorGrayLight,
                uncheckedThumbColor = AppTheme.colors.backElevated,
                uncheckedTrackColor = AppTheme.colors.supportOverlay,
                uncheckedBorderColor = AppTheme.colors.supportOverlay,
            )
        )
        DeadlineDatePicker(
            isDialogOpen = isDialogOpen,
            deadline = deadline,
            uiAction = uiAction,
            closeDialog = { isDialogOpen = false }
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun DeadlineDatePicker(
    isDialogOpen: Boolean,
    deadline: Date,
    uiAction: (ChangeTodoItemActions) -> Unit,
    closeDialog: () -> Unit
) {
    if (isDialogOpen) {
        val datePickerState = rememberDatePickerState(
            initialSelectedDateMillis = dateToUnix(deadline) * 1000
        )
        val confirmEnabled by remember(datePickerState.selectedDateMillis) {
            derivedStateOf { datePickerState.selectedDateMillis != null }
        }

        DatePickerDialog(
            onDismissRequest = closeDialog,
            confirmButton = {
                TextButton(
                    onClick = {
                        datePickerState.selectedDateMillis?.let {
                            uiAction(ChangeTodoItemActions.UpdateDeadline(it / 1000))
                            uiAction(ChangeTodoItemActions.UpdateDeadlineSet(true))
                        }
                        closeDialog()
                    },
                    enabled = confirmEnabled
                ) {
                    Text(stringResource(R.string.okay))
                }
            },
            dismissButton = {
                TextButton(
                    onClick = closeDialog
                ) {
                    Text(stringResource(R.string.cancel))
                }
            }
        ) {
            DatePicker(state = datePickerState)
        }
    }
}