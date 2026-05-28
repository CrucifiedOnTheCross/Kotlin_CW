package coursework.service

import coursework.model.CarWashClient
import coursework.model.CarWashStatistics
import coursework.model.SortField
import kotlinx.serialization.SerializationException
import kotlinx.serialization.builtins.ListSerializer
import kotlinx.serialization.json.Json
import java.io.File
import java.time.LocalDate
import java.time.format.DateTimeParseException

class CarWashService(private val clients: MutableList<CarWashClient> = mutableListOf()) {
    fun all(): List<CarWashClient> = clients.toList()

    fun add(client: CarWashClient): Result<Unit> {
        val validationError = validateClient(client)
        if (validationError != null) return Result.failure(IllegalArgumentException(validationError))
        if (clients.any { it.id == client.id }) {
            return Result.failure(IllegalArgumentException("Клиент с ID ${client.id} уже существует."))
        }
        clients.add(client)
        return Result.success(Unit)
    }

    fun nextId(): Int = (clients.maxOfOrNull { it.id } ?: 0) + 1

    fun findById(id: Int): CarWashClient? = clients.find { it.id == id }

    fun update(id: Int, updated: CarWashClient): Result<Unit> {
        val index = clients.indexOfFirst { it.id == id }
        if (index == -1) return Result.failure(NoSuchElementException("Клиент с ID $id не найден."))
        if (updated.id != id) {
            return Result.failure(IllegalArgumentException("ID клиента нельзя изменять."))
        }

        val validationError = validateClient(updated)
        if (validationError != null) return Result.failure(IllegalArgumentException(validationError))
        clients[index] = updated
        return Result.success(Unit)
    }

    fun delete(id: Int): Boolean = clients.removeIf { it.id == id }

    fun search(query: String): List<CarWashClient> {
        val normalized = query.trim().lowercase()
        if (normalized.isBlank()) return emptyList()

        return clients.filter {
            it.fullName.lowercase().contains(normalized) ||
                it.phone.lowercase().contains(normalized) ||
                it.carNumber.lowercase().contains(normalized) ||
                it.carModel.lowercase().contains(normalized)
        }
    }

    fun sortedBy(field: SortField, descending: Boolean = false): List<CarWashClient> {
        val sorted = when (field) {
            SortField.FULL_NAME -> clients.sortedBy { it.fullName.lowercase() }
            SortField.TOTAL_SPENT -> clients.sortedBy { it.totalSpent }
            SortField.VISIT_COUNT -> clients.sortedBy { it.visitCount }
            SortField.LAST_VISIT_DATE -> clients.sortedBy { LocalDate.parse(it.lastVisitDate) }
        }
        return if (descending) sorted.reversed() else sorted
    }

    fun statistics(): CarWashStatistics {
        val totalRevenue = clients.sumOf { it.totalSpent }
        val averageSpent = if (clients.isEmpty()) 0.0 else totalRevenue / clients.size
        return CarWashStatistics(
            clientCount = clients.size,
            totalRevenue = totalRevenue,
            averageSpent = averageSpent,
            maxSpentClient = clients.maxByOrNull { it.totalSpent }
        )
    }

    fun loadFromFile(fileName: String): LoadResult {
        val file = File(fileName)
        if (!file.exists()) return LoadResult.FileNotFound

        return try {
            val loaded = json.decodeFromString(ListSerializer(CarWashClient.serializer()), file.readText(Charsets.UTF_8))
            clients.clear()
            clients.addAll(loaded)
            LoadResult.Success(loaded.size)
        } catch (_: SerializationException) {
            LoadResult.Error("Файл найден, но содержит некорректный JSON.")
        } catch (exception: Exception) {
            LoadResult.Error("Не удалось прочитать файл: ${exception.message}")
        }
    }

    fun saveToFile(fileName: String) {
        val encoded = json.encodeToString(ListSerializer(CarWashClient.serializer()), clients)
        File(fileName).writeText(encoded, Charsets.UTF_8)
    }

    companion object {
        private val json = Json {
            prettyPrint = true
            ignoreUnknownKeys = true
            encodeDefaults = true
        }

        fun validateClient(client: CarWashClient): String? {
            return when {
                client.fullName.isBlank() -> "ФИО клиента не может быть пустым."
                client.phone.isBlank() -> "Телефон клиента не может быть пустым."
                client.carNumber.isBlank() -> "Госномер автомобиля не может быть пустым."
                client.carModel.isBlank() -> "Модель автомобиля не может быть пустой."
                client.visitCount < 0 -> "Количество посещений не может быть отрицательным."
                client.totalSpent < 0.0 -> "Общая сумма оплат не может быть отрицательной."
                client.discountPercent !in 0..50 -> "Скидка должна быть от 0 до 50 процентов."
                !isValidDate(client.lastVisitDate) -> "Дата последнего визита должна быть в формате ГГГГ-ММ-ДД."
                else -> null
            }
        }

        private fun isValidDate(value: String): Boolean {
            return try {
                LocalDate.parse(value)
                true
            } catch (_: DateTimeParseException) {
                false
            }
        }
    }
}

