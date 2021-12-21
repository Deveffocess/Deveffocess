package com.livo.nuo.models

import android.util.Patterns
import java.util.regex.Pattern

class RegExp{

    companion object {

        var EMPTY_TEXT = "^(?!\\s*$).+"
        var MOBILE_NUMBER = "^[0-9]{8,10}$"
        var NUMBER = "^[0-9]{1,12}\$"
        var NAME = "^[a-zA-Z ]*$"
        var _NAME = "^[a-zA-Z0-9]*$"
        var USERNAME = "[a-zA-Z0-9-_]+"
        // public static String PASSWORD = "((?=.*\\d)(?=.*[a-z])(?=.*[A-Z]).{5,20})";
//        var PASSWORD = "(.{6,20})"
        var PASSWORD = "^(?=.*[0-9])(?=.*[A-Z])(?=.*[.`~*(@){#}|\$/:%;^&+=!'<>,])(?=\\S+$).{8,20}$"

        var EMAIL_ADDRESS = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+"

        var isValidDecimal = "\\d+(\\.\\d{2})?|\\.\\d{2}.{0,100}$"
        var isValidRange = "^(10000|\\d)(\\.\\d{1,2})?\$"

        fun  isValidEmail(email1 : String) : Boolean{
            if(Pattern.compile(EMAIL_ADDRESS).matcher(email1).matches()){
                print("Email Address Valid")
                return true
            }else{
                print("Email Address Invalid")
                return false
            }
        }

        fun isValidName(name : String) : Boolean{

            if(Pattern.compile(NAME).matcher(name).matches()){
                print("name Valid")
                return true
            }else{
                print("Invalid Name")
                return false
            }
        }
        fun _isValidName(name : String) : Boolean{

            if(Pattern.compile(_NAME).matcher(name).matches()){
                print("name Valid")
                return true
            }else{
                print("Invalid Name")
                return false
            }
        }
        fun isValidUserName(name : String) : Boolean{

            if(Pattern.compile(USERNAME).matcher(name).matches()){
                print("name Valid")
                return true
            }else{
                print("Invalid Name")
                return false
            }
        }
        fun isNumber(name : String) : Boolean{

            if(Pattern.compile(NUMBER).matcher(name).matches()){
                print("number Valid")
                return true
            }else{
                print("Invalid Number")
                return false
            }
        }
        fun isValidDecimal(number : String) : Boolean{

            if(Pattern.compile(isValidDecimal).matcher(number).matches()){
                print("number Valid")
                return true
            }else{
                print("Invalid Number")
                return false
            }
        }
        fun isValidNumberRange(number : String) : Boolean{

            if(Pattern.compile(isValidRange).matcher(number).matches()){
                print("number Valid")
                return true
            }else{
                print("Invalid Number")
                return false
            }
        }

        fun isValidPassword(password : String) : Boolean{

            if(Pattern.compile(PASSWORD).matcher(password).matches()){
                print("password valid")
                return true
            }else{
                print("password Invalid")
                return false
            }
        }

        fun isValidMobileNumber(mobile :String):Boolean{

            if(Pattern.compile(MOBILE_NUMBER).matcher(mobile).matches()){
                print("mobile valid")
                return true
            }else{
                print("mobile no is invalid")
                return false
            }
        }

        fun checkEmpty(object1 : String?):Boolean{
            if(object1==null || object1==""){
                print("is Empty")
                return true
            }else{
                print("is UnEmpty")
                return false
            }
        }

        fun isInValidOtp(otp : String) : Boolean{
            if(otp.length < 4 || otp.length > 4){
                return  true
            }else{
                return false
            }
        }


    }
}