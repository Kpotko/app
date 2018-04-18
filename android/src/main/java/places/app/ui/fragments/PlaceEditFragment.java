package places.app.ui.fragments;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

import com.bumptech.glide.Glide;

import places.app.R;
import places.app.storage.DatabaseHelper;
import places.app.storage.PlaceModel;


public class PlaceEditFragment extends Fragment implements View.OnClickListener {


    private static final String START_TYPE = "START_TYPE";
    private static final String PLACE_ID = "PLACE_ID";
    private static final String LATLNG = "LATLNG";
    private static final String LAT = "LAT";
    private static final String LNG = "LNG";
    private static final String IMAGE_URI = "IMAGE_URI";

    private static final int REQUEST_IMAGE_OPEN = 1;

    private OnPlacesChangedListener onPlacesChangedListener;


    private String imageURI;
    private PlaceModel placeModel = new PlaceModel();

    private ImageButton ibImage;
    private EditText etDesc;
    private EditText etLat;
    private EditText etLng;
    private Button btnSave;
    private Button btnDelete;


    public static PlaceEditFragment newInstance(int placeId) {
        Bundle args = new Bundle();
        args.putString(START_TYPE, PLACE_ID);
        args.putInt(PLACE_ID, placeId);
        PlaceEditFragment fragment = new PlaceEditFragment();
        fragment.setArguments(args);
        return fragment;
    }

    public static PlaceEditFragment newInstance(double lat, double lng) {
        Bundle args = new Bundle();
        args.putString(START_TYPE, LATLNG);
        args.putDouble(LAT, lat);
        args.putDouble(LNG, lng);
        PlaceEditFragment fragment = new PlaceEditFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_place_edit, container, false);

        ibImage = view.findViewById(R.id.ib_image);
        etDesc = view.findViewById(R.id.et_desc);
        etLat = view.findViewById(R.id.et_lat);
        etLng = view.findViewById(R.id.et_lon);
        btnSave = view.findViewById(R.id.btn_save);
        btnDelete = view.findViewById(R.id.btn_delete);

        ibImage.setOnClickListener(this);
        btnSave.setOnClickListener(this);
        btnDelete.setOnClickListener(this);


        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        parseData();

        if (placeModel.getPid() == null) {
            btnDelete.setVisibility(View.GONE);
        }

        if (savedInstanceState != null && savedInstanceState.containsKey(IMAGE_URI)) {
            imageURI = savedInstanceState.getString(IMAGE_URI);
        } else {
            imageURI = placeModel.getImage();
        }

        Glide.with(getActivity())
                .load(imageURI)
                .fitCenter()
                .into(ibImage);

        etDesc.setText(placeModel.getDescription());
        etLat.setText(String.valueOf(placeModel.getLatitude()));
        etLng.setText(String.valueOf(placeModel.getLongitude()));

    }

    private void parseData() {
        Bundle bundle = getArguments();
        String type = bundle.getString(START_TYPE);
        switch (type) {
            case PLACE_ID: {
                int pid = bundle.getInt(PLACE_ID);
                placeModel = DatabaseHelper.getInstance(getContext()).getPlaceById(pid);
                break;
            }
            case LATLNG: {
                placeModel.setLatitude(bundle.getDouble(LAT));
                placeModel.setLongitude(bundle.getDouble(LNG));
                break;
            }
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString(IMAGE_URI, imageURI);

    }

    @Override
    public void onResume() {
        super.onResume();
        if (getActivity() != null) {
            getActivity().setTitle(R.string.editor_title);
        }
    }

    private void pickImage() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), 1);
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_IMAGE_OPEN && resultCode == Activity.RESULT_OK) {
            if (data != null) {
                imageURI = data.getData().toString();
                Glide.with(getActivity())
                        .load(imageURI)
                        .fitCenter()
                        .into(ibImage);
            }
        }
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.ib_image: {
                if (imageURI != null) {
                    ibImage.setImageResource(0);
                    imageURI = null;
                } else
                    pickImage();
                break;
            }
            case R.id.btn_save: {
                savePlace();
                break;
            }
            case R.id.btn_delete: {
                deletePlace();
                break;
            }
        }
    }

    private void savePlace() {
        placeModel.setImage(imageURI);
        placeModel.setDescription(etDesc.getText().toString());
        placeModel.setLatitude(Double.parseDouble(etLat.getText().toString()));
        placeModel.setLongitude(Double.parseDouble(etLng.getText().toString()));
        if (placeModel.getPid() == null) {
            DatabaseHelper.getInstance(getContext()).insertPlace(placeModel);
        } else {
            DatabaseHelper.getInstance(getContext()).updatePlace(placeModel);
        }
        notifyChanged();
        getActivity().getSupportFragmentManager().popBackStack();
    }

    private void deletePlace() {
        DatabaseHelper.getInstance(getContext()).deletePlace(placeModel.getPid());
        notifyChanged();
        getActivity().getSupportFragmentManager().popBackStack();

    }

    private void notifyChanged() {
        if (onPlacesChangedListener != null) {
            onPlacesChangedListener.onPlacesChanged();
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            onPlacesChangedListener = (OnPlacesChangedListener) context;
        } catch (ClassCastException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        onPlacesChangedListener = null;
    }

    public interface OnPlacesChangedListener {
        void onPlacesChanged();
    }
}
