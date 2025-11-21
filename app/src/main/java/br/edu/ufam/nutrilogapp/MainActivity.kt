package br.edu.ufam.nutrilogapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.remember
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.lifecycleScope
import androidx.navigation.compose.rememberNavController
import br.edu.ufam.nutrilogapp.navigation.NutrilogNavHost
import br.edu.ufam.nutrilogapp.ui.theme.NutrilogAppTheme
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
            NutrilogAppTheme {
                val navController = rememberNavController()
                NutrilogNavHost(navController = navController)
            }
        }
    }
}