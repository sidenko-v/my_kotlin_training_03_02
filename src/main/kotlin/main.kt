import kotlin.math.min

//В прошлый раз мы рассматривали упрощённый вариант вычисления комиссии. Пришла пора сделать нормальный.
//Q: Почему?

//A: Потому что так дешевле пользователям. За MasterCard и Maestro вообще не нужно платить пока не превысили лимит (замечание от 300 убираем), а для VK Pay всегда бесплатно:
//
//
//https://github.com/netology-code/kt-homeworks/blob/master/03_control/pic/vk-commission.png
//Напишите алгоритм расчёта в виде функции, передавая в функцию:
//
//Тип карты/счёта (по умолчанию - Vk Pay).
//Сумму предыдущих переводов в этом месяце (по умолчанию - 0).
//Сумму совершаемого перевода.
//Функция по-прежнему должна возвращать комиссию в копейках.
//
//Итог: у вас должен быть репозиторий на GitHub, в котором будет ваш Gradle-проект.

const val PAYMENT_TYPE_VK = "Vk Pay"
const val PAYMENT_TYPE_MASTERCARD = "Mastercard"
const val PAYMENT_TYPE_MAESTRO = "Maestro"
const val PAYMENT_TYPE_VISA = "Visa"
const val PAYMENT_TYPE_MIR = "Мир"

fun main() {

    val paymentType = PAYMENT_TYPE_VK
    val amountPreTransferInRub = 10000F
    val amountInRub = 40000F
    val commission = commissionCalculation(paymentType, rubToKopecks(amountPreTransferInRub), rubToKopecks(amountInRub))
}

fun rubToKopecks(amountInRub: Float): Int {
    val amountInKopecks = (amountInRub * 100).toInt()
    return amountInKopecks
}

fun commissionCalculation(
    paymentType: String = PAYMENT_TYPE_VK,
    amountPreTransferInKopecks: Int = 0,
    amountInKopecks: Int
): Int {

    var commission: Int = 0

    when (paymentType) {
        PAYMENT_TYPE_VK -> commission = 0
        PAYMENT_TYPE_MASTERCARD, PAYMENT_TYPE_MAESTRO -> commission =
            commissionCalculationForMastercardMaestro(amountInKopecks, amountPreTransferInKopecks)
        PAYMENT_TYPE_VISA, PAYMENT_TYPE_MIR
        -> commission = commissionCalculationForVisaMir(amountInKopecks)

    }

    return commission
}

fun commissionCalculationForMastercardMaestro(amountInKopecks: Int, amountPreTransferInKopecks: Int): Int {

    var commission: Int = 0

    val maxPreAmount = rubToKopecks(75000F)
    val comPercentage = 0.6F
    val comPart = comPercentage / 100

    when (amountPreTransferInKopecks) {
        in 0..maxPreAmount -> commission = 0
        else -> commission = (amountInKopecks * comPart + rubToKopecks(20F)).toInt()
    }
    return commission
}

fun commissionCalculationForVisaMir(amountInKopecks: Int): Int {
    var commission: Int = 0

    val minCommission: Int = rubToKopecks(35F)
    val comPercentage = 0.75F
    val comPart = comPercentage / 100
    val initCommission = (amountInKopecks * comPart).toInt()

    when (initCommission) {
        in 0..minCommission -> commission = minCommission
        else -> commission = initCommission
    }

    return commission

}
