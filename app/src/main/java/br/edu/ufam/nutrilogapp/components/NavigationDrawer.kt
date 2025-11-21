package br.edu.ufam.nutrilogapp.components

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import br.edu.ufam.nutrilogapp.R
import br.edu.ufam.nutrilogapp.navigation.NutrilogRoutes

@Composable
fun NavigationDrawer(
    drawerState: DrawerState,
    navController: NavController,
    currentRoute: String?,
    onDismiss: () -> Unit,
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit
) {
    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            ModalDrawerSheet(
                modifier = modifier
            ) {
                Text(
                    text = "Menu",
                    style = MaterialTheme.typography.titleLarge,
                    modifier = Modifier.padding(16.dp)
                )
                HorizontalDivider()

                NavigationDrawerItem(
                    label = { Text(stringResource(R.string.menu_dashboard)) },
                    selected = currentRoute == NutrilogRoutes.HOME,
                    onClick = {
                        navController.navigate(NutrilogRoutes.HOME) {
                            popUpTo(NutrilogRoutes.HOME) { inclusive = true }
                        }
                        onDismiss()
                    },
                    modifier = Modifier.padding(horizontal = 12.dp)
                )

                NavigationDrawerItem(
                    label = { Text(stringResource(R.string.menu_register_meal)) },
                    selected = currentRoute == NutrilogRoutes.REGISTRA_REFEICAO,
                    onClick = {
                        navController.navigate(NutrilogRoutes.REGISTRA_REFEICAO)
                        onDismiss()
                    },
                    modifier = Modifier.padding(horizontal = 12.dp)
                )

                NavigationDrawerItem(
                    label = { Text(stringResource(R.string.menu_my_day)) },
                    selected = false,
                    onClick = {
                        // TODO: Implementar navegação para "Meu Dia"
                        onDismiss()
                    },
                    modifier = Modifier.padding(horizontal = 12.dp)
                )

                NavigationDrawerItem(
                    label = { Text(stringResource(R.string.menu_settings)) },
                    selected = currentRoute == NutrilogRoutes.CONFIGURACOES,
                    onClick = {
                        navController.navigate(NutrilogRoutes.CONFIGURACOES)
                        onDismiss()
                    },
                    modifier = Modifier.padding(horizontal = 12.dp)
                )
            }
        },
        content = content
    )
}

