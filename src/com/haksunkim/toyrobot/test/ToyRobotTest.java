package com.haksunkim.toyrobot.test;

import org.junit.Test;

import com.haksunkim.toyrobot.ToyRobot;
import com.haksunkim.toyrobot.exception.InvalidFaceException;
import com.haksunkim.toyrobot.exception.InvalidInputException;
import com.haksunkim.toyrobot.exception.PositionOutOfRangeException;
import com.haksunkim.toyrobot.exception.RobotNotPlacedException;

public class ToyRobotTest {

	@Test
	public void test1() throws InvalidInputException, PositionOutOfRangeException, InvalidFaceException, RobotNotPlacedException {
		ToyRobot robot = new ToyRobot();
		robot.runCommand("PLACE 0,0,NORTH");
		robot.runCommand("MOVE");
		System.out.println(robot.report());
	}
	
	@Test
	public void test2() throws InvalidInputException, PositionOutOfRangeException, InvalidFaceException, RobotNotPlacedException {
		ToyRobot robot = new ToyRobot();
		robot.runCommand("PLACE 0,0,NORTH");
		robot.runCommand("LEFT");
		System.out.println(robot.report());
	}
	
	@Test
	public void test3() throws InvalidInputException, PositionOutOfRangeException, InvalidFaceException, RobotNotPlacedException {
		ToyRobot robot = new ToyRobot();
		robot.runCommand("PLACE 1,2,EAST");
		robot.runCommand("MOVE");
		robot.runCommand("MOVE");
		robot.runCommand("LEFT");
		robot.runCommand("MOVE");
		System.out.println(robot.report());
	}
}
