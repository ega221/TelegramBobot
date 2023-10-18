package ru.alekseenko.service.enums;

public enum ServiceCommand {
    HELP("/help"),
    REGISTRATION("/registration"),
    CANCEL("/cancel"),
    START("/start");

    private final String value;

    ServiceCommand(String cmd) {
        this.value = cmd;
    }

    @Override
    public String toString() {
        return value;
    }

    public static ServiceCommand fromValue(String str) {
        for (ServiceCommand command : ServiceCommand.values()) {
            if (command.value.equals(str)) {
                return command;
            }
        }
        return null;
    }
}
