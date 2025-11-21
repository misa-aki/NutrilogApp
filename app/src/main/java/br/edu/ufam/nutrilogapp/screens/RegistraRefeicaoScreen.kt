package br.edu.ufam.nutrilogapp.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import br.edu.ufam.nutrilogapp.R
import br.edu.ufam.nutrilogapp.components.ActionButton
import br.edu.ufam.nutrilogapp.components.InfoBox
import br.edu.ufam.nutrilogapp.ui.theme.NutrilogAppTheme

@Composable
fun RegistraRefeicaoScreen(
    onScanMealClick: () -> Unit = {},
    onSearchFoodClick: () -> Unit = {},
    onReadBarcodeClick: () -> Unit = {}
) {
    NutrilogAppTheme {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = stringResource(R.string.add_new_meal),
                style = MaterialTheme.typography.headlineLarge,
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier.padding(bottom = 48.dp)
            )

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = onScanMealClick,
                modifier = Modifier.padding(bottom = 16.dp).fillMaxWidth()
            ) {
                Text(stringResource(R.string.scan_meal))
            }

            Button(
                onClick = onSearchFoodClick,
                modifier = Modifier.padding(bottom = 16.dp).fillMaxWidth()
            ) {
                Text(stringResource(R.string.search_food))
            }

            Button(
                onClick = onReadBarcodeClick,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(stringResource(R.string.read_barcode))
            }

            Spacer(modifier = Modifier.weight(1f))

            InfoBox(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 24.dp)
            )
        }
    }
}

