package br.edu.ufam.nutrilogapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import br.edu.ufam.nutrilogapp.screens.ScannerScreen
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.lifecycleScope
import br.edu.ufam.nutrilogapp.ui.theme.NutrilogAppTheme
import com.seunomeprojeto.screens.LoginScreen
import com.seunomeprojeto.screens.SplashScreen
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {

    private var keepSplashOnScreen = true

    override fun onCreate(savedInstanceState: Bundle?) {
        val splashScreen = installSplashScreen()

        super.onCreate(savedInstanceState)

        splashScreen.setKeepOnScreenCondition {
            keepSplashOnScreen
        }

        lifecycleScope.launch {
            delay(100)
            keepSplashOnScreen = false
        }

        setContent {
            ScannerScreen()
//            LoginScreen (
//                onLoginSuccess = { /* Implementar navegação para a Home */ },
//                onNavigateToRegister = { /* Implementar navegação para o Cadastro */ }
//            )
        }

    }
}