package com.toy.project1.service;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.UUID;

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
import com.toy.project1.handler.FileHandler;
import com.toy.project1.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CustomOAuth2UserService {
	
	private final UserRepository userRepository;
	private final HttpServletRequest request;
	private final FileHandler fileHandelr;
	
	public String getGoogleAccessToken(String code) throws Exception {
		String access_token = "";
		String reqURL = "https://oauth2.googleapis.com/token";
		
		URL url = new URL(reqURL);
		HttpURLConnection conn = (HttpURLConnection) url.openConnection();
		conn.setRequestMethod("POST");
		conn.setDoOutput(true);
		BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(conn.getOutputStream()));
		StringBuilder sb = new StringBuilder();
		
		sb.append("grant_type=authorization_code");
        sb.append("&client_id=1056910107625-4k1ldt3tru60j8lbqg0jr4snu01ubniu.apps.googleusercontent.com");
        sb.append("&redirect_uri=http://localhost/users/login/oauth2/code/google"); 
        sb.append("&client_secret=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");
        sb.append("&code=" + code);
        bw.write(sb.toString());
        bw.flush();
        
        int responseCode = conn.getResponseCode();
        System.out.println("responseCode : " + responseCode);
		
        access_token = token(conn);

        bw.close();
        
        return access_token;
	}
	
	public String getNaverAccessToken(String code, String state) throws Exception {
		String access_token = "";
		String reqURL = "https://nid.naver.com/oauth2.0/token";
		
		URL url = new URL(reqURL);
		HttpURLConnection conn = (HttpURLConnection) url.openConnection();
		conn.setRequestMethod("GET");
		conn.setDoOutput(true);
		BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(conn.getOutputStream()));
		StringBuilder sb = new StringBuilder();
		
		sb.append("grant_type=authorization_code");
        sb.append("&client_id=jL9LamOAjHNUZjduawCh"); // TODO REST_API_KEY 입력
        sb.append("&redirect_uri=http://localhost/users/login/oauth2/code/naver"); // TODO 인가코드 받은 redirect_uri 입력
        sb.append("&client_secret=2vMPI2zP07");
        sb.append("&code=" + code);
        sb.append("&state=" + state);
        bw.write(sb.toString());
        bw.flush();
        
        int responseCode = conn.getResponseCode();
        System.out.println("responseCode : " + responseCode);
		
        access_token = token(conn);

        bw.close();
        
        return access_token;
	}
	
	
	public String getKakaoAccessToken (String code) throws Exception {
        String access_token = "";
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
        sb.append("&client_id=69e1593ec2fbbf39b11fa2c6e3e8c000"); // TODO REST_API_KEY 입력
        sb.append("&redirect_uri=http://localhost/users/login/oauth2/code/kakao"); // TODO 인가코드 받은 redirect_uri 입력
        sb.append("&client_secret=z5DXnZr18jL82gl6xuTRxiPpviAs1FO4");
        sb.append("&code=" + code);
        bw.write(sb.toString());
        bw.flush();
        
        //결과 코드가 200이라면 성공
        int responseCode = conn.getResponseCode();
        System.out.println("responseCode : " + responseCode);
        
        //요청을 통해 얻은 JSON타입의 Response 메세지 읽어오기
        access_token = token(conn);
        
        bw.close();

        return access_token;
    }
	
	public void saveGoogle(String access_token) throws Exception {
		String reqURL = "https://www.googleapis.com/userinfo/v2/me?access_token="+access_token;;
		
		URL url = new URL(reqURL);
		HttpURLConnection conn = (HttpURLConnection)url.openConnection();
		
		conn.setRequestMethod("GET");
		conn.setDoOutput(true);
		conn.setRequestProperty("Authorization", "Bearer " + access_token);
		
		
		BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
		String line = "";
		String result = "";
		while((line = br.readLine()) != null) {
			result += line;
		}
		
		ObjectMapper objectMapper = new ObjectMapper();
		JsonNode jsonNode = objectMapper.readTree(result);
		System.out.println(jsonNode);
		String email = jsonNode.get("email").asText();
		String name = jsonNode.get("name").asText();
		System.out.println(email);
		System.out.println(name);
		
		UserSaveRequestDTO userDTO = UserSaveRequestDTO.builder()
				.email(email)
				.name(name)
				.nickname(randomNickname())
				.authId(AuthId.GOOGLE)
				.build();
		
		User user = save(userDTO);
		forceLogin(user, access_token);
		
		br.close();
	}
	
	public void saveNaver(String access_token) throws Exception {
		String reqURL = "https://openapi.naver.com/v1/nid/me";
		
		URL url = new URL(reqURL);
		HttpURLConnection conn = (HttpURLConnection)url.openConnection();
		
		conn.setRequestMethod("GET");
		conn.setDoOutput(true);
		conn.setRequestProperty("Authorization", "Bearer " + access_token);
		
		BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
		String line = "";
		String result = "";
		while((line = br.readLine()) != null) {
			result += line;
		}
		
		ObjectMapper objectMapper = new ObjectMapper();
		JsonNode jsonNode = objectMapper.readTree(result);
		String email = jsonNode.get("response").get("email").asText();
		String name = jsonNode.get("response").get("name").asText();
		
		UserSaveRequestDTO userDTO = UserSaveRequestDTO.builder()
				.email(email)
				.name(name)
				.nickname(randomNickname())
				.authId(AuthId.NAVER)
				.build();
		
		User user = save(userDTO);
		forceLogin(user, access_token);
		
		br.close();
	}
	
	public void saveKakao(String access_token) throws Exception {
		String reqURL = "https://kapi.kakao.com/v2/user/me";
		
		URL url = new URL(reqURL);
		HttpURLConnection conn = (HttpURLConnection)url.openConnection();
		
		conn.setRequestMethod("POST");
		conn.setDoOutput(true);
		conn.setRequestProperty("Authorization", "Bearer " + access_token);
		
		BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
		String line = "";
		String result = "";
		while((line = br.readLine()) != null) {
			result += line;
		}
		System.out.println("response Body : " + result);
		
		ObjectMapper objectMapper = new ObjectMapper();
		JsonNode jsonNode = objectMapper.readTree(result);
		Boolean has_email = jsonNode.get("kakao_account").get("has_email").asBoolean();
		String email = "";
		String nickname = jsonNode.get("properties").get("nickname").asText();
		
		if(has_email) email = jsonNode.get("kakao_account").get("email").asText();
		
		UserSaveRequestDTO userDTO = UserSaveRequestDTO.builder()
				.email(email)
				.nickname(randomNickname())
				.name(nickname)
				.authId(AuthId.KAKAO)
				.build();
		
		User user = save(userDTO);
		forceLogin(user, access_token);
		
		br.close();
	}
	
	public void deleteGoogle(String access_token, Long id) throws Exception {
		String reqURL = "https://oauth2.googleapis.com/revoke";
		URL url = new URL(reqURL);
		HttpURLConnection conn = (HttpURLConnection)url.openConnection();
		
		conn.setRequestMethod("POST");
		conn.setDoOutput(true);
		BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(conn.getOutputStream()));
		StringBuilder sb = new StringBuilder();
		
		sb.append("token=" + access_token);
        bw.write(sb.toString());
        bw.flush();
		
        int responseCode = conn.getResponseCode();
        System.out.println("responseCode : " + responseCode);
        if(responseCode == 200) {
        	delete(id);
        }
	}
	
	public void deleteKakao(String access_token, Long id) throws Exception {
		String reqURL = "https://kapi.kakao.com/v1/user/unlink";
		URL url = new URL(reqURL);
		HttpURLConnection conn = (HttpURLConnection)url.openConnection();
		
		conn.setRequestMethod("POST");
		conn.setRequestProperty("Authorization", "Bearer " + access_token);
		
		BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
		
		String result = "";
		String line = "";
		
		while((line = br.readLine()) != null) {
			result += line;
		}
		
		if(!result.equals("")) {
			delete(id);
		}
		
		br.close();
	}
	
	public void deleteNaver(String access_token, Long id) throws Exception {
		String reqURL = "https://nid.naver.com/oauth2.0/token";
		URL url = new URL(reqURL);
		HttpURLConnection conn = (HttpURLConnection) url.openConnection();
		
		conn.setRequestMethod("GET");
		conn.setDoOutput(true);
		BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(conn.getOutputStream()));
		StringBuilder sb = new StringBuilder();
		
		sb.append("grant_type=delete");
        sb.append("&client_id=jL9LamOAjHNUZjduawCh");
        sb.append("&client_secret=2vMPI2zP07");
        sb.append("&access_token=" + access_token);
        sb.append("&service_provider=NAVER");
        bw.write(sb.toString());
        bw.flush();
        
        int responseCode = conn.getResponseCode();
        System.out.println("responseCode : " + responseCode);
        if(responseCode == 200) {
        	delete(id);
        }
	}
	
	private String token(HttpURLConnection conn) throws Exception {
		BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
		String line = "";
		String result = "";
		while ((line = br.readLine()) != null) {
			result += line;
		}
		System.out.println("Response Body : " + result);
		
		ObjectMapper objectMapper = new ObjectMapper();
		JsonNode jsonNode = objectMapper.readTree(result);
		        
		String access_token = jsonNode.get("access_token").asText();
        
        br.close();
        
        return access_token;
	}
	
	private User save(UserSaveRequestDTO userDTO) {
		User user = userRepository.findByEmail(userDTO.getEmail())
				.orElse(userDTO.toEntity());
		
		return userRepository.save(user);
	}
	
	private void forceLogin(User user, String access_token) {
		Authentication authentication = new UsernamePasswordAuthenticationToken(user, user.getPassword(), AuthorityUtils.createAuthorityList(user.getRole().toString()));
		
		SecurityContext securityContext = SecurityContextHolder.getContext();
		securityContext.setAuthentication(authentication);
		
		HttpSession httpSession = request.getSession(true);
		httpSession.setAttribute(HttpSessionSecurityContextRepository.SPRING_SECURITY_CONTEXT_KEY, securityContext);
		
		httpSession.setAttribute("access_token", access_token);
	}
	
	private void delete(Long id) throws Exception {
		User user = userRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("존재하지 않는 계정입니다."));
		if(!user.getProfile_image().equals("profile.png")) {			
			fileHandelr.fileDelete("images/upload/user", user.getProfile_image());
		}
		
		userRepository.delete(user);
	}
	
	private String randomNickname() {
		int nicknameLength = 5;
		String nickname = UUID.randomUUID().toString().substring(0, nicknameLength);
		
		return nickname;
	}

}
