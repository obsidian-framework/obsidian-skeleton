package fr.kainovaii.core;

import fr.kainovaii.core.database.DB;
import fr.kainovaii.core.database.MigrationManager;
import fr.kainovaii.core.web.di.ComponentScanner;
import fr.kainovaii.core.web.di.Container;
import fr.kainovaii.core.web.WebServer;
import org.javalite.activejdbc.LazyList;

import java.util.logging.Logger;

public class Spark
{
    public final static Logger logger =  Logger.getLogger("Spark");;
    private static String webPort;

    public void connectDatabase()
    {
        System.out.println("Loading database");
        DB.initSQLite("Spark/data.db", logger);
    }

    public void loadMigrations()
    {
        MigrationManager migrations = new MigrationManager(DB.getInstance(), logger);
        migrations.discover("fr.kainovaii.spark.app.migrations");
        migrations.migrate();
        migrations.status();
    }

    public void loadContainer()
    {
        ComponentScanner.scanPackage("fr.kainovaii.spark.app");
        Container.printRegistered();
    }

    public static EnvLoader loadConfigAndEnv()
    {
        EnvLoader env = new EnvLoader();
        env.load();
        return env;
    }

    public void startWebServer() { new WebServer().start(); }

    public static int getWebPort() { return Integer.parseInt(Spark.loadConfigAndEnv().get("PORT_WEB")); }

    public void registerMotd()
    {
        EnvLoader env = new EnvLoader();

        env.load();
        final String RESET = "\u001B[0m";
        final String CYAN = "\u001B[36m";
        final String YELLOW = "\u001B[33m";
        final String GREEN = "\u001B[32m";
        final String MAGENTA = "\u001B[35m";

        System.out.println(CYAN + "+--------------------------------------+" + RESET);
        System.out.println(CYAN + "|          Spark 1.0            |" + RESET);
        System.out.println(CYAN + "+--------------------------------------+" + RESET);
        System.out.println(GREEN + "| Developpeur       : KainoVaii        |" + RESET);
        System.out.println(GREEN + "| Version           : 1.0              |" + RESET);
        System.out.println(GREEN + "| Environnement     : " + env.get("ENVIRONMENT") + "              |" + RESET);
        System.out.println(CYAN + "+--------------------------------------+" + RESET);
        System.out.println(CYAN + "|      Chargement des modules...       |" + RESET);
        System.out.println(CYAN + "+--------------------------------------+" + RESET);
        System.out.println();
    }

    public void initWebsite()
    {

    }
}
