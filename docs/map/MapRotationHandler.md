The map rotation handler changes the map automatically after a set time. 
To use the map rotation handler, at least **2 maps** must be available.

**How I can use the Map Rotation Handler?**
```java
@Override
public void onGameEnable() {
    //First of all you have to instantiate a new map manager object.
    //You can do this easily by calling getMapManager() in the onGameEnabled() method of your main class.
    //Second you have to call the method getMapRotationHandler() and set the rotation interval, 
    //in this case the interval is set to 600 seconds ~ 10 minutes.
    getMapManager()
            .getMapRotationHandler()
            .setRotationInterval(600)
            .startRotation();
}
```

### `setRotationInterval(long roationInvterval)`
This method sets the rotation interval, if the interval is less than 60 seconds the method will throw a **IllegalArgumentException**.

### `startRotation()`
Starts the map rotation for the set interval. Throws a **IllegalStateException** if the rotation is already running.

### `getCurrentMap()`
Returns the current map as GameAPIMap object.

### `getLastRotation()`
Returns the timestamp of the last rotation as long.

### `getRotationInterval()`
Returns the set rotation interval as long.

### `getFormattedTimeUntilNextRotation`
Returns the remaining time until the next change as formatted string.