package ru.artbez.composeroulette.utils

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.toSize

@Composable
fun menuWithList(betFieldName : String, itemList : List<String>) : String {

    var menuHidden by remember { mutableStateOf(false) }
    var selectedText by remember { mutableStateOf("") }

    var textFieldSize by remember { mutableStateOf(Size.Zero) }

    val menuArrowIcon = if (menuHidden) Icons.Filled.KeyboardArrowUp else Icons.Filled.KeyboardArrowDown

    OutlinedTextField(
        value = selectedText,
        onValueChange = { selectedText = it },
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 10.dp)
            .onGloballyPositioned { coordinates ->
                textFieldSize = coordinates.size.toSize()
            },
        label = {
            Text(
                text = betFieldName,
                fontWeight = FontWeight.Bold
            )
                },
        trailingIcon = {
            Icon(
                imageVector = menuArrowIcon,
                contentDescription = "Icon that show/hide menu list",
                modifier = Modifier.clickable { menuHidden = !menuHidden })
        },
        enabled = false,
        colors = TextFieldDefaults.outlinedTextFieldColors(
            disabledTextColor = LocalContentColor.current.copy(LocalContentAlpha.current),
            backgroundColor = Color.Transparent,
            disabledBorderColor = MaterialTheme.colors.onSurface.copy(alpha = ContentAlpha.disabled),
            disabledLabelColor = MaterialTheme.colors.onSurface.copy(ContentAlpha.medium),
        )
    )
    DropdownMenu(
        expanded = menuHidden,
        onDismissRequest = { menuHidden = false },
        modifier = Modifier.width(with(LocalDensity.current) { textFieldSize.width.toDp() - 50.dp})
    ) {
        itemList.forEach { item ->
            DropdownMenuItem(onClick = {
                selectedText = item
                menuHidden = false
            })
            {
                Text(
                    text = item,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
    return selectedText
}