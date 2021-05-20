package com.example.main_light_app

//import android.R
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView


class ContactsAdapter(
    private val Type_: Array<String>,
    private val Title_: Array<String>,
    private val imgid_dots: Array<Int>,
    private val imgid_edit: Array<Int>,
    private val imgid_on: Array<Int>
) : RecyclerView.Adapter<ContactsAdapter.ViewHolder>()
{


    // Provide a direct reference to each of the views within a data item
    // Used to cache the views within the item layout for fast access
    inner class ViewHolder(listItemView: View) : RecyclerView.ViewHolder(listItemView) {

        val textView = listItemView.findViewById(R.id.textView_x) as TextView
        val textView2 = listItemView.findViewById(R.id.textView2) as TextView
        val imageView = listItemView.findViewById(R.id.imageView1) as ImageView
        val imageView2 = listItemView.findViewById(R.id.imageView2) as ImageView
        val imageView3 = listItemView.findViewById(R.id.imageView3) as ImageView

    }

    // ... constructor and member variables
    // Usually involves inflating a layout from XML and returning the holder
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ContactsAdapter.ViewHolder {
        val context = parent.context
        val inflater = LayoutInflater.from(context)
        // Inflate the custom layout
        val contactView = inflater.inflate(R.layout.rowelement, parent, false)
        // Return a new holder instance

        return ViewHolder(contactView)
    }

    // Involves populating data into the item through holder
    override fun onBindViewHolder(viewHolder: ContactsAdapter.ViewHolder, position: Int) {
        // Get the data model based on position
        //val contact: Contact = mContacts.get(position)

        viewHolder.textView.text = Type_[position]
        viewHolder.textView2.text = Title_[position]
        viewHolder.imageView.setImageResource(imgid_dots[position])
        viewHolder.imageView2.setImageResource(imgid_edit[position])
        viewHolder.imageView3.setImageResource(imgid_on[position])

        viewHolder.imageView3.setOnClickListener() {
            //Toast.makeText(mContext, "Ho cliccato!", Toast.LENGTH_SHORT).show()
            viewHolder.imageView3.setImageResource(R.drawable.ic_settings_power_24px_2);
            imgid_on[position]=R.drawable.ic_settings_power_24px_2
        }
    }

    // Returns the total count of items in the list
    override fun getItemCount(): Int {
        return Type_.size
    }
}



class MainActivity : AppCompatActivity() {

    lateinit var button_AP:Button
    lateinit var button_Devices:Button
    lateinit var image_music:ImageView
    lateinit var image_mic:ImageView
    lateinit var broadcaster_receiver: UDPBroadcaster
    lateinit var rv: RecyclerView
    var listofdevices: MutableList<List<String>>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        button_AP =  findViewById<View>(R.id.Change_Wifi_AccessPoint) as Button
        button_Devices =  findViewById<View>(R.id.Scan_for_Devices) as Button

        image_music =  findViewById<View>(R.id.image_music) as ImageView
        image_mic =  findViewById<View>(R.id.image_mic) as ImageView
        image_music.setImageResource(R.mipmap.ic_launcher_round)
        image_mic.setImageResource(R.mipmap.ic_launcher_music_round)

        broadcaster_receiver =  UDPBroadcaster(this)

        button_AP.setOnClickListener {


            val myIntent = Intent(this@MainActivity, scan_wifi::class.java)
            //myIntent.putExtra("key", value) //Optional parameters

            this@MainActivity.startActivity(myIntent)

        }

        button_Devices.setOnClickListener {


            broadcaster_receiver.recvUDPBroadcast()

            Handler().postDelayed({
                listofdevices = broadcaster_receiver.close()
                if (listofdevices!=null){
                val siz = listofdevices?.size as Int

                if (siz > 0)
                {

                var Type_: ArrayList<String> = ArrayList<String>(siz)
                var Title_: ArrayList<String> = ArrayList<String>(siz)
                var imgid_dots: ArrayList<Int> = ArrayList<Int>(siz)
                var imgid_edit: ArrayList<Int> = ArrayList<Int>(siz)
                var imgid_on: ArrayList<Int> = ArrayList<Int>(siz)

                for (u in 0..siz-1) {
                    Type_.add(u.toString())
                    var temptitle: List<String>? = listofdevices?.get(u)
                    Title_.add(temptitle?.get(0) as String)
                    imgid_dots.add( R.drawable.ic_audiotrack_24px)
                    imgid_edit.add( R.drawable.ic_create_24px)
                    imgid_on.add( R.drawable.ic_settings_power_24px)

                }



                rv = findViewById<View>(R.id.RecView_devices) as RecyclerView
                // Initialize contacts
                // Create adapter passing in the sample user data
                val adapter = ContactsAdapter(Type_.toTypedArray(), Title_.toTypedArray(),
                    imgid_dots.toTypedArray(), imgid_edit.toTypedArray(), imgid_on.toTypedArray())
                // Attach the adapter to the recyclerview to populate items
                rv.adapter = adapter
                // Set layout manager to position the items
                rv.layoutManager = LinearLayoutManager(this)

            }}
            }, 3000)

        }

    }
}