package com.livo.nuo.utility


import android.net.Uri

class MyAppSession {

    companion object {
        var locale = "en"
        var isLocaleEnglish = false
        var AboutUsLink = "http://sponsee.sg/about-us"
        var TermsLink = "http://sponsee.sg/terms"
        var ContractLink = "http://sponsee.sg/contracting"
        var FAQLink = "http://sponsee.sg/faq"
        var UpdateContract = "UpdateContract"
        var EditRequest = "EditRequest"
        var ReadRequest = "ReadRequest"
        var HideRequest = "HideRequest"
        var CreateRequest = "CreateRequest"
        var Campaign = "Campaign"
        var user = "user"
        var explore = "Explore"
        var All = "All"
        var Add = "Add"
        var pickup_fullAddress = ""
        var pickup_lat = ""
        var pickUp_long = ""
        var pickupDate = ""
        var pickup_addressNote = ""
        var dropoff_fullAddress = ""
        var dropoff_lat = ""
        var dropoff_long = ""
        var dropoffDate = ""
        var dropoff_addressNote = ""
        var productTitle = ""
        var weight = ""
        var width = ""
        var height = ""
        var depth = ""
        var isTwoPeople = false

        var notiIcon=false

        var Withdrawn = "Withdrawn"
        var userToken = ""
        var deviceId = ""
        var deviceType = "1"
        var fcmId: String=""
        var notificationCount = 0
        var userModel = ""
        var langModel = ""
        var Sponsor = "Sponsor"
        var Sponsee = "Sponsee"
        var Categories = ""
        var Name = ""
        var UserName = ""
        var AccountType = ""
        var from = ""
        var FCM_TOKEN = ""
        var SIGNUP = "signup"
        var FORGETPASSWORD = "forgetpassword"
        var LOGIN = "login"
        var FACEBOOK = "facebook"
        var GMAIL = "gmail"
        var EMAIl = "EMAIL"
        var LOGINWITH = ""
        var SIGNUPWITH = ""
        var PHONE = "PHONE"
        var SOCIALNAME = ""
        var fbImage = ""
        var EVENTORGANIZATION = "Event Organiser"
        var ORGANIZATION = "Organisation"
        var INDIVIDUAL = "Individual"
        var FILTER = "Filter"
        var gmailImage : Uri?= null
        var RC_IMAGE = 102
        var Autherization = "Authorization"
        var Approved = "1"
        var Pickup = "2"
        var dropOff = "3"
        var completed = "4"
        var completedSender = "5"
        var productId = 0
        var chatId = 0
        var tranName = ""
        var transImage = ""
        var transporterId = 0
        var price = ""
       // var productModel : ProductModel? = null
        var imageArray = ArrayList<String>()
    }
}