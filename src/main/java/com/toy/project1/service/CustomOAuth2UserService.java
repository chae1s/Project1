package com.toy.project1.service;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.toy.project1.domain.AuthId;
import com.toy.project1.domain.User;
import com.toy.project1.dto.UserSaveRequestDTO;
import com.toy.project1.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CustomOAuth2UserService {
	
	private final UserRepository userRepository;
	private final HttpServletRequest request;
	
	
	public String getKakaoAccessToken (String code) throws Exception {
        String access_token = "";
        String refresh_token = "";
        String reqURL = "https://kauth.kakao.com/oauth/token";

        URL url = new URL(reqURL);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        
        //POST 요청을 위해 기본값이 false인 setDoOutput을 true로
        conn.setRequestMethod("POST");
        conn.setDoOutput(true);
        
        //POST 요청에 필요로 요구하는 파라미터 스트림을 통해 전송
        BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(conn.getOutputStream()));
        StringBuilder sb = new StringBuilder();
        sb.append("grant_type=authorization_code");
        sb.append("&client_id=a2854f3149b99cbd369249697d4babf9"); // TODO REST_API_KEY 입력
        sb.append("&redirect_uri=http://localhost/users/login/oauth2/code/kakao"); // TODO 인가코드 받은 redirect_uri 입력
        sb.append("&client_secret=z5DXnZr18jL82gl6xuTRxiPpviAs1FO4");
        sb.append("&code=" + code);
        bw.write(sb.toString());
        bw.flush();
        
        //결과 코드가 200이라면 성공
        int responseCode = conn.getResponseCode();
        System.out.println("responseCode : " + responseCode);
        
        //요청을 통해 얻은 JSON타입의 Response 메세지 읽어오기
        BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
        String line = "";
        String result = "";
        
        while ((line = br.readLine()) != null) {
        	result += line;
        }
        System.out.println("response body : " + result);
        
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode jsonNode = objectMapper.readTree(result);
        access_token = jsonNode.get("access_token").asText();
        refresh_token = jsonNode.get("refresh_token").asText();
        
        
        System.out.println("access_token : " + access_token);
        System.out.println("refresh_token : " + refresh_token);
        
        br.close();
        bw.close();

        return access_token;
    }
	
	public void saveKakaoUser(String access_token) throws Exception {
		String reqURL = "https://kapi.kakao.com/v2/user/me";
		
		URL url = new URL(reqURL);
		HttpURLConnection conn = (HttpURLConnection)url.openConnection();
		
		conn.setRequestMethod("POST");
		conn.setDoOutput(true);
		conn.setRequestProperty("Authorization", "Bearer " + access_token);
		
		int responseCode = conn.getResponseCode();
		System.out.println("responseCode : " + responseCode );
		
		BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
		String line = "";
		String result = "";
		while((line = br.readLine()) != null) {
			result += line;
		}
		System.out.println("response Body : " + result);
		
		ObjectMapper objectMapper = new ObjectMapper();
		JsonNode jsonNode = objectMapper.readTree(result);
		Long id = jsonNode.get("id").asLong();
		Boolean has_email = jsonNode.get("kakao_account").get("has_email").asBoolean();
		String email = "";
		String nickname = jsonNode.get("properties").get("nickname").asText();
		
		if(has_email) email = jsonNode.get("kakao_account").get("email").asText();
		
		UserSaveRequestDTO userDTO = new UserSaveRequestDTO();
		userDTO.setId(id);
		userDTO.setEmail(email);
		userDTO.setName(nickname);
		userDTO.setNickname(nickname);
		userDTO.setAuthId(AuthId.KAKAO);
		
		User user = save(userDTO);
		forceLogin(user);
		
		br.close();
	}
	
	private User save(UserSaveRequestDTO userDTO) {
		User user = userRepository.findByEmail(userDTO.getEmail())
				.orElse(userDTO.toEntity());
		
		return userRepository.save(user);
	}
	
	private void forceLogin(User user) {
		Authentication authentication = new UsernamePasswordAuthenticationToken(user, user.getPassword(), AuthorityUtils.createAuthorityList(user.getRole().toString()));
		
		SecurityContext securityContext = SecurityContextHolder.getContext();
		securityContext.setAuthentication(authentication);
		
		HttpSession httpSession = request.getSession(true);
		httpSession.setAttribute(HttpSessionSecurityContextRepository.SPRING_SECURITY_CONTEXT_KEY, securityContext);
		
	}

}
