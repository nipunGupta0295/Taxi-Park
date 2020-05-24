package taxipark

import java.util.*
import kotlin.collections.HashMap
import kotlin.math.min


/*Drivers: [D-0, D-1, D-2, D-3]
Passengers: [P-0, P-1, P-2, P-3, P-4, P-5, P-6, P-7, P-8, P-9]
Trips: [(D-2, [P-9], 9, 36.0), (D-1, [P-0], 15, 28.0), (D-2, [P-1], 37, 30.0), (D-0, [P-9], 24, 10.0), (D-1, [P-2], 1, 6.0), (D-0, [P-0, P-9], 9, 7.0), (D-2, [P-3, P-2, P-8], 18, 39.0, 0.1), (D-1, [P-9, P-4], 19, 1.0, 0.2), (D-1, [P-3], 16, 23.0), (D-2, [P-7], 10, 31.0, 0.2)]*/
/*
 * Task #1. Find all the drivers who performed no trips.
 */
fun TaxiPark.findFakeDrivers(): Set<Driver> = allDrivers.minus(trips.map { it.driver })
/*allDrivers.filter { d -> trips.none{it.driver==d} }.toSet()*/
    /*var contains = false
    val drivers = LinkedList<Driver>()
    for (i in allDrivers) {
        for (j in trips) {
            if (i.name == j.driver.name) {
                contains = true
                break
            }
        }
        if (!contains) {
            drivers.add(i)
        }
        contains = false
    }
    return drivers.toSet()*/



/*
 * Task #2. Find all the clients who completed at least the given number of trips.
 */
fun TaxiPark.findFaithfulPassengers(minTrips: Int): Set<Passenger> {
    var noOfTrips = 0
    val passengers = LinkedList<Passenger>()
    for (i in allPassengers) {
        for (j in trips) {
            if (j.passengers.contains(i)) ++noOfTrips
        }
        if (noOfTrips >= minTrips) passengers.add(i)
        noOfTrips = 0
    }
    return passengers.toSet()
}


/*
 * Task #3. Find all the passengers, who were taken by a given driver more than once.
 */
fun TaxiPark.findFrequentPassengers(driver: Driver): Set<Passenger> {
    var sameDriver = 0
    val passengers = LinkedList<Passenger>()
//    val newTripSet = trips.filter { it.driver == driver }
    for (i in allPassengers) {
        for (j in trips) {
            if (j.driver == driver && j.passengers.contains(i)) ++sameDriver
        }
        if (sameDriver > 1) passengers.add(i)
        sameDriver = 0
    }
    return passengers.toSet()
}


/*
 * Task #4. Find the passengers who had a discount for majority of their trips.
 */
fun TaxiPark.findSmartPassengers(): Set<Passenger> {
    /*val tripMap=HashMap<Double?,Set<Passenger>>()
    for(i in trips){
        tripMap.put(i.discount,i.passengers)
    }
*/
    val passengers = LinkedList<Passenger>()
    var totalTrips = 0
    var discountTrips = 0
    for (i in allPassengers) {
        for (j in trips) {
            if (j.passengers.contains(i)) {
                if (j.discount != null && j.discount > 0) ++discountTrips
                ++totalTrips
            }
        }
        if (discountTrips > totalTrips - discountTrips) passengers.add(i)
        totalTrips = 0
        discountTrips = 0
    }
    return passengers.toSet()
}


/*
 * Task #5. Find the most frequent trip duration among minute periods 0..9, 10..19, 20..29, and so on.
 * Return any period if many are the most frequent, return `null` if there're no trips.
 */
fun TaxiPark.findTheMostFrequentTripDurationPeriod(): IntRange? {
    /*val minMap = trips.groupBy { it.duration }
    var freqDuration=0
    var maxFrequency=0
    if(trips.isEmpty()) return null
    for ((k, v) in minMap) {
        if(v.size>maxFrequency){
            maxFrequency=v.size
            freqDuration=k
        }
    }
    return (freqDuration/10)*10..(freqDuration/10)*10+9*/

    //var map1=trips.associateBy { (it.duration / 10) * 10..(it.duration / 10) * 10 + 9 to it }
    var map = HashMap<IntRange, LinkedList<Trip>>()
    for (i in trips) {
        if (!map.containsKey((i.duration / 10) * 10..(i.duration / 10) * 10 + 9)) {
            var list = LinkedList<Trip>()
            list.add(i)
            map.put((i.duration / 10) * 10..(i.duration / 10) * 10 + 9, list)
        } else {
            var list = map.getValue((i.duration / 10) * 10..(i.duration / 10) * 10 + 9)
            list.add(i)
            map.put((i.duration / 10) * 10..(i.duration / 10) * 10 + 9, list)
        }
    }

    var freqDuration: IntRange? = null
    var maxFrequency = 0
    if (trips.isEmpty()) return null
    for ((k, v) in map) {
        if (v.size > maxFrequency) {
            maxFrequency = v.size
            freqDuration = k
        }
    }
    return freqDuration
}

/*{

}*/
/*
 * Task #6.
 * Check whether 20% of the drivers contribute 80% of the income.
 */
fun TaxiPark.checkParetoPrinciple(): Boolean {
    if (trips.isEmpty()){
        return false
    }
    val map = trips.groupBy { it.driver }
    var totalCost = 0.0
    var tCost = 0.0
    val list = LinkedList<Double>()
    for ((k, v) in map) {
        for (i in v) {
            totalCost += i.cost
            tCost += i.cost
        }
        list.add(totalCost)
        totalCost = 0.0
    }
    list.sortDescending()
    var topCost = 0.0
    for (i in 0 until  (allDrivers.size*0.2).toInt()) {
        topCost += list[i]
    }
    if (topCost >= 0.8 * tCost) return true
    return false
}


fun main(args: Array<String>) {

}