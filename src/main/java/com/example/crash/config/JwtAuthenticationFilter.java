package com.example.crash.config;

import com.example.crash.service.JwtService;
import com.example.crash.service.UserService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter{ //OncePerRequestFilter 한 요청당 한 번만 실행

    @Autowired
    private JwtService jwtService;

    @Autowired
    private UserService userService;

    //doFilterInternal = 모든 HTTP(get,post등) 요청에 대해 실행
    //HttpServletRequest request: 클라이언트의 요청 정보를 담고 있는 객체. 클라이언트의 IP 주소
    /*ex)담겨있는 정보 (HttpServletRequest)
    요청 URL
    요청 파라미터
    세션 정보
    HTTP 메서드(GET, POST 등)
    헤더 정보(예: Authorization 헤더)
    쿠키 정보 등
    HttpServletResponse response: 서버가 클라이언트에게 응답을 보내는 객체.
    FilterChain filterChain: 필터 체인에서 다음 필터로 요청과 응답을 전달하는 객체*/

    //HttpServletResponse response: 서버가 클라이언트에게 응답을 보내는 객체.
    /*ex)담겨있는 정보 (HttpServletResponse)
    상태 코드(예: 200 OK, 404 Not Found)
    응답 헤더
    응답 본문
    쿠키 설정 등*/

    //FilterChain filterChain: 필터 체인에서 다음 필터로 요청과 응답을 전달하는 객체.

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        // todo: jwt 인증 로직
        String BEARER_PREFIX = "Bearer ";

        //1.클라이언트는 서버로 요청을 보낼 때 Authorization 헤더에 JWT 토큰을 포함하여 인증 정보를 전송합니다.서버는 이 Authorization 헤더를 통해 JWT 토큰을 확인하고, 사용자를 인증한다
        //2.로그인한 사용자가 서버에 HTTP 요청을 보낼 때, 그 요청의 헤더에 JWT 토큰 값을 실어서 보냅니다. 서버는 요청을 받으면, getHeader 메서드를 통해 이 JWT 토큰 값을 호출
        var authorization = request.getHeader(HttpHeaders.AUTHORIZATION);

        //getContext=로그인한 사용자의 인증 정보와 권한을 저장하고 관리하는 객체
        var securityContext = SecurityContextHolder.getContext();

        //이 조건문은 사용자가 이미 인증된 상태인지를 확인하는데 중요한 역할
        if (    !ObjectUtils.isEmpty(authorization)&& //authorization이 값이 비어있지 않으면서
                authorization.startsWith(BEARER_PREFIX) && //BEARER_PREFIX로 시작하는 경우에만 jwt 인증로직 실행
                securityContext.getAuthentication() == null) { //getAuthentication(=인증정보) 가 null이냐

            //Bearer 접두사를 제거하고 나머지 JWT 토큰 값을 추출하는 역할
            var accessToken = authorization.substring(BEARER_PREFIX.length());
            //jwtService를 사용하여 추출된 JWT 토큰에서 사용자 이름을 가져오는 역할을 합니다.
            var username =  jwtService.getUsername(accessToken);
            //userService를 사용하여 사용자 이름으로 사용자 정보를 로드하는 역할
            var userDetails = userService.loadUserByUsername(username);

            // 인증정보를 담고 있다
            //null= 이부분은 사용자 비밀번호를 뜻한다 = 이미 인증된 사용자이기 때문에 빼도 상관없다,getAuthorities= 권한목록
            //userDetails는 UserDetails 인터페이스를 구현한 객체로, 사용자 이름, 비밀번호, 권한 등의 정보를 포함
            //null=이미 인증된(위 조건문을 본다) 사용자이므로 비밀번호를 null로 설정해도 괜찮다 , 만약 비번이 필요하면 userDetails여기서 뽑아오면 된다
            //userDetails.getAuthorities()= 사용자의 권한 목록을 반환
            var authenticationToken = new UsernamePasswordAuthenticationToken(userDetails,null,userDetails.getAuthorities());

            // 현재 처리 되고있는 api 정보도 담아준다
            //WebAuthenticationDetails 객체는 요청이 발생한 클라이언트의 IP 주소와 같은 정보를 포함합니다. 즉, 사용자가 localhost:8080/api/user로 요청을 보냈을 때, 서버는 이 요청을 처리하면서 클라이언트의 IP 주소를 확인 할 수 있다
            //WebAuthenticationDetailsSource: 요청의 세부 정보(세션 정보와 IP 주소와 같은 요청 관련 세부 정보)를 생성하는 클래스입니다.
            // 정리 :HttpServletRequest 객체를 기반으로 WebAuthenticationDetails 객체를 생성합니다. 이 객체는 요청의 세부 정보(IP 주소, 세션 ID 등)를 포함
            authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

            // 생성이 완료된 인증정보를securityContext 세팅
            //authenticationToken은 사용자의 인증 정보(여기에는 사용자의 UserDetails(사용자 정보), 비밀번호(여기서는 null), 그리고 권한 목록)를 담고 있는 객체
            //securityContext 객체에 인증 정보를 설정 이를 통해 서버는 현재 사용자가 인증된 상태임을 인식
            /*최종정리(중요)
            * 정리하면 로그인한 사용자의 토큰 발급 된거 가지고 와서 그거를 인증완료된
            * 토큰인지 검사하고 인증이 완료된 토큰이 아니면 그 인증이 안된토큰을 인증시켜서 그 토큰을 securityContext에 설정해서 사용자를 인증된 상태로 한다는 거다*/
            securityContext.setAuthentication(authenticationToken);

            // SecurityContextHolder에다가 securityContext를 세팅 해주면 jwt인증 과정이 끝난다
            //SecurityContextHolder에 보안 컨텍스트를 설정합니다 이를 통해 서버는 현재 요청이 인증된 사용자로부터 온 것임을 인식
            /*SecurityContextHolder.setContext(securityContext);는 서버가 현재 요청이 인증된 사용자로부터 온 것임을 인식하도록 보안 컨텍스트를 설정하는 역할을 합니다.
            이를 통해 localhost:8080/user/api에 대한 요청이 인증된 사용자가 보냈는지 확인할 수 있습니다. */
            //보안 컨텍스트를 관리하는 중앙 클래스이다
            SecurityContextHolder.setContext(securityContext);
        }

        //모든 작업(위 if문 작업 등)이 끝난 후, 필터 체인의 다음 필터로 요청과 응답을 전달합니다. 이를 통해 다른 필터들이 정상적으로 동작할 수 있게 해준다
        filterChain.doFilter(request,response);

        /*JwtAuthenticationFilter 실행:
        먼저 JwtAuthenticationFilter가 실행되어 JWT 인증 로직을 처리합니다.
        모든 작업이 완료된 후, filterChain.doFilter(request, response)를 호출합니다.
        UsernamePasswordAuthenticationFilter 실행:
        JwtAuthenticationFilter가 호출한 filterChain.doFilter(request, response)에 의해 다음 필터인 UsernamePasswordAuthenticationFilter가 실행됩니다.
        이 필터는 주로 사용자 이름과 비밀번호 기반의 인증을 처리하지만, 이미 인증된 사용자가 있을 경우 추가 인증 작업이 필요하지 않을 수 있습니다.

        참고(중요)
        filterChain.doFilter(request, response) 이거를 동작 시켜주기 위해 WebConfiguration에서
        .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class) 이거를
        사용했다*/

    }


}
