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
    val testThree = DimensionalValue("3.0 g")
    val testTwoV = DimensionalValue(2.0, "m")
    println(testThree)
    println(testThree.plus(testVal))
//    println(testVal.minus(testThree))
    println(testTwoV.unaryMinus())
}

class DimensionalValue  {
    /**
     * Величина с БАЗОВОЙ размерностью (например для 1.0Kg следует вернуть результат в граммах -- 1000.0)
     */
    private val value: Double
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
        this.value = value
        this.dimension = fromAbbreviation(abbreviation)
    }

    /**
     * Конструктор из строки. Формат строки: значение пробел размерность (1 Kg, 3 mm, 100 g и так далее).
     */
    constructor(s: String) {
        val (v, dimension) = s.split(" ")
        value = v.toDouble()
        this.dimension = fromAbbreviation(dimension)
    }

    override fun toString(): String {
        return "value: $value dimension: $dimension"
    }

    /**
     * Сложение с другой величиной. Если базовая размерность разная, бросить IllegalArgumentException
     * (нельзя складывать метры и килограммы)
     */
    operator fun plus(other: DimensionalValue): DimensionalValue {
        val sumValue =  value + other.value
        if (dimension != other.dimension) {
            throw IllegalArgumentException()
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
    operator fun minus(other: DimensionalValue): DimensionalValue = TODO()

    /**
     * Умножение на число
     */
    operator fun times(other: Double): DimensionalValue = TODO()

    /**
     * Деление на число
     */
    operator fun div(other: Double): DimensionalValue = TODO()

    /**
     * Деление на другую величину. Если базовая размерность разная, бросить IllegalArgumentException
     */
    operator fun div(other: DimensionalValue): Double = TODO()

    /**
     * Сравнение на равенство
     */
    override fun equals(other: Any?): Boolean = TODO()

    /**
     * Сравнение на больше/меньше. Если базовая размерность разная, бросить IllegalArgumentException
     */
//    override fun compareTo(other: DimensionalValue): Int = TODO()
}

/**
 * Размерность. В этот класс можно добавлять новые варианты (секунды, амперы, прочие), но нельзя убирать
 */
enum class Dimension(val abbreviation: String) {
    METER("m"),
    GRAM("g");
}

fun fromAbbreviation(abbreviation: String) : Dimension {
    for (d in Dimension.values()) {
        if (d.abbreviation == abbreviation) {
            return d
        }
    }
    throw IllegalArgumentException()
}

/**
 * Приставка размерности. Опять-таки можно добавить новые варианты (деци-, санти-, мега-, ...), но нельзя убирать
 */
enum class DimensionPrefix(val abbreviation: String, val multiplier: Double) {
    KILO("K", 1000.0),
    MILLI("m", 0.001);
}
