package Controller;

import Model.Account;
import Model.Message;
import Service.AccountService;
import Service.MediaService;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import io.javalin.Javalin;
import io.javalin.http.Context;

/**
 * TODO: You will need to write your own endpoints and handlers for your controller. The endpoints you will need can be
 * found in readme.md as well as the test cases. You should
 * refer to prior mini-project labs and lecture materials for guidance on how a controller may be built.
 */
public class SocialMediaController {
    private AccountService accountService = new AccountService();
    private MediaService mediaService = new MediaService();
    /**
     * In order for the test cases to work, you will need to write the endpoints in the startAPI() method, as the test
     * suite must receive a Javalin object from this method.
     * @return a Javalin app object which defines the behavior of the Javalin controller.
     */
    public Javalin startAPI() {
        Javalin app = Javalin.create();
        app.post("/register", this::registerHandler);
        app.post("/login", this::loginHandler);

        app.post("/messages", this::createMessagesHandler);
        app.get("/messages", this::getAllMessagesHandler);
        app.get("/messages/{message_id}", this::getMessageByIdHandler);
        app.delete("/messages/{message_id}", this::deleteMessageByIdHandler);
        app.patch("/messages/{message_id}", this::updateMessageByIdHandler);

        app.get("/accounts/{account_id}/messages", this::getMessageByAccountIdHandler);

        return app;
    }

    /**
     * This is an example handler for an example endpoint.
     * @param context The Javalin Context object manages information about both the HTTP request and response.
     */
    private void registerHandler (Context ctx) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        Account newAccount = mapper.readValue(ctx.body(), Account.class);

        try {
            Account createdAccount = accountService.registerAccount(newAccount.getUsername(), newAccount.getPassword());
            ctx.json(createdAccount);
            ctx.status(200);
        } catch (IllegalArgumentException e) {
            ctx.status(400).result(e.getMessage());
        }
    }

    private void loginHandler (Context ctx) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        Account loginRequest = mapper.readValue(ctx.body(), Account.class);

        try {
            Account loggedInAccount = accountService.login(loginRequest.getUsername(), loginRequest.getPassword());
            ctx.json(loggedInAccount);
            ctx.status(200);
        } catch (IllegalArgumentException e) {
            ctx.status(401).result(e.getMessage());
        }
    }

    private void createMessagesHandler(Context ctx) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        Message newMessage = mapper.readValue(ctx.body(), Message.class);

        try {
            Message createdMessage = mediaService.createMessage(newMessage);
            ctx.json(createdMessage);
            ctx.status(200);
        } catch (IllegalArgumentException e) {
            ctx.status(400).result(e.getMessage());
        } catch (Exception e) {
            ctx.result("");
            ctx.status(400);
        }
    }

    private void getAllMessagesHandler (Context ctx) throws JsonProcessingException {
        List<Message> messages = mediaService.getAllMessages();
        ctx.json(messages);
        ctx.status(200);
    }

    private void getMessageByIdHandler (Context ctx) throws JsonProcessingException {
        try {
            int message_id = Integer.parseInt(ctx.pathParam("message_id"));
            Message message = mediaService.getMessageById(message_id);
            
            if (message != null) {
                ctx.json(message);
                ctx.status(200);
            } else {
                ctx.result("");
            }
        } catch (NumberFormatException e) {
            ctx.result("");
            ctx.status(200);
        } catch (Exception e) {
            ctx.result("");
            ctx.status(200);
        }
    }

    private void deleteMessageByIdHandler (Context ctx) throws JsonProcessingException {
        try {
            int message_id = Integer.parseInt(ctx.pathParam("message_id"));
            Message message = mediaService.getMessageById(message_id);

            if (message != null) {
                mediaService.deleteMessageById(message_id);
                ctx.json(message);
            } else {
                ctx.result("");
            }
        } catch (NumberFormatException e) {
            ctx.result("");
            ctx.status(200);
        } catch (Exception e) {
            ctx.result("");
            ctx.status(200);
        }
    }

    private void updateMessageByIdHandler(Context ctx) throws JsonProcessingException {
        try {
            int message_id = Integer.parseInt(ctx.pathParam("message_id"));
            ObjectMapper mapper = new ObjectMapper();
            Message newMessage = mapper.readValue(ctx.body(), Message.class);
            String newText = newMessage.getMessage_text();
            Message existinMessage = mediaService.getMessageById(message_id);

            if (existinMessage == null) {
                ctx.status(400).result("");
                return;
            }
            if (newText == null || newText.isBlank() || newText.length() > 255) {
                ctx.status(400).result("");
                return;
            }
    
            boolean updated = mediaService.updateMessageById(message_id, newText);
            
            if (updated) {
                Message updatedMessage = mediaService.getMessageById(message_id);
                ctx.json(updatedMessage);
                ctx.status(200);
            } else {
                ctx.status(400).result("");
            }
        } catch (NumberFormatException e) {
            ctx.status(400).result("");
        } catch (Exception e) {
            ctx.status(400).result("");
        }
    }

    private void getMessageByAccountIdHandler (Context ctx) throws JsonProcessingException {
        try {
            int account_id = Integer.parseInt(ctx.pathParam("account_id"));
            List<Message> messages = mediaService.getMessagesByAccountId(account_id);

            if (messages == null) {
                messages = new ArrayList<>();
            }

            ctx.json(messages);
            ctx.status(200);
        } catch (NumberFormatException e) {
            ctx.result("");
            ctx.status(200);
        } catch (Exception e) {
            ctx.json(new ArrayList<>());
            ctx.status(200);
        }
    }
}