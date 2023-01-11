package ie.sean.project.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@Profile("test")
public class TestWebSecurity {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.authorizeRequests()

                .antMatchers(HttpMethod.POST).hasRole("HOS")

                .antMatchers(HttpMethod.POST, "api/offices").hasRole("HOD")
                .antMatchers(HttpMethod.DELETE, "api/offices/{number}").hasAnyRole("HOS","HOD")
                .antMatchers(HttpMethod.PATCH, "api/offices/{number}/{newOccupancy}").hasAnyRole("HOS","HOD")

                .antMatchers(HttpMethod.PATCH, "api/offices/{number}").hasRole("HOS")

                .antMatchers(HttpMethod.GET).permitAll()

                .anyRequest().authenticated()
                .and()
                .httpBasic().and().formLogin().and().csrf().disable();

        return http.build();
    }

    @Bean
    PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

    @Bean
    public UserDetailsService user(){
        String encoded = passwordEncoder().encode("secret");
        UserDetails HOD = User.builder().password(encoded).username("Head Of Department").roles("HOD").build();
        UserDetails HOS = User.builder().password(encoded).username("Head Of School").roles("HOS").build();
        return new InMemoryUserDetailsManager(HOD, HOS);
    }

}