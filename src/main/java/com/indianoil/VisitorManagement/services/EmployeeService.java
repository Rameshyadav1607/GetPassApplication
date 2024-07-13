package com.indianoil.VisitorManagement.services;

import com.indianoil.VisitorManagement.entity.AdminEntity;
import com.indianoil.VisitorManagement.entity.EmployeeEntity;
import com.indianoil.VisitorManagement.entity.SettingsEntity;
import com.indianoil.VisitorManagement.interfaces.GetEmployeeByLocCode;
import com.indianoil.VisitorManagement.login.EmployeeLoginModal;
import com.indianoil.VisitorManagement.login.JwtUtils;
import com.indianoil.VisitorManagement.repository.AdminRepository;
import com.indianoil.VisitorManagement.repository.EmployeeRepository;
import com.indianoil.VisitorManagement.repository.SettingsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.naming.Context;
import javax.naming.NamingException;
import javax.naming.directory.DirContext;
import javax.naming.directory.InitialDirContext;
import java.util.Hashtable;
import java.util.List;
import java.util.Optional;

@Service
public class EmployeeService {
    @Autowired
    private EmployeeRepository employeeRepository;
    @Autowired
    AuthenticationManager authenticationManager;
    @Autowired
    private final SettingsRepository settingsRepository;

    public EmployeeService(EmployeeRepository employeeRepository, SettingsRepository settingsRepository, AdminRepository adminRepository) {
        this.employeeRepository = employeeRepository;
        this.settingsRepository = settingsRepository;
        this.adminRepository = adminRepository;
    }

    @Autowired
    private AdminRepository adminRepository;
    @Autowired
    JwtUtils jwtUtils;
  public List<GetEmployeeByLocCode> getemployeebyloc(String loc) {

      System.out.println(loc);
      System.out.println(employeeRepository.findemployeeatloc(loc));
        return employeeRepository.findemployeeatloc(loc);
   }
    public Optional<EmployeeEntity> getEmployee(int id){
        return employeeRepository.findById(id);

    }

    @Transactional
    public EmployeeEntity loadbyId(int username) throws UsernameNotFoundException {
        EmployeeEntity employeeEntity = employeeRepository.findById(username)
                .orElseThrow(() -> new UsernameNotFoundException("User Not Found with id: " + username));

        return employeeEntity;
    }
    public String appendzeroestocurr(String s){
      String append="";
      if(s.length()<4 && s.length()>0) {
          int num=s.length();
          if(num==2)
              append="00"+s;
          else if (num ==3) {
              append="0"+s;
          }
          else if (num ==1) {
              append="000"+s;
          }

      } else if ( s.length()==4) {
       append=s;
      }
        return append;
    }
    public String GetRole(Optional<EmployeeEntity> employeeEntity){
        System.out.println("Test");
        Optional<AdminEntity> adminEntity=adminRepository.findById(employeeEntity.get().getEmp_code());


        if( adminEntity.isEmpty()==false) {
            System.out.println(adminEntity);


            return adminEntity.get().getRole();
        }
     if( employeeEntity.get().getLocn_ic_yn()!=null && employeeEntity.get().getLocn_ic_yn().equalsIgnoreCase("X")) {

         return "INCHARGE";
     }

      if(employeeEntity.get().getLoc_code().equalsIgnoreCase(appendzeroestocurr(employeeEntity.get().getCurr_comp_code())))
      {
          if(employeeEntity.get().getCurr_comp_code().equalsIgnoreCase("100"))
          {
              return "HO";
          }
          else {
              return "SO";
          }
      }
      else {
          return "OFFICER";
      }
    }
    public ResponseEntity<?> login(EmployeeLoginModal loginRequest) throws Exception {


//    Authentication authentication = authenticationManager
//            .authenticate(new UsernamePasswordAuthenticationToken(loginRequest.getId(), loginRequest.getPassword()));
////        System.out.println("authentication"+authentication);
//    SecurityContextHolder.getContext().setAuthentication(authentication);
//
//        if(Authenticate(appendZeros(String.valueOf(loginRequest.getId())),loginRequest.getPassword())) {
//            System.out.println("workking");
    Optional<EmployeeEntity> employeeEntity = employeeRepository.findById(loginRequest.getId());
if(employeeEntity!=null)
{
        employeeEntity.get().setRole(GetRole(employeeEntity));
        System.out.println("entity"+employeeEntity);
    ResponseCookie jwtCookie = jwtUtils.generateJwtCookie(employeeEntity);
        System.out.println("jwt"+jwtCookie);


    return ResponseEntity.ok().header(HttpHeaders.SET_COOKIE, jwtCookie.toString())
            .body(employeeEntity);
}
        else {
            return ResponseEntity.notFound().build();
        }
    }
    public boolean Authenticate(String user, String pass) throws Exception {

        Hashtable<String, String> env = null;
        DirContext ctx = null;
        try {
            env = new Hashtable<String, String>();
            env.put(Context.INITIAL_CONTEXT_FACTORY, "com.sun.jndi.ldap.LdapCtxFactory");
            env.put(Context.PROVIDER_URL, "LDAP://dcmkho03:389 LDAP://dcmkho1:389 LDAP://dcmkho2:389 ");
            // env.put(Context.PROVIDER_URL, "LDAP://dcmkho1:389");
            // env.put(Context.PROVIDER_URL, "LDAP://ds.indianoil.in:389");
            env.put(Context.SECURITY_AUTHENTICATION, "DIGEST-MD5");
            env.put(Context.SECURITY_PRINCIPAL, user);
            env.put(Context.SECURITY_CREDENTIALS, pass);
            ctx = new InitialDirContext(env);
            ctx.close();
        } catch (NamingException ne) {
            System.out.println(ne);
            return false;
        } finally {
            env = null;
            ctx = null;
        }
        return true;

    }
    public String appendZeros(String id)
    {
        while(id.length()<8){
            id="0"+id;
        }return id;


    }

    public Optional<SettingsEntity> getsettings(int id) {
      AdminEntity adminEntity=adminRepository.getEmployeerole(id);
      Optional<EmployeeEntity> employeeEntity=employeeRepository.findById(id);
      if(adminEntity!=null)
          return settingsRepository.findById(adminEntity.getLoc_code());
      if( employeeEntity.get().getLocn_ic_yn()!=null && employeeEntity.get().getLocn_ic_yn().equalsIgnoreCase("X"))
          return settingsRepository.findById(employeeEntity.get().getLoc_code());

      return Optional.of(new SettingsEntity());
    }

    public Optional<SettingsEntity> UpdateSettings(int id, SettingsEntity settingsEntity) {
        System.out.println(settingsEntity);
        Optional<EmployeeEntity> employeeEntity = employeeRepository.findById(id);
        return Optional.of(settingsRepository.save(settingsEntity));

    }

//    public ResponseEntity<AdminEntity> getsettings(int id) {
//
//    }
}
