package br.ufc.great.syspromocity;

import java.io.File;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import br.ufc.great.syspromocity.util.Constantes;

/**Classe principal do sistema que carrega os padrões e convenções do spring boot
 * @author armandosoaressousa
 *
 */
@SpringBootApplication
public class SystemApplication {

	public static void main(String[] args) {
		String couponsPath = new Constantes().filePathQRCode;
		String uploadsPath = new Constantes().uploadDirectory;
		
		new File(couponsPath).mkdir();
		new File(uploadsPath).mkdir();
		
		SpringApplication.run(SystemApplication.class, args);
	}
}
