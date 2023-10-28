package framework_mechanism;

import annotations.definitions.*;
import framework.response.Response;
import org.reflections.Reflections;
import org.reflections.scanners.SubTypesScanner;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;
import java.util.stream.Collectors;

public class DIEngine {

    private static DIEngine instance;

    private final List<DependencyContainer> qualifiers = new ArrayList<>();

    private HashMap<Class, Object> mapOfSingletons = new HashMap<>();

    private Set<Class> classes;

    private List<Object> controllers;
    private List<Object> singletonBeans;
    private List<Object> services;
    private HashMap<Route, HttpRoute> httpRoutes = new HashMap<>();

    private String  packagePath= "framework.example";
    private DIEngine(){
        System.out.println("Discovery constructor");

        classes = loadAllClasses();
        controllers = initializeControllers();
        services = initializeServices();
        initializeSingletonBeans();
        System.out.println("All classes: ");
        System.out.println(classes);
        System.out.println("==================");;
        System.out.println("Controllers: ");
        System.out.println(controllers);
        System.out.println("==================");;
        System.out.println("Services: ");
        System.out.println(controllers);


    }

    public static DIEngine getInstance() {
        if (instance == null) {
            instance = new DIEngine();
        }
        return instance;
    }


    private Set<Class> loadAllClasses(){
        Reflections reflections = new Reflections(packagePath, new SubTypesScanner(false));
        return reflections.getSubTypesOf(Object.class).stream().collect(Collectors.toSet());
    }


    private List<Object> initializeServices(){
        List<Object> services = new ArrayList<>();
        for (Class singleClass: classes){
            if(singleClass.isAnnotationPresent(Service.class)){
                try {
                    Object obj = singleClass.getDeclaredConstructor().newInstance();
                    services.add(obj);
                    mapOfSingletons.put(singleClass,obj);
                } catch (InstantiationException e) {
                    throw new RuntimeException(e);
                } catch (IllegalAccessException e) {
                    throw new RuntimeException(e);
                } catch (InvocationTargetException e) {
                    throw new RuntimeException(e);
                } catch (NoSuchMethodException e) {
                    throw new RuntimeException(e);
                }
            }
        }
        return services;
    }



    private void autowire(Object parentObject) {
        try {
        Field[] fields = parentObject.getClass().getDeclaredFields();
        for (Field field : fields) {
            if (field.isAnnotationPresent(Autowired.class)) {
                field.setAccessible(true);
                Object fieldObject = mapOfSingletons.get(field.getClass());
                if(fieldObject == null){
                    fieldObject =field.getClass().getDeclaredConstructor().newInstance();
                }

                field.set(parentObject,fieldObject);
                field.setAccessible(false);
                autowire(fieldObject.getClass());
            }
        }
        } catch (InstantiationException e) {
            throw new RuntimeException(e);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        } catch (InvocationTargetException e) {
            throw new RuntimeException(e);
        } catch (NoSuchMethodException e) {
            throw new RuntimeException(e);
        }
    }


//return true if found
    private boolean isInFieldInDependencyContainer(String qualifierName,Class qualifier) {
        return false;
    }

    private void initializeSingletonBeans(){
        List<Object> beans = new ArrayList<>();
        try {
        for (Class singleClass: classes){
            if(singleClass.isAnnotationPresent(Bean.class)){
                Bean bean = (Bean) singleClass.getAnnotation(Bean.class);
                if(bean.scope().equals("singleton")){
                    mapOfSingletons.put(singleClass,singleClass.getDeclaredConstructor().newInstance());

                }
            }

        }
        } catch (InstantiationException e) {
            throw new RuntimeException(e);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        } catch (InvocationTargetException e) {
            throw new RuntimeException(e);
        } catch (NoSuchMethodException e) {
            throw new RuntimeException(e);
        }

    }

    private List<Object> initializeControllers(){
        List<Object> controllers = new ArrayList<>();

        for (Class singleClass: classes){
            if(singleClass.isAnnotationPresent(Controller.class)){

                try {
                    Object obj = singleClass.getDeclaredConstructor().newInstance();
                    controllers.add(obj);
                    for (Method method : singleClass.getDeclaredMethods()) {
                        if ((method.isAnnotationPresent(GET.class)||method.isAnnotationPresent(POST.class))&&method.isAnnotationPresent(Path.class)){

                            Path path = method.getAnnotation(Path.class);
                            if(method.isAnnotationPresent(GET.class)){
                                HttpRoute route = new HttpRoute(path.value(),"GET",singleClass,method);
                                httpRoutes.put(new Route("GET",path.value()),route);
                                mapOfSingletons.put(singleClass,obj);

                            }else{
                                HttpRoute route =new HttpRoute(path.value(),"POST",singleClass,method);
                                httpRoutes.put(new Route("POST",path.value()),route);
                                mapOfSingletons.put(singleClass,obj);
                                
                            }
                        }
                        else{
                            throw new RuntimeException(singleClass.getName()+"."+method.getName()+" is not annotated correctly.");
                        }

                    }

                } catch (InstantiationException e) {
                    throw new RuntimeException(e);
                } catch (IllegalAccessException e) {
                    throw new RuntimeException(e);
                } catch (InvocationTargetException e) {
                    throw new RuntimeException(e);
                } catch (NoSuchMethodException e) {
                    throw new RuntimeException(e);
                }
            }

        }

        for (HttpRoute value : httpRoutes.values()) {
            System.out.println(value);
        }
        return controllers;
    }


    public Response generateResponse(Route route){
        HttpRoute httpRoute = httpRoutes.get(route);
        if(httpRoute==null){
            return null;
        }
        System.out.println("DIEngine.generateResponse[method: " + httpRoute.method +" , route: " + httpRoute.route + "]" );
        Object obj;
        for (Object singleController:controllers) {
            if (httpRoute.controller.equals(singleController.getClass())){
                obj = singleController;
                try {
                    Response response = (Response) httpRoute.methodObject.invoke(obj);
                    return response;
                } catch (IllegalAccessException e) {
                    throw new RuntimeException(e);
                } catch (InvocationTargetException e) {
                    throw new RuntimeException(e);
                }
            }
        }

        return null;
    }
}
