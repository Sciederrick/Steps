package ke.derrick.steps


interface StepsDestinations {
    val route: String
}

object Main: StepsDestinations {
    override val route = "main"
}

object UserPreferences: StepsDestinations {
    override val route = "user_preferences"
}
