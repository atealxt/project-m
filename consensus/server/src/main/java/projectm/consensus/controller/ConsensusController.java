package projectm.consensus.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.ApiParam;
import projectm.consensus.ConsensusServer;
import projectm.consensus.NodeAddress;
import projectm.consensus.State;
import projectm.consensus.service.NotifyResult;

@RestController
@RequestMapping("api/consensus")
public class ConsensusController {

	protected Logger logger = LoggerFactory.getLogger(getClass());
	@Autowired
	private ConsensusServer consensusServer;

	@PostMapping("/state")
	public State changeState(//
			@ApiParam(value = "State Value") //
			@RequestParam(value = "state", required = true) String state, //
			HttpServletRequest request, HttpServletResponse response) {
		return consensusServer.transition(State.parse(state));
	}

	@GetMapping("/state")
	public State getState(//
			HttpServletRequest request, HttpServletResponse response) {
		return consensusServer.getState();
	}

	@GetMapping("/notify")
	public NotifyResult notify(//
			@ApiParam(value = "IP") //
			@RequestParam(value = "ip", required = true) String ip, //
			@ApiParam(value = "Port") //
			@RequestParam(value = "port", required = true) String port, //
			@ApiParam(value = "State Value") //
			@RequestParam(value = "state", required = true) String state, //
			HttpServletRequest request, HttpServletResponse response) {
		return consensusServer.notify(new NodeAddress(ip, Integer.parseInt(port)), State.parse(state));
	}

	@GetMapping("/resource")
	public String getResource(//
			@ApiParam(value = "key") //
			@RequestParam(value = "key", required = true) String key, //
			HttpServletRequest request, HttpServletResponse response) {
		// redirect to leader if it's not.
		if (consensusServer.getState() != State.LEADER) {
			NodeAddress leader = consensusServer.getLeaderAddress();
			response.setStatus(HttpServletResponse.SC_FOUND);
			response.setHeader("Location", leader.getHttpAddr() + "/api/consensus/resource?key=" + key);
			return null;
		}
		return consensusServer.getResource(key).getValue();
	}
}
