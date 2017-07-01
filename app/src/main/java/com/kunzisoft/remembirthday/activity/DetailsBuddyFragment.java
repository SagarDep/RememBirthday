package com.kunzisoft.remembirthday.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.kunzisoft.remembirthday.R;
import com.kunzisoft.remembirthday.Utility;
import com.kunzisoft.remembirthday.adapter.AutoMessagesAdapter;
import com.kunzisoft.remembirthday.element.Contact;
import com.kunzisoft.remembirthday.element.DateUnknownYear;

/**
 * Activity who show the details of buddy selected
 */
public class DetailsBuddyFragment extends Fragment {

    private static final String TAG = "DETAILS_BUDDY_FRAGMENT";

    protected RecyclerView autoMessagesListView;
    protected AutoMessagesAdapter autoMessagesAdapter;

    public void setBuddy(Contact currentContact) {
        Bundle args = new Bundle();
        args.putParcelable(BuddyActivity.EXTRA_BUDDY, currentContact);
        setArguments(args);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_details_buddy, container, false);

        TextView dayAndMonthTextView = (TextView) root.findViewById(R.id.fragment_details_buddy_dayAndMonth);
        TextView yearTextView = (TextView) root.findViewById(R.id.fragment_details_buddy_year);
        TextView daysLeftTextView = (TextView) root.findViewById(R.id.fragment_details_buddy_days_left);

        FloatingActionButton buttonAddAutoMessage = (FloatingActionButton) root.findViewById(R.id.fragment_details_buddy_button_add_auto_message);
        autoMessagesListView = (RecyclerView) root.findViewById(R.id.fragment_details_buddy_list_auto_messages);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        autoMessagesListView.setLayoutManager(linearLayoutManager);
        buttonAddAutoMessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //TODO add new item in list of messages
                autoMessagesAdapter.addDefaultAutoMessage();
            }
        });

        Contact contact = null;
        if(getArguments()!=null) {
            contact = getArguments().getParcelable(BuddyActivity.EXTRA_BUDDY);
        }
        if(contact != null) {
            if(contact.hasBirthday()) {
                // Display date
                DateUnknownYear currentBuddyBirthday = contact.getBirthday();

                // Assign text for day and month
                dayAndMonthTextView.setText(currentBuddyBirthday.toStringMonthAndDay(java.text.DateFormat.FULL));

                // Assign text for year
                if (contact.getBirthday().containsYear()) {
                    yearTextView.setVisibility(View.VISIBLE);
                    yearTextView.setText(currentBuddyBirthday.toStringYear());
                } else {
                    yearTextView.setVisibility(View.GONE);
                    yearTextView.setText("");
                    //TODO Add button for year
                }
                // Number days left before birthday
                Utility.assignDaysRemainingInTextView(daysLeftTextView, contact.getBirthdayDaysRemaining());
            } else {
                //TODO Error
            }
        }
        return root;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        autoMessagesAdapter = new AutoMessagesAdapter();
        autoMessagesListView.setAdapter(autoMessagesAdapter);
    }
}
