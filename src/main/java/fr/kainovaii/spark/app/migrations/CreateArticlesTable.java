package fr.kainovaii.spark.app.migrations;

import fr.kainovaii.core.database.Migration;

public class CreateArticlesTable extends Migration
{
    @Override
    public void up()
    {
        createTable("articles", table -> {
            table.id();
            table.string("title").notNull();
            table.text("content").notNull();
            table.integer("status").defaultValue(String.valueOf(1));
            table.integer("user_id");
            table.timestamps();
        });
    }

    @Override
    public void down() {
        dropTable("articles");
    }
}