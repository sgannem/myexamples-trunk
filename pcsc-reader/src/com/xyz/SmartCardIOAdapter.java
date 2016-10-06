package com.xyz;

import java.util.List;

import javax.smartcardio.Card;
import javax.smartcardio.CardChannel;
import javax.smartcardio.CardException;
import javax.smartcardio.CardTerminal;
import javax.smartcardio.CommandAPDU;
import javax.smartcardio.ResponseAPDU;
import javax.smartcardio.TerminalFactory;

import com.nxp.nfclib.LibraryManager;
import com.nxp.nfclib.sysinterfaces.IApduHandler;
import com.nxp.nfclib.sysinterfaces.IReader;

/**
 * Adapter to link MIFARE SDK with javax smartcardio.
 * 
 * @author nxp82733
 *
 */
public class SmartCardIOAdapter implements IApduHandler {

	CardChannel channel;
	Card card;

//	@Override
	public byte[] apduExchange123(final byte[] arg0) {
		LibraryManager MIFARESdk = new LibraryManager();
		byte[] wrappedData = new byte[]{(byte)0xFF,(byte)0x00,(byte)0x00,(byte)0x00};
		wrappedData = MIFARESdk.getUtility().append(wrappedData,arg0);
		ResponseAPDU answer;
		
		try {
			System.out.println("sending: " + MIFARESdk.getUtility().dumpBytes(wrappedData));
			channel.transmit(new CommandAPDU(wrappedData));
			
			
			//System.out.println("answer: " + answer.toString());
			return null;
		} catch (CardException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return null;
	}
	
	@Override
	public byte[] apduExchange(final byte[] arg0) {
		LibraryManager MIFARESdk = new LibraryManager();
		byte[] wrappedData = new byte[]{(byte)0xFF,(byte)0x00,(byte)0x00,(byte)0x00};
		wrappedData = MIFARESdk.getUtility().append(wrappedData,arg0);
		ResponseAPDU answer;
		
		try {
			System.out.println("sending: " + MIFARESdk.getUtility().dumpBytes(wrappedData));
			answer = channel.transmit(new CommandAPDU(wrappedData));
			System.out.println("answer: " + answer.toString());
			return answer.getBytes();
		} catch (CardException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return null;
	}

	/**
	 * Lists out the terminals connected. Currently connects to the second
	 * reader listed ( for the ACR 128U reader on Windows 7). This can be
	 * changed to
	 */
	public void start(int terminalNumber) {
		try {
			// Display the list of terminals
			List<CardTerminal> terminals = getAvaialbleTerminals();

			// Use the first terminal
			CardTerminal terminal = terminals.get(terminalNumber);

			terminal.waitForCardPresent(0);

			// Connect with the card
			card = terminal.connect("*");
			System.out.println("card: " + card);
			channel = card.getBasicChannel();

		} catch (Exception e) {
			System.out.println("Ouch: " + e.toString());
		}
	}

	public List<CardTerminal> getAvaialbleTerminals() throws CardException {
		TerminalFactory factory = TerminalFactory.getDefault();
		List<CardTerminal> terminals = factory.terminals().list();

		return terminals;
	}

	/**
	 * stops the card terminal.
	 */
	public void stop() {
		// Disconnect the card
		try {
			card.disconnect(false);
		} catch (CardException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public IReader getReader() {
		// TODO Auto-generated method stub
		return null;
	}

}
