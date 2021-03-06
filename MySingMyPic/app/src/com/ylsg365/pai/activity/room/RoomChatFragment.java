package com.ylsg365.pai.activity.room;

import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.malinskiy.superrecyclerview.SuperRecyclerView;
import com.ylsg365.pai.R;
import com.ylsg365.pai.activity.base.TabFragment;
import com.ylsg365.pai.app.YinApi;
import com.ylsg365.pai.util.JsonUtil;
import com.ylsg365.pai.util.LogUtil;

public class RoomChatFragment extends TabFragment implements
        AbsListView.OnItemClickListener {
    private RoomChatAdapter mAdapter;
    private String nid;

    // TODO: Rename and change types of parameters
    public static RoomChatFragment newInstance(String param1, String param2) {
        RoomChatFragment fragment = new RoomChatFragment();
        Bundle args = new Bundle();
        // args.putString(ARG_PARAM1, param1);
        // args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    public RoomChatFragment() {
    }

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public RoomChatFragment(String nid) {
        this.nid = nid;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_fresh_news_forword,
                container, false);

        SuperRecyclerView recyclerView = (SuperRecyclerView) rootView
                .findViewById(R.id.recycler_forward);
        // recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        recyclerView.addItemDecoration(new RecyclerView.ItemDecoration() {

            Paint paint = new Paint();

            @Override
            public void onDraw(Canvas c, RecyclerView parent,
                    RecyclerView.State state) {
                super.onDraw(c, parent, state);
                super.onDrawOver(c, parent, state);
                paint.setColor(getResources().getColor(R.color.line_radio));
                for (int i = 0, size = parent.getChildCount(); i < size; i++) {
                    View child = parent.getChildAt(i);
                    c.drawLine(child.getLeft() + 20, child.getBottom(),
                            child.getRight(), child.getBottom(), paint);
                }
            }

            @Override
            public void onDrawOver(Canvas c, RecyclerView parent,
                    RecyclerView.State state) {

            }

            @Override
            public void getItemOffsets(Rect outRect, View view,
                    RecyclerView parent, RecyclerView.State state) {
                super.getItemOffsets(outRect, view, parent, state);
            }
        });
        mAdapter = new RoomChatAdapter();
        recyclerView.setAdapter(mAdapter);

        getChatList();

        return rootView;
    }
    
    public void refresh() {
        getChatList();
    }

    private void getChatList() {
        YinApi.getHouseChat(nid, new Response.Listener<String>() {
            @Override
            public void onResponse(String res) {
                LogUtil.logd("getHouseChat", res);
                JSONObject response = null;
                try {
                    response = new JSONObject(res);
                } catch (JSONException e) {
                }
                if (response != null && JsonUtil.getBoolean(response, "status")) {
                    List<JSONObject> infoList = mAdapter.getList();
                    infoList.clear();
                    JSONArray infoJsonArray = JsonUtil.getJSONArray(response, "chats");
                    int length = infoJsonArray.length();
                    for (int i = 0; i < length; i++) {
                        infoList.add(JsonUtil.getJSONObject(infoJsonArray, i));
                    }
                    mAdapter.notifyDataSetChanged();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
            }
        });
    }

    public RoomChatAdapter getAdapter() {
        return mAdapter;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position,
            long id) {
    }

    @Override
    public String getTitle() {
        return "公聊";
    }
}
