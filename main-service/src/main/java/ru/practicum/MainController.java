package ru.practicum;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.ewm.StatClient;

@RestController
@RequiredArgsConstructor
public class MainController {
    private final StatClient statClient;
}
