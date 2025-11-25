package br.edu.ufam.nutrilogapp.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.NavType
import androidx.navigation.navArgument
import br.edu.ufam.nutrilogapp.screens.AtualizaMetaScreen
import br.edu.ufam.nutrilogapp.screens.ConfiguracoesScreen
import br.edu.ufam.nutrilogapp.screens.DetalhesAlimentoScreen
import br.edu.ufam.nutrilogapp.screens.EditarConfiguracoesScreen
import br.edu.ufam.nutrilogapp.screens.HomeScreen
import br.edu.ufam.nutrilogapp.screens.PesquisarAlimentoScreen
import br.edu.ufam.nutrilogapp.screens.RegistraRefeicaoScreen
import br.edu.ufam.nutrilogapp.screens.RegisterScreen
import br.edu.ufam.nutrilogapp.screens.ScannerScreen
import com.seunomeprojeto.screens.LoginScreen

object NutrilogRoutes {
    const val LOGIN = "login"
    const val REGISTER = "register"
    const val HOME = "home"
    const val REGISTRA_REFEICAO = "registra_refeicao"
    const val CONFIGURACOES = "configuracoes"
    const val EDITAR_CONFIGURACOES = "editar_configuracoes"
    const val ATUALIZA_META = "atualiza_meta"
    const val PESQUISAR_ALIMENTO = "pesquisar_alimento"
    const val SCANNER = "scanner"
    const val DETALHES_ALIMENTO = "detalhes_alimento/{barcode}"
    const val DETALHES_ALIMENTO_BARCODE_ARG = "barcode"
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
                    navController.navigate(NutrilogRoutes.REGISTER)
                }
            )
        }

        composable(NutrilogRoutes.REGISTER) {
            RegisterScreen(
                onRegisterSuccess = {
                    navController.navigate(NutrilogRoutes.LOGIN) {
                        popUpTo(NutrilogRoutes.REGISTER) {
                            inclusive = true
                        }
                    }
                },
                onBackClick = {
                    navController.popBackStack()
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
                onSearchFoodClick = {
                    navController.navigate(NutrilogRoutes.PESQUISAR_ALIMENTO)
                },
                onReadBarcodeClick = {
                    navController.navigate(NutrilogRoutes.SCANNER)
                }
            )
        }

        composable(NutrilogRoutes.PESQUISAR_ALIMENTO) {
            PesquisarAlimentoScreen(
                onBackClick = {
                    navController.popBackStack()
                },
                onAlimentoFound = { alimento ->
                    navController.navigate(
                        "detalhes_alimento/${alimento.openfoodfacts_id}"
                    )
                }
            )
        }

        composable(NutrilogRoutes.SCANNER) {
            ScannerScreen(
                onCancelClick = {
                    navController.popBackStack()
                },
                onConfirmClick = { barcode ->
                    navController.navigate(
                        "detalhes_alimento/$barcode"
                    )
                }
            )
        }

        composable(
            route = NutrilogRoutes.DETALHES_ALIMENTO,
            arguments = listOf(
                navArgument(NutrilogRoutes.DETALHES_ALIMENTO_BARCODE_ARG) {
                    type = NavType.StringType
                }
            )
        ) { backStackEntry ->
            val barcode = backStackEntry.arguments?.getString(NutrilogRoutes.DETALHES_ALIMENTO_BARCODE_ARG) ?: ""
            
            DetalhesAlimentoScreen(
                barcode = barcode,
                onBackClick = {
                    navController.popBackStack()
                },
                onContinueClick = {
                    navController.popBackStack()
                }
            )
        }

        composable(NutrilogRoutes.CONFIGURACOES) {
            ConfiguracoesScreen(
                onEditNameClick = { /* TODO: Implementar edição de nome */ },
                onEditInformationClick = {
                    navController.navigate(NutrilogRoutes.EDITAR_CONFIGURACOES)
                },
                onEditGoalClick = {
                    navController.navigate(NutrilogRoutes.ATUALIZA_META)
                }
            )
        }

        composable(NutrilogRoutes.EDITAR_CONFIGURACOES) {
            EditarConfiguracoesScreen(
                onBackClick = {
                    navController.popBackStack()
                },
                onLogoutClick = {
                    // TODO: Implementar logout e navegar para login
                    navController.navigate(NutrilogRoutes.LOGIN) {
                        popUpTo(0) { inclusive = true }
                    }
                }
            )
        }

        composable(NutrilogRoutes.ATUALIZA_META) {
            AtualizaMetaScreen(
                onUpdateClick = {
                    navController.popBackStack()
                }
            )
        }
    }
}

