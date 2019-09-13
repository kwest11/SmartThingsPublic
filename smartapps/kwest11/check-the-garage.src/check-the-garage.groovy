/**
 *  Check The Garage
 *
 *  Copyright 2019 Kody West
 *
 *  Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 *  in compliance with the License. You may obtain a copy of the License at:
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software distributed under the License is distributed
 *  on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License
 *  for the specific language governing permissions and limitations under the License.
 *
 */
definition(
    name: "Check The Garage",
    namespace: "kwest11",
    author: "Kody West",
    description: "Checks the garage sensor at a certain time to determine if its closed.",
    category: "Safety & Security",
    iconUrl: "https://s3.amazonaws.com/smartapp-icons/Convenience/Cat-Convenience.png",
    iconX2Url: "https://s3.amazonaws.com/smartapp-icons/Convenience/Cat-Convenience@2x.png",
    iconX3Url: "https://s3.amazonaws.com/smartapp-icons/Convenience/Cat-Convenience@2x.png")


preferences {
  section("At this time every day") {
    input "time", "time", title: "Time of Day"
  }
  section("Make sure it's closed..."){
    input "contact", "capability.contactSensor", title: "Which contact sensor?", required: true
  }
  section( "Notifications" ) {
    input "sendPushMessage", "enum", title: "Send a push notification?", metadata:[values:["Yes", "No"]], required: false
    input "phone", "phone", title: "Send a text message?", required: false
  }
}

def installed() {
  log.debug "Installed with settings: ${settings}"
  schedule(time, "setTimeCallback")
}

def updated(settings) {
  log.debug "Updated with settings: ${settings}"
  unschedule()
  schedule(time, "setTimeCallback")
}

def setTimeCallback() {
    doorOpenCheck()
}

def doorOpenCheck() {
  def currentState = contact.contactState
  if (currentState?.value == "open") {
    def msg = "${contact.displayName} is open."
    log.debug msg
    if (sendPushMessage) {
      sendPush msg
    }
    if (phone) {
      sendSms phone, msg
    }
  } else {
    def msg = "${contact.displayName} is closed."
    log.debug msg
  }
}