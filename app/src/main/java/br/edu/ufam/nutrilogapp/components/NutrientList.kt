package br.edu.ufam.nutrilogapp.components

import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import br.edu.ufam.nutrilogapp.R
import br.edu.ufam.nutrilogapp.data.model.Nutrients

@Composable
fun NutrientList(
    nutrients: Nutrients,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        NutrientRow(
            label = stringResource(R.string.nutrients),
            value = "${nutrients.total}g"
        )
        NutrientRow(
            label = stringResource(R.string.sugars),
            value = "${nutrients.sugars}g"
        )
        NutrientRow(
            label = stringResource(R.string.carbohydrates),
            value = "${nutrients.carbohydrates}g"
        )
        NutrientRow(
            label = stringResource(R.string.proteins),
            value = "${nutrients.proteins}g"
        )
        NutrientRow(
            label = stringResource(R.string.fats),
            value = "${nutrients.fats}g"
        )
        NutrientRow(
            label = stringResource(R.string.fibers),
            value = "${nutrients.fibers}g"
        )
    }
}

@Composable
private fun NutrientRow(
    label: String,
    value: String,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurface
        )
        Text(
            text = value,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.primary
        )
    }
}

