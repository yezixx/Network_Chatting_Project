package com.example.team3.client_chat;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.team3.R;

import java.util.List;

public class ChatAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<Message> mMessageList;

    public ChatAdapter(List<Message> messageList) {
        mMessageList = messageList;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == Message.TYPE_NOTICE) {  // 공지사항 ViewType
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.emergency_notice, parent, false);
            return new NoticeViewHolder(view);
        } else {  // 일반 메시지 ViewType
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.chat_log, parent, false);
            return new ChatViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        Message message = mMessageList.get(position);

        if (holder instanceof NoticeViewHolder) {
            ((NoticeViewHolder) holder).bind(message);  // 공지사항 바인딩
        } else if (holder instanceof ChatViewHolder) {
            ((ChatViewHolder) holder).bind(message);  // 일반 메시지 바인딩
        }
    }

    @Override
    public int getItemCount() {
        return mMessageList.size();  // 메시지 목록 크기
    }

    @Override
    public int getItemViewType(int position) {
        return mMessageList.get(position).getType();  // 메시지 타입 반환
    }

    public static class ChatViewHolder extends RecyclerView.ViewHolder {
        TextView mMessageTextView;
        TextView mTimeTextView;
        TextView mSenderTextView;
        ImageView mSenderImageView;

        public ChatViewHolder(View itemView) {
            super(itemView);
            mMessageTextView = itemView.findViewById(R.id.mesg_Text);
            mTimeTextView = itemView.findViewById(R.id.ch_time);
            mSenderTextView = itemView.findViewById(R.id.ch_id);
            mSenderImageView = itemView.findViewById(R.id.ch_left_image);
        }

        public void bind(Message message) {
            mMessageTextView.setText(message.getMessage());
            mTimeTextView.setText(message.getTime());
            mSenderTextView.setText(message.getSender());
            mSenderImageView.setImageResource(R.drawable.person_img);  // 사용자 이미지
        }
    }

    // 공지사항 ViewHolder
    public static class NoticeViewHolder extends RecyclerView.ViewHolder {
        TextView noticeText;

        public NoticeViewHolder(View itemView) {
            super(itemView);
            noticeText = itemView.findViewById(R.id.notice_text);  // 공지사항 텍스트뷰
        }

        public void bind(Message message) {
            noticeText.setText(message.getMessage());  // 공지사항 텍스트 설정
        }
    }
}
