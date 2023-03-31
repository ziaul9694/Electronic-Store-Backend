package com.cwz.electronic.store.services.Impl;

import com.cwz.electronic.store.dtos.PageableResponse;
import com.cwz.electronic.store.dtos.UserDto;
import com.cwz.electronic.store.entities.Role;
import com.cwz.electronic.store.entities.User;
import com.cwz.electronic.store.exception.ResourceNotFoundException;
import com.cwz.electronic.store.helper.HelperForPR;
import com.cwz.electronic.store.repositories.RoleRepository;
import com.cwz.electronic.store.repositories.UserRepository;
import com.cwz.electronic.store.services.UserService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    public UserRepository userRepository;
    @Autowired
    public ModelMapper modelMapper;
    @Value("${user.profile.image.path}")
    public String imagePath;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private RoleRepository roleRepository;
    @Value("${admin.role.id}")
    private String role_admin_id;
    @Value("${normal.role.id}")
    private String role_normal_id;
    @Override
    public UserDto createUser(UserDto userDto) {

        //generate random userId in String format
        String userId = UUID.randomUUID().toString();
        userDto.setUserId(userId);
        userDto.setPassword(passwordEncoder.encode(userDto.getPassword()));
        User user = dtoToEntity(userDto);

        Role role = roleRepository.findById(role_normal_id).get();
        user.getRoles().add(role);

        User newUser = userRepository.save(user);
        UserDto newUserDto = entityToDto(newUser);
        return newUserDto;
    }

    @Override
    public UserDto updateUser(UserDto userDto, String userId) {

        User user = userRepository.findById(userId).orElseThrow(() -> new ResourceNotFoundException("User not found with given ID"));
        user.setName(userDto.getName());
        user.setPassword(userDto.getPassword());
        user.setAbout(userDto.getAbout());
        user.setGender(userDto.getGender());
        user.setImageName(userDto.getImageName());

        User updatedUser = userRepository.save(user);

        UserDto updatedUserDto = entityToDto(updatedUser);

        return updatedUserDto;
    }

    @Override
    public void deleteUser(String userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new ResourceNotFoundException("User with given Id is not found"));
        String fullPath = imagePath + user.getImageName();
        try{
            Path path = Paths.get(fullPath);
            Files.delete(path);
        }catch (NoSuchFileException ex){
            ex.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        userRepository.delete(user);
    }

    @Override
    public PageableResponse<UserDto> getAllUsers(int pageNumber, int pageSize, String sortBy, String sortDir) {
        Sort sort = (sortDir.equalsIgnoreCase("desc")) ? (Sort.by(sortBy).descending()) : (Sort.by(sortBy).ascending());
        Pageable pageable = PageRequest.of(pageNumber,pageSize,sort);
        Page<User> page = userRepository.findAll(pageable);

        PageableResponse<UserDto> response = HelperForPR.getPageableResponse(page, UserDto.class);
        return response;
    }

    @Override
    public UserDto getSingleUserById(String userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new ResourceNotFoundException("User not found with given ID"));
        UserDto userDto = entityToDto(user);
        return userDto;
    }

    @Override
    public UserDto getSingleUserByEmail(String email) {
        User user = userRepository.findByEmail(email).orElseThrow(() -> new ResourceNotFoundException("User not found with given Email"));
        UserDto userDto = entityToDto(user);
        return userDto;
    }

    @Override
    public List<UserDto> searchUser(String keyword) {
        List<User> users = userRepository.findByNameContaining(keyword);
        List<UserDto> userDtoList = users.stream().map(user -> entityToDto(user)).collect(Collectors.toList());
        return userDtoList;
    }


    private User dtoToEntity(UserDto userDto) {
//        User user = User.builder()
//                .userId(userDto.getUserId())
//                .name(userDto.getName())
//                .email(userDto.getEmail())
//                .password(userDto.getEmail())
//                .gender(userDto.getGender())
//                .imageName(userDto.getImageName())
//                .build();
        User user = modelMapper.map(userDto, User.class);
        return user;
    }

    private UserDto entityToDto(User newUser) {

//        UserDto userDto = UserDto.builder()
//                .userId(newUser.getUserId())
//                .name(newUser.getName())
//                .email(newUser.getEmail())
//                .password(newUser.getEmail())
//                .gender(newUser.getGender())
//                .imageName(newUser.getImageName())
//                .build();

        UserDto userDto = modelMapper.map(newUser, UserDto.class);
        return userDto;
    }

}
