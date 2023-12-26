import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class TextEditor {

    public static void main(String[] args) {
        String inputTextFile = "TEXT.txt";
        String commandsFile = "Command.txt";
        String outputTextFile = "TEXTOUT.txt";

        try {
            List<String> textLines = readTextFile(inputTextFile);
            List<String> commands = readCommands(commandsFile);

            List<StringBuilder> text = new ArrayList<>();
            for (String line : textLines) {
                text.add(new StringBuilder(line));
            }

            int currentLine = 0;
            int currentPos = 0;
            boolean insertMode = false;

            for (String command : commands) {
                String[] tokens = command.split("\\s+");
                String action = tokens[0];

                switch (action) {
                    case "goto":
                        currentLine = Integer.parseInt(tokens[1]) - 1;
                        currentPos = Integer.parseInt(tokens[2]) - 1;
                        break;
                    case "right":
                        if (currentPos < text.get(currentLine).length() - 1) {
                            currentPos++;
                        }
                        break;
                    case "left":
                        if (currentPos > 0) {
                            currentPos--;
                        }
                        break;
                    case "up":
                        if (currentLine > 0) {
                            currentLine--;
                        }
                        break;
                    case "down":
                        if (currentLine < text.size() - 1) {
                            currentLine++;
                        }
                        break;
                    case "ctrl":
                        handleCtrlCommand(tokens[1], text, currentLine, currentPos);
                        break;
                    case "backspace":
                        if (currentPos > 0) {
                            text.get(currentLine).deleteCharAt(currentPos - 1);
                            currentPos--;
                        }
                        break;
                    case "del":
                        if (currentPos < text.get(currentLine).length()) {
                            text.get(currentLine).deleteCharAt(currentPos);
                        }
                        break;
                    case "ins":
                        insertMode = !insertMode;
                        break;
                    case "enter":
                        text.add(currentLine + 1, new StringBuilder());
                        currentLine++;
                        currentPos = 0;
                        break;
                    default:
                        if (insertMode) {
                            text.get(currentLine).insert(currentPos, action);
                            currentPos++;
                        } else {
                            if (currentPos < text.get(currentLine).length()) {
                                text.get(currentLine).setCharAt(currentPos, action.charAt(0));
                                currentPos++;
                            }
                        }
                        break;
                }
            }

            writeTextToFile(outputTextFile, text);
            System.out.println("Successfully processed commands. Output written to " + outputTextFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static List<String> readTextFile(String fileName) throws IOException {
        List<String> lines = new ArrayList<>();
        BufferedReader reader = new BufferedReader(new FileReader(fileName));
        String line;
        while ((line = reader.readLine()) != null) {
            lines.add(line);
        }
        reader.close();
        return lines;
    }

    private static List<String> readCommands(String fileName) throws IOException {
        List<String> commands = new ArrayList<>();
        BufferedReader reader = new BufferedReader(new FileReader(fileName));
        String command;
        while ((command = reader.readLine()) != null) {
            commands.add(command.trim());
        }
        reader.close();
        return commands;
    }

    private static void handleCtrlCommand(String ctrlCommand, List<StringBuilder> text, int currentLine, int currentPos) {
        switch (ctrlCommand) {
            case "left":
                while (currentPos > 0 && !Character.isLetterOrDigit(text.get(currentLine).charAt(currentPos - 1))) {
                    currentPos--;
                }
                while (currentPos > 0 && Character.isLetterOrDigit(text.get(currentLine).charAt(currentPos - 1))) {
                    currentPos--;
                }
                break;
            case "right":
                while (currentPos < text.get(currentLine).length() && !Character.isLetterOrDigit(text.get(currentLine).charAt(currentPos))) {
                    currentPos++;
                }
                while (currentPos < text.get(currentLine).length() && Character.isLetterOrDigit(text.get(currentLine).charAt(currentPos))) {
                    currentPos++;
                }
                break;
            case "up":
                if (currentLine > 0) {
                    currentLine--;
                    currentPos = 0;
                }
                break;
            case "down":
                if (currentLine < text.size() - 1) {
                    currentLine++;
                    currentPos = 0;
                }
                break;
            case "backspace":
                while (currentPos > 0 && !Character.isLetterOrDigit(text.get(currentLine).charAt(currentPos - 1))) {
                    text.get(currentLine).deleteCharAt(currentPos - 1);
                    currentPos--;
                }
                while (currentPos > 0 && Character.isLetterOrDigit(text.get(currentLine).charAt(currentPos - 1))) {
                    text.get(currentLine).deleteCharAt(currentPos - 1);
                    currentPos--;
                }
                break;
            case "del":
                while (currentPos < text.get(currentLine).length() && !Character.isLetterOrDigit(text.get(currentLine).charAt(currentPos))) {
                    text.get(currentLine).deleteCharAt(currentPos);
                }
                while (currentPos < text.get(currentLine).length() && Character.isLetterOrDigit(text.get(currentLine).charAt(currentPos))) {
                    text.get(currentLine).deleteCharAt(currentPos);
                }
                break;
            default:
                break;
        }
    }

    private static void writeTextToFile(String fileName, List<StringBuilder> text) throws IOException {
        BufferedWriter writer = new BufferedWriter(new FileWriter(fileName));
        for (StringBuilder line : text) {
            writer.write(line.toString());
            writer.newLine();
        }
        writer.close();
    }
}
