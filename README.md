# Peak Seeker
A library used to check peak of a signal by a callback. Signal datas are coming from an accelerometer using mqtt communication protocol.

### Implementation
```sh
// Create PeakSeeker object
val peakSeeker = PeakSeeker("tcp://mybroker.com", "PeakSeekerClientName")

// Perform connection
peakSeeker.connect("username", "password")

// Subscribe to topic
peakSeeker.subscribe("topicName")

// Set custom callback to see status change
peakSeeker.onStatusChangedCallback = {
    println(peakSeeker.peak.toString())
}
```