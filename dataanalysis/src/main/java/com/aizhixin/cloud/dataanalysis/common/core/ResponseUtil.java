package com.aizhixin.cloud.dataanalysis.common.core;

import com.aizhixin.cloud.dataanalysis.common.util.http.HttpResponse;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.util.EntityUtils;

import java.io.IOException;

public interface ResponseUtil {
	default HttpResponse getHttpResponse(CloseableHttpResponse httpResponse)
			throws IOException {
		HttpResponse res = new HttpResponse();

		if (httpResponse != null) {
			res.setStatusCode(httpResponse.getStatusLine().getStatusCode());
			res.setResponseBody(EntityUtils.toString(httpResponse.getEntity()));
		}
		return res;
	}
}
