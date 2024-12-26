package com.ra.projectmd05.service.auth;

import com.ra.projectmd05.constants.RoleName;
import com.ra.projectmd05.constants.UserStatus;
import com.ra.projectmd05.exception.DataExistException;
import com.ra.projectmd05.exception.UnauthorizedAccessException;
import com.ra.projectmd05.model.dto.request.UserLoginRequestDTO;
import com.ra.projectmd05.model.dto.request.UserRegisterRequestDTO;
import com.ra.projectmd05.model.dto.request.VerifyUserDTO;
import com.ra.projectmd05.model.dto.response.SuggestedFriendDTO;
import com.ra.projectmd05.model.dto.response.UserLoginResponseDTO;
import com.ra.projectmd05.model.dto.response.UserRegisterResponseDTO;
import com.ra.projectmd05.model.entity.Role;
import com.ra.projectmd05.model.entity.User;
import com.ra.projectmd05.model.entity.UserInfo;
import com.ra.projectmd05.repository.FriendRepository;
import com.ra.projectmd05.repository.RoleRepository;
import com.ra.projectmd05.repository.UserInfoRepository;
import com.ra.projectmd05.repository.UserRepository;
import com.ra.projectmd05.security.UserPrinciple;
import com.ra.projectmd05.security.jwt.JwtProvider;
import com.ra.projectmd05.service.Token.TokenService;
import com.ra.projectmd05.service.user.UserServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

import static com.ra.projectmd05.constants.RoleName.ROLE_SUPER_ADMIN;
import static com.ra.projectmd05.constants.RoleName.ROLE_USER;


@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService{
    @Autowired

    private JavaMailSender mailSender;

    private final AuthenticationProvider authenticationProvider;

    private final JwtProvider jwtProvider;

    private final RoleRepository roleRepository;

    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;
    private final TokenService tokenService;
    private final UserInfoRepository userInfoRepository;
    private final FriendRepository friendRepository;
    private final UserServiceImpl userServiceImpl;

    @Override
    public UserLoginResponseDTO login(UserLoginRequestDTO userLoginRequestDTO) {
        Authentication authentication;
        try {
            authentication = authenticationProvider.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            userLoginRequestDTO.getEmail(),
                            userLoginRequestDTO.getPassword()
                    )
            );
        } catch (AuthenticationException e) {
            throw new BadCredentialsException("Tài khoản hoặc mật khẩu không đúng");
        }

        UserPrinciple userPrinciple = (UserPrinciple) authentication.getPrincipal();
        User user = userPrinciple.getUser();
        // Kiểm tra trạng thái người dùng, nếu chưa active thì không cho phép đăng nhập
        if (user.getStatus().equals(UserStatus.VERIFY)) {
            throw new UnauthorizedAccessException("Tài khoản chưa được xác thực, vui lòng kiểm tra email để xác thực tài khoản.");
        }
        if (user.getStatus().equals(UserStatus.BLOCKED)) {
            throw new UnauthorizedAccessException("Tài khoản đã bị khóa.không thể đăng nhập");
        }

        // --------------------N start----------------------
        // Kiểm tra quyền của người dùng
        boolean hasValidRole = user.getRoles().stream()
                .anyMatch(role -> role.getRoleName().equals(ROLE_SUPER_ADMIN) || role.getRoleName().equals(ROLE_USER));

        //---------------------N end -----------------------

        // login được thì đưa ra token
        String token = jwtProvider.generateToken(userPrinciple);
        // Kiểm tra xem token có bị vô hiệu hóa không
        if (tokenService.isTokenInvalidated(token)) {
            throw new UnauthorizedAccessException("Token đã bị vô hiệu hóa");
        }
        return UserLoginResponseDTO.builder()
                .accessToken(token)
                .typeToken("Bearer")
                .userName(userPrinciple.getUsername())
                .userInfo(userInfoRepository.findByUserId(userPrinciple.getUser().getId()).orElseThrow(()-> new NoSuchElementException("khong ton tai nguoi dung")))
                .roles(userPrinciple.getUser().getRoles().stream().map(item -> item.getRoleName().toString()).collect(Collectors.toSet()))
                .build();
    }

    @Override
    public void register(UserRegisterRequestDTO requestDTO) throws DataExistException {
        Date today = new Date(); // Ngày hiện tại
        // Kiểm tra nếu ngày sinh là sau ngày hôm nay
        if (!requestDTO.getBirthDay().before(today)) {
            throw new DataExistException("Ngày sinh phải trước ngày hiện tại!!!!");
        }
        if(userRepository.existsByEmail(requestDTO.getEmail())){
            throw new DataExistException("email đã tồn tại");
        }
        if(userRepository.existsByPhone(requestDTO.getPhone())){
            throw new DataExistException("số điện thoại đã tồn tại");
        }

        Set<Role> roles = new HashSet<>();
        // tu cai mang ten role tu request ban len lay vef cac doi tuong role tuonwg ung roi day vaof roles
            Role role = roleRepository.findRoleByRoleName(ROLE_USER).orElseThrow(()->new NoSuchElementException("chưa tạo bảng role"));
            roles.add(role);
        User user = userRegisterRequestDTOConvertToUser(requestDTO);
        user.setRoles(roles);
        User userNew = userRepository.save(user);
        sendVerificationEmail(userNew);              // Gửi email xác thực sau khi đăng ký
    }
    // Gửi email xác thực
    @Override
    public void sendVerificationEmail(User user) {
        // Tạo mã xác thực ngẫu nhiên (6 ký tự)
        String verificationCode = generateRandomVerificationCode();

        // Tạo nội dung email với mã xác thực
        String verificationMessage = "Vui lòng nhập mã xác thực sau để hoàn tất quá trình đăng ký: " + verificationCode;

        // Tạo email xác thực
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(user.getEmail());  // Đặt email người nhận
        message.setSubject("Xác thực tài khoản");  // Tiêu đề email
        message.setText(verificationMessage);  // Nội dung email
        message.setFrom("hhyhoanhaiyen@gmail.com");

        // Gửi email
        try {
            mailSender.send(message);  // Gửi email qua JavaMailSender
            System.out.println("Email đã được gửi thành công đến   :  " + user.getEmail());
        } catch (Exception e) {
            // Log thêm thông tin lỗi để dễ dàng debug
            e.printStackTrace();
            throw new RuntimeException("Lỗi khi gửi email xác thực cho người dùng: " + user.getEmail(), e);
        }

        // Cập nhật mã xác thực vào người dùng (lưu mã xác thực vào cơ sở dữ liệu)
        user.setVerifyCode(verificationCode);  // Lưu mã xác thực vào User
        user.setVerifyCodeDate(LocalDateTime.now());  // Lưu ngày giờ gửi mã
        userRepository.save(user);  // Lưu thông tin người dùng vào cơ sở dữ liệu
    }


    // Hàm tạo mã xác thực ngẫu nhiên (6 ký tự ngẫu nhiên)
    private String generateRandomVerificationCode() {
        return UUID.randomUUID().toString().substring(0, 6);
    }



    @Override
    public List<UserRegisterResponseDTO> findAll() {
        List<User> list = userRepository.findAll();
        return list.stream().map(this::userConvertToResponseDTO).collect(Collectors.toList());
    }
    private Role findRoleByRoleName(RoleName roleName) {
        return roleRepository.findRoleByRoleName(roleName).orElseThrow(()->new NoSuchElementException("Role Not Found"));
    }

    @Override
    // Hàm kiểm tra mã xác thực và chuyển trạng thái từ 'verify' sang 'active'
    public boolean verifyUserCode(VerifyUserDTO verifyUserDTO) {
        // Lấy người dùng
        User user = userRepository.findByEmail(verifyUserDTO.getEmail()).orElseThrow(()->new NoSuchElementException("Email này chưa đăng kí tài khoản,Người dùng không tồn tại."));
        // Kiểm tra mã xác thực
        if (user.getVerifyCode().equals(verifyUserDTO.getVerifyCode())) {
            // Kiểm tra thời gian hết hạn mã xác thực (ví dụ: 10 phút)
            LocalDateTime currentTime = LocalDateTime.now();
            Duration duration = Duration.between(user.getVerifyCodeDate(), currentTime); // Tính sự khác biệt giữa thời gian tạo mã và thời gian hiện tại

            // Nếu mã xác thực đã hết hạn (quá 10 phút)
            if (duration.toMinutes() > 10) {
                throw new RuntimeException("Mã xác thực đã hết hạn.");
            }
            user.setStatus(UserStatus.ACTIVE);
            userRepository.save(user);
            return true;
        } else {
            throw new RuntimeException("Mã xác thực không chính xác.");
        }
    }





    @Override
    public User userRegisterRequestDTOConvertToUser  (UserRegisterRequestDTO dto) {
        return User.builder()
                .firstName(dto.getFirstName())
                .lastName(dto.getLastName())
                .email(dto.getEmail())
                .password(new BCryptPasswordEncoder().encode(dto.getPassword()))
                .phone(dto.getPhone())
                .birthDay(dto.getBirthDay()) // Nếu có
                .status(UserStatus.VERIFY )   // Mặc định VERIFY  khi tạo mới
                .createdAt(LocalDateTime.now())       // Gán thời gian tạo
                .updatedAt(LocalDateTime.now())       // Gán thời gian cập nhật
                .isLogin(false)              // Mặc định chưa đăng nhập
                .gender(dto.getGender())
                .build();
    }
    // Chuyển đổi từ User sang UserRegisterResponseDTO
    @Override
    public UserRegisterResponseDTO userConvertToResponseDTO(User user) {
        User currentUser = userServiceImpl.getCurrentUserInfo();
        UserInfo info = userInfoRepository.findByUserId(user.getId()).orElseThrow(()->new NoSuchElementException("khong tim thay nguoi dung"));
        SuggestedFriendDTO suggestedFriendDTO =  SuggestedFriendDTO.builder()
                .commonFriendsCount(friendRepository.countCommonFriends(currentUser.getId(),user.getId()))
                .suggestedUser(user)
                .build();

        return UserRegisterResponseDTO.builder()
                .id(user.getId())
                .username(user.getFirstName() + " " + user.getLastName()) // Ghép tên đầy đủ
                .email(user.getEmail())
                .phone(user.getPhone())
                .status(user.getStatus())
                .roles(user.getRoles())
                .info(info)
                .suggestedFriendDTO(suggestedFriendDTO)
                .build();
    }


    // Phương thức gửi lại mã xác thực
    @Override
    public void resendVerificationEmail(String email) {
        // Tìm người dùng dựa trên email
        User user = userRepository.findByEmail(email).orElseThrow(()->new NoSuchElementException("Email này chưa đăng kí tài khoản,Người dùng không tồn tại."));
        // Xóa mã xác thực cũ nếu có (trong trường hợp người dùng yêu cầu gửi lại mã xác thực)
        user.setVerifyCode(null);
        user.setVerifyCodeDate(null);
        // Tạo token mới và gửi email xác thực
        sendVerificationEmail(user);
    }

    // Hàm cập nhật email cho người dùng
    @Override
    public void updateEmail(Long userId, String newEmail) {
        User user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("Người dùng không tồn tại.bạn cần đăng kí trước"));
        userRepository.findByEmail(newEmail).orElseThrow(()->new RuntimeException("Email này đã được đăng ký."));
        // Cập nhật email mới
        user.setEmail(newEmail);
        userRepository.save(user);
        // Gửi lại mã xác thực
        sendVerificationEmail(user);
    }



    // Phương thức kiểm tra và xóa tài khoản nếu không xác thực trong 24 giờ
    @Scheduled(fixedRate = 24 * 60 * 60 * 1000)
    public void deleteUnverifiedUsersOlderThan24Hours() {
        // Lấy tất cả người dùng có verifyCode không null và trạng thái VERIFY
        List<User> users = userRepository.findUnverifiedUsers(UserStatus.VERIFY);

        // Lấy thời gian hiện tại
        LocalDateTime currentTime = LocalDateTime.now();

        // Duyệt qua danh sách người dùng và kiểm tra thời gian tạo tài khoản
        for (User user : users) {
            // Kiểm tra nếu người dùng chưa xác thực và thời gian tạo tài khoản lớn hơn 24 giờ
            Duration duration = Duration.between(user.getCreatedAt(), currentTime);
            long timeLimit = 24 * 60 * 60 * 1000; // 24 giờ tính bằng milliseconds

            if (duration.toHours() > timeLimit) {
                // Nếu thời gian tạo tài khoản > 24 giờ, xóa người dùng này
                userRepository.delete(user);
            }
        }
    }


}
