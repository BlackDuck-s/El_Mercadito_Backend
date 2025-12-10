package com.uam.mercadito.user;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/users/me/locations")
@RequiredArgsConstructor
public class LocationController {

    private final LocationRepository locationRepository;
    private final AppUserRepository userRepository;

    @GetMapping
    public List<Location> getMyLocations(Principal principal) {
        AppUser user = userRepository.findByEmail(principal.getName())
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
        return locationRepository.findByUserId(user.getId());
    }

    @PostMapping
    public Location createLocation(Principal principal, @RequestBody Location location) {
        AppUser user = userRepository.findByEmail(principal.getName())
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
        
        location.setUser(user);
        
        if (locationRepository.findByUserId(user.getId()).isEmpty()) {
            location.setDefault(true);
        }
        return locationRepository.save(location);
    }
}