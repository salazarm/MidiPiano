package player;
import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import org.junit.Test;

public class TokenTest {
	@Test
	public void testString(){
		FileInputStream fis = null;
		BufferedInputStream bis = null;
		DataInputStream dis = null;
		StringBuffer sb = new StringBuffer();
		try {
		fis = new FileInputStream("C:\\Users\\DW\\workspace\\dishaan-salazarm-donggu\\abcPlayer\\sample_abc\\fur_elise.abc");
		bis = new BufferedInputStream(fis);
		dis = new DataInputStream(bis);

		while (dis.available() != 0) {
		sb.append(dis.readLine());
		sb.append("\n");
		}
		fis.close();
		bis.close();
		dis.close();

		} catch (FileNotFoundException e) {
		e.printStackTrace();
		} catch (IOException e) {
		e.printStackTrace();
		}
		String input = sb.toString();
		Lexer result = new Lexer(input);
		for (Token c: result.getBody()){
			System.out.println(c.getValue());
		}
		for (Token c: result.getHeader()){
			System.out.println(c.getValue());
		}
	}
}
