package com.example.explorerscreen

import android.os.Bundle
import android.os.Parcelable
import android.widget.Button
import android.widget.EditText
import android.widget.RadioGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.explorerscreen.databinding.ActivityMainBinding
import com.google.android.material.bottomsheet.BottomSheetDialog


class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var personAdapter: PersonAdapter
    private lateinit var personList: List<Person>
    private var recyclerViewState: Parcelable? = null
    private var minAge: Int? = null
    private var maxAge: Int? = null
    private var gender: String? = null
    private var location: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Initialize data
        personList = listOf(
            Person("John Doe", "25", "Male", "New York"),
            Person("Jane Smith", "30", "Female", "Los Angeles"),
            Person("Sam Wilson", "22", "Male", "Chicago"),
            Person("Anna Johnson", "28", "Female", "Houston"),
            Person("Michael Brown", "35", "Male", "Phoenix"),
            Person("Emily Davis", "29", "Female", "Philadelphia"),
            Person("Daniel Martinez", "31", "Male", "San Antonio"),
            Person("Olivia Jones", "27", "Female", "San Diego"),
            Person("William Garcia", "33", "Male", "Dallas"),
            Person("Sophia Rodriguez", "26", "Female", "San Jose"),
            Person("Matthew Wilson", "34", "Male", "Austin"),
            Person("Isabella Hernandez", "32", "Female", "Jacksonville"),
            Person("Joseph Lopez", "24", "Male", "Fort Worth"),
            Person("Charlotte Gonzalez", "30", "Female", "Columbus"),
            Person("James Perez", "28", "Male", "Charlotte"),
            Person("Amelia Moore", "31", "Female", "Indianapolis"),
            Person("David Taylor", "29", "Male", "San Francisco"),
            Person("Ava Anderson", "27", "Female", "Seattle"),
            Person("Benjamin Thomas", "33", "Male", "Denver"),
            Person("Mia Jackson", "26", "Female", "Washington"),
            Person("Christopher White", "34", "Male", "Boston"),
            Person("Harper Harris", "28", "Female", "El Paso"),
            Person("Ethan Martin", "32", "Male", "Nashville"),
            Person("Evelyn Young", "30", "Female", "Detroit"),
            Person("Jacob Lee", "25", "Male", "Oklahoma City"),
            Person("Abigail Allen", "29", "Female", "Las Vegas"),
            Person("Alexander King", "31", "Male", "Portland"),
            Person("Elizabeth Wright", "27", "Female", "Memphis"),
            Person("Michael Scott", "33", "Male", "Louisville"),
            Person("Emma Green", "26", "Female", "Baltimore")
        )

        // Set up RecyclerView
        binding.recyclerview.layoutManager = LinearLayoutManager(this)
        personAdapter = PersonAdapter(personList)
        binding.recyclerview.adapter = personAdapter

        // Set up SearchView
        binding.searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                filter(newText)
                return true
            }
        })

        // Set up filter icon click listener
        val filterIcon = binding.filterIcon
        filterIcon.setOnClickListener {
            val bottomSheetDialog = BottomSheetDialog(this)
            val bottomSheetView = layoutInflater.inflate(R.layout.filter_options_layout, null)
            bottomSheetDialog.setContentView(bottomSheetView)

            // Initialize views
            val ageMinEditText = bottomSheetView.findViewById<EditText>(R.id.ageMinEditText)
            val ageMaxEditText = bottomSheetView.findViewById<EditText>(R.id.ageMaxEditText)
            val genderRadioGroup = bottomSheetView.findViewById<RadioGroup>(R.id.genderRadioGroup)
            val locationEditText = bottomSheetView.findViewById<EditText>(R.id.locationEditText)
            val applyFilterButton = bottomSheetView.findViewById<Button>(R.id.applyFilterButton)

            // Set click listener for apply filter button
            applyFilterButton.setOnClickListener {
                // Get selected filter options
                minAge = ageMinEditText.text.toString().toIntOrNull()
                maxAge = ageMaxEditText.text.toString().toIntOrNull()

                // Get selected gender
                gender = when (genderRadioGroup.checkedRadioButtonId) {
                    R.id.maleRadioButton -> "Male"
                    R.id.femaleRadioButton -> "Female"
                    else -> null
                }

                location = locationEditText.text.toString()

                // Apply the filter
                applyFilter()

                // Dismiss the bottom sheet dialog
                bottomSheetDialog.dismiss()
            }

            // Show the bottom sheet dialog
            bottomSheetDialog.show()
        }
    }

    private fun filter(text: String?) {
        val filteredList = personList.filter { person ->
            // Filter by name
            val nameMatches = text.isNullOrBlank() || person.name.contains(text, ignoreCase = true)

            // Filter by age
            val ageInRange = minAge == null || maxAge == null ||
                    (person.age.toIntOrNull() ?: 0 in (minAge!!..maxAge!!))

            // Filter by gender
            val genderMatches = gender.isNullOrBlank() || person.gender.equals(gender, ignoreCase = true)

            // Filter by location
            val locationMatches = location.isNullOrBlank() || person.location.contains(location!!, ignoreCase = true)

            nameMatches && ageInRange && genderMatches && locationMatches
        }
        recyclerViewState = binding.recyclerview.layoutManager?.onSaveInstanceState() // Save scroll position
        personAdapter.filterList(filteredList)
        binding.recyclerview.layoutManager?.onRestoreInstanceState(recyclerViewState) // Restore scroll position
    }
    private fun applyFilter() {
        val filteredList = personList.filter { person ->
            val age = person.age.toIntOrNull()
            if (age != null && minAge != null && maxAge != null) {
                if (age < minAge!! || age > maxAge!!) {
                    return@filter false
                }
            }

            if (gender != null && person.gender != gender) {
                return@filter false
            }

            if (location != null && !person.location.contains(location!!, ignoreCase = true)) {
                return@filter false
            }

            return@filter true
        }

        recyclerViewState = binding.recyclerview.layoutManager?.onSaveInstanceState()
        personAdapter.filterList(filteredList)
        binding.recyclerview.layoutManager?.onRestoreInstanceState(recyclerViewState)
    }
}
//complete both screen
