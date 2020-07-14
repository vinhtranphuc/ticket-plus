package com.so.service;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.so.common.BaseService;
import com.so.common.Const;
import com.so.common.Utils;
import com.so.jpa.entity.ConfirmationToken;
import com.so.jpa.entity.Role;
import com.so.jpa.entity.RoleName;
import com.so.jpa.entity.User;
import com.so.jpa.repository.ConfirmationTokenRepository;
import com.so.jpa.repository.RoleRepository;
import com.so.jpa.repository.UserRepository;
import com.so.mybatis.mapper.UserMapper;
import com.so.mybatis.model.UserVO;

@Service
public class UserService extends BaseService {

	@Autowired
	UserRepository userRepository;

	@Autowired
	RoleRepository roleRepository;
	
	@Autowired
	UserMapper userMapper;
	
	@Autowired
	ConfirmationTokenRepository confirmationTokenRepository;

	@Autowired
	PasswordEncoder passwordEncoder;

	public boolean isExistsByUsername(String userName) {
		return userRepository.existsByUsername(userName);
	}

	public boolean isExistsByEmail(String email) {
		return userRepository.existsByEmail(email);
	}

	public Optional<Role> getRoleByName(RoleName roleName) {
		return roleRepository.findByName(roleName);
	}

	public User saveUser(User user) {
		return userRepository.save(user);
	}

	public void saveConfirmToken(ConfirmationToken confirmationToken) {
		confirmationTokenRepository.save(confirmationToken);
	}
	
	public boolean isUserDisabled(String usernameOrEmail) {
		Optional<User> user = userRepository.findByUsernameOrEmail(usernameOrEmail,usernameOrEmail);
		return user.isPresent() && !user.get().isEnabled();
	}
	
	public Optional<ConfirmationToken> findByConfirmationToken(String token) {
		return confirmationTokenRepository.findByToken(token);
	}
	
	public void setEnabledUserByToken(ConfirmationToken confirmationToken) {
		User user = userRepository.findByEmailIgnoreCase(confirmationToken.getUser().getEmail()).get();
		user.setEnabled(true);
		userRepository.save(user);
	}

	public UserVO getUserById(Long userId) {
		Map<String,Object> param = new HashMap<String,Object>();
		param.put("user_id",userId);
		UserVO user = userMapper.getUserById(param);
		if(StringUtils.equals("local", user.getProvider())) {
			user.setAvatar_uri(convertAvatarImgToUri(user.getAvatar_img()));
		} else {
			user.setAvatar_uri(user.getSocial_avatar_url());
		}
		return user;
	}

	private String convertAvatarImgToUri(String avatarImg) {
		if(!FilenameUtils.isExtension(avatarImg, Const.imgExtensions)) {
			avatarImg = avatarImg+Const.DEFAULT_IMG_TYPE;
		}
		return Const.getUserAvatarUri(Utils.getLocalIp()+":"+severPost,avatarImg);
	}
}
