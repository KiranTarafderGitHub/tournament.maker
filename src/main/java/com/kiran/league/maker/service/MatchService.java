package com.kiran.league.maker.service;

import java.util.List;

import com.kiran.league.maker.common.bean.rest.ScheduleView;
import com.kiran.league.maker.persist.entity.Round;
import com.kiran.league.maker.persist.entity.Team;
import com.kiran.league.maker.persist.entity.Tournament;

public interface MatchService {
	
	public void createMatchSchedule(List<Team> teams, List<Round> rounds);
	
	public ScheduleView  getScheduleForTournament(Tournament tournament);

}
