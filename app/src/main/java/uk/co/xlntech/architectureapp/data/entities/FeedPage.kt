package uk.co.xlntech.architectureapp.data.entities

data class FeedPage(
        val data: List<TipSummary>,
        val skip: Int,
        val totalCount: Int,
        val resultCount: Int
)