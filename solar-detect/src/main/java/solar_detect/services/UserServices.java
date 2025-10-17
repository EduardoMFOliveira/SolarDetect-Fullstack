package solar_detect.services;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import solar_detect.dto.RegisterRequest;
import solar_detect.exceptions.BusinessException;
import solar_detect.exceptions.ResourceNotFoundException;
import solar_detect.models.User;
import solar_detect.repository.UserRepository;

@Service
@RequiredArgsConstructor
public class UserServices implements UserDetailsService{


    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("Usuário não encontrado: " + email));
    }

    public User registerUser(RegisterRequest request) {
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new BusinessException("Email já está em uso");
        }
        
        if (userRepository.existsByDocumentNumber(request.getDocumentNumber())) {
            throw new BusinessException("Documento já está em uso");
        }
        
        User user = new User();
        user.setName(request.getName());
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setDocumentNumber(request.getDocumentNumber());
        user.setPhone(request.getPhone());
        user.setAddress(request.getAddress());
        user.setUserType(request.getUserType());
        
        return userRepository.save(user);
    }

    public User findByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("Usuário não encontrado: " + email));
    }
    
}
