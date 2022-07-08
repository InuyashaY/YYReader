package yzl.swu.yyreader.viewholder;

import android.view.View;
import android.widget.TextView;

import de.hdodenhof.circleimageview.CircleImageView;
import yzl.swu.yyreader.R;


public class CustomCommentViewHolder {
    public TextView userName,prizes,comment;
    public CircleImageView ico;

    public CustomCommentViewHolder(View view) {
        userName=view.findViewById(R.id.user);
        prizes=view.findViewById(R.id.prizes);
        comment=view.findViewById(R.id.data);
        ico=view.findViewById(R.id.icon_header);

    }
}
