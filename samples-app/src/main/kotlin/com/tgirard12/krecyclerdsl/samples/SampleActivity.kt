package com.tgirard12.krecyclerdsl.samples

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import android.util.Log
import com.squareup.picasso.Picasso
import com.tgirard12.krecyclerdsl.dataClassAdapter
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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.sample_activity)
        setSupportActionBar(toolbar)

        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.addItemDecoration(DividerItemDecoration(this, DividerItemDecoration.VERTICAL))

        service.search("kotlin")
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        { refreshRecyclerView(it) },
                        { Log.e("SampleActivity", it.message, it) })
    }

    private fun refreshRecyclerView(repositories: Repositories) {
        recyclerView.adapter = dataClassAdapter<Repositories.Repository, ItemDataClassView>(repositories.items, R.layout.item_dataclass_adapter) {
            onBindViewHolder { view, repository ->
                view.name.text = repository.name
                view.description.text = repository.description
                picasso.load(repository.owner.avatar_url).into(view.image)
            }
            onItemClickListener { _, repository ->
                startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(repository.html_url)))
            }
        }
    }
}
