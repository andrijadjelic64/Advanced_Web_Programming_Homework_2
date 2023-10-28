package framework.example.controller;

import annotations.definitions.Autowired;
import annotations.definitions.Controller;
import annotations.definitions.GET;
import annotations.definitions.Path;
import framework.example.service.Service1;
import framework.response.JsonResponse;
import framework.response.Response;

@Controller
public class Controller2 {

    @Autowired(verbose = false)
    Service1 service1;

    @GET
    @Path("/route3")
    public Response generateResponse3(){
        return new JsonResponse("hello from route 1");    }
    @GET
    @Path("/route4")
    public Response generateResponse4(){
        return new JsonResponse("hello from route 2");
    }
}
