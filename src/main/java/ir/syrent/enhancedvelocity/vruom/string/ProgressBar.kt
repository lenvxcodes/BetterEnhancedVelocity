package ir.syrent.enhancedvelocity.vruom.string

object ProgressBar {

    fun progressBar(current: Int, max: Int, total: Int, completeString: String, notCompleteString: String): String {
        val percent = current.toFloat() / max
        val progressBars = (total * percent).toInt()
        return completeString.repeat(progressBars) + notCompleteString.repeat(total - progressBars)
    }
}
