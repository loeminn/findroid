package dev.jdtech.jellyfin.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.OutlinedTextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.tv.material3.Button
import androidx.tv.material3.ExperimentalTvMaterial3Api
import androidx.tv.material3.Icon
import androidx.tv.material3.MaterialTheme
import androidx.tv.material3.Text
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import dev.jdtech.jellyfin.core.R as CoreR
import dev.jdtech.jellyfin.ui.destinations.LoginScreenDestination
import dev.jdtech.jellyfin.viewmodels.AddServerViewModel

@OptIn(ExperimentalTvMaterial3Api::class)
@Destination
@Composable
fun AddServerScreen(
    navigator: DestinationsNavigator,
    addServerViewModel: AddServerViewModel = hiltViewModel()
) {
    val uiState by addServerViewModel.uiState.collectAsState()
    val navigateToLogin by addServerViewModel.navigateToLogin.collectAsState(initial = false)
    if (navigateToLogin) {
        navigator.navigate(LoginScreenDestination)
    }

    var text by rememberSaveable {
        mutableStateOf("")
    }
    val isError = uiState is AddServerViewModel.UiState.Error
    val isLoading = uiState is AddServerViewModel.UiState.Loading
    val context = LocalContext.current

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Brush.linearGradient(listOf(Color.Black, Color(0xFF001721))))
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.Center)
        ) {
            Text(
                text = stringResource(id = CoreR.string.add_server),
                fontSize = 48.sp,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(32.dp))
            OutlinedTextField(
                value = text,
                leadingIcon = {
                    Icon(
                        painter = painterResource(id = CoreR.drawable.ic_server),
                        contentDescription = null
                    )
                },
                onValueChange = { text = it },
                label = { Text(text = stringResource(id = CoreR.string.edit_text_server_address_hint)) },
                singleLine = true,
                keyboardOptions = KeyboardOptions(
                    autoCorrect = false,
                    keyboardType = KeyboardType.Uri,
                    imeAction = ImeAction.Go
                ),
                isError = isError,
                enabled = !isLoading,
                supportingText = {
                    if (isError) {
                        Text(
                            text = (uiState as AddServerViewModel.UiState.Error).message.joinToString {
                                it.asString(
                                    context.resources
                                )
                            },
                            color = MaterialTheme.colorScheme.error
                        )
                    }
                },
                modifier = Modifier
                    .width(360.dp)
            )
            Spacer(modifier = Modifier.height(16.dp))
            Box {
                Button(
                    onClick = {
                        addServerViewModel.checkServer(text)
                    },
                    enabled = !isLoading,
                    modifier = Modifier.width(360.dp)
                ) {
                    Text(text = stringResource(id = CoreR.string.button_connect))
                }
                // TODO better progress indicator
                if (isLoading) {
                    CircularProgressIndicator(
                        color = MaterialTheme.colorScheme.primary,
                        modifier = Modifier
                            .width(48.dp)
                            .height(48.dp)
                            .padding(8.dp)
                    )
                }
            }
        }
    }
}
