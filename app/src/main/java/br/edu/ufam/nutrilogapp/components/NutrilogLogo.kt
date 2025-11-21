package br.edu.ufam.nutrilogapp.components


import androidx.compose.foundation.Image
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import br.edu.ufam.nutrilogapp.R

@Composable
fun NutriLogLogo(modifier: Modifier = Modifier) {
    Image(
        painter = painterResource(id = R.drawable.nutrilog_logo), // Seu SVG importado
        contentDescription = "Logo NutriLog",
        // A cor do ícone no seu SVG deve ser definida para usar a cor `primary` do tema
        colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.primary),
        modifier = modifier
    )
}