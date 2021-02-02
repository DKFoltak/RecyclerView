package edu.gustavo.recyclerview

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.os.Environment
import android.util.Log
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import java.io.File
import java.io.BufferedReader
import java.io.InputStreamReader
import java.io.FileInputStream
import java.io.OutputStreamWriter
import java.io.FileOutputStream

class MainActivity : AppCompatActivity() {

    private val TAG: String = "Ficheros"
    private val REQUEST_SD_WRITE = 1

    var modelos: ModeloViewModel? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(findViewById(R.id.toolbar))

        findViewById<FloatingActionButton>(R.id.add).setOnClickListener { view ->
            Snackbar.make(view, resources.getString(R.string.anadir), Snackbar.LENGTH_LONG)
                .setAction(resources.getString(R.string.accion), null).show()
        }

        modelos = ViewModelProvider(this).get(ModeloViewModel::class.java)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        return when (item.itemId) {
            R.id.action_limpiar -> {
                modelos = ViewModelProvider(this).get(ModeloViewModel::class.java)
                true
            }
            R.id.action_settings -> {
                val intent = Intent(this, SettingsActivity::class.java)
                startActivity(intent)
                true
            }
            R.id.action_backup -> {
                if (!isExternalStorageAvailable || isExternalStorageReadOnly) {
                    Toast.makeText(
                        this,
                        resources.getString(R.string.no_SD_card),
                        Toast.LENGTH_LONG
                    ).show()
                } else {
                    val permission = ContextCompat.checkSelfPermission(
                        this,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE
                    )
                    if (permission != PackageManager.PERMISSION_GRANTED) {
                        Log.i(TAG, "Permission to record denied")
                        if (ActivityCompat.shouldShowRequestPermissionRationale(
                                this,
                                Manifest.permission.WRITE_EXTERNAL_STORAGE
                            )
                        ) {
                            AlertDialog.Builder(this)
                                .setMessage(resources.getString(R.string.peticionPermiso))
                                .setTitle(resources.getString(R.string.titulo_peticion_permiso))
                                .setPositiveButton(resources.getString(R.string.mensaje_ok)) { _, _ ->
                                    Log.i(TAG, "Clicked")
                                    makeRequest()
                                }
                                .create()
                                .show()
                        }
                    }
                    makeRequest()
                }
                true
            }
            R.id.action_restore -> {
                if (!isExternalStorageAvailable) {
                    Toast.makeText(
                        this,
                        resources.getString(R.string.no_SD_card),
                        Toast.LENGTH_LONG
                    ).show()
                } else {
                    try {
                        val fin = BufferedReader(
                            InputStreamReader(
                                FileInputStream(
                                    File(
                                        getExternalFilesDir(null),
                                        "prueba_sd.txt"
                                    )
                                )
                            )
                        )
                        val texto = fin.readLine()
                        Toast.makeText(this, texto, Toast.LENGTH_LONG).show()
                        fin.close()
                    } catch (ex: Exception) {
                        Log.e(TAG, resources.getString(R.string.errorFichero))
                    }
                }
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun makeRequest() {
        ActivityCompat.requestPermissions(
            this,
            arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE),
            REQUEST_SD_WRITE
        )
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        when (requestCode) {
            REQUEST_SD_WRITE -> {
                if (grantResults.isEmpty() || grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                    Log.i(TAG, "Permission has been denied by user")
                } else {
                    BackupAction()
                    Log.i(TAG, "Permission has been granted by user")
                }
            }
            else -> super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        }
    }

    private val isExternalStorageReadOnly: Boolean
        get() {
            val extStorageState = Environment.getExternalStorageState()
            return (Environment.MEDIA_MOUNTED_READ_ONLY == extStorageState)
        }

    private val isExternalStorageAvailable: Boolean
        get() {
            val extStorageState = Environment.getExternalStorageState()
            return (Environment.MEDIA_MOUNTED == extStorageState)
        }

    fun BackupAction() {
        try {
            val fout = OutputStreamWriter(
                FileOutputStream(
                    File(
                        getExternalFilesDir(null),
                        "prueba_sd.txt"
                    )
                )
            )
            fout.write("Texto de prueba.")
            fout.close()
            Toast
                .makeText(this, resources.getString(R.string.ficheroCreado), Toast.LENGTH_LONG)
                .show()
        } catch (ex: Exception) {
            Log.e(TAG, "Error al escribir fichero a tarjeta SD")
        }
    }
}