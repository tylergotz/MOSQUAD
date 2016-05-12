package com.mosquitosquad.foxcities.mosquad;

import android.app.Activity;
import android.content.ClipData;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.DragEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.Query;
import com.firebase.client.ValueEventListener;
import com.firebase.geofire.GeoFire;
import com.firebase.geofire.GeoLocation;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Tyler Gotz on 4/28/2016.
 */
public class AssignToDriver extends Activity
{
    public class Item implements Serializable
    {
        String itemString;
        public Item(String t)
        {
            this.itemString = t;
        }
    }

    public class PassObject
    {
        View view;
        Item item;
        List<Item> srcList;

        public PassObject(View v, Item i, List<Item> list)
        {
            this.view = v;
            this.item = i;
            this.srcList = list;
        }
    }

    static class ViewHolder
    {
        TextView textView;
    }

    public class ItemsListAdapter extends BaseAdapter
    {
        private Context context;
        private List<Item> list;

        public ItemsListAdapter(Context context, List<Item> list)
        {
            this.context = context;
            this.list = list;
        }

        @Override
        public int getCount()
        {
            return list.size();
        }

        @Override
        public Object getItem(int pos)
        {
            return list.get(pos);
        }

        @Override
        public long getItemId(int position)
        {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent)
        {
            View rowView = convertView;
            if(rowView == null)
            {
                LayoutInflater inflater = ((Activity) context).getLayoutInflater();
                rowView = inflater.inflate(R.layout.row, null);

                ViewHolder viewHolder = new ViewHolder();
                viewHolder.textView = (TextView) rowView.findViewById(R.id.rowTextView);
                rowView.setTag(viewHolder);
            }

            ViewHolder holder = (ViewHolder) rowView.getTag();
            holder.textView.setText(list.get(position).itemString);

            return rowView;
        }

        public List<Item> getList() {
            return list;
        }
    }

    private List<Item> items1, items2, items3, items4;
    private ListView listView1, listView2, listView3, listView4;
    private ItemsListAdapter itemsListAdapter1, itemsListAdapter2, itemsListAdapter3, itemsListAdapter4;
    private LinearLayoutListView area1, area2, area3, area4;
    private TextView driver1, driver2, driver3;
    private ArrayList<String> listOfCustomers;
    private String date;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Firebase.setAndroidContext(this);
        setContentView(R.layout.activity_assign_driver);
        driver1 = (TextView) findViewById(R.id.driver1Text);
        driver2 = (TextView) findViewById(R.id.driver2Text);
        driver3 = (TextView) findViewById(R.id.driver3Text);
        final String[] drivers;
        Bundle b = getIntent().getExtras();
        if(b != null)
        {
            drivers = b.getStringArray("drivers");
            driver1.setText(drivers[0]);
            driver2.setText(drivers[1]);
            driver3.setText(drivers[2]);
            listOfCustomers = b.getStringArrayList("list");
            date = b.getString("date");
        }

        listView1 = (ListView) findViewById(R.id.spraysList);
        listView2 = (ListView) findViewById(R.id.driverList1);
        listView3 = (ListView) findViewById(R.id.driverList2);
        listView4 = (ListView) findViewById(R.id.driverList3);
        area1 = (LinearLayoutListView) findViewById(R.id.pane1);
        area2 = (LinearLayoutListView) findViewById(R.id.pane2);
        area3 = (LinearLayoutListView) findViewById(R.id.pane3);
        area4 = (LinearLayoutListView) findViewById(R.id.pane4);
        area1.setOnDragListener(myOnDragListener);
        area2.setOnDragListener(myOnDragListener);
        area3.setOnDragListener(myOnDragListener);
        area4.setOnDragListener(myOnDragListener);
        area1.setListView(listView1);
        area2.setListView(listView2);
        area3.setListView(listView3);
        area4.setListView(listView4);

        items1 = new ArrayList<>();
        items2 = new ArrayList<>();
        items3 = new ArrayList<>();
        items4 = new ArrayList<>();
        final Firebase firebase = new Firebase("https://mosquito-squad.firebaseio.com/");
        final String[] customerArray = listOfCustomers.toArray(new String[listOfCustomers.size()]);
        Query query = firebase.child("users").child("clients");
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String[] values = dataSnapshot.getValue().toString().split("=");
                for (int i = 0; i < values.length; i++) {
                    for (int j = 0; j < customerArray.length; j++) {
                        String[] getName = customerArray[j].split("-");
                        if (values[i].contains(getName[0].substring(0, getName[0].length() - 1))) {
                            String s = values[i].substring(0, values[i].indexOf(",")) + "(" + values[i + 2].substring(0, values[i + 2].indexOf(",")) + ")";
                            Item item = new Item(s);
                            items1.add(item);

                        }
                    }
                }


            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });

        itemsListAdapter1 = new ItemsListAdapter(this, items1);
        itemsListAdapter2 = new ItemsListAdapter(this, items2);
        itemsListAdapter3 = new ItemsListAdapter(this, items3);
        itemsListAdapter4 = new ItemsListAdapter(this, items4);
        listView1.setAdapter(itemsListAdapter1);
        listView2.setAdapter(itemsListAdapter2);
        listView3.setAdapter(itemsListAdapter3);
        listView4.setAdapter(itemsListAdapter4);
        listView1.setOnItemClickListener(listOnItemClickListener);
        listView2.setOnItemClickListener(listOnItemClickListener);
        listView3.setOnItemClickListener(listOnItemClickListener);
        listView4.setOnItemClickListener(listOnItemClickListener);
        listView1.setOnItemLongClickListener(myOnItemLongClickListener);
        listView2.setOnItemLongClickListener(myOnItemLongClickListener);
        listView3.setOnItemLongClickListener(myOnItemLongClickListener);
        listView4.setOnItemLongClickListener(myOnItemLongClickListener);

        Button button = (Button) findViewById(R.id.goToMapBtn);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Firebase ref = firebase.child("assignedSprays").child("truck1").child(driver1.getText().toString());
                Map<String, String> route1= new HashMap<>();
                for(int i = 0; i < items2.size(); i++)
                {
                    route1.put("spray" + String.valueOf(i), items2.get(i).itemString);
                }
                ref.setValue(route1);
                Firebase ref2 = firebase.child("assignedSprays").child("truck2").child(driver2.getText().toString());
                Map<String, String> route2= new HashMap<>();
                for(int i = 0; i < items3.size(); i++)
                {
                    route2.put("spray" + String.valueOf(i), items3.get(i).itemString);
                }
                ref2.setValue(route2);
                Firebase ref3 = firebase.child("assignedSprays").child("truck3").child(driver3.getText().toString());
                Map<String, String> route3= new HashMap<>();
                for(int i = 0; i < items4.size(); i++)
                {
                    route3.put("spray" + String.valueOf(i), items4.get(i).itemString);
                }
                ref3.setValue(route3);


                GeoFire geoFire = new GeoFire(firebase.child("truckLocations"));
                geoFire.setLocation("truck1", new GeoLocation(44.235142, -88.422241));
                geoFire.setLocation("truck2", new GeoLocation(44.235142, -88.422241));
                geoFire.setLocation("truck3", new GeoLocation(44.235142, -88.422241));

                String[] list1 = new String[items2.size()];
                for(int i = 0; i < items2.size(); i++)
                {
                    list1[i] = items2.get(i).itemString;
                }
                String[] list2 = new String[items3.size()];
                for(int i = 0; i < items3.size(); i++)
                {
                    list2[i] = items3.get(i).itemString;
                }
                String[] list3 = new String[items4.size()];
                for(int i = 0; i < items4.size(); i++)
                {
                    list3[i] = items3.get(i).itemString;
                }
                Bundle b = new Bundle();
                b.putStringArray("spraysForTruck1", list1);
                b.putStringArray("spraysForTruck2", list2);
                b.putStringArray("spraysForTruck3", list3);
                b.putString("date", date);
                Intent intent = new Intent(AssignToDriver.this, SpraysPlottedOnMap.class);
                intent.putExtras(b);
                startActivity(intent);
            }
        });

    }

    private void traverseList(List<Item> list, Map<String, String> truck)
    {
        for(int i = 0; i < list.size(); i++)
        {
            truck.put("spray" + String.valueOf(i), list.get(i).itemString);

        }
    }

    AdapterView.OnItemLongClickListener myOnItemLongClickListener = new AdapterView.OnItemLongClickListener() {
        @Override
        public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
            Item selectedItem = (Item) (parent.getItemAtPosition(position));
            ItemsListAdapter associatedAdapter = (ItemsListAdapter) (parent.getAdapter());
            List<Item> associatedList = associatedAdapter.getList();
            PassObject passObj = new PassObject(view, selectedItem, associatedList);
            ClipData clipData = ClipData.newPlainText("", "");
            View.DragShadowBuilder shadowBuilder = new View.DragShadowBuilder(view);
            view.startDrag(clipData, shadowBuilder, passObj, 0);
            return true;
        }
    };

    View.OnDragListener myOnDragListener = new View.OnDragListener() {
        @Override
        public boolean onDrag(View v, DragEvent event) {

            switch (event.getAction())
            {

                case DragEvent.ACTION_DROP:
                    PassObject passObj = (PassObject) event.getLocalState();
                    View view = passObj.view;
                    Item passedItem = passObj.item;
                    List<Item> srcList = passObj.srcList;
                    ListView oldParent = (ListView) view.getParent();
                    ItemsListAdapter scrAdapter = (ItemsListAdapter)(oldParent.getAdapter());
                    LinearLayoutListView newParent = (LinearLayoutListView) v;
                    ItemsListAdapter destAdapter = (ItemsListAdapter)(newParent.listView.getAdapter());
                    List<Item> destList = destAdapter.getList();
                    if(removeItemToList(srcList, passedItem))
                    {
                        addItemToList(destList, passedItem);
                    }
                    scrAdapter.notifyDataSetChanged();
                    destAdapter.notifyDataSetChanged();
                    newParent.listView.smoothScrollToPosition(destAdapter.getCount()-1);
                    break;
                case DragEvent.ACTION_DRAG_ENDED:
                    break;
                default:
                    break;
            }
            return true;
        }
    };


    AdapterView.OnItemClickListener listOnItemClickListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            Toast.makeText(AssignToDriver.this, ((Item)(parent.getItemAtPosition(position))).itemString, Toast.LENGTH_SHORT).show();

        }
    };



    private boolean removeItemToList(List<Item> list, Item item)
    {
        return list.remove(item);
    }

    private boolean addItemToList(List<Item> list, Item item)
    {
        return list.add(item);
    }

}
