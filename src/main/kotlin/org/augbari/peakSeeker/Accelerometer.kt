package org.augbari.peakSeeker

/**
 * Object used to store accelerometer data
 * */
class Accelerometer {

    // Current values
    var x: Double = 0.0
    var y: Double = 0.0
    var z: Double= 0.0

    // Data series
    var series: MutableList<DoubleArray> = mutableListOf()

    /**
     * Set x, y, z values of this object
     *
     * @param array specific values to set
     * */
    fun setValues(array: DoubleArray) {
        x = array[0]
        y = array[1]
        z = array[2]

        // Add this values to previous one
        addValuesToSeries()
    }

    /**
     * Add current values to series and remove old ones
     * */
    fun addValuesToSeries() {
        series.add(doubleArrayOf(x, y, z))

        // Remove very old data
        if(series.size > 50) {
            series.removeAt(0)
        }
    }

    /**
     * Clear the series
     * */
    fun clearSeries() {
        series.clear()
    }

    /**
     * Get [x, y, z] values of this object
     * */
    fun getValues(): DoubleArray {
        return doubleArrayOf(x, y, z)
    }

}