package com.livo.nuo


import android.app.Activity
import com.livo.nuo.models.CountryCodeModel
import java.util.ArrayList

object DummyData{


    public fun getCountryList(currContext : Activity) : ArrayList<CountryCodeModel>{
        val countryCodeList = ArrayList<CountryCodeModel>()

        val firstCountryCode = CountryCodeModel()
        firstCountryCode.country_code = "+45"
        firstCountryCode.country_name = currContext.getString(R.string.denmark)
        firstCountryCode.image = R.drawable.denmark

        val secondCountryCode = CountryCodeModel()
        secondCountryCode.country_code = "+46"
        secondCountryCode.country_name = currContext.getString(R.string.sweden)
        secondCountryCode.image = R.drawable.swden

       /* val thirdCountryCode = CountryCodeModel()
        thirdCountryCode.country_code = "+91"
        thirdCountryCode.country_name = currContext.getString(R.string.india)
        thirdCountryCode.image = R.drawable.india*/


        countryCodeList.add(firstCountryCode)
        countryCodeList.add(secondCountryCode)
        //countryCodeList.add(thirdCountryCode)

        return countryCodeList
    }
    public fun getLanguages(currContext : Activity) : ArrayList<CountryCodeModel>{
        val countryCodeList = ArrayList<CountryCodeModel>()

        val firstCountryCode = CountryCodeModel()
        firstCountryCode.id = 0
        firstCountryCode.language_code = "da"
        firstCountryCode.country_name = currContext.getString(R.string.denish)
        firstCountryCode.image = R.drawable.denmark

        val secondCountryCode = CountryCodeModel()
        secondCountryCode.id = 1
        secondCountryCode.language_code = "en"
        secondCountryCode.country_name = currContext.getString(R.string.englishTitle)
        secondCountryCode.image = R.drawable.eng_flag

        val thirdCountryCode = CountryCodeModel()
        thirdCountryCode.id = 2
        thirdCountryCode.language_code = "sv"
        thirdCountryCode.country_name = currContext.getString(R.string.swedish)
        thirdCountryCode.image = R.drawable.flag_sv

        countryCodeList.add(firstCountryCode)
        countryCodeList.add(secondCountryCode)
        countryCodeList.add(thirdCountryCode)

        return countryCodeList
    }
}