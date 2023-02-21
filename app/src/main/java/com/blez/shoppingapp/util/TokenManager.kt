package com.blez.shoppingapp.util

import android.content.Context
import android.content.SharedPreferences
import android.net.Uri
import com.blez.shoppingapp.util.Constants.PREFS_TOKEN_FILE
import com.blez.shoppingapp.util.Constants.USER_EMAIL
import com.blez.shoppingapp.util.Constants.USER_GIVEN_NAME
import com.blez.shoppingapp.util.Constants.USER_NAME
import com.blez.shoppingapp.util.Constants.USER_PROFILE_PICS
import com.blez.shoppingapp.util.Constants.USER_TOKEN

class TokenManager( private val context : Context){
    private var prefs : SharedPreferences = context.getSharedPreferences(PREFS_TOKEN_FILE,Context.MODE_PRIVATE)

    fun saveToken(token : String)
    {
        val editor = prefs.edit()
        editor.putString(USER_TOKEN,token)
        editor.apply()
    }
    fun saveEmail(email : String)
    {
        val editor = prefs.edit()
        editor.putString(USER_EMAIL,email)
        editor.apply()
    }

    fun saveGivenName(name : String)
    {
        val editor = prefs.edit()
        editor.putString(USER_GIVEN_NAME,name)
        editor.apply()
    }


    fun getGivenName() : String?{
        return prefs.getString(USER_GIVEN_NAME,null)
    }


    fun savePic(pic: String)
    {
        val editor = prefs.edit()
        editor.putString(USER_PROFILE_PICS,pic)
        editor.apply()
    }


    fun getPic() : String?{
        return prefs.getString(USER_PROFILE_PICS,null)
    }


    fun saveUserName(username : String){
        val editor = prefs.edit()
        editor.putString(USER_NAME,username)
        editor.apply()
    }
    fun getUserName() : String?{
        return prefs.getString(USER_NAME,null)
    }



    fun getToken() : String?
    {
        return prefs.getString(USER_TOKEN,null)
    }
    fun getEmail() : String?{
        return prefs.getString(USER_EMAIL,null)
    }
    fun deteleCredit(){
        val editor = prefs.edit()
        editor.clear()
        editor.apply()
    }

}