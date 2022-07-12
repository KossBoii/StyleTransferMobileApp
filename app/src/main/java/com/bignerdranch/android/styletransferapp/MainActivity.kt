package com.bignerdranch.android.styletransferapp

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix
import android.os.Bundle
import android.os.Trace
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.ImageButton
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.wonderkiln.camerakit.CameraListener
import com.wonderkiln.camerakit.CameraView
import org.tensorflow.contrib.android.TensorFlowInferenceInterface

private const val INPUT_SIZE = 256
private const val INPUT_NAME = "input"
private const val OUTPUT_NAME = "output_new"

// Path to pre-trained models
private const val MODEL_CRAYON = "file:///android_asset/crayon_float.pb"
private const val MODEL_CUBIST = "file:///android_asset/cubist_float.pb"
private const val MODEL_DENOISED = "file:///android_asset/denoised_starry_float.pb"
private const val MODEL_FEATHERS = "file:///android_asset/feathers_float.pb"
private const val MODEL_MOSAIC = "file:///android_asset/mosaic_float.pb"
private const val MODEL_SCREAM = "file:///android_asset/scream_float.pb"
private const val MODEL_UDNIE = "file:///android_asset/udnie_float.pb"
private const val MODEL_WAVE = "file:///android_asset/wave_float.pb"

class MainActivity : AppCompatActivity() {

    private lateinit var botNavView : BottomNavigationView
    private lateinit var mRecyclerView : RecyclerView
    private lateinit var cameraView : CameraView
    private lateinit var imageViewResult : ImageView

    private lateinit var datas : ArrayList<Model>
    private lateinit var resIds : IntArray

    private lateinit var homeAdapter : HomeAdapter
    private lateinit var matrix : Matrix

    private lateinit var intVals : IntArray
    private lateinit var floatVals : FloatArray
    private lateinit var inferenceInterface: TensorFlowInferenceInterface
    private var isProcessing : Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Init Bot NavView
        botNavView = findViewById(R.id.bot_nav)
        botNavView.selectedItemId = R.id.home

        botNavView.setOnNavigationItemSelectedListener { item ->
            when(item.itemId){
                R.id.dashboard -> {
                    startActivity(Intent(applicationContext, DashBoard::class.java))
                    overridePendingTransition(0, 0)
                    true
                }
                R.id.home -> {
                    true
                }
                R.id.about -> {
                    startActivity(Intent(applicationContext, About::class.java))
                    overridePendingTransition(0, 0)
                    true
                }
            }
            true
        }

        intVals = IntArray(INPUT_SIZE * INPUT_SIZE)
        floatVals = FloatArray(INPUT_SIZE * INPUT_SIZE * 3)
        matrix = Matrix()
        matrix.postScale(1f, 1f)

        //---------------------------------------------------------------
        //                       CameraView + RecyclerView
        //---------------------------------------------------------------
        cameraView = findViewById(R.id.camera)
        imageViewResult = findViewById(R.id.Result)
        mRecyclerView = findViewById(R.id.recyclerview)

        var linLayoutManager = LinearLayoutManager(this)
        linLayoutManager.orientation = LinearLayoutManager.HORIZONTAL
        mRecyclerView.layoutManager = linLayoutManager

        //---------------------------------------------------------------
        //                       Init resIds
        //---------------------------------------------------------------
        resIds = intArrayOf(
            R.drawable.a_apersonal_logo, R.drawable.a_crayon, R.drawable.a_cubist,
            R.drawable.a_denoised_starry, R.drawable.a_feathers, R.drawable.a_mosaic,
            R.drawable.a_scream, R.drawable.a_udnie, R.drawable.a_wave
        )

        //---------------------------------------------------------------
        //                       Init datas
        //---------------------------------------------------------------
        datas = ArrayList<Model>()
        for(i in 0..8){
            var model : Model = Model()
            model.type = i
            model.resId = resIds[i]
            datas.add(model)
        }

        homeAdapter = HomeAdapter(datas)
        mRecyclerView.adapter = homeAdapter

        //---------------------------------------------------------------
        //                       Setup CameraView
        //---------------------------------------------------------------
        cameraView.setCameraListener(object : CameraListener() {
            override fun onPictureTaken(picture: ByteArray) {
                if(!isProcessing){
                    isProcessing = true
                    super.onPictureTaken(picture)
                    val bitmap0 = BitmapFactory.decodeByteArray(picture, 0, picture.size)
                    var bitmap = Bitmap.createScaledBitmap(bitmap0, INPUT_SIZE, INPUT_SIZE, false)
                    bitmap = Bitmap.createBitmap(
                            bitmap, 0, 0, bitmap.width, bitmap.height,
                            matrix, true
                    )

                    val bitmapOut: Bitmap = processImage(bitmap)

                    this@MainActivity.runOnUiThread(java.lang.Runnable {
                        imageViewResult.setImageBitmap(bitmapOut)
                        imageViewResult.visibility = View.VISIBLE
                    })
                    isProcessing = false
                }
            }
        })

        //---------------------------------------------------------------
        //                       Setup Listeners
        //---------------------------------------------------------------
        homeAdapter.buttonSetOnClick(object : ButtonInterface {
            override fun onClick(view: View, model: Model) {
                var isimageViewResultShown = true
                for(i in 1..8){
                    if(datas[i].isChosen && datas[i] === model){
                        imageViewResult.visibility = View.GONE
                        isimageViewResultShown = false
                    }
                }

                if(isimageViewResultShown){
                    when (model.type) {
                        0 -> {
                            cameraView.toggleFacing()
                            imageViewResult.visibility = View.GONE
                        }
                        1 -> {  // MODEL_CRAYON
                            inferenceInterface = TensorFlowInferenceInterface(assets, MODEL_CRAYON)
                            cameraView.captureImage()
                        }
                        2 -> {  // MODEL_CUBIST
                            inferenceInterface = TensorFlowInferenceInterface(assets, MODEL_CUBIST)
                            cameraView.captureImage()
                        }
                        3 -> {  // MODEL_DENOISED
                            inferenceInterface = TensorFlowInferenceInterface(assets, MODEL_DENOISED)
                            cameraView.captureImage()
                        }
                        4 -> {  // MODEL_FEATHERS
                            inferenceInterface = TensorFlowInferenceInterface(assets, MODEL_FEATHERS)
                            cameraView.captureImage()
                        }
                        5 -> {  // MODEL_MOSAIC
                            inferenceInterface = TensorFlowInferenceInterface(assets, MODEL_MOSAIC)
                            cameraView.captureImage()
                        }
                        6 -> {  // MODEL_SCREAM
                            inferenceInterface = TensorFlowInferenceInterface(assets, MODEL_SCREAM)
                            cameraView.captureImage()
                        }
                        7 -> {  // MODEL_UDNIE
                            inferenceInterface = TensorFlowInferenceInterface(assets, MODEL_UDNIE)
                            cameraView.captureImage()
                        }
                        8 -> {  // MODEL_WAVE
                            inferenceInterface = TensorFlowInferenceInterface(assets, MODEL_WAVE)
                            cameraView.captureImage()
                        }
                    }
                }

                for(i in 0..8){
                    if(i != model.type && i!= 0)
                        datas[i].isChosen = false
                }
                model.isChosen = !model.isChosen
                homeAdapter.notifyDataSetChanged()
            }
        })

        cameraView.setCropOutput(true)

    }

    override fun onResume() {
        super.onResume()
        cameraView.start()
    }

    override fun onPause() {
        cameraView.stop()
        super.onPause()
    }

    private fun preprocessImage(origin: Bitmap, newWidth: Int, newHeight: Int) : Bitmap? {
        if(origin != null){
            val widthRatio : Float = newWidth.toFloat() / origin.width
            val heightRatio : Float = newHeight.toFloat() / origin.height

            var matrix : Matrix = Matrix()
            matrix.postScale(widthRatio, heightRatio)
            val newBitmap : Bitmap = Bitmap.createBitmap(
                origin, 0, 0, origin.width, origin.height,
                matrix, false
            )
            if(!origin.isRecycled()){
                origin.recycle()
            }
            return newBitmap
        }
        return null
    }

    fun processImage(bitmap: Bitmap) : Bitmap {
        val scaledBitmap: Bitmap? = preprocessImage(bitmap, INPUT_SIZE, INPUT_SIZE)
        scaledBitmap?.getPixels(
            intVals, 0, scaledBitmap.width, 0, 0,
            scaledBitmap.width, scaledBitmap.height
        )

        for (i in intVals.indices) {
            val tempVal: Int = intVals[i]
            floatVals[i * 3 + 0] = ((tempVal shr 16) and 0xFF) * 1.0f
            floatVals[i * 3 + 1] = ((tempVal shr 8) and 0xFF) * 1.0f
            floatVals[i * 3 + 2] = (tempVal and 0xFF) * 1.0f
        }

        Trace.beginSection("feed")
        inferenceInterface.feed(INPUT_NAME, floatVals, INPUT_SIZE.toLong(), INPUT_SIZE.toLong(), 3)
        Trace.endSection()

        Trace.beginSection("run")
        inferenceInterface.run(arrayOf<String>(OUTPUT_NAME))
        Trace.endSection()

        Trace.beginSection("fetch")
        inferenceInterface.fetch(OUTPUT_NAME, floatVals)
        Trace.endSection()

        for (i in intVals.indices) {
            intVals[i] = (-0x1000000
                    or (floatVals[i * 3 + 0].toInt() shl 16)
                    or (floatVals[i * 3 + 1].toInt() shl 8)
                    or floatVals[i * 3 + 2].toInt())
        }
        scaledBitmap?.setPixels(
            intVals, 0, scaledBitmap.width, 0, 0,
            scaledBitmap.width, scaledBitmap.height
        )
        return scaledBitmap!!
    }



    //---------------------------------------------------------------
    //                       HomeHolder (ViewHolder)
    //---------------------------------------------------------------
    private inner class HomeHolder(view: View)
        : RecyclerView.ViewHolder(view) {
        var idButton : ImageButton = view.findViewById(R.id.imageButton)
        var checkBox : CheckBox = view.findViewById(R.id.checkBox)
    }

    //---------------------------------------------------------------
    //                       HomeAdapter
    //---------------------------------------------------------------
    private inner class HomeAdapter(var myList: ArrayList<Model>)
        : RecyclerView.Adapter<HomeHolder>() {
        private lateinit var buttonInterface : ButtonInterface

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HomeHolder {
            val view = layoutInflater.inflate(R.layout.item_home, parent, false)
            return HomeHolder(view)
        }

        override fun onBindViewHolder(holder: HomeHolder, position: Int) {
            holder.idButton.setImageResource(myList[position].resId)
            holder.idButton.setOnClickListener { view ->
                buttonInterface.onClick(view, myList[position])
            }
            Log.i("VIEW_HOLDER", "$(myList[position].isChosen)")
            holder.checkBox.isChecked = myList[position].isChosen
            holder.checkBox.isClickable = false
        }

        override fun getItemCount(): Int = myList.size

        fun buttonSetOnClick(btnInter: ButtonInterface){
            buttonInterface = btnInter
        }
    }


}