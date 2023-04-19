package fxc.dev.fox_purchase.billing.utils


class BillingFormatUtils {
    companion object {
        fun getTimePackage(period: String?): String {
            return when (period) {
                "P1W" -> TIME_WEEK
                "P1M" -> TIME_MONTH
                "P1Y" -> TIME_YEAR
                else -> ""
            }
        }

        private const val TIME_WEEK = "Week"
        private const val TIME_MONTH = "Month"
        private const val TIME_YEAR = "Year"
    }
}