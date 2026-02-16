package fr.kainovaii.obsidian.app.controllers;

import fr.kainovaii.obsidian.http.controller.BaseController;
import fr.kainovaii.obsidian.http.controller.annotations.Controller;
import fr.kainovaii.obsidian.routing.methods.GET;
import spark.Request;
import spark.Response;

import java.util.Map;

@Controller
public class DocController extends BaseController
{
    @GET(value = "/docs")
    private Object homepage(Request req, Response res)
    {
        return render("docs/home.html", Map.of());
    }
}