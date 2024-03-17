/**
 * by Jakub Wawak
 * kubawawak@gmail.com
 * all rights reserved
 */
package com.jakubwawak.mochi;

import com.jakubwawak.mochi.backend.database.Database_Connector;
import com.jakubwawak.mochi.enitity.Mochi;
import com.jakubwawak.mochi.enitity.Vault;
import com.jakubwawak.mochi.maintanance.ConsoleColors;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.page.AppShellConfigurator;
import com.vaadin.flow.spring.annotation.EnableVaadin;
import com.vaadin.flow.theme.Theme;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Application for note-taking using markdown
 */
@SpringBootApplication
@EnableVaadin({"com.jakubwawak.mochi"})
@Theme(value="general_theme")
public class MochiApplication implements AppShellConfigurator {

	public static String version = "v1.0.0";
	public static String build = "mochi150323REV1";

	public static String databaseURL = "mongodb://localhost:27017";


	public static Database_Connector database;
	public static Mochi mochiConfiguration;

	public static Vault currentVault;


	public static int log_database_dump_flag = 1;

	/**
	 * Main java application
	 * @param args
	 */
	public static void main(String[] args) {
		showHeader();
		database = new Database_Connector();
		database.setDatabase_url(databaseURL);
		database.connect();
		if ( database.connected ){
			mochiConfiguration = database.getConfiguration();
			if ( mochiConfiguration != null ){
				database.log("CONFIGURATION","Configuration loaded from "+mochiConfiguration.database_lastupdate);
				SpringApplication.run(MochiApplication.class, args);
			}
			else{
				System.out.println("Configuration is null, database integrity lost!");
			}
		}
		else{
			System.out.println("Failed to connect to mongodb instance, check connection string!");
		}
	}

	/**
	 * Function for showing header
	 */
	public static void showHeader(){
		String query = "                      _     _ \n" +
				" _ __ ___   ___   ___| |__ (_)\n" +
				"| '_ ` _ \\ / _ \\ / __| '_ \\| |\n" +
				"| | | | | | (_) | (__| | | | |\n" +
				"|_| |_| |_|\\___/ \\___|_| |_|_|\n";
		query = query + "\n"+build + "\\" +  version;
		System.out.println(ConsoleColors.RED+query+ConsoleColors.RESET);
	}

	/**
	 * Function for showing notification service data
	 * @param notificationString
	 * @param notificationState
	 */
	public static void notificationService(String notificationString, int notificationState){
		try{
			Notification noti = Notification.show(notificationString);
			switch(notificationState){
				case 1:
					noti.addThemeVariants(NotificationVariant.LUMO_SUCCESS);
					break;
				case 2:
					noti.addThemeVariants(NotificationVariant.LUMO_PRIMARY);
					break;
				case 3:
					noti.addThemeVariants(NotificationVariant.LUMO_WARNING);
					break;
				case 4:
					noti.addThemeVariants(NotificationVariant.LUMO_ERROR);
					break;
			}
		}catch(Exception ex){}
	}

}
