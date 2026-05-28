package coursework.ui

import coursework.model.CarWashClient

fun showClients(clients: List<CarWashClient>) {
    if (clients.isEmpty()) {
        println("Список клиентов пуст.")
        return
    }

    println(
        "%-4s %-24s %-15s %-12s %-18s %-20s %-8s %-12s %-12s %-8s".format(
            "ID",
            "ФИО",
            "Телефон",
            "Госномер",
            "Автомобиль",
            "Услуга",
            "Визиты",
            "Оплаты",
            "Дата",
            "Скидка"
        )
    )
    println("-".repeat(140))
    clients.forEach {
        println(
            "%-4d %-24s %-15s %-12s %-18s %-20s %-8d %-12s %-12s %-8s".format(
                it.id,
                it.fullName.take(24),
                it.phone.take(15),
                it.carNumber.take(12),
                it.carModel.take(18),
                it.serviceType.title.take(20),
                it.visitCount,
                formatMoney(it.totalSpent),
                it.lastVisitDate,
                "${it.discountPercent}%"
            )
        )
    }
}

