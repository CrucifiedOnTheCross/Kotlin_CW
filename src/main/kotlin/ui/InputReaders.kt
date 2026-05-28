package coursework.ui

import coursework.model.ServiceType
import java.time.LocalDate
import java.time.format.DateTimeParseException

fun readServiceType(): ServiceType {
    while (true) {
        println("Тип услуги:")
        ServiceType.entries.forEachIndexed { index, type ->
            println("${index + 1}. ${type.title}")
        }
        val index = readInt("Выберите тип услуги: ") - 1
        if (index in ServiceType.entries.indices) return ServiceType.entries[index]
        println("Ошибка: выберите номер из списка.")
    }
}

fun readOptionalServiceType(default: ServiceType): ServiceType {
    println("Тип услуги [${default.title}]:")
    ServiceType.entries.forEachIndexed { index, type ->
        println("${index + 1}. ${type.title}")
    }
    while (true) {
        print("Выберите тип услуги или нажмите Enter: ")
        val input = readln().trim()
        if (input.isBlank()) return default
        val index = input.toIntOrNull()?.minus(1)
        if (index != null && index in ServiceType.entries.indices) return ServiceType.entries[index]
        println("Ошибка: выберите номер из списка.")
    }
}

fun readRequiredString(prompt: String): String {
    while (true) {
        print(prompt)
        val value = readln().trim()
        if (value.isNotBlank()) return value
        println("Ошибка: поле не может быть пустым.")
    }
}

fun readOptionalString(prompt: String, default: String): String {
    print(prompt)
    return readln().trim().ifBlank { default }
}

fun readInt(prompt: String, min: Int? = null, max: Int? = null): Int {
    while (true) {
        print(prompt)
        val value = readln().trim().toIntOrNull()
        if (value == null) {
            println("Ошибка: введите целое число.")
            continue
        }
        if (min != null && value < min) {
            println("Ошибка: значение не может быть меньше $min.")
            continue
        }
        if (max != null && value > max) {
            println("Ошибка: значение не может быть больше $max.")
            continue
        }
        return value
    }
}

fun readOptionalInt(prompt: String, default: Int, min: Int? = null, max: Int? = null): Int {
    while (true) {
        print(prompt)
        val input = readln().trim()
        if (input.isBlank()) return default
        val value = input.toIntOrNull()
        if (value == null) {
            println("Ошибка: введите целое число.")
            continue
        }
        if (min != null && value < min) {
            println("Ошибка: значение не может быть меньше $min.")
            continue
        }
        if (max != null && value > max) {
            println("Ошибка: значение не может быть больше $max.")
            continue
        }
        return value
    }
}

fun readDouble(prompt: String, min: Double? = null): Double {
    while (true) {
        print(prompt)
        val value = readln().trim().replace(',', '.').toDoubleOrNull()
        if (value == null) {
            println("Ошибка: введите число.")
            continue
        }
        if (min != null && value < min) {
            println("Ошибка: значение не может быть меньше ${formatMoney(min)}.")
            continue
        }
        return value
    }
}

fun readOptionalDouble(prompt: String, default: Double, min: Double? = null): Double {
    while (true) {
        print(prompt)
        val input = readln().trim()
        if (input.isBlank()) return default
        val value = input.replace(',', '.').toDoubleOrNull()
        if (value == null) {
            println("Ошибка: введите число.")
            continue
        }
        if (min != null && value < min) {
            println("Ошибка: значение не может быть меньше ${formatMoney(min)}.")
            continue
        }
        return value
    }
}

fun readDate(prompt: String): String {
    while (true) {
        print(prompt)
        val value = readln().trim()
        try {
            LocalDate.parse(value)
            return value
        } catch (_: DateTimeParseException) {
            println("Ошибка: дата должна быть в формате ГГГГ-ММ-ДД.")
        }
    }
}

fun readOptionalDate(prompt: String, default: String): String {
    while (true) {
        print(prompt)
        val value = readln().trim()
        if (value.isBlank()) return default
        try {
            LocalDate.parse(value)
            return value
        } catch (_: DateTimeParseException) {
            println("Ошибка: дата должна быть в формате ГГГГ-ММ-ДД.")
        }
    }
}

fun readYesNo(prompt: String): Boolean {
    while (true) {
        print(prompt)
        when (readln().trim().lowercase()) {
            "д", "да", "y", "yes" -> return true
            "н", "нет", "n", "no" -> return false
            else -> println("Ошибка: введите д/н.")
        }
    }
}

fun formatMoney(value: Double): String = "%.2f".format(value)

