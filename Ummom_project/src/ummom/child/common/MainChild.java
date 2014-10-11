package ummom.child.common;

import ummom.login.R;
import ummom.parent.common.ProfileParent;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MenuItem.OnMenuItemClickListener;

public class MainChild extends FragmentActivity {
			
	private SlidingTabsBasicFragment fragment;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_child);
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        fragment = new SlidingTabsBasicFragment();
        transaction.replace(R.id.sample_content_fragment, fragment);
        transaction.commit();	
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
    	
    	getActionBar().setDisplayShowHomeEnabled(false);
        getMenuInflater().inflate(R.menu.main, menu);
        
        menu.findItem(R.id.action_person).setOnMenuItemClickListener(new OnMenuItemClickListener() {
			
			public boolean onMenuItemClick(MenuItem arg0) {
				Intent intent =new Intent(getApplicationContext(),ProfileChild.class);
				startActivity(intent);
				overridePendingTransition(R.anim.page_appear, R.anim.page_donmove);
				return false;
			}
		});
		


        return true;
        
    }
    
}
