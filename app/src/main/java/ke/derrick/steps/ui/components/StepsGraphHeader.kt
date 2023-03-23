package ke.derrick.steps.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.DropdownMenu
import androidx.compose.material.DropdownMenuItem
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import ke.derrick.steps.R
import ke.derrick.steps.ui.theme.RoundedShapes

@Composable
fun StepsGraphHeader(numSteps: Int = 0) {
    Row(horizontalArrangement = Arrangement.SpaceBetween, modifier = Modifier.fillMaxWidth()) {
        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(
            dimensionResource(id = R.dimen.spacing_md))) {
            Surface(shape = RoundedShapes.large, color = MaterialTheme.colorScheme.surface,
                modifier = Modifier.size(55.dp)
            ) {
                Icon(painter = painterResource(id = R.drawable.ic_flame_fill_24dp),
                    contentDescription = null, modifier = Modifier.padding(dimensionResource(id = R.dimen.spacing_md)),
                    tint = MaterialTheme.colorScheme.secondary)
            }
            Column {
                Text(text = numSteps.toString(), style = MaterialTheme.typography.headlineSmall)
                Text(text = "STEPS", style = MaterialTheme.typography.labelLarge,
                    modifier = Modifier.padding(start = 4.dp))
            }
        }

        Box {
            var expanded by rememberSaveable { mutableStateOf(false) }

            Row(horizontalArrangement = Arrangement.spacedBy(dimensionResource(id = R.dimen.spacing_sm)),
            modifier = Modifier.clickable { expanded = true }) {
                Text(text = "this week", style = MaterialTheme.typography.labelMedium)
                Icon(painter = painterResource(id = R.drawable.ic_caret_down_16dp),
                    contentDescription = null)
            }

            DropdownMenu(expanded = expanded,
                offset = DpOffset(x=8.dp, y=8.dp),
                onDismissRequest = { expanded = false }) {
                DropdownMenuItem(onClick = { /*TODO*/ }) {
                     Text(text = stringResource(id = R.string.dropdown_menu_item_last_week),
                        modifier = Modifier.padding(start = 4.dp),
                        style = MaterialTheme.typography.bodyMedium)
                }
            }
        }

    }
}

@Preview(showBackground = true)
@Composable
fun PreviewStepsGraphHeader() {
    StepsGraphHeader()
}
