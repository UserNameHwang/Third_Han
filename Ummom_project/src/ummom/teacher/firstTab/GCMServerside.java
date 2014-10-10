package ummom.teacher.firstTab;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import com.google.android.gcm.server.Message;
import com.google.android.gcm.server.MulticastResult;
import com.google.android.gcm.server.Result;
import com.google.android.gcm.server.Sender;

public class GCMServerside extends Thread {

	private int mode;
	public GCMServerside(int mode) {
		// TODO Auto-generated constructor stub
		this.mode = mode;
	}

	
	@Override
	public void run() {
		// TODO Auto-generated method stub
		Sender sender = new Sender("AIzaSyD3cJfC9aFHRvd2vKuazRA7QcNfv-QTDqM");

		String regId = "APA91bG4eZDGWuQLGkDG3GAT6vg6xtLckfNgIUXPTv4NhJciwii_rl9ZkRIl5zjeG3iXYosMES-uCeePXrx-NkYNCsBgw6pxsSvQ5JFiOYlDUnNKZ4q4iQJk7rL2QRr8XVdESsGSgSoaf6gJAD5u2dq259WROUMbog";

		Message message = null;
		if( mode == 0 ){
			message = new Message.Builder().addData("msg", "assignment notify")
				.build();
		}
		
		else if( mode == 1 )
		{
			message = new Message.Builder().addData("msg", "assignment remove")
					.build();
		}
		// List에 받아온 GCM 등록 ID를 저장한다.
		List<String> list = new ArrayList<String>();

		list.add(regId);

		MulticastResult multiResult = null;
		try {
			multiResult = sender.send(message, list, 5);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		if (multiResult != null) {

			List<Result> resultList = multiResult.getResults();

			for (Result result : resultList) {

				System.out.println(result.getMessageId());

			}

		}
	}

}