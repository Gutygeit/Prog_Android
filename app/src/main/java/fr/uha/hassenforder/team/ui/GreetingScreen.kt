package fr.uha.hassenforder.team.ui

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.annotation.RootGraph

@Destination<RootGraph>(start = true)
@Composable
fun GreetingScreen() {
    Text("Hello girls and boys, here my best application to learn Android")
}