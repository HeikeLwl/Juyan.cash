package jufeng.juyancash;

import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.VolleyError;

public abstract class VolleyInterface {
	public static Listener<String> listener;
	public static ErrorListener errorListener;

	public VolleyInterface(Listener<String> listener, ErrorListener errorListener) {
		VolleyInterface.listener = listener;
		VolleyInterface.errorListener = errorListener;
	}

	public abstract void onSuccess(String arg0);

	public abstract void onError(VolleyError arg0);

	public Listener<String> loadingListener() {
		listener = new Listener<String>() {

			@Override
			public void onResponse(String arg0) {
				// TODO Auto-generated method stub
				onSuccess(arg0);
			}
		};
		return listener;
	}

	public ErrorListener errorListener() {
		errorListener = new ErrorListener() {

			@Override
			public void onErrorResponse(VolleyError arg0) {
				// TODO Auto-generated method stub
				onError(arg0);
			}
		};
		return errorListener;
	}
}
