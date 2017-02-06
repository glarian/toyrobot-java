package com.haksunkim.toyrobot;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

import com.haksunkim.toyrobot.exception.InvalidFaceException;
import com.haksunkim.toyrobot.exception.InvalidInputException;
import com.haksunkim.toyrobot.exception.PositionOutOfRangeException;
import com.haksunkim.toyrobot.exception.RobotNotPlacedException;

public class ToyRobot {
	private static final int MIN_X = 0;
	private static final int MAX_X = 4;
	private static final int MIN_Y = 0;
	private static final int MAX_Y = 4;
	private static final String FACE_NORTH = "NORTH";
	private static final String FACE_SOUTH = "SOUTH";
	private static final String FACE_EAST = "EAST";
	private static final String FACE_WEST = "WEST";
	private static final String CMD_PLACE = "PLACE";
	private static final String CMD_MOVE = "MOVE";
	private static final String CMD_REPORT = "REPORT";
	private static final String CMD_LEFT = "LEFT";
	private static final String CMD_RIGHT = "RIGHT";
	private static final String OUTPUT_PATH = "output.txt";
	private static final String INPUT_PATH = "input.txt";
	
	private int pos_x = -1;
	private int pos_y = -1;
	private String face;
	private boolean isPlaced;
	
	public ToyRobot() {
		setIsPlaced(false);
		File outputFile = new File(OUTPUT_PATH);
		if (!outputFile.exists()) {
			try {
				outputFile.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	// operate toy robot with input file
	public void operate() {
		// read input file
		try {
			File inputFile = new File(INPUT_PATH);
			FileReader fr = new FileReader(inputFile);
			BufferedReader bfr = new BufferedReader(fr);
			String line;
			
			while ((line = bfr.readLine()) != null) {
				// output line to output file
				output(line);
				System.out.println(line);
				try {
					runCommand(line);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			fr.close();
		} catch (IOException ioe) {
			// do nothing base on the requirements
			ioe.printStackTrace();
		}
	}
	
	// interpret line and run command
	public void runCommand(String line) throws InvalidInputException, PositionOutOfRangeException, InvalidFaceException, RobotNotPlacedException {
		// interpret command line, and do action
		String[] command = line.split("\\s");
		if (command.length > 0) {
			if (command.length == 2 && command[0].equals(CMD_PLACE)) place(command[1]);
			else if (command[0].equals(CMD_MOVE)) move();
			else if (command[0].equals(CMD_LEFT)) left();
			else if (command[0].equals(CMD_RIGHT)) right();
			else if (command[0].equals(CMD_REPORT)) output(report());
		}
	}
	
	// place the robot based on command line
	public void place(String commandLine) throws InvalidInputException, PositionOutOfRangeException {
		String[] placement = commandLine.split(",");
		if (placement.length == 3) {
			int positionX = Integer.valueOf(placement[0]);
			int positionY = Integer.valueOf(placement[1]);
			place(positionX,positionY,placement[2]);
		} else throw new InvalidInputException("Input line has invalid command");
	}
	
	// place the robot on pos_x, pos_y
	public void place(int pos_x, int pos_y, String face) throws PositionOutOfRangeException, InvalidInputException {
		if ((pos_x >= MIN_X && pos_x <= MAX_X) && (pos_y >= MIN_Y && pos_y <= MAX_Y)) {
			if (   face.equals(FACE_NORTH)
				|| face.equals(FACE_EAST)
				|| face.equals(FACE_SOUTH)
				|| face.equals(FACE_WEST)) {
				setPosX(pos_x);
				setPosY(pos_y);
				setFace(face);
				setIsPlaced(true);
			} else throw new InvalidInputException("Input line has invalid command");			
		} else throw new PositionOutOfRangeException("Position out of range.");
	}
	
	// move robot
	public void move() throws InvalidFaceException, PositionOutOfRangeException, RobotNotPlacedException, InvalidInputException {
		if (!isPlaced()) throw new RobotNotPlacedException("This robot has not been placed yet.");
		// check face, and increase or decrease pos_x, pos_y
		int currentPositionX = getPosX();
		int currentPositionY = getPosY();
		String currentFace = getFace();
		if (currentFace.equals(FACE_NORTH)) currentPositionY++;
		else if (currentFace.equals(FACE_EAST)) currentPositionX++;
		else if (currentFace.equals(FACE_SOUTH)) currentPositionY--;
		else if (currentFace.equals(FACE_WEST)) currentPositionX--;
		else throw new InvalidFaceException("This robot has invalid face.");
		
		place(currentPositionX, currentPositionY, face);
	}
	
	// turn counterclockwise
	public void left() throws InvalidFaceException, RobotNotPlacedException {
		if (!isPlaced()) throw new RobotNotPlacedException("This robot has not been placed yet.");
		String currentFace = getFace();
		if (currentFace.equals(FACE_NORTH)) setFace(FACE_WEST);
		else if (currentFace.equals(FACE_EAST)) setFace(FACE_NORTH);
		else if (currentFace.equals(FACE_SOUTH)) setFace(FACE_EAST);
		else if (currentFace.equals(FACE_WEST)) setFace(FACE_SOUTH);
		else throw new InvalidFaceException("This robot has invalid face.");
	}
	
	// turn clockwise
	public void right() throws InvalidFaceException, RobotNotPlacedException {
		if (!isPlaced()) throw new RobotNotPlacedException("This robot has not been placed yet.");
		String currentFace = getFace();
		if (currentFace.equals(FACE_NORTH)) setFace(FACE_EAST);
		else if (currentFace.equals(FACE_EAST)) setFace(FACE_SOUTH);
		else if (currentFace.equals(FACE_SOUTH)) setFace(FACE_WEST);
		else if (currentFace.equals(FACE_WEST)) setFace(FACE_NORTH);
		else throw new InvalidFaceException("This robot has invalid face.");
	}
	
	// returns String with current robot position and facing
	public String report() throws RobotNotPlacedException {
		if (!isPlaced()) throw new RobotNotPlacedException("This robot has not been placed yet.");
		String outputString = "Output: " + this.toString();
		
		return outputString;
	}
	
	// write output to output file
	private void output(String output) {
		output = output + "\n";
		try {
		    Files.write(Paths.get(OUTPUT_PATH), output.getBytes(), StandardOpenOption.APPEND);
		}catch (IOException e) {
		    //exception handling left as an exercise for the reader
			e.printStackTrace();
		}
	}
	
	// set pos_x
	public void setPosX(int pos_x) {
		this.pos_x = pos_x;
	}
	
	// set pos_y
	public void setPosY(int pos_y) {
		this.pos_y = pos_y;
	}
	
	// set face
	public void setFace(String face) {
		this.face = face;
	}
	
	// return pos_x	
	public int getPosX() {
		return this.pos_x;
	}
	
	// return pos_y
	public int getPosY() {
		return this.pos_y;
	}
	
	// return face
	public String getFace() {
		return this.face;
	}
	
	public void setIsPlaced(boolean isPlaced) {
		this.isPlaced = isPlaced;
	}
	
	public boolean isPlaced() {
		return this.isPlaced;
	}
	
	// compose and return string with pos_x,pos_y,face
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append(pos_x);
		sb.append(",");
		sb.append(pos_y);
		sb.append(",");
		sb.append(face);
		
		return sb.toString();
	}
}
