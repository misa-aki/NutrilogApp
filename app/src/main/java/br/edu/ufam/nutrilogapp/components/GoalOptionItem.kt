package br.edu.ufam.nutrilogapp.components

import androidx.compose.foundation.layout.*
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import br.edu.ufam.nutrilogapp.data.model.GoalOption
import br.edu.ufam.nutrilogapp.ui.theme.PrimaryBlue

@Composable
fun GoalOptionItem(
    option: GoalOption,
    selected: Boolean,
    onSelect: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        RadioButton(
            selected = selected,
            onClick = onSelect,
            colors = RadioButtonDefaults.colors(
                selectedColor = PrimaryBlue
            )
        )
        Spacer(modifier = Modifier.width(8.dp))
        Text(
            text = option.description,
            color = PrimaryBlue,
            modifier = Modifier.weight(1f)
        )
    }
}

