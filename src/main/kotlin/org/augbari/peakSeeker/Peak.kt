package org.augbari.peakSeeker

class Peak {

    var value: Double = -1.0
    var axis: PeakAxis = PeakAxis.UNDEFINED
    var type: PeakType = PeakType.NONE

    enum class PeakType {
        NONE, UP, DOWN, LEFT, RIGHT, FORWARD, BACKWARD
    }

    enum class PeakAxis {
        X, Y, Z, UNDEFINED
    }

}
