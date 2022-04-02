package com.kiran.league.maker.controller;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.kiran.league.maker.common.bean.rest.TournamentCreate;
import com.kiran.league.maker.common.exception.InvalidDataException;
import com.kiran.league.maker.common.exception.NoDataFoundException;
import com.kiran.league.maker.persist.entity.Tournament;
import com.kiran.league.maker.persist.entity.TournamentType;
import com.kiran.league.maker.persist.entity.UserEntity;
import com.kiran.league.maker.service.TournamentAdminService;
import com.kiran.league.maker.service.TournamnetService;

@Controller
@RequestMapping("/public")
public class PublicController {

	
	private static final Log log = LogFactory.getLog(PublicController.class);
	
	@Autowired
	TournamnetService tournamnetService;
	
	@Autowired
	TournamentAdminService tournamentAdminService;
	
	@Autowired
	PasswordEncoder encoder;
	
	@Value("${application.url}")
	String applicationUrl;
	
	@Value("${league.view.admin.url}")
	String adminUrl;
	
	@Value("${league.view.user.url}")
	String userUrl;
	
	@Value("${user.admin.default.password}")
	String defaultAdminPassword;
	
	
	@GetMapping("/league/create.html")
    public ModelAndView createLeague(ModelAndView model)
    {
		
        try
        {
        	TournamentCreate tournament = new TournamentCreate();
        	model.addObject("tournament",tournament);
        	model.addObject("tournamentType",TournamentType.values());
        	model.setViewName("public/league/create");
        }
        catch (Exception e)
        {
            log.error(e.getMessage(),e);
            model.addObject("errorMsg",e.getMessage());
            model.setViewName("error");
        }
        
        
        return model;
    }
	
	@PostMapping("/league/create.html")
    public ModelAndView createLeaguePost(ModelAndView model, @ModelAttribute TournamentCreate tournamentCreate)
    {
		
        try
        {
        	log.info("createLeaguePost called");
        	log.info(tournamentCreate.toString());
        	Tournament tournament = tournamnetService.createNewTournamnet(tournamentCreate);
        	
        	UserEntity user = tournamentAdminService.creteAdminUserForTournament(tournament);
        	
        	String adminAccessUrl = applicationUrl+adminUrl+tournament.getCode();
        	String userAccessUrl = applicationUrl+userUrl+tournament.getCode();
        	
        	model.addObject("adminAccessUrl",adminAccessUrl);
        	model.addObject("userAccessUrl",userAccessUrl);
        	
        	model.addObject("adminUsername",user.getUsername());
        	model.addObject("adminPassword",defaultAdminPassword);
        	model.addObject("successMsg","League Successfully Created");
        	model.addObject("tournament",tournament);
        	
        	model.setViewName("public/league/credential");
        }
        catch(NoDataFoundException e)
        {
        	log.error(e.getMessage(),e);
        	model.addObject("errorMsg",e.getMessage());
        	model.setViewName("error");
        }
        catch(InvalidDataException e)
        {
        	log.error(e.getMessage(),e);
        	model.addObject("errorMsg",e.getMessage());
        	model.setViewName("error");
        }
        catch (Exception e)
        {
            log.error(e.getMessage(),e);
            model.addObject("errorMsg",e.getMessage());
            model.setViewName("error");
        }
        
        return model;
    }
	
	@GetMapping("/league/{leagueCode}")
    public ModelAndView viewLeague(ModelAndView model, @PathVariable String leagueCode)
    {
		
        try
        {
        	TournamentCreate tournament = new TournamentCreate();
        	model.addObject("tournament",tournament);
        	model.addObject("tournamentType",TournamentType.values());
        	model.setViewName("public/league/view");
        }
        catch (Exception e)
        {
            log.error(e.getMessage(),e);
            model.setViewName("error");
        }
        
        
        return model;
    }
}
