package com.hr.attendance.domain.tenant.user.service;

import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.passay.CharacterRule;
import org.passay.EnglishCharacterData;
import org.passay.LengthRule;
import org.passay.PasswordData;
import org.passay.PasswordValidator;
import org.passay.RuleResult;
import org.passay.WhitespaceRule;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.hr.attendance.domain.tenant.exception.type.InvalidPasswordStrengthException;
import com.hr.attendance.domain.tenant.exception.type.UserNotActivatedException;
import com.hr.attendance.domain.tenant.user.model.PasswordResetToken;
import com.hr.attendance.domain.tenant.user.model.QUser;
import com.hr.attendance.domain.tenant.user.model.Role;
import com.hr.attendance.domain.tenant.user.model.User;
import com.hr.attendance.domain.tenant.user.repository.FailedAuthenticationLogRepository;
import com.hr.attendance.domain.tenant.user.repository.PasswordResetTokenRepository;
import com.hr.attendance.domain.tenant.user.repository.UserRepository;
import com.querydsl.core.types.Predicate;

@Service
@Transactional
public class UserDetailsServiceImpl implements UserDetailsService {
	@Autowired
	private UserRepository userRepository;
	@Autowired
	private PasswordResetTokenRepository passwordResetTokenRepo;
	@Autowired
	private BCryptPasswordEncoder bCryptPasswordEncoder;
	@Autowired
	private FailedAuthenticationLogRepository failedAuthAttemptRepo;
	private final Logger log = LoggerFactory.getLogger(this.getClass());
	@Value("${backednApp.passwordResetTokenExpiryInHours}")
	private int passwordResetTokenExpiryInHours;

	@Override
	@Transactional(readOnly = true)
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException,NoSuchElementException {
		
		Optional<User> user = userRepository.findByUsername(username);
		
		
		if (!user.isPresent())
			throw new UsernameNotFoundException("username not found");
		log.info("LOGIN PROCESS STARTED ******************");
		if (user.get().getPassword() == null || user.get().getPassword().isEmpty())
			throw new UserNotActivatedException("User has not been activated, recover your password first");

		Set<GrantedAuthority> grantedAuthorities = new HashSet<>();

		for (Role role : user.get().getAuthorities()) {
			role.getPermissions().forEach(permission -> {
				GrantedAuthority authority = new SimpleGrantedAuthority(permission.getAuthority());
				if (!grantedAuthorities.contains(authority))
					grantedAuthorities.add(authority);
			});
		}

		return new org.springframework.security.core.userdetails.User(user.get().getUsername(), user.get().getPassword(),
				user.get().isEnabled(), user.get().isAccountNonExpired(), user.get().isCredentialsNonExpired(), user.get().isAccountNonLocked(),
				grantedAuthorities);
	}

	public User createUser(User user) {
		user.setCredentialsNonExpired(true);
		user.setAccountNonLocked(true);
		user.setEnabled(true);
		user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
		return userRepository.save(user);
	}

	public void updateUser(User user) {
		userRepository.save(user);
	}

	public List<User> getAllUsers() {
		return (List<User>) userRepository.findAll();
	}

	public User findByUsername(String username) {
		return userRepository.findByUsername(username).get();
	}

	public void changePassword(String newPassword, Long userId) throws InvalidPasswordStrengthException {

		String validationMsg = this.validatePasswordStregth(newPassword);
		if (!StringUtils.isBlank(validationMsg)) {
			throw new InvalidPasswordStrengthException(validationMsg);
		}

		User userEntity = userRepository.findById(userId).get();
		userEntity.setPassword(bCryptPasswordEncoder.encode(newPassword));
		userEntity.setPasswordChangedDate(CalendarService.getTimeStamp());
		userEntity.setCredentialsNonExpired(true);
		userRepository.save(userEntity);
	}

	public boolean userExists(String username) {
		Predicate predicate = QUser.user.username.eq(username);
		return userRepository.exists(predicate);
	}
	
	public boolean userExists(long employeeId){
		Predicate whereEmployeeId = QUser.user.refEmployee.id.eq(employeeId);
		return userRepository.exists(whereEmployeeId);
	}

	public String createPasswordResetTokenForUser(User user) {
		Calendar c = Calendar.getInstance();
		c.add(Calendar.HOUR, passwordResetTokenExpiryInHours);
		Date expiresOn = c.getTime();

		String token = UUID.randomUUID().toString();
		Date createdOn = Calendar.getInstance().getTime();

		PasswordResetToken tokenEntity = new PasswordResetToken(user, token, createdOn, expiresOn);
		passwordResetTokenRepo.save(tokenEntity);

		return token;
	}

	public String validatePasswordResetToken(long id, String token) {
		PasswordResetToken resetTokenEntity = passwordResetTokenRepo.findByToken(token);

		if ((resetTokenEntity == null) || (resetTokenEntity.getUser().getId() != id)) {
			return "Provided token is invalid.";
		}

		Calendar cal = Calendar.getInstance();

		if (resetTokenEntity.getExpiresOn().before(cal.getTime())) {
			return "Provided Token has expired";
		}

		return null;
	}

	public void removePasswordResetToken(String token) {
		PasswordResetToken tokenEntity = passwordResetTokenRepo.findByToken(token);

		if (tokenEntity != null) {
			passwordResetTokenRepo.delete(tokenEntity);
		}
	}

	public User getUserByUsername(String username) {
		Predicate predicate = QUser.user.username.eq(username);
		return userRepository.findOne(predicate).get();
	}

	public User getUserByUserId(Long id) {
		return userRepository.findById(id).get();
	}

	public User findUserByEmail(String email) {
		return userRepository.findOne(QUser.user.refEmployee.email.eq(email)).get();
	}

	private String validatePasswordStregth(String password) {
		final PasswordValidator validator = new PasswordValidator(Arrays.asList(new LengthRule(5, 30),
				new CharacterRule(EnglishCharacterData.UpperCase, 1), new CharacterRule(EnglishCharacterData.Digit, 1),
				new CharacterRule(EnglishCharacterData.Special, 1), new WhitespaceRule()));
		final RuleResult result = validator.validate(new PasswordData(password));
		if (!result.isValid()) {
			String validationMessages = validator.getMessages(result).stream().collect(Collectors.joining(","));
			return validationMessages;
		}
		return null;
	}

	public void lockUserAccount(Long id) {
		User user = userRepository.findById(id).get();
		user.setAccountNonLocked(false);

		userRepository.save(user);
	}

	public void expireCredentials(Long id) {
		User user = userRepository.findById(id).get();
		user.setCredentialsNonExpired(false);

		userRepository.save(user);
	}

//	public int updateFailedLoginAttempts(Long userId) {
//		QFailedAuthenticationLog qFailedAuthLog = QFailedAuthenticationLog.failedAuthenticationLog;
//		Predicate where = qFailedAuthLog.user.id.eq(userId);
//
//		FailedAuthenticationLog failedAuthLog = failedAuthAttemptRepo.findOne(where).get();
//
//		if (failedAuthLog == null) {
//			failedAuthLog = new FailedAuthenticationLog();
//			failedAuthLog.setUser(this.getUserByUserId(userId));
//		}
//
//		failedAuthLog.setAttempts(failedAuthLog.getAttempts() + 1); // Increment
//																	// the count
//		failedAuthLog.setLastAttemptOn(new Date());
//
//		failedAuthAttemptRepo.save(failedAuthLog);
//		
//		return failedAuthLog.getAttempts();
//	}
	
//	public void resetFailedLoginAttempts(Long userId){
//		QFailedAuthenticationLog qFailedAuthLog = QFailedAuthenticationLog.failedAuthenticationLog;
//		Predicate where = qFailedAuthLog.user.id.eq(userId);
//
//		FailedAuthenticationLog failedAuthLog = failedAuthAttemptRepo.findOne(where).get();
//		if(failedAuthLog != null){
//			failedAuthAttemptRepo.delete(failedAuthLog);
//		}
//	}
	
	public String findUsernameByEmployeeId(long id){
		Predicate where = QUser.user.refEmployee.id.eq(id);
		User user = userRepository.findOne(where).get();
		if(user == null)
			return null;
		
		return user.getUsername();
	}
}
