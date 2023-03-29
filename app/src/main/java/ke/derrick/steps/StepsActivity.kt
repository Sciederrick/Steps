package ke.derrick.steps

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.os.Bundle
import android.os.IBinder
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.lifecycleScope
import ke.derrick.steps.StepsActivity.Companion.TAG
import ke.derrick.steps.service.StepsTrackerService
import ke.derrick.steps.ui.theme.StepsTheme

class StepsActivity: ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            StepsTheme {
                StepsScreen()
            }
        }
    }

    companion object {
        const val TAG = "StepsActivity"
    }
}

@Composable
fun StepsScreen() {
    val mContext = LocalContext.current
    var mService: StepsTrackerService? by remember { mutableStateOf(null) }
    var mBound: Boolean by rememberSaveable { mutableStateOf(false) }

    /** Defines callbacks for service binding, passed to bindService().  */
    val connection = remember(mContext) {
        object : ServiceConnection {
            override fun onServiceConnected(className: ComponentName, service: IBinder) {
                // We've bound to LocalService, cast the IBinder and get LocalService instance.
                val binder = service as StepsTrackerService.LocalBinder
                mService = binder.getService()
                mBound = true
            }

            override fun onServiceDisconnected(arg0: ComponentName) {
                mBound = false
            }
        }
    }

    DisposableEffect(mContext, connection) {
        Intent(mContext, StepsTrackerService::class.java).also { intent ->
            mContext.bindService(intent, connection, Context.BIND_AUTO_CREATE)
        }
        onDispose {
            mContext.unbindService(connection)
        }
    }

    Box(modifier = Modifier
        .background(MaterialTheme.colorScheme.primary)
        .fillMaxSize()
        .padding(vertical = 16.dp, horizontal = 12.dp)
    ) {
        Button(onClick = {
            val intent = Intent(mContext, MainActivity::class.java).apply {
                flags = Intent.FLAG_ACTIVITY_SINGLE_TOP
            }
            mContext.startActivity(intent)
        },
            modifier = Modifier.align(Alignment.TopStart),
            contentPadding = PaddingValues(start = 0.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent)
        ) {
            Icon(painter = painterResource(id = R.drawable.ic_left_32dp), contentDescription = null)
            Text(text = "Back", style = MaterialTheme.typography.bodyLarge)
        }

        val numSteps = mService?.numSteps?.collectAsState()
        if (mBound) {
            Column(modifier = Modifier.align(Alignment.Center),
                horizontalAlignment = Alignment.CenterHorizontally) {
                Text(color = Color.White,
                    text = numSteps?.value.toString(), textAlign = TextAlign.Center,
                    style = MaterialTheme.typography.headlineLarge)
                Text(color = Color.White,
                    text = "STEPS", textAlign = TextAlign.Center,
                    style = MaterialTheme.typography.labelLarge)
            }
        } else {
            Text(modifier = Modifier.align(Alignment.Center),
                color = Color.White,
                text = "SERVICE NOT AVAILABLE", textAlign = TextAlign.Center,
                style = MaterialTheme.typography.labelLarge)
        }


    }
}

@Preview(showBackground = true)
@Composable
fun PreviewStepsScreen() {
    StepsScreen()
}