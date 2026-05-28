package coursework.model

import kotlinx.serialization.Serializable

@Serializable
enum class ServiceType(val title: String) {
    BASIC_WASH("Базовая мойка"),
    COMPLEX_WASH("Комплексная мойка"),
    DRY_CLEANING("Химчистка салона"),
    POLISHING("Полировка кузова"),
    DETAILING("Детейлинг")
}

