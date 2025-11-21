package br.edu.ufam.nutrilogapp.components

import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import br.edu.ufam.nutrilogapp.R
import br.edu.ufam.nutrilogapp.ui.theme.PrimaryBlue

@Composable
fun ProfilePicture(
    modifier: Modifier = Modifier,
    imageUrl: String? = null
) {
    Icon(
        imageVector = Icons.Default.AccountCircle,
        contentDescription = stringResource(R.string.user_profile),
        modifier = modifier
            .size(120.dp)
            .clip(CircleShape),
        tint = PrimaryBlue
    )
}

