package com.tgirard12.krecyclerdsl.samples

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.util.Log
import com.squareup.picasso.Picasso
import com.tgirard12.krecyclerdsl.*
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.content_sample.*
import kotlinx.android.synthetic.main.sample_activity.*
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory


class SampleActivity : AppCompatActivity() {

    private var retrofit = Retrofit.Builder()
            .baseUrl("https://api.github.com/")
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .build()
    private var service = retrofit.create<GitHubService>(GitHubService::class.java)
    private val picasso by lazy { Picasso.with(this.applicationContext) }

    private var search = false
    private var nextPage = 1
    private var adapter: DataClassAdapter<ItemDataClassView, Repositories.Repository>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.sample_activity)
        setSupportActionBar(toolbar)

        recyclerView.layoutManager = LinearLayoutManager(this)

        recyclerView.addDividerItemDecorationHorizontal()
        recyclerView.addDividerItemDecorationVertical()
        recyclerView.addInfiniteScrollListener { search() }
        search()
    }

    private fun search() {
        if (search)
            return

        search = true
        service.search("kotlin", page = nextPage)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSuccess { nextPage++ }
                .doFinally { search = false }
                .subscribe(
                        { refreshRecyclerView(it) },
                        { Log.e("SampleActivity", it.message, it) })
    }

    private fun refreshRecyclerView(repositories: Repositories) {
        if (adapter == null) {
            adapter = dataClassAdapter<ItemDataClassView, Repositories.Repository>(R.layout.item_dataclass_adapter, repositories.items) {
                onBindViewHolder { view, repository ->
                    view.name.text = repository.name
                    view.description.text = repository.description
                    picasso.load(repository.owner.avatar_url).into(view.image)
                }
                onItemClickListener { _, repository ->
                    startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(repository.html_url)))
                }
            }
            recyclerView.adapter = adapter
        } else {
            adapter?.mutableItems?.addAll(repositories.items)
            adapter?.notifyDataSetChanged()
        }
    }
}
