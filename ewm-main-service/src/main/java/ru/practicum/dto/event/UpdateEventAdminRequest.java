package ru.practicum.dto.event;

import jakarta.validation.constraints.Positive;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import org.hibernate.validator.constraints.Length;
import ru.practicum.dto.location.LocationDto;
import ru.practicum.enums.StateActionAdmin;

import java.time.LocalDateTime;

@EqualsAndHashCode(callSuper = true)
@Getter
@Setter
@NoArgsConstructor
@SuperBuilder
public class UpdateEventAdminRequest extends UpdateEventRequest {
    private StateActionAdmin stateAction;

    public UpdateEventAdminRequest(@Length(min = 20, max = 2000) String annotation,
                                   @Positive Long category,
                                   @Length(min = 20, max = 2000) String description,
                                   LocalDateTime eventDate,
                                   LocationDto location,
                                   Boolean paid,
                                   Integer participantLimit,
                                   Boolean requestModeration,
                                   @Length(min = 3, max = 120) String title,
                                   StateActionAdmin stateAction) {
        super(annotation, category, description, eventDate, location, paid, participantLimit, requestModeration, title);
        this.stateAction = stateAction;
    }

    @Override
    public String toString() {
        return "UpdateEventAdminRequest{" +
                "stateAction=" + stateAction +
                "} " + super.toString();
    }
}
