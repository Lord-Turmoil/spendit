package top.tony.spendit.api.config;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import top.tony.spendit.api.models.Account;
import top.tony.spendit.api.models.repos.AccountRepository;

@Component
@RequiredArgsConstructor
@Slf4j
public class DataLoader implements CommandLineRunner {
    private final AccountRepository accountRepository;

    @Override
    public void run(String... args) throws Exception {
        if (accountRepository.count() == 0) {
            log.warn("Creating default admin account, please change the password!");
            // WARNING: I know, password exposed, but will be changed on deployment.
            accountRepository.save(new Account("Admin", "password", true));
        } else {
            log.info("Default admin account already exists, skipping creation.");
        }
    }
}

