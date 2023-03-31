package com.cwz.electronic.store.controllers;

import com.cwz.electronic.store.dtos.ApiResponseMessage;
import com.cwz.electronic.store.dtos.ImageResponse;
import com.cwz.electronic.store.dtos.PageableResponse;
import com.cwz.electronic.store.dtos.UserDto;
import com.cwz.electronic.store.entities.User;
import com.cwz.electronic.store.services.FileService;
import com.cwz.electronic.store.services.UserService;
import org.apache.tomcat.jni.File;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StreamUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    private UserService userService;
    @Autowired
    private FileService fileService;
    @Value("${user.profile.image.path}")
    private String imageUploadPath;

    //create
    @PostMapping("/create")
    public ResponseEntity<UserDto> createUser(@Valid @RequestBody UserDto userDto) {
        UserDto userDto1 = userService.createUser(userDto);
        return new ResponseEntity<>(userDto1, HttpStatus.CREATED);
    }

    //update
    @PutMapping("/update/{userId}")
    public ResponseEntity<UserDto> updateUser(@RequestBody UserDto userDto, @PathVariable String userId) {
        UserDto updateUserDto = userService.updateUser(userDto, userId);
        return new ResponseEntity<>(updateUserDto, HttpStatus.OK);
    }

    //delete
    @DeleteMapping("/delete/{userId}")
    public ResponseEntity<ApiResponseMessage> deleteUser(@PathVariable String userId) {
        userService.deleteUser(userId);
        ApiResponseMessage thisUserHasBeenDeleted = ApiResponseMessage.builder()
                .message("This user has been deleted")
                .success(true)
                .status(HttpStatus.OK)
                .build();
        return new ResponseEntity<>(thisUserHasBeenDeleted, HttpStatus.OK);
    }

    //get all
    @GetMapping("/all")
    public ResponseEntity<PageableResponse<UserDto>> getAllUser(@RequestParam(value = "pageNumber", defaultValue = "0", required = false) int pageNumber, @RequestParam(value = "pageSize", defaultValue = "10", required = false) int pageSize, @RequestParam(value = "sortBy", defaultValue = "name", required = false) String sortBy, @RequestParam(value = "sortDir", defaultValue = "asc", required = false) String sortDir) {
        return new ResponseEntity<>(userService.getAllUsers(pageNumber, pageSize, sortBy, sortDir), HttpStatus.OK);
    }

    // find single by ID
    @GetMapping("/user/{userId}")
    public ResponseEntity<UserDto> getSingleUser(@PathVariable String userId) {
        return new ResponseEntity<>(userService.getSingleUserById(userId), HttpStatus.OK);
    }

    //find single by email
    @GetMapping("/email/{email}")
    public ResponseEntity<UserDto> getSingleUserByEmail(@PathVariable String email) {
        return new ResponseEntity<>(userService.getSingleUserByEmail(email), HttpStatus.OK);
    }

    //search user
    @GetMapping("/search/{keyword}")
    public ResponseEntity<List<UserDto>> getUserByKeyword(@PathVariable String keyword) {
        return new ResponseEntity<>(userService.searchUser(keyword), HttpStatus.OK);
    }

    //upload user image
    @PostMapping("/image/user/{userId}")
    public ResponseEntity<ImageResponse> uploadUserImage(@RequestParam("userImage") MultipartFile image, @PathVariable String userId) throws IOException {
        String imageName = fileService.uploadFile(image, imageUploadPath);
        UserDto user = userService.getSingleUserById(userId);
        user.setImageName(imageName);
        userService.updateUser(user, userId);
        ImageResponse imageResponse = ImageResponse.builder()
                .imageName(imageName)
                .message("image has been uploaded")
                .success(true)
                .status(HttpStatus.CREATED)
                .build();
        return new ResponseEntity<>(imageResponse, HttpStatus.CREATED);
    }

    //serve user image
    @GetMapping(value = "/image/user/{userId}")
    public void serveUserImage(@PathVariable String userId, HttpServletResponse response) throws IOException {
        UserDto user = userService.getSingleUserById(userId);
        InputStream resource = fileService.getResource(imageUploadPath, user.getImageName());
        response.setContentType(MediaType.IMAGE_JPEG_VALUE);
        StreamUtils.copy(resource, response.getOutputStream());
    }

}
