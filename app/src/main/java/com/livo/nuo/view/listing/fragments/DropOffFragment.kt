package com.livo.nuo.view.listing.fragments

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.IntentSender
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.location.Address
import android.location.Geocoder
import android.net.Uri
import android.os.AsyncTask
import android.os.Bundle
import android.os.Handler
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.common.api.ResolvableApiException
import com.google.android.gms.common.api.Status
import com.google.android.gms.location.*
import com.google.android.gms.maps.*
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.libraries.places.api.Places
import com.google.android.libraries.places.api.model.Place
import com.google.android.libraries.places.widget.Autocomplete
import com.google.android.libraries.places.widget.AutocompleteActivity
import com.google.android.libraries.places.widget.AutocompleteSupportFragment
import com.google.android.libraries.places.widget.listener.PlaceSelectionListener
import com.google.android.libraries.places.widget.model.AutocompleteActivityMode
import com.kusu.PlaceSearchWidget
import com.kusu.SearchResult
import com.livo.nuo.R
import com.livo.nuo.lib.rating_bar.BaseRatingBar.TAG
import com.livo.nuo.models.DateDayModel
import com.livo.nuo.utility.MapUtility
import com.livo.nuo.view.listing.NewListingActivity
import com.livo.nuo.view.listing.adapter.PickUpAdapter
import org.w3c.dom.Text
import java.io.IOException
import java.text.DateFormat
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*
import java.util.regex.Pattern


class DropOffFragment : Fragment() , OnMapReadyCallback {

    private var datedayList = ArrayList<DateDayModel>()
    lateinit var rvDateList:RecyclerView
    private var currActivity : Activity? = null

    private var mMap: GoogleMap? = null
    private var mLocationPermissionGranted = false
    private var citydetail: TextView? = null
    private var addressline2: EditText? = null

    private var isZooming = false

    //Declaration of FusedLocationProviderClient
    private var fusedLocationProviderClient: FusedLocationProviderClient? = null
    private val filterTaskList: MutableList<AsyncTask<*, *, *>> =
        ArrayList()
    var regex = "^(-?\\d+(\\.\\d+)?),\\s*(-?\\d+(\\.\\d+)?)$"
    var latLongPattern = Pattern.compile(regex)
    private var doAfterPermissionProvided = 0
    private var doAfterLocationSwitchedOn = 1
    private var currentLatitude = 0.0
    private var currentLongitude = 0.0
    private var locationRequest: LocationRequest? = null
    private var locationCallback: LocationCallback? = null


    lateinit var tvEnterPickUpAddress:TextView
    lateinit var etPickupLocation:EditText
    lateinit var tvSubmit:TextView
    lateinit var etAddressNote:EditText
    lateinit var tvCharacterCount:TextView
    lateinit var pref:SharedPreferences

    private val AUTOCOMPLETE_REQUEST_CODE = 62

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    companion object {
        private const val REQUEST_CHECK_SETTINGS = 2
        private const val REQUEST_ID_MULTIPLE_PERMISSIONS = 2
        fun newInstance() : Fragment{
            val f = DropOffFragment()
            return f
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        var root= inflater.inflate(R.layout.fragment_drop_off, container, false)

        tvEnterPickUpAddress=root.findViewById(R.id.tvEnterPickUpAddress)
        etPickupLocation=root.findViewById(R.id.etPickupLocation)
        rvDateList=root.findViewById(R.id.rvDateList)
        tvSubmit=root.findViewById(R.id.tvSubmit)
        etAddressNote=root.findViewById(R.id.etAddressNote)
        tvCharacterCount=root.findViewById(R.id.tvCharacterCount)

        ininViews()


        if (!Places.isInitialized()) {
            Places.initialize(currActivity!!, getString(R.string.api_key), Locale.US);
        }
        // Initialize the AutocompleteSupportFragment.
        val autocompleteFragment =
            childFragmentManager.findFragmentById(R.id.autocomplete_fragment)
                    as AutocompleteSupportFragment

        // Specify the types of place data to return.
        autocompleteFragment.setPlaceFields(listOf(Place.Field.ID, Place.Field.NAME))

        // Set up a PlaceSelectionListener to handle the response.
        autocompleteFragment.setOnPlaceSelectedListener(object : PlaceSelectionListener {
            override fun onPlaceSelected(place: Place) {
                // TODO: Get info about the selected place.
                Log.i(TAG, "Place: ${place.name}, ${place.id}")
            }
            override fun onError(status: Status) {
                // TODO: Handle the error.
                Log.i(TAG, "An error occurred: $status")
            }
        })



        return root
    }

    fun ininViews(){

        currActivity=requireActivity()


        /*val date = Date()
        var df = SimpleDateFormat("yyyy-MM-dd")
        val c1 = Calendar.getInstance()
        val currentDate = df.format(date)

        c1.add(Calendar.DAY_OF_YEAR, 30)
        df = SimpleDateFormat("yyyy-MM-dd")
        val resultDate = c1.time
        val dueDate = df.format(resultDate)

        val dates = getDates(currentDate, dueDate)
        for (date in dates!!) {
            datedayList?.add(
                DateDayModel(
                    date.date.toString(),
                    date.day.toString(),
                    SimpleDateFormat("yyyy").format(date) as String+"-"+ SimpleDateFormat("MM").format(date) as String+"-"+ SimpleDateFormat("dd").format(date) as String
                )
            )
        }

        rvDateList.setHasFixedSize(true)
        rvDateList.layoutManager = LinearLayoutManager(currActivity,
            LinearLayoutManager.HORIZONTAL,false)
        var adapter = PickUpAdapter(currActivity!!,datedayList,1)
        rvDateList.adapter = adapter
*/


        tvEnterPickUpAddress.setOnClickListener({

            /*PlaceSearchWidget.initialize(currActivity,
                currActivity!!.resources.getString(R.string.api_key),
                object : PlaceSearchWidget.PlaceSearchListener {
                    override fun successPlaceSearch(searchResult: SearchResult) {
                        tvEnterPickUpAddress.setText(searchResult.label)
                    }

                    override fun failedPlaceSearch(error: String) {

                    }
                })*/

            val fields = listOf(Place.Field.ID, Place.Field.NAME,Place.Field.LAT_LNG,Place.Field.ADDRESS)

            // Start the autocomplete intent.
            val intent = Autocomplete.IntentBuilder(AutocompleteActivityMode.FULLSCREEN, fields)
                .build(currActivity!!)
            startActivityForResult(intent, AUTOCOMPLETE_REQUEST_CODE)
        })



        // Initialize bundle
        (currActivity as NewListingActivity).dropaddressBundle = Bundle()

        //intitalization of FusedLocationProviderClient
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(currActivity!!)

        //Prepare for Request for current location
        getLocationRequest()

        //define callback of location request
        locationCallback = object : LocationCallback() {
            override fun onLocationAvailability(locationAvailability: LocationAvailability) {
                Log.d(
                    TAG,
                    "onLocationAvailability: isLocationAvailable =  " + locationAvailability.isLocationAvailable
                )
            }

            override fun onLocationResult(locationResult: LocationResult) {
                Log.d(TAG, "onLocationResult: $locationResult")
                if (locationResult == null) {
                    return
                }
                when (doAfterLocationSwitchedOn) {
                    1 -> startParsingAddressToShow()
                    2 ->                         //on click of imgCurrent
                        showCurrentLocationOnMap(false)
                    3 ->                         //on Click of Direction Tool
                        showCurrentLocationOnMap(true)
                }

                //Location fetched, update listener can be removed
                fusedLocationProviderClient?.removeLocationUpdates(locationCallback)
            }
        }

        // Try to obtain the map from the SupportMapFragment.
        val mapFragment =
            childFragmentManager.findFragmentById(R.id.map1) as SupportMapFragment
        mapFragment.getMapAsync(this)
        //if you want to open the location on the LocationPickerActivity through intent
        val i = currActivity!!.intent
        if (i != null) {
            val extras = i.extras
            if (extras != null) {
                (currActivity as NewListingActivity).dropmAddress = extras.getString(MapUtility.ADDRESS)
                //temp -> get lat , log from db
                (currActivity as NewListingActivity).dropmLatitude =
                    currActivity!!.intent.getDoubleExtra(MapUtility.LATITUDE, 0.0)
                (currActivity as NewListingActivity).dropmLongitude =
                    currActivity!!.intent.getDoubleExtra(MapUtility.LONGITUDE, 0.0)
            }
        }



        tvEnterPickUpAddress.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(p0: Editable?) {

            }

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

                tvSubmit.setText(currActivity!!.resources.getString(R.string.select))
                tvSubmit.background = currActivity!!.resources.getDrawable(R.drawable.red_round_shap)

                Handler().postDelayed({
                    pref = currActivity!!.getSharedPreferences("DropOff", Context.MODE_PRIVATE)
                    var dat = pref.contains("date")!!

                    if (dat)
                        (currActivity as NewListingActivity).showButtonNext()
                    else
                        (currActivity as NewListingActivity).hideButtonNext()

                },50)
            }

        })
        etPickupLocation.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(p0: Editable?) {

            }

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
//                pickupAddress = p0!!.toString()
            }

        })
        etAddressNote.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(p0: Editable?) {

            }

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                (currActivity as NewListingActivity).dropAddressNote = p0!!.toString()
                tvCharacterCount.text = ""+  (currActivity as NewListingActivity).productTitle.length+activity!!.resources.getString(R.string.count_characters120)
            }

        })


    }


    private fun getLocationRequest() {
        locationRequest = LocationRequest()
        locationRequest!!.interval = 10000
        locationRequest!!.fastestInterval = 3000
        locationRequest!!.priority = LocationRequest.PRIORITY_HIGH_ACCURACY
    }



    private fun getDates(dateString1: String, dateString2: String): List<Date>? {
        val dates: ArrayList<Date> = ArrayList<Date>()
        val df1: DateFormat = SimpleDateFormat("yyyy-MM-dd")
        var date1: Date? = null
        var date2: Date? = null
        try {
            date1 = df1.parse(dateString1)
            date2 = df1.parse(dateString2)
        } catch (e: ParseException) {
            e.printStackTrace()
        }
        val cal1: Calendar = Calendar.getInstance()
        cal1.setTime(date1)
        val cal2: Calendar = Calendar.getInstance()
        cal2.setTime(date2)
        while (!cal1.after(cal2)) {
            dates.add(cal1.getTime())
            cal1.add(Calendar.DATE,1)
        }
        return dates
    }


    fun getLocationFromAddress(context: Context?, strAddress: String?): LatLng? {
        val coder = Geocoder(context)
        val address: List<Address>?
        var p1: LatLng? = null
        try {
            // May throw an IOException
            address = coder.getFromLocationName(strAddress, 5)
            if (address == null) {
                return null
            }
            if(address.isNotEmpty()){
                val location = address[0]
                p1 = LatLng(location.latitude, location.longitude)
                (currActivity as NewListingActivity).dropmLatitude = location.latitude
                (currActivity as NewListingActivity).dropmLongitude = location.longitude
                addMarker()
            }

        } catch (ex: IOException) {
            ex.printStackTrace()
        }
        return p1
    }


    private fun startParsingAddressToShow() {
        //get address from intent to show on map
        if ((currActivity as NewListingActivity).dropmAddress == null ||
            (currActivity as NewListingActivity).dropmAddress!!.isEmpty()) {

            //if intent does not have address,
            //cell is blank
            showCurrentLocationOnMap(false)
        } else  //check if address contains lat long, then extract
        //format will be lat,lng i.e 19.23234,72.65465
            if (latLongPattern.matcher((currActivity as NewListingActivity).dropmAddress).matches()) {
                val p =
                    Pattern.compile("(-?\\d+(\\.\\d+)?)") // the pattern to search for
                val m = p.matcher((currActivity as NewListingActivity).dropmAddress)

                // if we find a match, get the group
                var i = 0
                while (m.find()) {
                    // we're only looking for 2s group, so get it
                    if (i == 0) (currActivity as NewListingActivity).dropmLatitude = m.group().toDouble()
                    if (i == 1) (currActivity as NewListingActivity).dropmLongitude = m.group().toDouble()
                    i++
                }
                //show on map
                addressByGeoCodingLatLng
                addMarker()
            } else {
                //get  latlong from String address via reverse geo coding
                //Since lat long not present in db
                if ((currActivity as NewListingActivity).dropmLatitude == 0.0 &&
                    (currActivity as NewListingActivity).dropmLongitude == 0.0) {
                    latLngByRevGeoCodeFromAdd
                } else {
                    // Latlong is more accurate to get exact point on map ,
                    // String address might not be sufficient (i.e Mumbai, Mah..etc)
                    addMarker()
                }
            }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putDouble("latitude", (currActivity as NewListingActivity).dropmLatitude)
        outState.putDouble("longitude", (currActivity as NewListingActivity).dropmLongitude)
        outState.putString("userAddress", (currActivity as NewListingActivity).dropmAddress)
        outState.putBundle("addressBundle", (currActivity as NewListingActivity).dropaddressBundle)
        outState.putDouble("currentLatitude", currentLatitude)
        outState.putDouble("currentLongitude", currentLongitude)
    }


    private fun addMarker() {
        val cameraUpdate: CameraUpdate
        val SPACE = " , "
        val coordinate = LatLng((currActivity as NewListingActivity).dropmLatitude,
            (currActivity as NewListingActivity).dropmLongitude)
        if (mMap != null) {
            val markerOptions: MarkerOptions
            try {
                mMap!!.clear()
                tvEnterPickUpAddress!!.setText("" + (currActivity as NewListingActivity).dropmAddress)
                (currActivity as NewListingActivity).dropAddress = ""+(currActivity as NewListingActivity).dropmAddress
                etPickupLocation.setText(""+(currActivity as NewListingActivity).dropmAddress)
                markerOptions = MarkerOptions().position(coordinate)
                    .title((currActivity as NewListingActivity).dropmAddress).icon(
                        BitmapDescriptorFactory.fromBitmap(
                            resizeMapIcons(
                                "location_red",
                                60,
                                60
                            )
                        )
                    )
                cameraUpdate = if (isZooming) {
                    //  camera will not Update
                    CameraUpdateFactory.newLatLngZoom(coordinate, mMap!!.cameraPosition.zoom)
                } else {
                    // camera will Update zoom
                    CameraUpdateFactory.newLatLngZoom(coordinate, 18f)
                }
                mMap!!.animateCamera(cameraUpdate)
                mMap!!.mapType = GoogleMap.MAP_TYPE_NORMAL
                val marker = mMap!!.addMarker(markerOptions)
                //marker.showInfoWindow();
            } catch (ex: Exception) {
                ex.printStackTrace()
            }
        }
        try {
            (currActivity as NewListingActivity).dropuserAddressline2 =
                (currActivity as NewListingActivity).dropuserAddressline2!!.substring(0,
                    (currActivity as NewListingActivity).dropuserAddressline2!!.indexOf(
                        (currActivity as NewListingActivity).dropuserCity!!))
            // userAddressline.replace(userCity,"");
            //  userAddressline.replace(userPostalCode,"");
            //   userAddressline.replace(userState,"");
            //  userAddressline.replace(userCountry,"");
        } catch (ex: Exception) {
            Log.d(TAG, "address error $ex")
        }
        try {
            addressline2?.setText((currActivity as NewListingActivity).dropuserAddressline2)
            citydetail!!.text =
                (currActivity as NewListingActivity).dropuserCity + SPACE +
                        (currActivity as NewListingActivity).dropuserPostalCode +
                        SPACE + (currActivity as NewListingActivity).dropuserState +
                        SPACE + (currActivity as NewListingActivity).dropuserCountry
        } catch (ex: Exception) {
            ex.printStackTrace()
        }
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
        mMap!!.clear()
        mMap!!.mapType = GoogleMap.MAP_TYPE_NORMAL
        mMap!!.uiSettings.isMapToolbarEnabled = false
        if (mMap!!.isIndoorEnabled) {
            mMap!!.isIndoorEnabled = false
        }
        mMap!!.setInfoWindowAdapter(object : GoogleMap.InfoWindowAdapter {
            // Use default InfoWindow frame
            override fun getInfoWindow(arg0: Marker): View? {
                return null
            }

            // Defines the contents of the InfoWindow
            override fun getInfoContents(arg0: Marker): View {
                val v: View =
                    layoutInflater.inflate(R.layout.info_window_layout, null)

                // Getting the position from the marker
                val latLng = arg0.position
                (currActivity as NewListingActivity).dropmLatitude = latLng.latitude
                (currActivity as NewListingActivity).dropmLongitude = latLng.longitude
                val tvLat = v.findViewById<TextView>(R.id.address)
                tvLat.text = (currActivity as NewListingActivity).dropmAddress
                return v
            }
        })
        mMap!!.uiSettings.isZoomControlsEnabled = true

        // Setting a click event handler for the map
        mMap!!.setOnMapClickListener { latLng ->
            mMap!!.clear()
            (currActivity as NewListingActivity).dropmLatitude = latLng.latitude
            (currActivity as NewListingActivity).dropmLongitude = latLng.longitude
            Log.e("latlng", latLng.toString() + "")
            isZooming = true
            addMarker()
            if (!MapUtility.isNetworkAvailable(currActivity!!)) {
                MapUtility.showToast(currActivity!!, "Please Connect to Internet")
            }
            addressByGeoCodingLatLng
        }
        if (checkAndRequestPermissions()) {
            startParsingAddressToShow()
        } else {
            doAfterPermissionProvided = 1
        }
    }


    private fun checkAndRequestPermissions(): Boolean {
        val locationPermission = ContextCompat.checkSelfPermission(
            currActivity!!,
            Manifest.permission.ACCESS_FINE_LOCATION
        )
        val coarsePermision = ContextCompat.checkSelfPermission(
            currActivity!!,
            Manifest.permission.ACCESS_COARSE_LOCATION
        )
        val listPermissionsNeeded: MutableList<String> =
            ArrayList()
        if (locationPermission != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.ACCESS_FINE_LOCATION)
        }
        if (coarsePermision != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.ACCESS_COARSE_LOCATION)
        }
        if (!listPermissionsNeeded.isEmpty()) {
            ActivityCompat.requestPermissions(
                currActivity!!,
                listPermissionsNeeded.toTypedArray(),
                REQUEST_ID_MULTIPLE_PERMISSIONS
            )
            return false
        }

        //getSettingsLocation();
        return true
    }

    private fun showCurrentLocationOnMap(isDirectionClicked: Boolean) {
        if (checkAndRequestPermissions()) {
            @SuppressLint("MissingPermission") val lastLocation =
                fusedLocationProviderClient!!.lastLocation
            lastLocation.addOnSuccessListener(
                currActivity!!
            ) { location ->
                if (location != null) {
                    mMap!!.clear()
                    if (isDirectionClicked) {
                        currentLatitude = location.latitude
                        currentLongitude = location.longitude
                        val mLatitude = (currActivity as NewListingActivity).dropmLatitude
                        val mLongitude = (currActivity as NewListingActivity).dropmLongitude
                        //Go to Map for Directions
                        val intent = Intent(
                            Intent.ACTION_VIEW, Uri.parse(
                                "http://maps.google.com/maps?saddr=$currentLatitude, $currentLongitude&daddr=$mLatitude, $mLongitude"
                            )
                        )
                        currActivity!!.startActivity(intent)
                    } else {
                        //Go to Current Location
                        (currActivity as NewListingActivity).dropmLatitude = location.latitude
                        (currActivity as NewListingActivity).dropmLongitude = location.longitude
                        addressByGeoCodingLatLng
                    }
                } else {
                    //Gps not enabled if loc is null
                    settingsLocation
//                    Toast.makeText(
//                        currActivity!!,
//                        "Location not Available",
//                        Toast.LENGTH_SHORT
//                    ).show()
                }
            }
            lastLocation.addOnFailureListener { //If perm provided then gps not enabled
                //                getSettingsLocation();
//                Toast.makeText(
//                    currActivity!!,
//                    "Location Not Availabe",
//                    Toast.LENGTH_SHORT
//                ).show()
            }
        }
    }

    fun resizeMapIcons(iconName: String?, width: Int, height: Int): Bitmap {
        val imageBitmap = BitmapFactory.decodeResource(
            resources,
            resources.getIdentifier(iconName, "drawable", currActivity!!.packageName)
        )
        return Bitmap.createScaledBitmap(imageBitmap, width, height, false)
    }


    private val addressByGeoCodingLatLng: Unit
        private get() {

            //Get string address by geo coding from lat long
            if ((currActivity as NewListingActivity).dropmLatitude != 0.0 &&
                (currActivity as NewListingActivity).dropmLongitude != 0.0) {
                if (MapUtility.popupWindow != null && MapUtility.popupWindow.isShowing()) {
                    MapUtility.hideProgress()
                }
                Log.d(TAG, "getAddressByGeoCodingLatLng: START")
                //Cancel previous tasks and launch this one
                for (prevTask in filterTaskList) {
                    prevTask.cancel(true)
                }
                filterTaskList.clear()
                val asyncTask = GetAddressFromLatLng()
                filterTaskList.add(asyncTask)
                asyncTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR,
                    (currActivity as NewListingActivity).dropmLatitude,
                    (currActivity as NewListingActivity).dropmLongitude)
            }
        }//Cancel previous tasks and launch this one

    //Get string address by geo coding from lat long
    private val latLngByRevGeoCodeFromAdd: Unit
        private get() {

            //Get string address by geo coding from lat long
            if ((currActivity as NewListingActivity).dropmLatitude == 0.0 &&
                (currActivity as NewListingActivity).dropmLongitude == 0.0) {
                if (MapUtility.popupWindow != null && MapUtility.popupWindow.isShowing()) {
                    MapUtility.hideProgress()
                }
                Log.d(TAG, "getLatLngByRevGeoCodeFromAdd: START")
                //Cancel previous tasks and launch this one
                for (prevTask in filterTaskList) {
                    prevTask.cancel(true)
                }
                filterTaskList.clear()
                val asyncTask = GetLatLngFromAddress()
                filterTaskList.add(asyncTask)
                asyncTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, (currActivity as NewListingActivity).dropmAddress)
            }
        }


    private val settingsLocation: Unit
        private get() {
            val builder = LocationSettingsRequest.Builder()
                .addLocationRequest(locationRequest)
            val result =
                LocationServices.getSettingsClient(currActivity!!).checkLocationSettings(builder.build())
            result.addOnCompleteListener { task ->
                try {
                    val response = task.getResult(
                        ApiException::class.java
                    )
                    // All location settings are satisfied. The client can initialize location
                    // requests here.
                    //...
                    if (response != null) {
                        val locationSettingsStates =
                            response.locationSettingsStates
                        Log.d(TAG, "getSettingsLocation: $locationSettingsStates")
                        startLocationUpdates()
                    }
                } catch (exception: ApiException) {
                    Log.d(TAG, "getSettingsLocation: $exception")
                    when (exception.statusCode) {
                        LocationSettingsStatusCodes.RESOLUTION_REQUIRED ->                             // Location settings are not satisfied. But could be fixed by showing the
                            // user a dialog.
                            try {
                                // Cast to a resolvable exception.
                                val resolvable =
                                    exception as ResolvableApiException
                                // Show the dialog by calling startResolutionForResult(),
                                // and check the result in onActivityResult().
                                resolvable.startResolutionForResult(
                                    currActivity!!,
                                    REQUEST_CHECK_SETTINGS
                                )
                            } catch (e: IntentSender.SendIntentException) {
                                // Ignore the error.
                            } catch (e: ClassCastException) {
                                // Ignore, should be an impossible error.
                            }
                        LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE -> {
                        }
                    }
                }
            }
        }

    @SuppressLint("StaticFieldLeak")
    private inner class GetAddressFromLatLng :
        AsyncTask<Double?, Void?, Bundle?>() {
        var latitude: Double? = null
        var longitude: Double? = null
        override fun onPreExecute() {
            super.onPreExecute()
            MapUtility.showProgress(currActivity!!)
        }

        protected override fun doInBackground(vararg p0: Double?): Bundle? {
            return try {
                latitude = p0[0]
                longitude = p0[1]
                val geocoder: Geocoder
                val addresses: List<Address>?
                geocoder = Geocoder(currActivity!!, Locale.getDefault())
                val sb = StringBuilder()

                //get location from lat long if address string is null
                addresses = geocoder.getFromLocation(latitude!!, longitude!!, 1)
                if (addresses != null && addresses.size > 0) {
                    val address = addresses[0].getAddressLine(0)
                    if (address != null) (currActivity as NewListingActivity).dropaddressBundle!!.putString("addressline2", address)
                    sb.append(address).append(" ")
                    val city = addresses[0].locality
                    if (city != null) (currActivity as NewListingActivity).dropaddressBundle!!.putString("city", city)
                    //sb.append(city).append(" ")
                    val state = addresses[0].adminArea
                    if (state != null) (currActivity as NewListingActivity).dropaddressBundle!!.putString("state", state)
                    //sb.append(state).append(" ")
                    val country = addresses[0].countryName
                    if (country != null) (currActivity as NewListingActivity).dropaddressBundle!!.putString("country", country)
                    //sb.append(country).append(" ")
                    val postalCode = addresses[0].postalCode
                    if (postalCode != null) (currActivity as NewListingActivity).dropaddressBundle!!.putString("postalcode", postalCode)
                    //sb.append(postalCode).append(" ")
                    // return sb.toString();
                    (currActivity as NewListingActivity).dropaddressBundle!!.putString("fulladdress", sb.toString())
                    (currActivity as NewListingActivity).dropaddressBundle
                } else {
                    null
                }
            } catch (e: IOException) {
                e.printStackTrace()
                (currActivity as NewListingActivity).dropaddressBundle!!.putBoolean("error", true)
                (currActivity as NewListingActivity).dropaddressBundle
                //return roundAvoid(latitude) + "," + roundAvoid(longitude);
            }

            // return bu;
        }

        // setting address into different components
        override fun onPostExecute(userAddress: Bundle?) {
            super.onPostExecute(userAddress)
            (currActivity as NewListingActivity).dropmAddress = userAddress!!.getString("fulladdress").toString()
            (currActivity as NewListingActivity).dropuserCity = userAddress.getString("city")
            (currActivity as NewListingActivity).dropuserState = userAddress.getString("state")
            (currActivity as NewListingActivity).dropuserPostalCode = userAddress.getString("postalcode")
            (currActivity as NewListingActivity).dropuserCountry = userAddress.getString("country")
            (currActivity as NewListingActivity).dropuserAddressline2 = userAddress.getString("addressline2")
            MapUtility.hideProgress()
            addMarker()
        }
    }

    private inner class GetLatLngFromAddress :
        AsyncTask<String?, Void?, LatLng>() {
        override fun onPreExecute() {
            super.onPreExecute()
            MapUtility.showProgress(currActivity!!)
        }

        protected override fun doInBackground(vararg p0: String?): LatLng? {
            var latLng = LatLng(0.0, 0.0)
            try {
                val geocoder: Geocoder
                val addresses: List<Address>?
                geocoder = Geocoder(currActivity!!, Locale.getDefault())

                //get location from lat long if address string is null
                addresses = geocoder.getFromLocationName((currActivity as NewListingActivity).dropmAddress?.get(0).toString(), 1)
                if (addresses != null && addresses.size > 0) {
                    latLng = LatLng(addresses[0].latitude, addresses[0].longitude)
                }
            } catch (ignored: Exception) {
            }
            return latLng
        }

        override fun onPostExecute(latLng: LatLng) {
            super.onPostExecute(latLng)
            (currActivity as NewListingActivity).dropmLatitude = latLng.latitude
            (currActivity as NewListingActivity).dropmLongitude = latLng.longitude
            MapUtility.hideProgress()
            addMarker()
        }
    }

    fun roundAvoid(value: Double): Double {
        val scale = Math.pow(10.0, 6.0)
        return Math.round(value * scale) / scale
    }

    override fun onDestroy() {
        super.onDestroy()
        for (task in filterTaskList) {
            task.cancel(true)
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        mLocationPermissionGranted = false
        when (requestCode) {
            REQUEST_ID_MULTIPLE_PERMISSIONS -> {

                // If request is cancelled, the result arrays are empty.
                if (grantResults.size > 0
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED
                ) {
                    mLocationPermissionGranted = true
                }
            }
        }
    }

    override fun onResume() {
        super.onResume()
        //Do tasks for which permission was granted by user in onRequestPermission()
        if (!currActivity!!.isFinishing && mLocationPermissionGranted) {
            // perform action required b4 asking permission
            mLocationPermissionGranted = false
            when (doAfterPermissionProvided) {
                1 -> startParsingAddressToShow()
                2 -> showCurrentLocationOnMap(false)
                3 -> showCurrentLocationOnMap(true)
            }
        }

    }

    private fun startLocationUpdates() {
        if (ContextCompat.checkSelfPermission(
                currActivity!!,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(
                currActivity!!,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
//            Toast.makeText(
//                currActivity!!,
//                "Location not Available",
//                Toast.LENGTH_SHORT
//            ).show()
            return
        }
        fusedLocationProviderClient!!.requestLocationUpdates(
            locationRequest,
            locationCallback,
            null /* Looper */
        )
            .addOnSuccessListener { Log.d(TAG, "startLocationUpdates: onSuccess: ") }
            .addOnFailureListener { e ->
                if (e is ApiException) {
                    Log.d(
                        TAG,
                        "startLocationUpdates: " + e.message
                    )
                } else {
                    Log.d(TAG, "startLocationUpdates: " + e.message)
                }
            }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == AUTOCOMPLETE_REQUEST_CODE) {
            when (resultCode) {
                Activity.RESULT_OK -> {
                    data?.let {
                        val place = Autocomplete.getPlaceFromIntent(data)
                        tvEnterPickUpAddress.setText("${place.address}")

                        val data = place.latLng.toString() // assume this is the data

                        val tempArray =data.substring(data.indexOf("(") + 1, data.lastIndexOf(")")).split(",")
                            .toTypedArray()
                        val latitude = tempArray[0].toDouble()
                        val longitude = tempArray[1].toDouble()
                        Log.i(TAG, "Place: $latitude")
                        (currActivity as NewListingActivity).dropmLatitude=latitude
                        (currActivity as NewListingActivity).dropmLongitude=longitude

                        updateLocation()
                    }
                }
                AutocompleteActivity.RESULT_ERROR -> {
                    // TODO: Handle the error.
                    data?.let {
                        val status = Autocomplete.getStatusFromIntent(data)

                    }
                }
                Activity.RESULT_CANCELED -> {
                    // The user canceled the operation.
                }
            }
            return
        }
        super.onActivityResult(requestCode, resultCode, data)
    }

    fun updateLocation(){
        etPickupLocation.setText(tvEnterPickUpAddress.text.toString())
        (currActivity as NewListingActivity).dropAddress = tvEnterPickUpAddress.text.toString()


        (currActivity as NewListingActivity).dropuserCity = tvEnterPickUpAddress.text.toString()
        (currActivity as NewListingActivity).dropmAddress = tvEnterPickUpAddress.text.toString()
        getLocationFromAddress(currActivity,tvEnterPickUpAddress.text.toString())
        tvSubmit.background = currActivity!!.resources.getDrawable(R.drawable.black_round_shape_less_corners)
        tvSubmit.setText(currActivity!!.resources.getString(R.string.selected))

    }

    override fun setUserVisibleHint(isVisibleToUser: Boolean) {
        super.setUserVisibleHint(isVisibleToUser)
        Handler().postDelayed({
        if (isVisibleToUser) {

            val date = Date()
            var df = SimpleDateFormat("yyyy-MM-dd")
            val c1 = Calendar.getInstance()
            val currentDate = df.format(date)

            pref = currActivity!!.getSharedPreferences("PickUp", Context.MODE_PRIVATE)
            var dat = pref.getString("date", "")!!

            c1.add(Calendar.DAY_OF_YEAR, 30)
            df = SimpleDateFormat("yyyy-MM-dd")
            val resultDate = c1.time
            val dueDate = df.format(resultDate)


            datedayList.clear()
            val dates = getDates(dat, dueDate)
            for (date in dates!!) {
                datedayList?.add(
                    DateDayModel(
                        date.date.toString(),
                        date.day.toString(),
                        SimpleDateFormat("yyyy").format(date) as String+"-"+ SimpleDateFormat("MM").format(date) as String+"-"+ SimpleDateFormat("dd").format(date) as String
                    )
                )
            }

            rvDateList.setHasFixedSize(true)
            rvDateList.layoutManager = LinearLayoutManager(currActivity,
                LinearLayoutManager.HORIZONTAL,false)
            var adapter = PickUpAdapter(currActivity!!,datedayList,1)
            rvDateList.adapter = adapter
            adapter.notifyDataSetChanged()
            rvDateList.visibility=View.VISIBLE

        } else {
            rvDateList.visibility=View.GONE
        }
        },2000)
    }

}