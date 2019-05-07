package love.people.RatingMicroService.controllers;

import love.people.RatingMicroService.controllers.dto.PlaceAddDTO;
import love.people.RatingMicroService.entity.Place;
import love.people.RatingMicroService.entity.User;
import love.people.RatingMicroService.entity.enums.UserRole;
import love.people.RatingMicroService.repository.PlaceRepository;
import love.people.RatingMicroService.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/places")
public class PlaceController {
    @Autowired
    private PlaceRepository placeRepository;
    @Autowired
    private UserRepository userRepository;

    @PostMapping
    private String addPlace(@RequestBody PlaceAddDTO placeDTO) {
        if (userRepository.findByUuid(placeDTO.getUuid()).getUserRole().equals(UserRole.ADMIN_ROLE)) {
            Place place = new Place();
            place.setAvgRating(5.0);
            place.setNumberOfRatings(1L);
            place.setName(placeDTO.getName());
            place.setDescription(placeDTO.getDescription());
            placeRepository.save(place);
            return "Respect";
        } else {
            return "401 and f**k you, mother's hacker";
        }
    }

    @GetMapping
    private List<Place> allPlaces() {
        return placeRepository.findAll();
    }

    @PostMapping("/rating")
    private String updateRating(@RequestParam int rating, @RequestParam String uuid, @RequestParam long placeId) {
        User user = userRepository.findByUuid(uuid);
        if (user != null && placeRepository.findById(placeId).isPresent()) {
            Place place = placeRepository.findById(placeId).get();
            long numberOfRatings = place.getNumberOfRatings();
            place.setAvgRating(((place.getAvgRating() * numberOfRatings) + rating) / (numberOfRatings + 1L));
            place.setNumberOfRatings(numberOfRatings + 1L);
            return "Respect";
        }
            return "Could be anything ^_^";
    }
}
