package com.aliveztechnosoft.gamerbaazi.refer_earn;


import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.aliveztechnosoft.gamerbaazi.MainActivity;
import com.aliveztechnosoft.gamerbaazi.MemoryData;
import com.aliveztechnosoft.gamerbaazi.R;
import com.aliveztechnosoft.gamerbaazi.utilities.MainData;
import com.aliveztechnosoft.gamerbaazi.utilities.UserDetails;

/**
 * A simple {@link Fragment} subclass.
 */
public class ReferEarnFragment extends Fragment {

    private String getShareTxt;
    private MainActivity mainActivity;

    public ReferEarnFragment() {
        // Required empty public constructor
    }


    public ReferEarnFragment(MainActivity mainActivity) {
        this.mainActivity = mainActivity;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_refer_earn_freagment, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        final TextView referralCode = view.findViewById(R.id.referralCodeTV);
        final TextView referMessage = view.findViewById(R.id.referMessage);
        final TextView inviteMessage = view.findViewById(R.id.inviteMessage);
        final Button inviteNowBtn = view.findViewById(R.id.inviteNowBtn);
        final ImageView backBtn = view.findViewById(R.id.backBtn);

        final String referralCodeStr = UserDetails.get(requireContext(), "").getReferralCode();
        referralCode.setText(referralCodeStr);

        referMessage.setText("Refer your friend and get a reward of amount "+ MainData.get(requireContext(), "").getReferralAmount());
        inviteMessage.setText("Invite your friends to App and ask them to register using your Referral link. You will get amount of "+ MainData.get(requireContext(), "").getReferralAmount()+" from each friend.");

        getShareTxt  =  MainData.get(requireContext(), "").getAppShareTxt();

        inviteNowBtn.setOnClickListener(v1 -> {

            getShareTxt = getShareTxt.replaceAll("%referral_link%", MemoryData.getData("r.txt", "", requireContext())+ "?from=refer_friend&referral_id=" + referralCodeStr);

            Intent sendIntent = new Intent();
            sendIntent.setAction(Intent.ACTION_SEND);
            sendIntent.putExtra(Intent.EXTRA_TEXT, getShareTxt);
            sendIntent.setType("text/plain");

            Intent shareIntent = Intent.createChooser(sendIntent, "Share Via");
            startActivity(shareIntent);
        });

        backBtn.setOnClickListener(view1 -> mainActivity.homeLayout.performClick());
    }
}
