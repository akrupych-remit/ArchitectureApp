package uk.co.xlntech.architectureapp.data.entities

data class TipSummary(
        val id: String,
        val name: String,
        val description: String,
        val image: String,
        val category: String,
        val placeName: String,
        val profileName: String,
        val location: Location
)