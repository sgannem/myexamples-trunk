package de.intarsys.security.smartcard.smartcardio;

import java.security.Security;
import java.util.List;

import javax.smartcardio.Card;
import javax.smartcardio.CardTerminal;
import javax.smartcardio.CardTerminals;
import javax.smartcardio.CardTerminals.State;
import javax.smartcardio.CommandAPDU;
import javax.smartcardio.ResponseAPDU;
import javax.smartcardio.TerminalFactory;

import de.intarsys.security.smartcard.card.ATR;
import de.intarsys.security.smartcard.pcsc.Utilities;

public class Connect {

	public static void main(String[] args) {
		try {
			new Connect().run();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	protected void connect(CardTerminal cardTerminal) throws Exception {
		System.out.println("" + cardTerminal + " connect");
		Card card = cardTerminal.connect("*");
		javax.smartcardio.ATR atr = card.getATR();
		System.out.println(Utilities.dumpBytes(atr.getBytes()));
		
//		CommandAPDU apdu1 = new CommandAPDU(new byte[]{(byte) 0xFF,0x00,0x00,0x00,0x01,0x60});
//		CommandAPDU apdu2 = new CommandAPDU(new byte[]{(byte) 0xFF,0x00,0x00,0x00,0x01,(byte) 0xAF});
		//CommandAPDU apdu1 = new CommandAPDU(new byte[]{(byte) 0xFF,0x60,0x00,0x00});
		
		
//		CommandAPDU apdu1 = new CommandAPDU(new byte[]{(byte) 0x90,0x60,0x00,0x00,0});
//		CommandAPDU apdu2 = new CommandAPDU(new byte[]{(byte) 0x90,(byte) 0xAF,0x00,0x00,0});
		
		CommandAPDU apdu1 = new CommandAPDU(new byte[]{(byte) 0x90,0x60,0x00,0x00});
		CommandAPDU apdu2 = new CommandAPDU(new byte[]{(byte) 0xFF,(byte) 0xAF,0x00,0x00,0});
		
		
		CommandAPDU niyathcommand = new CommandAPDU((byte)0xFF, (byte)0xCA, (byte)0x00, (byte)0x00, (byte)0x00);
		//ResponseAPDU resApdu = card.getBasicChannel().transmit(niyathcommand);
		ResponseAPDU resApdu = card.getBasicChannel().transmit(apdu1);
		System.out.println(Utilities.dumpBytes(resApdu.getBytes()));
		System.out.println(Utilities.dumpBytes(resApdu.getData()));
		
		resApdu = card.getBasicChannel().transmit(apdu2);
		System.out.println(Utilities.dumpBytes(resApdu.getBytes()));
		System.out.println(Utilities.dumpBytes(resApdu.getData()));
		resApdu = card.getBasicChannel().transmit(apdu2);
		System.out.println(Utilities.dumpBytes(resApdu.getBytes()));
		System.out.println(Utilities.dumpBytes(resApdu.getData()));
		
		try {
			System.out.println("" + card + " begin transaction");
			card.beginExclusive();
			System.out.println("" + card + " end transaction");
			card.endExclusive();
			System.out.println("" + card + " Exception not occured.");
		} finally {
			System.out.println("" + card + " disconnect");
			card.disconnect(false);
		}
	}

	public void run() throws Exception {
		Security.insertProviderAt(new SmartcardioProvider(), 1);
		//
		TerminalFactory factory = TerminalFactory.getDefault();
		CardTerminals terminals = factory.terminals();
		//terminals.getTerminal("");
		while (System.in.available() == 0) {
			List<CardTerminal> list = terminals.list(State.ALL);
			if (list.isEmpty()) {
				System.out.println("no terminals");
			}
			
			
			for (CardTerminal cardTerminal : list) {
				try {
					cardTerminal.waitForCardPresent(1000);
					connect(cardTerminal);
				} catch (Exception e) {
					System.out.println("error connecting " + e);
				}
			}
			System.out.println("wait for change");
			if (terminals.waitForChange(1000)) {
				System.out.println("change detected...");
			} else {
				System.out.println("timeout");
			}
		}
	}
}
