package com.bignerdranch.nyethack

data class Coordinate(val x: Int, val y: Int) {
    operator fun plus(other: Coordinate) = Coordinate(x + other.x, y + other.y)
}

enum class Direction(
    private val directionCoordinate: Coordinate
    ) {
    North(Coordinate(0, -1)),
    East(Coordinate(1, 0)),
    South(Coordinate(0, 1)),
    West(Coordinate(-1, 0));

    fun updateCoordinate(coordinate: Coordinate) =
        coordinate + directionCoordinate
}

@JvmInline
value class Kilometers(private val kilometers: Double) {
    operator fun plus(other: Kilometers) =
        Kilometers(kilometers + other.kilometers)

    fun toMiles() = kilometers / 1.609
}

@JvmInline
value class Miles(private val miles: Double) {
    operator fun plus(other: Miles) =
        Miles(miles + other.miles)

    fun toKilometers() = miles * 1.609
}