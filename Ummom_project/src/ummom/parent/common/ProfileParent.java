package ummom.parent.common;

import ummom.login.R;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.widget.TextView;

public class ProfileParent extends FragmentActivity {
	TextView Pname;
	TextView Pphone;
	TextView Pbirth;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.profile_parent);
		getActionBar().hide();
		
				
	}

	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		super.onBackPressed();
		overridePendingTransition(R.anim.page_donmove, R.anim.page_disappear);
		finish();

	}
}
