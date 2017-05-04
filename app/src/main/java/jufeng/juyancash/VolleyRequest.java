package jufeng.juyancash;

import android.content.Context;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request.Method;
import com.android.volley.toolbox.StringRequest;

import java.util.Map;

public class VolleyRequest {
    public static StringRequest stringRequest;

    public static void RequestPost(Context context, String url, String tag, final Map<String, String> params,
                                   VolleyInterface volleyInterface) {
        VolleyHelper.getInstance(context).cancelPendingRequests(tag);
        stringRequest = new StringRequest(Method.POST, url, volleyInterface.loadingListener(),
                volleyInterface.errorListener()) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                // TODO Auto-generated method stub
                return params;
            }
        };
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(50000, 0, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        VolleyHelper.getInstance(context).addToRequestQueue(stringRequest, tag);
    }
}
