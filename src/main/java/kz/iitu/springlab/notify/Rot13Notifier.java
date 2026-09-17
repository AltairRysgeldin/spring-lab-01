package kz.iitu.springlab.notify;

import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Component("rot13")
@Order(3)
public class Rot13Notifier implements Notifier {

    private static final Logger log = LoggerFactory.getLogger(Rot13Notifier.class);

    @PostConstruct
    public void init() {
        log.info("ROT13 Notifier initialized!");
    }

    @Override
    public String send(String message) {
        StringBuilder result = new StringBuilder();
        for (char c : message.toCharArray()) {
            if (c >= 'a' && c <= 'm' || c >= 'A' && c <= 'M') {
                result.append((char) (c + 13));
            } else if (c >= 'n' && c <= 'z' || c >= 'N' && c <= 'Z') {
                result.append((char) (c - 13));
            } else {
                result.append(c);
            }
        }
        return "rot13: " + result.toString();
    }

    @Override
    public String channel() { return "rot13"; }
}