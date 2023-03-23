package ke.derrick.steps.ui.components

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.material.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import ke.derrick.steps.R
import ke.derrick.steps.ui.theme.RoundedShapes

@Composable
fun StatsCards() {
    Row(modifier = Modifier.horizontalScroll(rememberScrollState())) {
        Card(modifier = Modifier
            .size(300.dp)
            .padding(
                vertical = dimensionResource(id = R.dimen.spacing_sm),
                horizontal = dimensionResource(id = R.dimen.spacing_md)),
            shape = RoundedShapes.small
        ) {
            Box(modifier = Modifier.fillMaxSize()) {
                Image(painter = painterResource(id = R.mipmap.calories_1),
                    contentDescription = null, contentScale = ContentScale.Crop)
                Surface(
                    shape = RoundedShapes.medium,
                    color = MaterialTheme.colorScheme.background,
                    modifier = Modifier
                        .padding(16.dp)
                        .align(Alignment.TopStart)
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(painter = painterResource(id = R.drawable.ic_burn_32dp),
                            contentDescription = null, tint = MaterialTheme.colorScheme.primary)
                        Column(modifier = Modifier.padding(8.dp)) {
                            Text(text = "Calories", style = MaterialTheme.typography.labelSmall)
                            Text(text = "Burnt", style = MaterialTheme.typography.labelSmall)
                        }
                    }

                }

                Text(text = "1.2kCal", style = MaterialTheme.typography.headlineMedium, color = Color.White,
                    modifier = Modifier
                        .padding(16.dp)
                        .align(Alignment.BottomStart))

            }
        }

        Card(modifier = Modifier
            .size(300.dp)
            .padding(
                vertical = dimensionResource(id = R.dimen.spacing_sm),
                horizontal = dimensionResource(id = R.dimen.spacing_md)
            ),
            shape = RoundedShapes.small) {
            Box(modifier = Modifier.fillMaxSize()) {
                Image(painter = painterResource(id = R.mipmap.distance_1),
                    contentDescription = null, contentScale = ContentScale.Crop)

                Surface(
                    shape = RoundedShapes.medium,
                    color = MaterialTheme.colorScheme.background,
                    modifier = Modifier
                        .padding(16.dp)
                        .align(Alignment.TopStart)
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(painter = painterResource(id = R.drawable.ic_walk_32dp),
                            contentDescription = null, tint = MaterialTheme.colorScheme.primary)
                        Column(modifier = Modifier.padding(8.dp)) {
                            Text(text = "Distance", style = MaterialTheme.typography.labelSmall)
                            Text(text = "Covered", style = MaterialTheme.typography.labelSmall)
                        }
                    }

                }

                Text(text = "3.3KM", style = MaterialTheme.typography.headlineMedium, color = Color.White,
                    modifier = Modifier
                        .padding(16.dp)
                        .align(Alignment.BottomStart))
            }
        }
    }
}
