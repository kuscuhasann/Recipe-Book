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
import java.io.ByteArrayOutputStream


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
        val foodName=edtFoodName.text.toString()
        val foodMaterials=edtFoodMetarials.text.toString()
        selectedBitmap.let {
            val lowSizeBitmap=createLowSizeBitmap(selectedBitmap!!,300)

            val outputStream=ByteArrayOutputStream()
            lowSizeBitmap.compress(Bitmap.CompressFormat.PNG,50,outputStream)
            val imageByteArray=outputStream.toByteArray()

            try
            {
                context.let {
                    //Create DATABASE
                    val database=it!!.openOrCreateDatabase("FoodsDb",Context.MODE_PRIVATE,null)
                    //Create TABLE
                    database.execSQL("CREATE TABLE IF NOT EXISTS tblFoods(id INTEGER PRIMARY KEY,foodName VARCHAR,foodMaterials VARCHAR, image BLOB) ")
                    //INDEXING VALUES Not (0,1,2) This indexing (?,?,?)=>(1,2,3)
                    val sqlString="INSERT INTO FoodsDb(foodName,foodMaterials,image)VALUES (?,?,?)"
                    val statement=database.compileStatement(sqlString)
                    statement.bindString(1,foodName)
                    statement.bindString(2,foodMaterials)
                    statement.bindBlob(3,imageByteArray)
                    statement.execute()
                }


            }
            catch (e:Exception)
            {
                e.printStackTrace()
            }
        }
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

    fun createLowSizeBitmap(selectedBitmap: Bitmap,maxSize:Int) :Bitmap
    {
        var selectedBitmapWidth=selectedBitmap.width
        var selectedBitmapHeight=selectedBitmap.height

        var bitmapRatio: Double=selectedBitmapWidth.toDouble()/selectedBitmapHeight.toDouble()
        if(bitmapRatio>1)
        {
          selectedBitmapWidth=maxSize
          val lowerHeitgh=selectedBitmapWidth/bitmapRatio
            selectedBitmapHeight  =lowerHeitgh.toInt()
        }
        else
        {
            selectedBitmapWidth=maxSize
            val lowerWidth=selectedBitmapHeight/bitmapRatio
            selectedBitmapWidth  =lowerWidth.toInt()
        }
        return Bitmap.createScaledBitmap(selectedBitmap,selectedBitmapWidth,selectedBitmapHeight,true)
    }


}