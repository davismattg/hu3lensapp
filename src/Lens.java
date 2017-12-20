
public class Lens {
	int id;
	String dataString;
	String manufacturer;
	String series;
	int manufacturerPosition;
	int seriesPosition;
	int focalLength1;
	int focalLength2;
	boolean isPrime;
	String serial;
	String note;
	boolean calibratedF;
	boolean calibratedI;
	boolean calibratedZ;
	boolean myListA;
	boolean myListB;
	boolean myListC;
	boolean isSelected;
	
	public Lens(int id) {
		this.id = id;
	}
	
	/* Assign the data string (that HU3 uses) of the Lens to the variable dataString */
	public void setDataString(String str) {
		dataString = str;
	}
	
	/* Assign the lens manufacturer to the variable manufacturer */
	public void setManufacturer(String str) {
		manufacturer = str;
	}
	
	/* Assign the series name of the Lens to the variable series */
	public void setSeries(String str) {
		series = str;
	}
	
	/* Assign the manufacturer position (index) of the Lens to the variable manufacturerPosition */
	public void setManufacturerPosition(int pos) {
		manufacturerPosition = pos;
	}
	
	/* Assign the series position (within manufacturer) of the Lens to the variable seriesPosition */
	public void setSeriesPosition(int pos) {
		seriesPosition = pos;
	}
	
	/* Assign the first focal length of the Lens to the variable focalLength1 */
	public void setFocalLength1(int focal) {
		focalLength1 = focal;
	}
	
	/* Assign the second focal length of the Lens to the variable focalLength2 */
	public void setFocalLength2(int focal) {
		focalLength2 = focal;
	}
	
	/* Assign the prime true/false attribute of the Lens to the variable isPrime */
	public void setIsPrime(boolean prime) {
		isPrime = prime;
	}
	
	/* Assign the serial string of the Lens to the variable serial */
	public void setSerial(String str) {
		serial = str;
	}
	
	/* Assign the note string of the Lens to the variable note */
	public void setNote(String str) {
		note = str;
	}
	
	/* Assign the boolean F calibrated status to the variable calibratedF */
	public void setCalibratedF(boolean cal) {
		calibratedF = cal;
	}
	
	/* Assign the boolean I calibrated status to the variable calibratedI */
	public void setCalibratedI(boolean cal) {
		calibratedI = cal;
	}
	
	/* Assign the boolean Z calibrated status to the variable calibratedZ */
	public void setCalibratedZ(boolean cal) {
		calibratedZ = cal;
	}
	
	/* Assign the boolean My List A status to the variable myListA */
	public void setMyListA(boolean list) {
		myListA = list; 
	}
	
	/* Assign the boolean My List B status to the variable myListB */
	public void setMyListB(boolean list) {
		myListB = list; 
	}
	
	/* Assign the boolean My List C status to the variable myListC */
	public void setMyListC(boolean list) {
		myListC = list; 
	}
	
	/* Assign the boolean isSelected status to the variable isSelected */
	public void setIsSelected(boolean selected) {
		isSelected = selected;
	}
	
	/*
	 * Retrieval methods
	 */
	
	/* Retrieve the data string (that HU3 uses) of the Lens */
	public String getDataString() {
		return dataString;
	}
	
	/* Retrieve the lens manufacturer string */
	public String getManufacturer() {
		return manufacturer;
	}
	
	/* Retrieve the series name of the Lens */
	public String getSeries() {
		return series;
	}
	
	/* Retrieve the manufacturer position (index) of the Lens */
	public int getManufacturerPosition() {
		return manufacturerPosition;
	}
	
	/* Retrieve the series position (within manufacturer) of the Lens */
	public int getSeriesPosition() {
		return seriesPosition;
	}
	
	/* Retrieve the first focal length of the Lens */
	public int getFocalLength1() {
		return focalLength1;
	}
	
	/* Retrieve the second focal length of the Lens */
	public int getFocalLength2() {
		return focalLength2;
	}
	
	/* Retrieve the prime true/false attribute of the Lens */
	public boolean getIsPrime() {
		return isPrime;
	}
	
	/* Retrieve the serial string of the Lens */
	public String getSerial() {
		return serial;
	}
	
	/* Retrieve the note string of the Lens */
	public String getNote() {
		return note;
	}
	
	/* Retrieve the F calibrated status */
	public boolean getCalibratedF() {
		return calibratedF;
	}
	
	/* Retrieve the I calibrated status */
	public boolean getCalibratedI() {
		return calibratedI;
	}
	
	/* Retrieve the Z calibrated status */
	public boolean getCalibratedZ() {
		return calibratedZ;
	}
	
	/* Retrieve the My List A status */
	public boolean getMyListA() {
		return myListA; 
	}
	
	/* Retrieve the My List B status */
	public boolean getMyListB() {
		return myListB; 
	}
	
	/* Retrieve the My List C status */
	public boolean getMyListC() {
		return myListC; 
	}
	
	/* Retrieve the isSelected status (for transmitting to the HU3) */
	public boolean getIsSelected() {
		return isSelected;
	}
}
