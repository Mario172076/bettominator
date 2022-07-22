package com.example.bettominator.service.implementation;

import com.example.bettominator.model.User;
import com.example.bettominator.model.exceptions.EmptyFieldException;
import com.example.bettominator.model.exceptions.PasswordRequirementsException;
import com.example.bettominator.model.exceptions.PasswordsDoNotMatchException;
import com.example.bettominator.model.exceptions.UserAlreadyExistsException;
import com.example.bettominator.repostiroy.UserRepository;
import com.example.bettominator.service.UserService;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserServiceImpl(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return this.userRepository.findByUsername(username).orElseThrow(() -> new UsernameNotFoundException(username));

    }

    private static boolean checkString(String str) {
        char ch;
        boolean capitalFlag = false;
        boolean lowerCaseFlag = false;
        boolean numberFlag = false;
        for (int i = 0; i < str.length(); i++) {
            ch = str.charAt(i);
            if (Character.isDigit(ch)) {
                numberFlag = true;
            } else if (Character.isUpperCase(ch)) {
                capitalFlag = true;
            } else if (Character.isLowerCase(ch)) {
                lowerCaseFlag = true;
            }
            if (numberFlag && capitalFlag && lowerCaseFlag)
                return true;
        }
        return false;
    }

    @Override
    public User register(String username, String password, String repeatPassword, String name, String surname, String email, String phone) {
        if (username == null || username.isEmpty() || password == null || password.isEmpty() || name == null || name.isEmpty() || surname == null || surname.isEmpty()
                || email == null || email.isEmpty() || phone == null || phone.isEmpty()) {
            throw new EmptyFieldException();
        }
        if (!checkString(password)) {
            throw new PasswordRequirementsException();
        }
        if (!password.equals(repeatPassword)) {
            throw new PasswordsDoNotMatchException();
        }
        if (this.userRepository.findById(username).isPresent() || this.userRepository.findByEmail(email).isPresent() || this.userRepository.findByPhone(phone).isPresent()) {
            throw new UserAlreadyExistsException();
        }
        User user = new User(username, passwordEncoder.encode(password), name, surname, email, phone);
        return this.userRepository.save(user);
    }

    @Override
    public User findById(String userId) {
        return this.userRepository.findById(userId).orElseThrow(() -> new UsernameNotFoundException(userId));
    }


}
