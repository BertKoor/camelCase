package nl.ordina.bertkoor.camelcase.model;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Value;

@Value
@AllArgsConstructor
@NoArgsConstructor(force = true, access = AccessLevel.PRIVATE)
public class CCRSResponse {
    private Integer rating;

    public boolean hasRating() {
        return this.rating != null;
    }
}
