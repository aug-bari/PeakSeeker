package org.augbari.peakSeeker

/**
 * Example class to show usage
 * */
class Example {

    companion object {
        @JvmStatic
        fun main(args: Array<String>) {

            // Create PeakSeeker object
            val peakSeeker = PeakSeeker("tcp://broker.shiftr.io", "PeakSeeker")

            // Perform connection
            peakSeeker.connect("aug-bari", ";)")

            // Subscribe to topic
            peakSeeker.subscribe("mpu6050")

            // Set custom callback to see status change
            peakSeeker.onStatusChangedCallback = {
                println(peakSeeker.peak.toString())
            }

        }
    }

}