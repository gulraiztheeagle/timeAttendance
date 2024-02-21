package com.hr.attendance.domain.tenant.security.service;

import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import com.hr.attendance.domain.tenant.security.RequestContext;
import com.hr.attendance.domain.tenant.security.dto.AccessTokenInfo;
import com.hr.attendance.domain.tenant.security.model.AccessToken;
import com.hr.attendance.domain.tenant.security.model.QAccessToken;
import com.hr.attendance.domain.tenant.security.repository.AccessTokenRepository;
import com.hr.attendance.domain.tenant.security.util.SerializationUtils;
import com.hr.attendance.domain.tenant.user.model.User;
import com.querydsl.core.types.Predicate;

@Service
public class TokenStoreImpl implements TokenStore{
	@Autowired
	private AccessTokenRepository tokenRepo;
	
	@Override
	public AccessTokenInfo getAccessToken(Authentication authentication) {
		User user = (User) authentication.getPrincipal();
		String key = extractKey(authentication);
		
		List<String> roles = user.getAuthorities().stream().map(t -> t.getAuthority()).collect(Collectors.toList());
		List<String> permissions = user.getPermissions().stream().map(t -> t.getAuthority()).collect(Collectors.toList());
		
		boolean hasDistributions = false;
		/*
		 * if(user.getRefEmployee().getDistributions() != null &&
		 * !user.getRefEmployee().getDistributions().isEmpty()){ hasDistributions =
		 * true; }
		 */
		
		AccessTokenInfo token = new AccessTokenInfo(key, "bearer", user.getRefEmployee().getFullName(), roles, permissions, "Default Tanent", 
				user.getRefEmployee().getFullName(), user.getRefEmployee().getDesignation().getName(),
				user.getRefEmployee().getDepartment().getName(), user.getRefEmployee().getEmail(),
				"Default", hasDistributions,user.getRefEmployee().getId());
		
		//Change the principal to enable Serialization
		authentication = new UsernamePasswordAuthenticationToken(user.getUsername(), user.getPassword(), user.getPermissions());
		
		//TODO: determine requester IP address
		//TODO: determine requester device
		AccessToken accessToken = new AccessToken(key, serializeAuthenticationToken(token), serializeAuthentication(authentication), user.getUsername(), null, null, null, "Default Schema");
		
		tokenRepo.save(accessToken);
		
		return token;
	}
	
	@Override
	public Authentication readAuthentication(AccessToken accessToken) {
		return SerializationUtils.deserialize(accessToken.getAuthentication());
	}
	
	private String extractKey(Authentication authentication) {
		Map<String, String> values = new LinkedHashMap<String, String>();
		String uuid = UUID.randomUUID().toString();
		values.put(USERNAME, authentication.getName());
		values.put(UUID_KEY, uuid);
		
		return generateKey(values);
	}

	private String generateKey(Map<String, String> values) {
		MessageDigest digest;
		try {
			digest = MessageDigest.getInstance("MD5");
			byte[] bytes = digest.digest(values.toString().getBytes("UTF-8"));
			return String.format("%032x", new BigInteger(1, bytes));
		} catch (NoSuchAlgorithmException nsae) {
			throw new IllegalStateException("MD5 algorithm not available.  Fatal (should be in the JDK).", nsae);
		} catch (UnsupportedEncodingException uee) {
			throw new IllegalStateException("UTF-8 encoding not available.  Fatal (should be in the JDK).", uee);
		}
	}
	
	private byte[] serializeAuthenticationToken(AccessTokenInfo token) {
		return SerializationUtils.serialize(token);
	}
	
	private byte[] serializeAuthentication(Authentication authentication) {
		return SerializationUtils.serialize(authentication);
	}
	
//	private byte[] serializeTenant(Tenant tenant){
//		return SerializationUtils.serialize(tenant);
//	}

	@Override
	public AccessToken readAccessToken(String accessTokenKey) {
		Predicate where = QAccessToken.accessToken.tokenId.eq(accessTokenKey);
		return tokenRepo.findOne(where).get();
	}

	/*
	 * @Override public Tenant readTenant(AccessToken accessToken) { return
	 * SerializationUtils.deserialize(accessToken.getTenant()); }
	 */
}
