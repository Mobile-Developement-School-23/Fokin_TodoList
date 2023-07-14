package com.example.todoapp.ui.compose

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.todoapp.R

@Preview
@Composable
fun PreviewChangeTodoDeleteComponent() {
    AppTheme {
        ChangeTodoDeleteComponent(enabled = true, onAction = {})
    }
}

@Composable
fun ChangeTodoDeleteComponent(
    enabled: Boolean,
    onAction: (ChangeTodoItemActions) -> Unit
) {
    TextButton(
        onClick = { onAction(ChangeTodoItemActions.Delete) },
        enabled = enabled,
        contentPadding = PaddingValues(vertical = 12.dp, horizontal = 16.dp),
        colors = ButtonDefaults.textButtonColors(
            contentColor = AppTheme.colors.colorRed,
            disabledContentColor = AppTheme.colors.labelDisable
        ),
        shape = RectangleShape,
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Start
        ) {
            Icon(imageVector = Icons.Default.Delete, contentDescription = null)
            Spacer(modifier = Modifier.size(12.dp))
            Text(text = stringResource(id = R.string.trash), style = AppTheme.typography.body)
        }
    }
}

