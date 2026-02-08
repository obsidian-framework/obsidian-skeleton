package fr.kainovaii.core.database;

import org.javalite.activejdbc.Base;
import org.reflections.Reflections;
import org.reflections.scanners.Scanners;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Set;
import java.util.logging.Logger;

public class MigrationManager
{
    private final DB database;
    private final Logger logger;
    private final List<Migration> migrations;
    private final String dbType;

    public MigrationManager(DB database, Logger logger) {
        this.database = database;
        this.logger = logger;
        this.migrations = new ArrayList<>();
        this.dbType = database.getType();
    }

    public MigrationManager add(Migration migration) {
        migration.type = this.dbType;
        migration.logger = this.logger;
        migrations.add(migration);
        return this;
    }

    public MigrationManager discover(String packageName)
    {
        try {
            Reflections reflections = new Reflections(packageName, Scanners.SubTypes);
            Set<Class<? extends Migration>> migrationClasses = reflections.getSubTypesOf(Migration.class);

            List<Migration> discoveredMigrations = new ArrayList<>();

            for (Class<? extends Migration> migrationClass : migrationClasses) {
                try {
                    Migration migration = migrationClass.getDeclaredConstructor().newInstance();
                    migration.type = this.dbType;
                    migration.logger = this.logger;
                    discoveredMigrations.add(migration);
                } catch (Exception e) {
                    logger.warning("Impossible d'instancier la migration: " + migrationClass.getName() + " - " + e.getMessage());
                }
            }

            discoveredMigrations.sort(Comparator.comparing(m -> m.getClass().getSimpleName()));
            migrations.addAll(discoveredMigrations);

            logger.info(discoveredMigrations.size() + " migration(s) découverte(s) dans " + packageName);

        } catch (Exception e) {
            logger.severe("Erreur lors de la découverte des migrations: " + e.getMessage());
        }

        return this;
    }

    public void migrate() {
        database.executeWithTransaction(() -> {
            createMigrationsTable();

            for (int i = 0; i < migrations.size(); i++) {
                String migrationName = "migration_" + (i + 1);

                if (!isMigrationExecuted(migrationName)) {
                    logger.info("Executing migration: " + migrationName);
                    migrations.get(i).up();
                    recordMigration(migrationName);
                    logger.info("✓ Migration completed: " + migrationName);
                } else {
                    logger.fine("Migration already executed: " + migrationName);
                }
            }

            logger.info("All migrations are up to date");
            return null;
        });
    }

    public void rollback() {
        database.executeWithTransaction(() -> {
            for (int i = migrations.size() - 1; i >= 0; i--) {
                String migrationName = "migration_" + (i + 1);

                if (isMigrationExecuted(migrationName)) {
                    logger.info("Rolling back migration: " + migrationName);
                    migrations.get(i).down();
                    removeMigration(migrationName);
                    logger.info("✓ Migration rolled back: " + migrationName);
                }
            }

            logger.info("All migrations have been rolled back");
            return null;
        });
    }

    public void rollbackLast() {
        database.executeWithTransaction(() -> {
            for (int i = migrations.size() - 1; i >= 0; i--) {
                String migrationName = "migration_" + (i + 1);

                if (isMigrationExecuted(migrationName)) {
                    logger.info("Rolling back last migration: " + migrationName);
                    migrations.get(i).down();
                    removeMigration(migrationName);
                    logger.info("✓ Last migration rolled back");
                    break;
                }
            }
            return null;
        });
    }

    public void fresh() {
        rollback();
        migrate();
    }

    public void status() {
        database.executeWithConnection(() -> {
            System.out.println("\n=== Migration Status ===");
            for (int i = 0; i < migrations.size(); i++) {
                String migrationName = "migration_" + (i + 1);
                String status = isMigrationExecuted(migrationName) ? "✓ Executed" : "✗ Pending";
                System.out.println(migrationName + " - " + status);
            }
            System.out.println("========================\n");
            return null;
        });
    }


    private void createMigrationsTable() {
        String idColumn = switch (dbType) {
            case "mysql" -> "INT AUTO_INCREMENT PRIMARY KEY";
            case "postgresql" -> "SERIAL PRIMARY KEY";
            default -> "INTEGER PRIMARY KEY AUTOINCREMENT";
        };

        Base.exec(String.format("""
            CREATE TABLE IF NOT EXISTS migrations (
                id %s,
                migration VARCHAR(255) NOT NULL,
                executed_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
            )
            """, idColumn));
    }

    private boolean isMigrationExecuted(String migrationName) {
        Object result = Base.firstCell("SELECT COUNT(*) FROM migrations WHERE migration = ?", migrationName);
        if (result == null) return false;

        long count = result instanceof Long ? (Long) result : Long.parseLong(result.toString());
        return count > 0;
    }

    private void recordMigration(String migrationName) {
        Base.exec("INSERT INTO migrations (migration) VALUES (?)", migrationName);
    }

    private void removeMigration(String migrationName) {
        Base.exec("DELETE FROM migrations WHERE migration = ?", migrationName);
    }
}