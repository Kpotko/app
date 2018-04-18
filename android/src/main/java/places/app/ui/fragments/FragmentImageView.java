package places.app.ui.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

import places.app.R;


public class FragmentImageView extends Fragment {

    private static String IMG_URL = "IMG_URL";

    private String imgUrl;

    private ImageView imageView;

    public static FragmentImageView newInstance(String imgURL) {
        Bundle args = new Bundle();
        args.putString(IMG_URL, imgURL);
        FragmentImageView fragment = new FragmentImageView();
        fragment.setArguments(args);
        return fragment;
    }

    public FragmentImageView() {
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        imgUrl = getArguments().getString(IMG_URL);
        View view = inflater.inflate(R.layout.fragment_imageview, container, false);
        imageView = view.findViewById(R.id.iv_image);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Glide.with(getActivity())
                .load(imgUrl)
                .fitCenter()
                .into(imageView);
    }

    @Override
    public void onResume() {
        super.onResume();
        if (getActivity() != null) {
            getActivity().setTitle(getString(R.string.imageview_title));
        }
    }
}

