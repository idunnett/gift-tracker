package com.example.gifttracker.datamodels


data class CalendarBean(var id:Long, var name:String, var description:String?, var location:String?,
                        var dtStart:Long, var peoples:List<AttendeePersonBean>)
data class AttendeePersonBean(val  email:String, val  name:String?)
