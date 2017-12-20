////package jssc_test;
//
////import jssc.SerialPort; 
////import jssc.SerialPortList;
////import jssc.SerialPortException;
////public class Main {
////
////	public static void main(String[] args) {
////		String[] portNames = SerialPortList.getPortNames();
////	    for(int i = 0; i < portNames.length; i++){
////	        System.out.println(portNames[i]);
////	    }
////	    
////		SerialPort serialPort = new SerialPort("COM4");
////		
////	    try {
////	    	serialPort.openPort();
////	    	serialPort.setParams(19200,  8,  1, 0);
////	    	serialPort.writeBytes("Y".getBytes());
//////	        System.out.println("Port opened: " + serialPort.openPort());
//////	        System.out.println("Params setted: " + serialPort.setParams(9600, 8, 1, 0));
//////	        System.out.println("\"Hello World!!!\" successfully writen to port: " + serialPort.writeBytes("Hello World!!!".getBytes()));
////	        System.out.println("Port closed: " + serialPort.closePort());
////	    }
////	    catch (SerialPortException ex){
////	        System.out.println(ex);
////	    }
////	}
////
////}
//
//
//import java.util.*;
//
//import jssc.SerialPort; 
//import jssc.SerialPortEvent; 
//import jssc.SerialPortEventListener; 
//import jssc.SerialPortException;
//
//public class Main {
//
//	static boolean startLensRx = false;
//	static SerialPort serialPort;	
//	static boolean portOpened = false;
//	static boolean isConnected = false;
//	static boolean transfer = false;
//	
//	static int baudRate = 19200;
//	static int currentLens = 0;
//	static ArrayList<String> lensArray = new ArrayList<String>();
//	static Integer numLenses;
//	
//	static byte[] SYN = {0x16};
//	static byte[] SOH = {0x01};
//	static byte[] ENQ = {0x05};
//	static byte[] SO = {0x0E};
//	static byte[] LF = {0x0A};
//	static byte[] CR = {0x0D};
//	static byte[] STX = {0x02};
//	static byte[] EOT = {0x04};
//	static byte[] ACK = {0x06};
//	static byte[] NAK = {0x15};
//	static byte[] init_dl = {0x11, 0x05};
//	static byte[] init_ul = {0x01, 0x05};
//	static byte[] ACK_SYN = {0x06, 0x16};
//	
//	static String EOTStr = new String(EOT);
//	static String ACKStr = new String(ACK);
//	static String NAKStr = new String(NAK);
//	
//	static StringBuilder lensSBuilder = new StringBuilder("");
//	
//	static byte[] RxBuffer;
//	
//	static Timer timer = new Timer();
//	
//	public static void main(String[] args) {
//	    serialPort = new SerialPort("COM1"); 											// declare the port. TODO: make the program dynamically scan port list and check for correct one						
//	//    Timer timer = new Timer();   
//	    
//	    try {
//	        portOpened = serialPort.openPort();																	// Open port
//	        if (portOpened) {																					// if the port opened OK
//		        serialPort.setParams(baudRate, 8, 1, 0);														// Set up port
//		        int mask = SerialPort.MASK_RXCHAR + SerialPort.MASK_CTS + SerialPort.MASK_DSR;					// Prepare mask
//		        serialPort.setEventsMask(mask);																	// Set mask
//		        serialPort.addEventListener(new SerialPortReader());											// Add SerialPortEventListener, called when diff events happen
//		        
//		        // create the 100ms timer task that will send the SYN character when the program is idle. This pings the HU3
//		        TimerTask ping = new TimerTask() {
//		        	public void run() {
//		        		if (!isConnected) {																		// only send the ping if we're not current connected to an HU3
//			        		try {
//								serialPort.writeBytes(SYN);														// write the SYN character to the serial port
//								System.out.println("SYN written to serial port");
//							} catch (SerialPortException e) {
//								e.printStackTrace();
//							}
//		        		}
//		        	}
//		        };
//		        	        
//		        timer.scheduleAtFixedRate(ping, 500, 100);														// run the timer every 100ms, after a 500ms initial delay
//	        }
//	    }
//	    catch (SerialPortException ex) {
//	        System.out.println(ex);
//	    }
//	        
//	}
//	
//	//buffer the input from the serial port so we are always dealing with complete packets
//	private static String buildLensPacket(byte[] bytes) {
//	    String text = bytesToText(bytes);
//	    if (text.contains(bytesToText(EOT))) {        //EOT is sent after transfer is complete
//	        System.out.println("EOT detected. Returning EOT");
//	        lensSBuilder.setLength(0);      // clear the buffer
//	        return EOTStr;
//	    }
//	    else {          // if no newline character detected, add response to the buffer
//	        lensSBuilder.append(text);
//	        if (text.contains("\r")) {
//	            String lensString = lensSBuilder.toString();
//	            lensSBuilder.setLength(0);
//	            return lensString;          // if newline detected, add the text to the buffer, then return the whole buffer
//	        } else {
//	            return "";
//	        }
//	    }
//	}
//	
//	private static String bytesToText(byte[] bytes) {
//	    String text = new String(bytes);
//	//    if (simplifyNewLine) {
//	//        text = text.replaceAll("(\\r\\n|\\r)", "\n");
//	//    }
//	    return text;
//	}
//	
//	private static void receiveLensData(String text) throws SerialPortException {
//	//	System.out.println("receiveLensData: " + text);
//	//	if (transfer) {
//			if (!startLensRx) {
//		        if (text.contains("Hand")) {
//		            System.out.println("Hand detected");
//		            timer.cancel();
//		//            lensModeConnected = true;
//		//            byte[] new_byte = {0x11, 0x05};
//		//            uartSendData(new_byte, false);
//		            serialPort.writeBytes(init_dl);
//		        } else {
//		            serialPort.writeBytes(ACK);
//		            startLensRx = true;
//		            String trimmedString = text.replaceAll("[^\\w]", "");
//		            numLenses = Integer.valueOf(trimmedString, 16);
//		            System.out.println("Number of lenses detected: " + numLenses);
//		//            runOnUiThread(new Runnable() {
//		//                @Override
//		//                public void run() {
//		//                    activateLensTransferProgress("RX");
//		//                }
//		//            });
//		        }
//		    } else {
//		        if (text.contains(EOTStr)) {
//		            System.out.println("EOT detected");
//		            serialPort.writeBytes(ACK_SYN);
//		//            lensModeConnected = false;
//		            transfer = false;
//		            startLensRx = false;
//		            currentLens = 0;
//		//            askToSaveLenses();
//		        } else {
//		            System.out.println("Lens " + currentLens + " of " + numLenses + ": " + text);
//		            lensArray.add(text);
//		            currentLens += 1;
//		            serialPort.writeBytes(ACK);
//		        }
//		
//		    }
//	//	}
//	}
//	
//	/*
//	 * In this class must implement the method serialEvent, through it we learn about 
//	 * events that happened to our port. But we will not report on all events but only 
//	 * those that we put in the mask. In this case the arrival of the data and change the 
//	 * status lines CTS and DSR
//	 */
//	
//	static class SerialPortReader implements SerialPortEventListener {
//	
//	    public void serialEvent(SerialPortEvent event) {
//	//    	System.out.println("SerialPort Event");
//	        if(event.isRXCHAR()){//If data is available
//	//        	System.out.println("RX DATA detected");
//	        	try {
//					RxBuffer = serialPort.readBytes();
//					String lensString = buildLensPacket(RxBuffer);
//					if (lensString.length() > 0) {
//						receiveLensData(lensString);
//						System.out.println("lensString: " + lensString.trim());
//					}
//	//				String RxBufferString = new String(RxBuffer);
//	//				System.out.println("RX: " + RxBufferString + "$$");
//	//				for (int i=0; i < RxBuffer.length; i++) {
//	//					System.out.println(RxBuffer[i]);
//	//					if (RxBuffer[i] == LF) {
//	//						System.out.println("Newline found at index " + i);
//	//					}
//	//				}
//				} catch (SerialPortException e) {
//					// TODO Auto-generated catch block
//					e.printStackTrace();
//				}
//	//            if(event.getEventValue() == 10){//Check bytes count in the input buffer
//	//                //Read data, if 10 bytes available 
//	//                try {
//	//                    byte buffer[] = serialPort.readBytes(10);
//	//                    System.out.println(new String(buffer));
//	//                }
//	//                catch (SerialPortException ex) {
//	//                    System.out.println(ex);
//	//                }
//	//            }
//	        }
//	//        else if(event.isCTS()){//If CTS line has changed state
//	//            if(event.getEventValue() == 1){//If line is ON
//	//                System.out.println("CTS - ON");
//	//            }
//	//            else {
//	//                System.out.println("CTS - OFF");
//	//            }
//	//        }
//	//        else if(event.isDSR()){///If DSR line has changed state
//	//            if(event.getEventValue() == 1){//If line is ON
//	//                System.out.println("DSR - ON");
//	//            }
//	//            else {
//	//                System.out.println("DSR - OFF");
//	//            }
//	//        }
//	    }
//	}
//
//}
