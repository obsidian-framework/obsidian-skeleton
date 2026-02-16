package fr.kainovaii.obsidian.app.controllers;

import fr.kainovaii.obsidian.http.controller.BaseController;
import fr.kainovaii.obsidian.http.controller.annotations.GlobalAdvice;
import spark.Request;
import spark.Response;

@GlobalAdvice
public class GlobalAdviceController extends BaseController
{
    public static void applyGlobals(Request req, Response res)
    {

    }
}