package launcher;

import db.Database;
import model.Permission;
import model.User;
import model.UserPermission;

public record SuperUserCreator() {
    public static void main(String[] args) {
        if (args.length < 2) {
            System.err.println("Must provide user name and password");
            return;
        }

        User user = new User(args[0], args[1]);
        Database db = new Database("toonboard.db");

        db.users.insert(user);
        db.userPermissions.insert(new UserPermission(user.name(), Permission.SUPER_USER));
    }
}
