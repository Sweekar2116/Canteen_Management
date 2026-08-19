package com.canteen.controller;

import com.canteen.dto.ChangePasswordRequest;
import com.canteen.dto.UpdateProfileRequest;
import com.canteen.dto.UserProfileResponse;
import com.canteen.security.UserPrincipal;
import com.canteen.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/profile")
@Tag(name = "User Profile", description = "User profile and password management APIs")
public class UserProfileController {

    private final UserService userService;

    public UserProfileController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    @Operation(summary = "Get currently authenticated user's profile details")
    public ResponseEntity<UserProfileResponse> getProfile(@AuthenticationPrincipal UserPrincipal currentUser) {
        return ResponseEntity.ok(userService.getProfile(currentUser.getId()));
    }

    @PutMapping
    @Operation(summary = "Update profile information")
    public ResponseEntity<UserProfileResponse> updateProfile(
        @AuthenticationPrincipal UserPrincipal currentUser,
        @Valid @RequestBody UpdateProfileRequest request
    ) {
        return ResponseEntity.ok(userService.updateProfile(currentUser.getId(), request));
    }

    @PutMapping("/password")
    @Operation(summary = "Change account password")
    public ResponseEntity<Void> changePassword(
        @AuthenticationPrincipal UserPrincipal currentUser,
        @Valid @RequestBody ChangePasswordRequest request
    ) {
        userService.changePassword(currentUser.getId(), request);
        return ResponseEntity.noContent().build();
    }
}
