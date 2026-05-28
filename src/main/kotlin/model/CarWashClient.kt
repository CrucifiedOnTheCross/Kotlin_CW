package coursework.model

import kotlinx.serialization.Serializable

@Serializable
data class CarWashClient(
    val id: Int,
    val fullName: String,
    val phone: String,
    val carNumber: String,
    val carModel: String,
    val serviceType: ServiceType,
    val visitCount: Int,
    val totalSpent: Double,
    val lastVisitDate: String,
    val discountPercent: Int
)

