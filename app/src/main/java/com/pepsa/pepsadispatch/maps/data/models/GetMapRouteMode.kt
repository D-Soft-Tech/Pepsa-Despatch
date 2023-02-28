package com.pepsa.pepsadispatch.maps.data.models

enum class GetMapRouteMode(val mode: String) {
    MODE_DRIVING("driving"),
    MODE_WALKING("walking"),
    MODE_BICYCLING("bicycling"),
    MODE_TRANSIT("transit"),
}
