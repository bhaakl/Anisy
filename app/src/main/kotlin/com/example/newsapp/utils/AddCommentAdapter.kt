package com.example.newsapp.utils

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.lifecycle.LifecycleCoroutineScope
import androidx.recyclerview.widget.RecyclerView
import com.example.newsapp.R
import com.example.newsapp.models.Comment
import com.example.newsapp.room.repo.CommentQuery
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class AddCommentAdapter(
    private val commentQuery: CommentQuery,
    private val lifecycleScope: LifecycleCoroutineScope
) : RecyclerView.Adapter<AddCommentAdapter.AddCommentViewHolder>() {
    class AddCommentViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val commentAuthor: TextView = itemView.findViewById(R.id.commentAuthor)
        val textCommentViewHolderShowing: TextView = itemView.findViewById(R.id.textCommentShowing)
    }

    private var comments: MutableList<Comment> = mutableListOf()

    init {
        lifecycleScope.launch {
            try {
                commentQuery.getAll().collect { newComments ->
                    comments.clear()
                    comments.addAll(newComments)
                    notifyDataSetChanged()
                }
            } catch (e: Exception) {
                Log.e("PickEvent", "Ошибка при загрузке событий", e)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AddCommentViewHolder {
        val itemView =
            LayoutInflater.from(parent.context).inflate(R.layout.comment_cards, parent, false)
        return AddCommentViewHolder(itemView)
    }

    override fun getItemCount(): Int = comments.size

    override fun onBindViewHolder(holder: AddCommentViewHolder, position: Int) {
        val comment = comments[position]
        holder.commentAuthor.text = comment.author
        holder.textCommentViewHolderShowing.text = comment.text
    }

    fun addComment(name: String, commAuthor: String) {
        lifecycleScope.launch(Dispatchers.IO) {
            val newComment = Comment(text = name, author = commAuthor)
            commentQuery.insert(newComment)
            withContext(Dispatchers.Main) {
                val newCommentPosition = comments.size
                comments.add(newComment)
                notifyItemInserted(newCommentPosition)
            }
        }
    }

    fun deleteAllEvents() {
        lifecycleScope.launch(Dispatchers.IO) {
            commentQuery.deleteAll()
        }
    }
}