package labyrinth.contracts.entities.lobby;

import com.google.gson.annotations.SerializedName;

/**
 * A Configuration that represents which bonuses are active
 * @author Lisa Pillep
 * @version 1.1
 */
public final class BoniConfiguration 
{
	//Properties
	
	@SerializedName("beam")
	private boolean isBeamEnabled = true;
	/** 
	 * Corresponding getter 
	 * @return If boni of type beam are enable 
	 */
	public boolean getIsBeamEnabled() { return isBeamEnabled; }
	/** 
	 * Corresponding setter 
	 * @param value Sets if boni of type beam are enable
	 */
	public void setIsBeamEnabled(boolean value) { this.isBeamEnabled = value; }
	
	@SerializedName("shiftSolid")
	private boolean isShiftSolidEnabled = true;
	/** 
	 * Corresponding getter 
	 * @return If boni of type shiftSolid are enable 
	 */
	public boolean getIsShiftSolidEnabled() { return isShiftSolidEnabled; }
	/** 
	 * Corresponding setter 
	 * @param value Sets if boni of type shiftSolid are enable
	 */
	public void setIsShiftSolidEnabled(boolean value) { this.isShiftSolidEnabled = value; }
	
	@SerializedName("swap")
	private boolean isSwapEnabled = true;
	/** 
	 * Corresponding getter 
	 * @return If boni of type swap are enable 
	 */
	public boolean getIsSwapEnabled() { return isSwapEnabled; }
	/** 
	 * Corresponding setter 
	 * @param value Sets if boni of type swap are enable
	 */
	public void setIsSwapEnabled(boolean value) { this.isSwapEnabled = value; }
	
	@SerializedName("shiftTwice")
	private boolean isShiftTwiceEnabled = true;
	/** 
	 * Corresponding getter 
	 * @return If boni of type shiftTwice are enable
	 */
	public boolean getIsShiftTwiceEnabled() { return isShiftTwiceEnabled; }
	/** 
	 * Corresponding setter 
	 * @param value Sets if boni of type shiftTwice are enable
	 */
	public void setIsShiftTwiceEnabled(boolean value) { this.isShiftTwiceEnabled = value; }	
}
