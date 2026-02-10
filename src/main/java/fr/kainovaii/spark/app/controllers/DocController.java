package fr.kainovaii.spark.app.controllers;

import fr.kainovaii.core.web.controller.BaseController;
import fr.kainovaii.core.web.controller.Controller;
import fr.kainovaii.core.web.route.methods.GET;
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