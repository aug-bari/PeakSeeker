package org.augbari.peakSeeker

import kotlinx.coroutines.experimental.async
import org.augbari.Mqtt
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken
import org.eclipse.paho.client.mqttv3.MqttCallback
import org.eclipse.paho.client.mqttv3.MqttMessage
import org.json.JSONObject
import kotlin.math.abs

/**
 * Peak Seeker is a library used to check if a peak is reached by an accelerometer communication via mqtt broker
 *
 * */
class PeakSeeker(broker: String, clientName: String): MqttCallback {

    // MQTT
    private val mqttClient = Mqtt(broker, clientName)

    // Objects
    private val accelerometer = Accelerometer()

    // Peak
    var peak = PeakType.NONE
    private val peakThreshold = 10.0

    // Custom callback on accelerometer status changed
    var onStatusChangedCallback: () -> Unit = {}

    /**
     * Connect to mqtt server providing username and password.
     *
     * @param username Authentication username used to connect to mqtt server.
     * @param password Authentication password used to connect to mqtt server.
     * */
    fun connect(username: String, password: String) {
        mqttClient.setCallback(this)
        mqttClient.connect(username, password)
    }

    /**
     * Disconnect from mqtt server.
     * */
    fun disconnect() {
        mqttClient.disconnect()
        println("Disconnected")
    }

    /**
     * Subscribe to mqtt topic.
     *
     * @param topic Topic to register to.
     * */
    fun subscribe(topic: String) {
        mqttClient.subscribe(topic)
    }

    /**
     * Send message to a specific topic with a specific content.
     *
     * @param topic Topic to which send the message.
     * @param data String data to send to topic.
     * */
    fun sendMessage(topic: String, data: String) {
        mqttClient.publish(topic, MqttMessage(data.toByteArray()))
    }

    /**
     * On message arrived callback.
     *
     * Required message format := {"gyro":{"x":0.26267204,"y":0.06963864,"z":0.26145032},"accel":{"x":-0.1656,"y":0.8746,"z":-0.41939998}}.
     *
     * @param topic Topic from which the message is arrived.
     * @param message MqttMessage arrived from server.
     * */
    override fun messageArrived(topic: String?, message: MqttMessage?) {

        // Parse JSON data
        val data = JSONObject(message.toString())
        val accel = data.getJSONObject("accel")

        // Check topic
        when(topic) {
            "mpu6050" -> {
                accelerometer.setValues(doubleArrayOf(accel.getDouble("x"), accel.getDouble("y"), accel.getDouble("z")))
            }
            "disconnect" -> {
                disconnect()
            }
        }

        // Check peak if is none
        if(peak == PeakType.NONE) {
            checkPeak()
        }

    }

    /**
     * Check peak in current series.
     *
     * Analyze points and check if a peak is reached in all axes.
     * */
    private fun checkPeak() {

        // Get x y and z series
        val xSeries = accelerometer.series.map { it[0] }
        val ySeries = accelerometer.series.map { it[1] }
        val zSeries = accelerometer.series.map { it[2] }

        // Search peak in x
        xSeries.forEachIndexed { index, value->
            if(index > 0) {

                // x peak is found
                if(abs(value - xSeries[index - 1]) > peakThreshold) {

                    // Update status
                    peak = if(value > 0) PeakType.RIGHT else PeakType.LEFT

                }

            }
        }

        // Search peak in y
        ySeries.forEachIndexed { index, value->
            if(index > 0) {

                // x peak is found
                if(abs(value - ySeries[index - 1]) > peakThreshold) {

                    // Update status
                    peak = if(value > 0) PeakType.FORWARD else PeakType.BACKWARD

                }

            }
        }

        // Search peak in z
        zSeries.forEachIndexed { index, value->
            if(index > 0) {

                // x peak is found
                if(abs(value - zSeries[index - 1]) > peakThreshold) {

                    // Update status
                    peak = if(value > 0) PeakType.UP else PeakType.DOWN

                }

            }
        }

        // If peak is found
        if(peak != PeakType.NONE) {

            // Call status changed callback
            onStatusChangedCallback()

            // Reset system after 1 second
            async {
                Thread.sleep(300)
                accelerometer.clearSeries()
                peak = PeakType.NONE
            }

        }

    }

    /**
     * On connection lost from mqtt server callback.
     *
     * @param cause - check the Eclipse PAHO documentation.
     * */
    override fun connectionLost(cause: Throwable?) {
        println("Connection to broker lost")
    }

    /**
     * On message delivery complete callback.
     *
     * @param token - check the Eclipse PAHO documentation.
     * */
    override fun deliveryComplete(token: IMqttDeliveryToken?) {}

}