@file:Suppress("UNUSED_PARAMETER")

package com.github.KathyBekh.Classes

/**
 * Класс "Величина с размерностью".
 *
 * Предназначен для представления величин вроде "6 метров" или "3 килограмма"
 * Общая сложность задания - средняя.
 * Величины с размерностью можно складывать, вычитать, делить, менять им знак.
 * Их также можно умножать и делить на число.
 *
 * В конструктор передаётся вещественное значение и строковая размерность.
 * Строковая размерность может:
 * - либо строго соответствовать одной из abbreviation класса Dimension (m, g)
 * - либо соответствовать одной из приставок, к которой приписана сама размерность (Km, Kg, mm, mg)
 * - во всех остальных случаях следует бросить IllegalArgumentException
 */

fun main() {
    val testVal = DimensionalValue("1 g")
    val testThree = DimensionalValue("3.0 Kg")
    val testTwoV = DimensionalValue(5.0, "m")
    val testFour = DimensionalValue(3.0, "g")
    println(DimensionalValue("2 g"))
    println(testThree)
    println(testThree.plus(testVal))
    println(testVal.minus(testThree))
    println(testTwoV.unaryMinus())
    println(testTwoV.times(3.0))
    println(testTwoV.times(3.0).div(5.0))
    println(testVal.div(testThree))
    println(testVal.equals(testTwoV))
    println(testThree.equals("3.0 g"))
    println(testThree.equals(3.0))
    println(testThree.equals(testThree))
    println(testThree.equals(testFour))
    println(testFour.compareTo(testThree))
    println(testVal.compareTo(testThree))
    println(testFour.compareTo(testVal))

}

class DimensionalValue {
    /**
     * Величина с БАЗОВОЙ размерностью (например для 1.0Kg следует вернуть результат в граммах -- 1000.0)
     */
    val value: Double
//        get() = field * 1000

    /**
     * БАЗОВАЯ размерность (опять-таки для 1.0Kg следует вернуть GRAM)
     */
    val dimension: Dimension
//        get() = field

    constructor(value: Double, dimension: Dimension) {
        this.value = value
        this.dimension = dimension
    }

    constructor(value: Double, abbreviation: String) {
        val dimensionalValue = DimensionPrefix.toDimensionalValue(abbreviation);
        this.value = value * dimensionalValue.value
        this.dimension = dimensionalValue.dimension
    }

    /**
     * Конструктор из строки. Формат строки: значение пробел размерность (1 Kg, 3 mm, 100 g и так далее).
     */
    constructor(amountWithPrefixedDimension: String) {
        val (value, prefixedDimension) = amountWithPrefixedDimension.split(" ")
        val dimensionalValue = DimensionPrefix.toDimensionalValue(prefixedDimension);
        this.value = value.toDouble() * dimensionalValue.value
        this.dimension = dimensionalValue.dimension
    }

    override fun toString(): String {
        return "value: $value dimension: $dimension"
    }

    /**
     * Сложение с другой величиной. Если базовая размерность разная, бросить IllegalArgumentException
     * (нельзя складывать метры и килограммы)
     */
    operator fun plus(other: DimensionalValue): DimensionalValue {
        val sumValue = value + other.value
        if (dimension != other.dimension) {
            throw IllegalArgumentException() as Throwable
        }
        return DimensionalValue(sumValue, dimension)
    }

    /**
     * Смена знака величины
     */
    operator fun unaryMinus(): DimensionalValue {
        val minusVal = -value
        val dim = dimension
        return DimensionalValue(minusVal, dim)
    }

    /**
     * Вычитание другой величины. Если базовая размерность разная, бросить IllegalArgumentException
     */
    operator fun minus(other: DimensionalValue): DimensionalValue {
        if (dimension != other.dimension) {
            throw IllegalArgumentException()
        }
        val minusValue = value - other.value
        return DimensionalValue(minusValue, dimension)
    }

    /**
     * Умножение на число
     */
    operator fun times(other: Double): DimensionalValue {
        val multipliedValue = value * other
        return DimensionalValue(multipliedValue, dimension)
    }

    /**
     * Деление на число
     */
    operator fun div(other: Double): DimensionalValue {
        val valueDivided = value / other
        return DimensionalValue(valueDivided, dimension)
    }

    /**
     * Деление на другую величину. Если базовая размерность разная, бросить IllegalArgumentException
     */
    operator fun div(other: DimensionalValue): Double {
        if (dimension != other.dimension) {
            throw IllegalArgumentException()
        }
        return value / other.value

    }

    /**
     * Сравнение на равенство
     */
    override fun equals(other: Any?): Boolean =
        if (other is DimensionalValue) {
            other.dimension == dimension && other.value == value
        } else {
            false
        }

    /**
     * Сравнение на больше/меньше. Если базовая размерность разная, бросить IllegalArgumentException
     */
    operator fun compareTo(other: DimensionalValue): Int {
        if (dimension != other.dimension) {
            throw IllegalArgumentException()
        }
        return when {
            value == other.value -> 0
            value > other.value -> 1
            else -> -1
        }
    }

    override fun hashCode(): Int {
        var result = value.hashCode()
        result = 31 * result + dimension.hashCode()
        return result
    }
}

/**
 * Размерность. В этот класс можно добавлять новые варианты (секунды, амперы, прочие), но нельзя убирать
 */
enum class Dimension(val abbreviation: String) {
    METER("m"),
    GRAM("g");

    companion object {
        fun fromAbbreviation(abbreviation: String): Dimension {
            for (dimension in Dimension.values()) {
                if (dimension.abbreviation == abbreviation) {
                    return dimension
                }
            }
            throw IllegalArgumentException()
        }
    }
}

/**
 * Приставка размерности. Опять-таки можно добавить новые варианты (деци-, санти-, мега-, ...), но нельзя убирать
 */
enum class DimensionPrefix(val abbreviation: String, val multiplier: Double) {
    KILO("K", 1000.0),
    MILLI("m", 0.001);

    companion object {
        fun toDimensionalValue(prefixedAbbreviation: String): DimensionalValue {
            if (prefixedAbbreviation.length != 1) {
                for (prefix in DimensionPrefix.values()) {
                    for (dimension in Dimension.values()) {
                        if (prefix.abbreviation == prefixedAbbreviation[0].toString()
                            && dimension.abbreviation == prefixedAbbreviation[1].toString()
                        ) {
                            return DimensionalValue(
                                prefix.multiplier,
                                Dimension.fromAbbreviation(dimension.abbreviation)
                            )
                        }
                    }
                }
            }
            return DimensionalValue(1.0, Dimension.fromAbbreviation(prefixedAbbreviation))
        }
    }
}

