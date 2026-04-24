package dk.ek.gruppe2.chooseyourfate.service.mysql;

import dk.ek.gruppe2.chooseyourfate.dto.AccountResponseDTO;
import dk.ek.gruppe2.chooseyourfate.dto.CreateAccountRequestDTO;
import dk.ek.gruppe2.chooseyourfate.exception.DuplicateResourceException;
import dk.ek.gruppe2.chooseyourfate.model.mysql.Account;
import dk.ek.gruppe2.chooseyourfate.repository.mysql.AccountRepository;
import dk.ek.gruppe2.chooseyourfate.service.CrudService;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.UUID;
import java.util.function.Function;

@Service
public class AccountService extends CrudService<Account, Integer, AccountResponseDTO> {

    private final AccountRepository accountRepository;
    private final PasswordEncoder encoder;

    public AccountService(AccountRepository accountRepository,
                                        PasswordEncoder encoder) {
        this.accountRepository = accountRepository;
        this.encoder = encoder;
    }

    @Override
    protected JpaRepository<Account, Integer> getRepository() {
        return accountRepository;
    }

    @Override
    protected Function<Account, AccountResponseDTO> toDTOMapper() {
        return AccountResponseDTO::new;
    }

    @Override
    protected String getEntityName() {
        return "Account";
    }

    public AccountResponseDTO createAccount(CreateAccountRequestDTO request) {
        if (accountRepository.existsByUsername(request.getUsername())) {
            throw new DuplicateResourceException("Username already exists");
        }
        if (accountRepository.existsByEmail(request.getEmail())) {
            throw new DuplicateResourceException("Email already exists");
        }

        Account account = request.toEntity();
        account.setCharacterLimit(3);
        account.setPassword(encoder.encode(request.getPassword()));

        Account saved = accountRepository.save(account);
        return new AccountResponseDTO(saved);
    }
}