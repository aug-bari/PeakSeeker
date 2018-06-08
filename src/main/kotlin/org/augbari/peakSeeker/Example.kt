package org.augbari.peakSeeker

/**
 * Example class to show usage
 * */
class Example {

    companion object : OnPeakListener {

        @JvmStatic
        fun main(args: Array<String>) {

            // Create PeakSeeker object
            val peakSeeker = PeakSeeker("tcp://broker.shiftr.io", "PeakSeeker", this)

            // Perform connection
            peakSeeker.connect("aug-bari", ";)")

            // Subscribe to topic
            peakSeeker.subscribe("mpu6050")

        }

        //
        override fun onPeak(peak: Peak) {
            println("Detected ${peak.type} peak on ${peak.axis} axis with value: ${peak.value}")
        }
    }

}