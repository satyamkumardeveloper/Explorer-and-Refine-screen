package com.example.explorerscreen


import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.explorerscreen.databinding.EachItemBinding

class PersonAdapter(private var personList: List<Person>) : RecyclerView.Adapter<PersonAdapter.PersonViewHolder>() {

    private var filteredPersonList: List<Person> = personList

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PersonViewHolder {
        val binding = EachItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return PersonViewHolder(binding)
    }

    override fun onBindViewHolder(holder: PersonViewHolder, position: Int) {
        val person = filteredPersonList[position]
        holder.bind(person)
    }

    override fun getItemCount() = filteredPersonList.size

    fun filterList(filteredList: List<Person>) {
        filteredPersonList = filteredList
        notifyDataSetChanged()
    }

    class PersonViewHolder(private val binding: EachItemBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(person: Person) {
            binding.Name.text = person.name
            binding.age.text = person.age
            binding.gender.text = person.gender
            binding.location.text = person.location
        }
    }
}
