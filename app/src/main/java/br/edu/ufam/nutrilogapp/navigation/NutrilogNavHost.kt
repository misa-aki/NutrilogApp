package br.edu.ufam.nutrilogapp.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import br.edu.ufam.nutrilogapp.screens.ConfiguracoesScreen
import br.edu.ufam.nutrilogapp.screens.HomeScreen
import br.edu.ufam.nutrilogapp.screens.RegistraRefeicaoScreen
import com.seunomeprojeto.screens.LoginScreen

object NutrilogRoutes {
    const val LOGIN = "login"
    const val HOME = "home"
    const val REGISTRA_REFEICAO = "registra_refeicao"
    const val CONFIGURACOES = "configuracoes"
}

@Composable
fun NutrilogNavHost(
    navController: NavHostController,
    startDestination: String = NutrilogRoutes.LOGIN
) {
    NavHost(
        navController = navController,
        startDestination = startDestination
    ) {
        composable(NutrilogRoutes.LOGIN) {
            LoginScreen(
                onLoginSuccess = {
                    navController.navigate(NutrilogRoutes.HOME) {
                        popUpTo(NutrilogRoutes.LOGIN) {
                            inclusive = true
                        }
                    }
                },
                onNavigateToRegister = {
                    // TODO: Implementar navegação para registro
                }
            )
        }

        composable(NutrilogRoutes.HOME) {
            HomeScreen(
                navController = navController,
                onMoreDetailsClick = { /* TODO: Implementar navegação para detalhes */ }
            )
        }

        composable(NutrilogRoutes.REGISTRA_REFEICAO) {
            RegistraRefeicaoScreen(
                onScanMealClick = { /* TODO: Implementar navegação para scan de refeição */ },
                onSearchFoodClick = { /* TODO: Implementar navegação para pesquisa */ },
                onReadBarcodeClick = { /* TODO: Implementar navegação para scanner */ }
            )
        }

        composable(NutrilogRoutes.CONFIGURACOES) {
            ConfiguracoesScreen(
                onEditNameClick = { /* TODO: Implementar edição de nome */ },
                onEditInformationClick = { /* TODO: Implementar edição de informações */ },
                onEditGoalClick = { /* TODO: Implementar navegação para atualizar meta */ }
            )
        }
    }
}

