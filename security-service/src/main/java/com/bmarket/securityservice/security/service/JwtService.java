package com.bmarket.securityservice.security.service;

import com.bmarket.securityservice.account.domain.entity.Account;
import com.bmarket.securityservice.account.domain.repository.AccountRepository;
import com.bmarket.securityservice.security.api.LoginResult;
import com.bmarket.securityservice.security.api.requestResultForm.ResponseRefresh;
import com.bmarket.securityservice.security.entity.RefreshToken;
import com.bmarket.securityservice.exception.custom_exception.security_ex.FailLoginException;
import com.bmarket.securityservice.exception.custom_exception.security_ex.InvalidTokenException;
import com.bmarket.securityservice.exception.custom_exception.security_ex.IsLogoutAccountException;
import com.bmarket.securityservice.exception.custom_exception.security_ex.NotFoundAccountException;
import com.bmarket.securityservice.utils.jwt.JwtUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

import static com.bmarket.securityservice.security.enums.JwtValue.BEARER;
import static com.bmarket.securityservice.utils.status.ResponseStatus.*;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class JwtService {

    private final AccountRepository accountRepository;
    private final JwtUtils jwtUtils;
    private final PasswordEncoder passwordEncoder;

    /**
     * 클라이언트는 로그인 실패시 로그인 아이디 혹은 비밀번호중 무었이 잘못됬는지 감추기 위해 FailLoginException(ResponseStatus.FAIL_LOGIN)
     */
    public LoginResult loginProcessing(String email, String password) {
        log.info("[LoginProcessing 실행]");
        Account account = accountRepository.findByEmail(email)
                .orElseThrow(() -> new FailLoginException(FAIL_LOGIN));

        passwordCheck(password, account.getPassword());

        account.login();

        String accessToken = jwtUtils.generateAccessToken(account.getMemberId());
        String refreshToken = issuedRefreshToken(account);

        String bearerToken = BEARER + accessToken;
        String bearerRefreshToken = BEARER + refreshToken;

        Date accessTokenExpired = jwtUtils.getExpired(accessToken);

        return new LoginResult(account.getId(), bearerToken, accessTokenExpired, bearerRefreshToken, account.getLastLoginTime());
    }

    /**
     * accountId로 계정 확인 후 로그아웃 처리
     */
    public void logoutProcessing(Long accountId) {
        log.info("[Logout]");
        accountRepository.findById(accountId)
                .orElseThrow(() -> new NotFoundAccountException(NOT_FOUND_ACCOUNT))
                .logout();
    }

    /**
     * 로그인 확인
     */
    @Transactional(readOnly = true)
    public boolean loginCheck(Long accountId) {
        return accountRepository.findById(accountId)
                .orElseThrow(() -> new IllegalArgumentException("d"))
                .isLogin();
    }

    /**
     * 리프레시 토큰 생성
     * 2. 계정에 저장된 리프레시 토큰이 없을시 생성 및 저장 후 토큰 반환
     * 3. 계정에 저장된 리프레시 토큰이 있을시 새 토큰으로 업데이트
     */
    public String issuedRefreshToken(Account account) {
        log.info("[issuedRefreshToken]");
        String newRefreshToken = jwtUtils.generateRefreshToken(account.getMemberId());
        return updateRefreshIfExistOrSave(account, newRefreshToken);
    }

    /**
     * 1.getAccountByIdInToken(): 토큰에 저장된 account 계정을 찾을 수 없을 시 InvalidException
     * 2.checkRefreshToken(): Account 에 저장된 Refresh 토큰이 없을시 IsLogoutAccountException
     * 3.getIfRefreshEqStored(): 저장된 리프레시 토큰과 비교해서 토큰이 일치한다면 새로운 토큰 발급 일치하지 않는다면 InvalidTokenException
     * 정상 토큰이 검증 되면 새로운 리프레시 토큰 반환
     */
    public String reissueRefreshToken(String token, String memberId) {
        log.info("[reissueRefreshToken]");

        Account account = getAccountByIdInToken(memberId);

        checkRefreshToken(account.getRefreshToken().getToken());

        return getIfRefreshEqStored(token, account);
    }

    public ResponseRefresh reissueRefreshToken(String token) {
        log.info("[reissueRefreshToken]");
        String memberId = jwtUtils.getMemberId(token);

        Account account = getAccountByIdInToken(memberId);
        if(!account.getRefreshToken().getToken().equals(token)){
            throw new IllegalArgumentException("잘못된 토큰입니다.");
        }

        String accessToken = jwtUtils.generateAccessToken(memberId);
        String refreshToken = jwtUtils.generateRefreshToken(memberId);
        account.getRefreshToken().changeRefreshToken(refreshToken);

        return new ResponseRefresh(accessToken, refreshToken);
    }


    /**
     * 로그인시 비밀번호 검증
     */
    private void passwordCheck(String password, String savedPassword) {
        log.info("password={}", password);
        log.info("save password={}", savedPassword);
        if (!passwordEncoder.matches(password, savedPassword)) {
            log.info("[패스워드 불일치]");
            throw new FailLoginException(FAIL_LOGIN);
        }
    }

    /**
     * 계정에 리프레시 토큰이 저장되어있을시 토큰 업데이트 ,없다면 저장
     */
    private String updateRefreshIfExistOrSave(Account account, String newRefreshToken) {
        log.info("[updateRefreshIfExistOrSave]");
        return account.getRefreshToken() == null ? createRefreshEntity(account, newRefreshToken)
                : account.getRefreshToken().changeRefreshToken(newRefreshToken);
    }

    /**
     * 리프레시 토큰 객체 생성후 Account 에 저장 후 저장된 토큰 반환
     */
    private String createRefreshEntity(Account account, String newRefreshToken) {
        log.info("[createRefreshEntity]");
        RefreshToken refreshToken = RefreshToken.createRefreshToken(newRefreshToken);
        account.addRefresh(refreshToken);
        return refreshToken.getToken();
    }

    /**
     * 저장된 리프레시 토큰과 비교해서 토큰이 일치한다면 새로운 토큰 발급 일치하지 않는다면
     * REFRESH_TOKEN_IS_DENIED
     */
    private String getIfRefreshEqStored(String refreshToken, Account account) {
        if (account.getRefreshToken().getToken().equals(refreshToken)) {
            log.info("[리프레시 토큰 일치]");
            String refresh = jwtUtils.generateRefreshToken(account.getMemberId());
            account.getRefreshToken().changeRefreshToken(refresh);
            return refresh;
        } else {
            log.info("[리프레시 토큰 불일치]");
            throw new InvalidTokenException(REFRESH_TOKEN_IS_DENIED);
        }
    }

    /**
     * 저장된 리프레시 토큰이 없다면 로그아웃으로 간주
     */
    private void checkRefreshToken(String refreshToken) {
        if (refreshToken == null) {
            throw new IsLogoutAccountException(THIS_ACCOUNT_IS_LOGOUT);
        }
    }

    /**
     * memberId 로 계정 조회
     */
    private Account getAccountByIdInToken(String memberId) {
        return accountRepository.findByMemberId(memberId)
                .orElseThrow(() -> new InvalidTokenException(REFRESH_TOKEN_IS_DENIED));
    }
}
