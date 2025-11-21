package br.edu.ufam.nutrilogapp.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import br.edu.ufam.nutrilogapp.R
import br.edu.ufam.nutrilogapp.components.ProfilePicture
import br.edu.ufam.nutrilogapp.components.UserInfoSection
import br.edu.ufam.nutrilogapp.components.GoalCard
import br.edu.ufam.nutrilogapp.components.InfoBox
import br.edu.ufam.nutrilogapp.ui.theme.NutrilogAppTheme
import br.edu.ufam.nutrilogapp.viewmodel.ConfiguracoesViewModel

@Composable
fun ConfiguracoesScreen(
    onEditNameClick: () -> Unit = {},
    onEditInformationClick: () -> Unit = {},
    onEditGoalClick: () -> Unit = {},
    viewModel: ConfiguracoesViewModel = viewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    NutrilogAppTheme {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
                .padding(horizontal = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(32.dp))

            ProfilePicture(
                imageUrl = uiState.userProfile?.profileImageUrl
            )

            Spacer(modifier = Modifier.height(16.dp))

            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                Text(
                    text = uiState.userProfile?.name ?: "",
                    style = MaterialTheme.typography.headlineMedium,
                    color = MaterialTheme.colorScheme.onBackground
                )
                Spacer(modifier = Modifier.width(8.dp))
                IconButton(
                    onClick = onEditNameClick,
                    modifier = Modifier.size(24.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Edit,
                        contentDescription = stringResource(R.string.edit),
                        tint = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.size(20.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            uiState.userProfile?.let { profile ->
                UserInfoSection(
                    userProfile = profile,
                    onEditClick = onEditInformationClick,
                    modifier = Modifier.fillMaxWidth()
                )
            }

            Spacer(modifier = Modifier.height(32.dp))

            uiState.userGoal?.let { goal ->
                GoalCard(
                    goal = goal,
                    onEditClick = onEditGoalClick,
                    modifier = Modifier.fillMaxWidth()
                )
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

