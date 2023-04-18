package ke.derrick.steps.util

import ke.derrick.steps.data.local.entities.Steps
import java.time.LocalDate
import java.time.LocalDateTime

/**
 * Creates default values for the graph when the remote source doesn't have any
 * This ensures the graph is always displayed
 * @param mLength The size corresponding to the number of points to display
 */
fun fillWithDefaultPoints(mLength: Int = 7): List<Pair<Float, String>> {
    return (1 .. mLength).map { n -> // default values when the DB is empty
        Pair(0.toFloat(),
            if (n > 31) convertToTwoDigitNumberString(n - 31)
            else convertToTwoDigitNumberString(n))
    }
}

/**
 * The graph requires a certain minimum input size
 * This function fills the input with default values if it doesn't meet the input requirements
 * @param minNumPoints The minimum input size of the graph
 * @param stepsList The raw input from the DB
 */
fun fillGapPoints(minNumPoints: Int = 7, stepsList: List<Steps?>): List<Pair<Float, String>> {
    var count = 1L
    val mDateTime = LocalDateTime.parse(stepsList[0]!!.createdAt)
    val mDate = LocalDate.parse(mDateTime.toString().split("T")[0])
    val actualNumPoints = stepsList.size
    val gapSize = minNumPoints - actualNumPoints
    val gapDaysStrArr = Array(gapSize) { "" }

    val graphList: ArrayList<Pair<Float, String>>
        = List(gapSize) { Pair(0F, "00") } as ArrayList<Pair<Float, String>>

    for (i in (0 until gapSize).reversed()) {
        val mDateBefore = mDate.minusDays(count).dayOfMonth
        graphList[i] = Pair(0F, convertToTwoDigitNumberString(mDateBefore))
        gapDaysStrArr[minNumPoints - (actualNumPoints + 1)] = convertToTwoDigitNumberString(mDateBefore)
        count+=1
    }
    for (steps in stepsList) {
        graphList.add(Pair(steps!!.count.toFloat(), steps.day))
    }

    return graphList.toList()
}