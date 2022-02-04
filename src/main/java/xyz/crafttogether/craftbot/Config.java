package xyz.crafttogether.craftbot;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.util.ArrayList;
import java.util.List;

public class Config {
    private final String token;
    private final String guild;
    private final String systemCommandsRole;
    private final ButtonRoles buttonRoles;
    private final String vcHub;

    public Config(JsonObject json) {
        this.token = json.get("token").getAsString();
        this.guild = json.get("guild").getAsString();
        this.systemCommandsRole = json.get("system-commands-role").getAsString();
        this.buttonRoles = new ButtonRoles(json.getAsJsonObject("button-roles"));
        this.vcHub = json.get("vc-hub").getAsString();
    }

    public String getToken() {
        return token;
    }

    public String getGuild() {
        return guild;
    }

    public String getSystemCommandsRole() {
        return systemCommandsRole;
    }

    public ButtonRoles getButtonRoles() {
        return buttonRoles;
    }

    public String getVcHub() {
        return vcHub;
    }

    public class ButtonRoles {
        private final String channel;
        private final String message;
        private final List<Role> roles;

        public ButtonRoles(JsonObject json) {
            this.channel = json.get("channel").getAsString();
            this.message = json.get("message").getAsString();
            final List<Role> list = new ArrayList<>();
            for (int i = 0; i < json.getAsJsonArray("roles").size(); i++) {
                final JsonObject role = json.getAsJsonArray("roles").get(i).getAsJsonObject();
                list.add(new Role(role));
            }
            this.roles = list;
        }

        public String getChannel() {
            return channel;
        }

        public String getMessage() {
            return message;
        }

        public List<Role> getRoles() {
            return roles;
        }

        public class Role {
            private final String id;
            private final String label;
            private final String role;

            public Role(JsonObject json) {
                this.id = json.get("id").getAsString();
                this.label = json.get("label").getAsString();
                this.role = json.get("role").getAsString();
            }

            public String getId() {
                return id;
            }

            public String getLabel() {
                return label;
            }

            public String getRole() {
                return role;
            }
        }

    }

}