package com.example.water_app.recyclerview

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.water_app.Donation.CommunicationActivity
import com.example.water_app.databinding.ItemMainRecyclerBinding
import com.example.water_app.model.PostData

class DonationAdapter(private val context: Context, private var donationList: List<PostData>?) : RecyclerView.Adapter<DonationAdapter.ViewHolder>() {

    class ViewHolder(val binding: ItemMainRecyclerBinding) : RecyclerView.ViewHolder(binding.root)

    // 아이템 레이아웃 설정
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DonationAdapter.ViewHolder {
        val binding = ItemMainRecyclerBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    // 내용 입력
    override fun onBindViewHolder(holder: DonationAdapter.ViewHolder, position: Int) {
        holder.binding.tvTitle.text = donationList?.get(position)?.cntr_ttl
        holder.binding.tvMoney.text = donationList?.get(position)?.cntr_obctr.toString()

        // 이미지 url
        var cntrurl : String = donationList?.get(position)?.cntr_file_id.toString()
        Glide.with(context).load(cntrurl).into(holder.binding.ivImage)

        // 퍼센트
        if (donationList?.get(position)?.ctbny_pc == null) {
            holder.binding.tvPercent.text = "0%"
            holder.binding.pbPercent.setProgress(0)
        }else{
            val collectPrice:Int? = donationList?.get(position)?.ctbny_pc
            val totalPrice:Int? = donationList?.get(position)?.cntr_obctr
            val pricePercent:Double? = collectPrice!!.toDouble() / totalPrice!! * 100

            holder.binding.tvPercent.text = pricePercent?.toInt().toString() + "%"
            holder.binding.pbPercent.setProgress(pricePercent!!.toInt())
        }

        // ClickListener
        holder.itemView.setOnClickListener {
            itemClickListener.onClick(it, position)

            //인텐트 putextra getextra 하는 부분
            val intent = Intent(holder.itemView?.context,CommunicationActivity::class.java)

            intent.putExtra("cntr_sn",donationList?.get(position)?.cntr_sn)
            intent.putExtra("cntr_ttl",donationList?.get(position)?.cntr_ttl)
            intent.putExtra("cntr_cn",donationList?.get(position)?.cntr_cn)

            ContextCompat.startActivity(holder.itemView.context, intent, null)
        }
    }

    // 리스트 내 아이템 개수
    override fun getItemCount(): Int {
        return donationList!!.size
    }

    // OnClickListener
    interface OnItemClickListener {
        fun onClick(v: View, position: Int)
    }

    private lateinit var itemClickListener : OnItemClickListener

    fun setItemClickListener(itemClickListener: OnItemClickListener) {
        this.itemClickListener = itemClickListener
    }

    // 데이터 변경시 리스트 다시 할당
    fun setData(newList: PostData){
        donationList = listOf(newList)
        // 새로고침
        notifyDataSetChanged()
    }
}