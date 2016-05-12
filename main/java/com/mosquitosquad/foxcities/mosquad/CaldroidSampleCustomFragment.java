package com.mosquitosquad.foxcities.mosquad;

import com.roomorama.caldroid.CaldroidFragment;
import com.roomorama.caldroid.CaldroidGridAdapter;

/**
 * Created by Tyler Gotz on 4/21/2016.
 */
public class CaldroidSampleCustomFragment extends CaldroidFragment
{
    @Override
    public CaldroidGridAdapter getNewDatesGridAdapter(int month, int year) {
        // TODO Auto-generated method stub
        return new CaldroidSampleCustomAdapter(getActivity(), month, year,
                getCaldroidData(), extraData);
    }
}
