package coursework

import coursework.service.CarWashService
import coursework.service.LoadResult
import coursework.ui.addClient
import coursework.ui.deleteClient
import coursework.ui.editClient
import coursework.ui.printMainMenu
import coursework.ui.readInt
import coursework.ui.searchClients
import coursework.ui.showClients
import coursework.ui.showStatistics
import coursework.ui.sortClients

fun main() {
    val fileName = "car_wash_clients.json"
    val service = CarWashService()

    when (val result = service.loadFromFile(fileName)) {
        is LoadResult.Success -> println("Загружено записей: ${result.count}")
        LoadResult.FileNotFound -> println("Файл $fileName не найден. Будет создан новый список клиентов.")
        is LoadResult.Error -> println(result.message)
    }

    while (true) {
        printMainMenu()
        when (readInt("Выберите пункт меню: ")) {
            1 -> showClients(service.all())
            2 -> addClient(service)
            3 -> editClient(service)
            4 -> deleteClient(service)
            5 -> searchClients(service)
            6 -> sortClients(service)
            7 -> showStatistics(service)
            8 -> {
                service.saveToFile(fileName)
                println("Данные сохранены в файл $fileName.")
            }
            9 -> {
                service.saveToFile(fileName)
                println("Данные сохранены. Завершение работы.")
                return
            }
            else -> println("Ошибка: выберите пункт от 1 до 9.")
        }
    }
}

