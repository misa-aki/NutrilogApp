package br.edu.ufam.nutrilogapp.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import br.edu.ufam.nutrilogapp.R
import br.edu.ufam.nutrilogapp.data.model.Alimento
import coil.compose.AsyncImage

@Composable
fun AlimentoDetailsCard(
    alimento: Alimento,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                text = alimento.nome,
                style = MaterialTheme.typography.headlineMedium,
                color = MaterialTheme.colorScheme.primary,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(bottom = 16.dp)
            )

            alimento.imagem_url?.let { imageUrl ->
                AsyncImage(
                    model = imageUrl,
                    contentDescription = alimento.nome,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(200.dp)
                        .padding(bottom = 16.dp),
                    contentScale = ContentScale.Fit
                )
            }

            if (alimento.marca != null) {
                InfoRow(
                    label = stringResource(R.string.brand),
                    value = alimento.marca
                )
            }

            if (alimento.categoria != null) {
                InfoRow(
                    label = stringResource(R.string.category),
                    value = alimento.categoria
                )
            }

            HorizontalDivider(modifier = Modifier.padding(vertical = 12.dp))

            Text(
                text = stringResource(R.string.nutrients),
                style = MaterialTheme.typography.titleLarge,
                color = MaterialTheme.colorScheme.primary,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(bottom = 12.dp)
            )

            NutrientRow(
                label = stringResource(R.string.calories),
                value = "${alimento.calorias.toInt()} kcal"
            )
            NutrientRow(
                label = stringResource(R.string.carbohydrates),
                value = "${alimento.carboidratos.toInt()}g"
            )
            NutrientRow(
                label = stringResource(R.string.sugars),
                value = "${alimento.acucares.toInt()}g"
            )
            NutrientRow(
                label = stringResource(R.string.proteins),
                value = "${alimento.proteinas.toInt()}g"
            )
            NutrientRow(
                label = stringResource(R.string.fats),
                value = "${alimento.gorduras.toInt()}g"
            )
            NutrientRow(
                label = stringResource(R.string.fibers),
                value = "${alimento.fibras.toInt()}g"
            )

            if (alimento.porcao_padrao != null) {
                Spacer(modifier = Modifier.height(8.dp))
                NutrientRow(
                    label = stringResource(R.string.standard_portion),
                    value = "${alimento.porcao_padrao.toInt()}g"
                )
            }

            if (alimento.classificacao_carboidratos != null || alimento.porcoes_carboidrato != null) {
                HorizontalDivider(modifier = Modifier.padding(vertical = 12.dp))

                Text(
                    text = stringResource(R.string.carbohydrate_info),
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.primary,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(bottom = 8.dp)
                )

                alimento.classificacao_carboidratos?.let {
                    InfoRow(
                        label = stringResource(R.string.carbohydrate_classification),
                        value = it
                    )
                }

                alimento.porcoes_carboidrato?.let {
                    NutrientRow(
                        label = stringResource(R.string.carbohydrate_portions),
                        value = String.format("%.2f", it)
                    )
                }
            }

            alimento.mensagem_diabetes?.let { message ->
                HorizontalDivider(modifier = Modifier.padding(vertical = 12.dp))
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.3f)
                    )
                ) {
                    Column(
                        modifier = Modifier.padding(12.dp)
                    ) {
                        Text(
                            text = stringResource(R.string.diabetes_message),
                            style = MaterialTheme.typography.titleSmall,
                            color = MaterialTheme.colorScheme.primary,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(bottom = 4.dp)
                        )
                        Text(
                            text = message,
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun InfoRow(
    label: String,
    value: String,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.primary.copy(alpha = 0.7f)
        )
        Text(
            text = value,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.primary,
            fontWeight = FontWeight.Medium
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
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onSurface
        )
        Text(
            text = value,
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.primary,
            fontWeight = FontWeight.Bold
        )
    }
}

