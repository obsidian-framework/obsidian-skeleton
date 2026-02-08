# Spark Skeleton ⚡

Spark Skeleton apporte les conventions de développement modernes (routing par annotations, migrations fluides, injection de dépendances) au framework Spark Java. Fini les routes déclarées manuellement et les migrations SQL brutes.

```java
@Controller
public class BlogController extends BaseController {
    
    @GET(value = "/blog", name = "blog_index")
    private Object index(ArticleRepository articleRepo) {
        List<Article> articles = DB.withConnection(() ->
            articleRepo.findPublished().stream().toList()
        );
        
        return render("blog/index.html", Map.of(
            "articles", articles
        ));
    }
}
```

## 🎯 Pourquoi ce projet ?

Spark Java est un excellent micro-framework, mais il manque de conventions modernes. Ce boilerplate comble le gap en ajoutant :

- **Des annotations de routing** pour ne plus déclarer tes routes manuellement
- **Un système de migrations** avec une API fluide inspirée de Laravel
- **Le pattern Repository** avec injection de dépendances automatique
- **Un ErrorHandler maison** pour des stack traces propres en dev
- **Un moteur de templates** (Pebble) intégré directement dans les controllers

## ✨ Features principales

| Feature | Description |
|---------|-------------|
| 🛣️ **Routing par annotations** | `@GET`, `@POST`, `@PUT`, `@DELETE`, `@PATCH` sur tes méthodes |
| 🗃️ **Migrations fluides** | `table.string("title").notNull()` au lieu de SQL brut |
| 💉 **Dependency Injection** | Injecte automatiquement tes `@Repository` dans les controllers |
| 📦 **ActiveRecord models** | ActiveJDBC avec getters/setters pour manipuler tes models proprement |
| 🎨 **Templating intégré** | `render("view.html", data)` directement dans tes controllers |
| 🐛 **Error Handler custom** | Stack traces détaillées en dev, pages clean en prod |

## 🚀 Quick Start

```bash
git clone https://github.com/kainovaii/spark-skeleton.git
cd spark-skeleton
./build.bat
```

→ L'app tourne sur `http://localhost:8888`

## 📦 Stack technique

- **Spark Java** - Micro-framework web
- **ActiveJDBC** - ORM léger avec pattern ActiveRecord
- **Pebble** - Moteur de templates moderne
- **Maven** - Build & dependency management

## 🔥 Exemples rapides

### Un controller avec injection

```java
@Controller
public class ArticleController extends BaseController {
    
    @GET(value = "/articles/:id", name = "articles.show")
    private Object show(Request req, Response res, ArticleRepository repo) {
        String id = req.params(":id");
        Article article = DB.withConnection(() -> repo.findById(id));
        
        return render("articles/show.html", Map.of("article", article));
    }
}
```

### Une migration fluide

```java
public class CreateArticlesTable extends Migration {
    @Override
    public void up() {
        createTable("articles", table -> {
            table.id();
            table.string("title").notNull();
            table.text("content");
            table.timestamps();
        });
    }
}
```

### Un repository simple

```java
@Repository
public class ArticleRepository {
    public LazyList<Article> findPublished() {
        return Article.where("status = ?", 1);
    }
}
```

## 📖 Documentation

La documentation complète est disponible à `http://localhost:8888/docs` une fois l'app lancée.

## 🤝 Contributing

Les pull requests sont les bienvenues. Pour des changements majeurs, ouvre d'abord une issue pour discuter de ce que tu veux changer.

## 📝 License

[MIT](LICENSE)

## 👨‍💻 Auteur

**KainoVaii** - [@kainovaii](https://github.com/kainovaii)

---

*Développé avec passion et ☕*