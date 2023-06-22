package ru.netology.servlet;
import ru.netology.controller.PostController;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import ru.netology.config.JavaConfig;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class MainServlet extends HttpServlet {
    private PostController controller;
    static private final String GET_METHOD = "GET";
    static private final String POST_METHOD = "POST";
    static private final String DELETE_METHOD = "DELETE";
    static private final String PATH_FOR_READING_AND_SAVE_POST = "/api/posts";
    static private final String PATH_FOR_REMOVE_POST = "/api/posts/\\d+";
    @Override
    public void init() {
        final var context = new AnnotationConfigApplicationContext(JavaConfig.class);
        controller = context.getBean(PostController.class);
    }

    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) {
        // если деплоились в root context, то достаточно этого
        try {
            final var path = req.getRequestURI();
            final var method = req.getMethod();
            // primitive routing
            if (method.equals(GET_METHOD) && path.equals(PATH_FOR_READING_AND_SAVE_POST)) {
                controller.all(resp);
                return;
            }
            if (method.equals(GET_METHOD) && path.matches(PATH_FOR_REMOVE_POST)) {
                // easy way
                controller.getById(getPostID(path), resp);
                return;
            }
            if (method.equals(POST_METHOD) && path.equals(PATH_FOR_READING_AND_SAVE_POST)) {
                controller.save(req.getReader(), resp);
                return;
            }
            if (method.equals(DELETE_METHOD) && path.matches(PATH_FOR_REMOVE_POST)) {
                // easy way
                controller.removeById(getPostID(path), resp);
                return;
            }
            resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
        } catch (Exception e) {
            e.printStackTrace();
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }
    }

    private long getPostID(String path) {
        return Long.parseLong(path.substring(path.lastIndexOf("/")+ 1));
    }
}

