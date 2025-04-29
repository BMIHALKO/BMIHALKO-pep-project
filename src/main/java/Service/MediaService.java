package Service;
/** You will need to design and create your own Service classes from scratch.
You should refer to prior mini-project lab examples and course material for guidance.
**/
import Model.Message;
import DAO.MediaDAO;
import DAO.AccountDAO;
import Model.Account;

import java.util.List;

public class MediaService {
    MediaDAO mediaDao;
    AccountDAO accountDAO;

    public MediaService() {
        mediaDao = new MediaDAO();
        accountDAO = new AccountDAO();
    }

    // Create new Message
    public Message createMessage(Message message) {
        // Make sure text is not null, not blank, and is <= 255 characters
        if (message.getMessage_text() == null ||
            message.getMessage_text().isBlank() ||
            message.getMessage_text().length() > 255) {
            return null;
        }

        // Make sure posted_by exists
        Account account = accountDAO.getAccountById(message.getPosted_by());
        if (account == null) {
            throw new IllegalArgumentException("");
        }

        return mediaDao.createMessage(message);
    }

    // Get all messages
    public List<Message> getAllMessages() {
        return mediaDao.getAllMessages();
    }

    // Get message by message_id
    public Message getMessageById(int message_id) {
        Message message = mediaDao.getMessageById(message_id);
        if (message == null) {
            throw new IllegalArgumentException("");
        }
        return message;
    }

    // Update an existing message
    public boolean updateMessageById(int message_id, String message_text) {
        Message existingmessage = mediaDao.getMessageById(message_id);
        if (existingmessage == null) {
            throw new IllegalArgumentException("");
        }
        return mediaDao.updateMessageById(message_id, message_text);
    }

    // Delete message by ID
    public boolean deleteMessageById(int message_id) {
        Message message = mediaDao.getMessageById(message_id);
        if (message == null) {
            throw new IllegalArgumentException("");
        }
        return mediaDao.deleteMessageById(message_id);
    }

    // Get all messages by ID
    public List<Message> getMessagesByAccountId(int account_id) {
        Account account = accountDAO.getAccountById(account_id);
        if (account == null) {
            throw new IllegalArgumentException("");
        }

        return mediaDao.getMessagesByAccountId(account_id);
    }
}