package com.example.bayongapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bayongapp.activities.CodOrDeliveryActivity;
import com.example.bayongapp.adapters.MyCartAdapter;
import com.example.bayongapp.models.MyCartModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class MyCartFragment extends Fragment {


    FirebaseFirestore db;
    FirebaseAuth auth;
    ProgressBar progressBar;
    TextView overTotalAmount;

    RecyclerView recyclerView;
    MyCartAdapter cartAdapter;
    List<MyCartModel> cartModelList;

    Button buyNow;

    public MyCartFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_my_cart, container, false);

        progressBar = root.findViewById(R.id.progressbar);
        progressBar.setVisibility(View.VISIBLE);

        db = FirebaseFirestore.getInstance();
        auth = FirebaseAuth.getInstance();
        recyclerView = root.findViewById(R.id.recyclerview);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setVisibility(View.GONE);

        buyNow = root.findViewById(R.id.buy_now);
        overTotalAmount = root.findViewById(R.id.textView7);

        cartModelList = new ArrayList<>();
        cartAdapter = new MyCartAdapter(getActivity(),cartModelList);
        recyclerView.setAdapter(cartAdapter);

        db.collection("CurrentUser").document(auth.getCurrentUser().getUid())
                .collection("AddToCart").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.isSuccessful()){
                    for(DocumentSnapshot documentSnapshot : task.getResult().getDocuments()){

                        String documentId = documentSnapshot.getId();

                        MyCartModel cartModel = documentSnapshot.toObject(MyCartModel.class);

                        cartModel.setDocumentId(documentId);

                        cartModelList.add(cartModel);
                        cartAdapter.notifyDataSetChanged();
                        progressBar.setVisibility(View.GONE);
                        recyclerView.setVisibility(View.VISIBLE);
                    }
                    calculateTotalAmount(cartModelList);
                }
            }
        });

        buyNow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), CodOrDeliveryActivity.class);
                intent.putExtra("itemList", (Serializable) cartModelList);
                startActivity(intent);
            }
        });


        return root;
    }

    private void calculateTotalAmount(List<MyCartModel> cartModelList) {

        double totalAmount = 0.0;
        for (MyCartModel myCartModel : cartModelList) {
            totalAmount += myCartModel.getTotalPrice();
        }

        overTotalAmount.setText("Total Amount : Php. "+ totalAmount);
    }
}