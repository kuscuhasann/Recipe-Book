package com.pakt_games.recipebook

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.provider.MediaStore
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import kotlinx.android.synthetic.main.fragment_recipe.*


class RecipeFragment : Fragment() {


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


}