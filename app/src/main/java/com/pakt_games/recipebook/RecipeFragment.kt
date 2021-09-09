package com.pakt_games.recipebook

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import kotlinx.android.synthetic.main.fragment_recipe.*


class RecipeFragment : Fragment() {

    var selectedImage:Uri?=null
    var selectedBitmap:Bitmap?=null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_recipe, container, false)

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        btnSaveRecipeId.setOnClickListener(){
            btnSaveRecipe(it)
        }
        btnImageSelectionId.setOnClickListener()
        {
            btnImageSelection(it)
        }

    }

    fun btnSaveRecipe(view: View)
    {
    println("Button Çalışıyoru")
    }
    fun btnImageSelection(view: View)
    {
        activity.let {

            if(ContextCompat.checkSelfPermission(it!!.applicationContext,Manifest.permission.READ_EXTERNAL_STORAGE) !=PackageManager.PERMISSION_GRANTED)
            {
                //Permission DENIED
                requestPermissions(arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),1)
            }
            else
            {
                //Permission GRANTED
                val galleryIntent=Intent(Intent.ACTION_PICK,MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
                startActivityForResult(galleryIntent,2)
            }
        }

    }
    //Permission Checked
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        if(requestCode==1)
        {
            if(grantResults.size>0 && grantResults[0]==PackageManager.PERMISSION_GRANTED)
            {
                //Permission Taked
                val galleryIntent=Intent(Intent.ACTION_PICK,MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
                startActivityForResult(galleryIntent,2)
            }
        }


        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }
    //Image Gallery Events Checked
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if(requestCode==2 && resultCode==Activity.RESULT_OK && data !=null)
        {
            selectedImage=data.data

            try {
                context?.let {
                    if(selectedImage!=null)
                    {
                        if(Build.VERSION.SDK_INT>=28)
                        {
                            val source=ImageDecoder.createSource(it!!.contentResolver,selectedImage!!)
                            selectedBitmap=ImageDecoder.decodeBitmap(source)
                            btnImageSelectionId.setImageBitmap(selectedBitmap)

                        }
                        else
                        {
                            selectedBitmap=MediaStore.Images.Media.getBitmap(it.contentResolver,selectedImage)
                            btnImageSelectionId.setImageBitmap(selectedBitmap)
                        }
                    }

                }


            }catch (e: Exception)
            {
                e.printStackTrace()
            }
        }
        super.onActivityResult(requestCode, resultCode, data)
    }


}