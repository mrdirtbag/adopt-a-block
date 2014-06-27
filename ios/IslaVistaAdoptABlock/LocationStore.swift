//
//  LocationStore.swift
//  IslaVistaAdoptABlock
//
//  Created by Thomas Kuo on 6/26/14.
//  Copyright (c) 2014 Citrix Hackathon. All rights reserved.
//

import CoreLocation

class LocationStore: NSObject, CLLocationManagerDelegate {
    
    var locationManager: CLLocationManager = CLLocationManager()
    var locationDictionary: Array<Dictionary<String,AnyObject>>?
    var currentLocations = Dictionary<String,Double>[]()
    
    func startLocation() {
        startLocation(nil)
    }
    
    func startLocation(delegate: CLLocationManagerDelegate?) {
        if currentLocations.count > 0 {
            currentLocations.removeAll(keepCapacity: false)
        }
        
        // Create the location manager if this object does not
        // already have one.
        if locationManager == nil {
            locationManager = CLLocationManager()
        }
        
        if !delegate {
            locationManager.delegate = self
        } else {
            locationManager.delegate = delegate
        }
        
        locationManager.desiredAccuracy = kCLLocationAccuracyHundredMeters;
        
        // Set a movement threshold for new events.
        locationManager.distanceFilter = 10; // meters
        
        locationManager.requestAlwaysAuthorization()
        locationManager.startUpdatingLocation()
    }
    
    func stopLocation() {
        locationManager.stopUpdatingLocation()
    }
    
    
    func locationManager(manager: CLLocationManager!, didUpdateLocations locations: AnyObject[]!) {
        let currLocation: CLLocation = locations[0] as CLLocation
        let epoch = round(1000*currLocation.timestamp.timeIntervalSince1970)
        let locationDictionary = ["lat": currLocation.coordinate.latitude, "long": currLocation.coordinate.longitude, "epoch": epoch]
        
        currentLocations += locationDictionary
    }
    
    func locationManager(manager: CLLocationManager!, didFailWithError error: NSError!) {
        println("LM Error")
    }
    
    func submitLocation(name: String, uid: String, num_buckets: Int) {
        var submission = ["username": name, "hashID":uid, "buckets":num_buckets, "points": currentLocations, "userCategory": "TEST", "comments": "I'm a grouch"]
        
        var e: NSError?
        let jsonData = NSJSONSerialization.dataWithJSONObject(
            submission,
            options: NSJSONWritingOptions(0),
            error: &e)
        
        var jsonString: String
        if !jsonData {
            jsonString = ""
        } else {
            jsonString = NSString(data: jsonData, encoding: NSUTF8StringEncoding)
        }
        
        println(jsonString)
        
        var request = NSMutableURLRequest(URL: NSURL(string: "http://adopt-a-block.herokuapp.com/ivCollection"))
        request.HTTPMethod = "POST"
        request.setValue("application/json", forHTTPHeaderField: "Content-Type")
        
        let uploadSession = NSURLSession.sharedSession()
        
        let uploadTask = uploadSession.uploadTaskWithRequest(request, fromData: jsonData) {data, response, error -> Void in
            if (!error) {
                println("Worked!")
            } else {
                println("Didn't work :(")
            }
        }
        
        uploadTask.resume()
    }
    
}