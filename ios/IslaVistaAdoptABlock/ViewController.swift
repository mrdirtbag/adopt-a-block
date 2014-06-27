//
//  ViewController.swift
//  IslaVistaAdoptABlock
//
//  Created by Thomas Kuo on 6/25/14.
//  Copyright (c) 2014 Citrix Hackathon. All rights reserved.
//

import UIKit
import CoreLocation

class ViewController: UIViewController, CLLocationManagerDelegate {
    @IBOutlet var nameTextField: UITextField
    @IBOutlet var actionButton: UIButton
    @IBOutlet var bucketLabel: UILabel
    @IBOutlet var bucketTextField: UITextField
    @IBOutlet var submitButton: UIButton
    var locationStore: LocationStore = LocationStore()
                            
    override func viewDidLoad() {
        super.viewDidLoad()
        // Do any additional setup after loading the view, typically from a nib.
        
        showCollectState()
    }

    override func didReceiveMemoryWarning() {
        super.didReceiveMemoryWarning()
        // Dispose of any resources that can be recreated.
    }

    @IBAction func pressedActionButton(sender: UIButton) {
        if sender.titleLabel.text == "Start" {
            sender.setTitle("Stop", forState: UIControlState.Normal)
            
            locationStore.startLocation()
        } else {
            sender.setTitle("Start", forState: UIControlState.Normal)
            
            locationStore.stopLocation()
            
            showSubmitState()
        }
    }

    @IBAction func pressedSubmit(sender: UIButton) {
        println(nameTextField.text)
        
        let defaults = NSUserDefaults.standardUserDefaults()
        let uid : String = String(defaults.objectForKey("UID") as NSString)
        
        if nameTextField.text == "" {
            var alert = UIAlertController(title: "No Name", message: "Enter a name to submit", preferredStyle: UIAlertControllerStyle.Alert)
            alert.addAction(UIAlertAction(title: "Click", style: UIAlertActionStyle.Default, handler: nil))
            self.presentViewController(alert, animated: true, completion: nil)
        } else if bucketTextField.text == "" || !bucketTextField.text.toInt() {
            var alert = UIAlertController(title: "No buckets", message: "Enter the number of buckets collected", preferredStyle: UIAlertControllerStyle.Alert)
            alert.addAction(UIAlertAction(title: "Click", style: UIAlertActionStyle.Default, handler: nil))
            self.presentViewController(alert, animated: true, completion: nil)
        } else {
            locationStore.submitLocation(nameTextField.text, uid: uid, num_buckets: bucketTextField.text.toInt()!)
        }
        
        showCollectState()
    }
    
    func showSubmitState() {
        bucketTextField.hidden = false
        bucketLabel.hidden = false
        submitButton.hidden = false
        
        actionButton.hidden = true
    }
    
    func showCollectState() {
        bucketTextField.hidden = true
        bucketLabel.hidden = true
        submitButton.hidden = true
        
        actionButton.hidden = false
    }
    
}

