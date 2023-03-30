package ke.derrick.steps.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.DropdownMenu
import androidx.compose.material.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import ke.derrick.steps.R

@Composable
fun TopBar() {
    Row(verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween,
        modifier = Modifier.fillMaxWidth()) {
        Row {
            Icon(painter = painterResource(id = R.drawable.ic_walk_32dp),
                contentDescription = null)
            Text(text = stringResource(id = R.string.app_dashboard), style = MaterialTheme.typography.headlineMedium)
        }

        Box {
            var expanded by rememberSaveable { mutableStateOf(false) }
            IconButton(onClick = { expanded = true }) {
                Icon(painter = painterResource(id = R.drawable.ic_more_32dp),
                    contentDescription = stringResource(id = R.string.menu_options_button_icon))
            }

            DropdownMenu(modifier = Modifier.background(MaterialTheme.colorScheme.surface),
                expanded = expanded, onDismissRequest = { expanded = false }) {
                DropdownMenuItem(onClick = { /*TODO*/ }) {
                    Icon(painter = painterResource(id = R.drawable.ic_map_location_24dp),
                        contentDescription = stringResource(id = R.string.dropdown_menu_item_go_to_google_maps))
                    Text(text = stringResource(id = R.string.dropdown_menu_item_google_maps_text),
                        modifier = Modifier.padding(start = 4.dp))
                }
                DropdownMenuItem(onClick = { /*TODO*/ }) {
                    Icon(painter = painterResource(id = R.drawable.ic_settings_24dp),
                        contentDescription = stringResource(id = R.string.dropdown_menu_item_adjust_settings_icon))
                    Text(text = stringResource(id = R.string.dropdown_menu_item_settings_text),
                        modifier = Modifier.padding(start = 4.dp))
                }
            }
        }

    }
}