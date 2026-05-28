package coursework.model

data class CarWashStatistics(
    val clientCount: Int,
    val totalRevenue: Double,
    val averageSpent: Double,
    val maxSpentClient: CarWashClient?
)

