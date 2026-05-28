package coursework.ui

import coursework.model.CarWashClient
import coursework.model.SortField
import coursework.service.CarWashService

fun addClient(service: CarWashService) {
    println("Добавление клиента")
    val client = readClientData(id = service.nextId())
    service.add(client)
        .onSuccess { println("Клиент добавлен с ID ${client.id}.") }
        .onFailure { println("Ошибка: ${it.message}") }
}

fun editClient(service: CarWashService) {
    val id = readInt("Введите ID клиента для редактирования: ")
    val current = service.findById(id)
    if (current == null) {
        println("Клиент с ID $id не найден.")
        return
    }

    println("Текущие данные:")
    showClients(listOf(current))
    println("Введите новые значения или нажмите Enter, чтобы оставить поле без изменений.")

    val updated = current.copy(
        fullName = readOptionalString("ФИО [${current.fullName}]: ", current.fullName),
        phone = readOptionalString("Телефон [${current.phone}]: ", current.phone),
        carNumber = readOptionalString("Госномер [${current.carNumber}]: ", current.carNumber),
        carModel = readOptionalString("Модель автомобиля [${current.carModel}]: ", current.carModel),
        serviceType = readOptionalServiceType(current.serviceType),
        visitCount = readOptionalInt("Количество посещений [${current.visitCount}]: ", current.visitCount, min = 0),
        totalSpent = readOptionalDouble("Общая сумма оплат [${formatMoney(current.totalSpent)}]: ", current.totalSpent, min = 0.0),
        lastVisitDate = readOptionalDate("Дата последнего визита [${current.lastVisitDate}]: ", current.lastVisitDate),
        discountPercent = readOptionalInt("Скидка, % [${current.discountPercent}]: ", current.discountPercent, min = 0, max = 50)
    )

    service.update(id, updated)
        .onSuccess { println("Данные клиента обновлены.") }
        .onFailure { println("Ошибка: ${it.message}") }
}

fun deleteClient(service: CarWashService) {
    val id = readInt("Введите ID клиента для удаления: ")
    if (service.delete(id)) {
        println("Клиент удален.")
    } else {
        println("Клиент с ID $id не найден.")
    }
}

fun searchClients(service: CarWashService) {
    print("Введите ФИО, телефон, госномер или модель автомобиля: ")
    val query = readln().trim()
    val found = service.search(query)
    if (found.isEmpty()) {
        println("Совпадения не найдены.")
    } else {
        showClients(found)
    }
}

fun sortClients(service: CarWashService) {
    println("1. По ФИО")
    println("2. По общей сумме оплат")
    println("3. По количеству посещений")
    println("4. По дате последнего визита")

    val field = when (readInt("Выберите поле сортировки: ")) {
        1 -> SortField.FULL_NAME
        2 -> SortField.TOTAL_SPENT
        3 -> SortField.VISIT_COUNT
        4 -> SortField.LAST_VISIT_DATE
        else -> {
            println("Ошибка: неизвестное поле сортировки.")
            return
        }
    }

    val descending = readYesNo("Сортировать по убыванию? (д/н): ")
    showClients(service.sortedBy(field, descending))
}

fun showStatistics(service: CarWashService) {
    val statistics = service.statistics()
    println("Количество клиентов: ${statistics.clientCount}")
    println("Общая выручка: ${formatMoney(statistics.totalRevenue)} руб.")
    println("Средняя сумма оплат на клиента: ${formatMoney(statistics.averageSpent)} руб.")
    val leader = statistics.maxSpentClient
    if (leader == null) {
        println("Клиент с максимальной суммой оплат: нет данных")
    } else {
        println("Клиент с максимальной суммой оплат: ${leader.fullName}, ${formatMoney(leader.totalSpent)} руб.")
    }
}

fun readClientData(id: Int): CarWashClient {
    return CarWashClient(
        id = id,
        fullName = readRequiredString("ФИО клиента: "),
        phone = readRequiredString("Телефон: "),
        carNumber = readRequiredString("Госномер автомобиля: "),
        carModel = readRequiredString("Модель автомобиля: "),
        serviceType = readServiceType(),
        visitCount = readInt("Количество посещений: ", min = 0),
        totalSpent = readDouble("Общая сумма оплат, руб.: ", min = 0.0),
        lastVisitDate = readDate("Дата последнего визита (ГГГГ-ММ-ДД): "),
        discountPercent = readInt("Скидка, % (0-50): ", min = 0, max = 50)
    )
}

