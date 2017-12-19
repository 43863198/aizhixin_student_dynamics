package com.aizhixin.cloud.dataanalysis.common.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.http.HttpStatus;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestTemplate;

import com.aizhixin.cloud.dataanalysis.common.domain.AccountDTO;
import com.aizhixin.cloud.dataanalysis.common.domain.IdNameDomain;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;

@Component
public class AuthUtilService {
	private final static Logger log = LoggerFactory
			.getLogger(AuthUtilService.class);

	@Value("${dl.dledu.back.userInfoUrl}")
	private String userInfoUrl;

	@Value("${dl.dledu.back.host}")
	private String dleduBackHost;

	@Value("${dl.dledu.back.userAvatarUrl}")
	private String userAvatarUrl;

	@Value("${dl.org.back.host}")
	private String orgBackHost;

	@Value("${dl.org.back.dbname}")
	private String orgBackDbName;

	public String getOrgDbName() {
		return orgBackDbName;
	}
	@Value("${dl.dd.back.dbname}")
	private String ddBackDbName;

	public String getDdDbName() {
		return ddBackDbName;
	}

	@Cacheable(value = "authorCache")
	public AccountDTO getSsoUserInfo(String token) {
		RestTemplate rest = new RestTemplate();
		HttpHeaders headers = new HttpHeaders();
		List<MediaType> mt = new ArrayList<MediaType>();
		mt.add(MediaType.APPLICATION_JSON_UTF8);
		headers.set(HttpHeaders.AUTHORIZATION, token);// {"access_token":"42c5852c-bdaf-4cea-a09c-0cab54988120","token_type":"bearer","refresh_token":"e4fa3c77-6041-482a-bc5a-2e0d2e5c48e4","expires_in":13933,"scope":"read
		headers.set("Encoding", "UTF-8");
		headers.setAccept(mt);// {"access_token":"eba06c1c-84b9-4706-a27f-0dd11bcc30bc","token_type":"bearer","refresh_token":"384aba1d-925c-4ffb-94f3-c674e550c79e","expires_in":85989,"scope":"read
								// write"}
		System.out.println(token);
		AccountDTO dto = null;
		HttpEntity<byte[]> entity = new HttpEntity<byte[]>(headers);
		try {
			ResponseEntity<AccountDTO> response = rest.exchange(dleduBackHost
					+ userInfoUrl, HttpMethod.GET, entity, AccountDTO.class);
			if (HttpStatus.SC_OK != response.getStatusCode().value()) {
				System.out.println(response.getStatusCode().value());
			} else {
				dto = response.getBody();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return dto;
	}

	public AccountDTO getavatarUserInfo(Long id) {
		RestTemplate rest = new RestTemplate();
		AccountDTO dto = new AccountDTO();
		try {
			String str = rest.getForObject(dleduBackHost + userAvatarUrl
					+ "?ids=" + id, String.class);
			JSONObject jsonObject = new JSONObject(str);
			Iterator<?> iterator = jsonObject.keys();
			while (iterator.hasNext()) {
				String key = (String) iterator.next();
				String value = jsonObject.get(key).toString();
				value = value.replaceAll("null", "\"\"");
				JSONObject jsonUser = new JSONObject(value);
				dto.setName(jsonUser.get("userName").toString());
				dto.setAvatar(jsonUser.get("avatar").toString());
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return dto;
	}

	public HashMap<Long, AccountDTO> getavatarUsersInfo(String ids) {

		RestTemplate rest = new RestTemplate();
		HashMap<Long, AccountDTO> map = new HashMap<Long, AccountDTO>();
		try {
			String str = rest.getForObject(dleduBackHost + userAvatarUrl
					+ "?ids=" + ids, String.class);
			JSONObject jsonObject = new JSONObject(str);
			Iterator<?> iterator = jsonObject.keys();
			while (iterator.hasNext()) {
				String key = (String) iterator.next();
				String value = jsonObject.get(key).toString();
				value = value.replaceAll("null", "\"\"");
				log.info("获取知新用户信息-->" + value);
				JSONObject jsonUser = new JSONObject(value);
				AccountDTO dto = new AccountDTO();
				dto.setId(jsonUser.getLong("id"));
				dto.setAvatar(jsonUser.getString("avatar"));
				dto.setPhone(jsonUser.getString("phoneNumber"));
				map.put(dto.getId(), dto);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return map;
	}



}
