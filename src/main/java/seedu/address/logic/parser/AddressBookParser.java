package seedu.address.logic.parser;

import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.Messages.MESSAGE_UNKNOWN_COMMAND;

import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import seedu.address.commons.core.LogsCenter;
import seedu.address.logic.commands.AddCommand;
import seedu.address.logic.commands.AddGuestCommand;
import seedu.address.logic.commands.AddGuestToTableCommand;
import seedu.address.logic.commands.AddTableCommand;
import seedu.address.logic.commands.ClearCommand;
import seedu.address.logic.commands.Command;
import seedu.address.logic.commands.CreateWeddingCommand;
import seedu.address.logic.commands.DeleteCommand;
import seedu.address.logic.commands.DeleteGuestFromTableCommand;
import seedu.address.logic.commands.DeleteTableCommand;
import seedu.address.logic.commands.DeleteWeddingCommand;
import seedu.address.logic.commands.EditCommand;
import seedu.address.logic.commands.EditGuestCommand;
import seedu.address.logic.commands.ExitCommand;
import seedu.address.logic.commands.FindCommand;
import seedu.address.logic.commands.FindTableCommand;
import seedu.address.logic.commands.GetAllTablesCommand;
import seedu.address.logic.commands.HelpCommand;
import seedu.address.logic.commands.ListCommand;
import seedu.address.logic.commands.DeleteGuestCommand;
import seedu.address.logic.commands.SeeRsvpListCommand;
import seedu.address.logic.commands.SetWeddingCommand;
import seedu.address.logic.commands.WeddingOverviewCommand;
import seedu.address.logic.parser.exceptions.ParseException;

/**
 * Parses user input.
 */
public class AddressBookParser {

    /**
     * Used for initial separation of command word and args.
     */
    private static final Pattern BASIC_COMMAND_FORMAT = Pattern.compile("(?<commandWord>\\S+)(?<arguments>.*)");
    private static final Logger logger = LogsCenter.getLogger(AddressBookParser.class);

    /**
     * Parses user input into command for execution.
     *
     * @param userInput full user input string
     * @return the command based on the user input
     * @throws ParseException if the user input does not conform the expected format
     */
    public Command parseCommand(String userInput) throws ParseException {
        final Matcher matcher = BASIC_COMMAND_FORMAT.matcher(userInput.trim());
        if (!matcher.matches()) {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT, HelpCommand.MESSAGE_USAGE));
        }

        final String commandWord = matcher.group("commandWord");
        final String arguments = matcher.group("arguments");

        // Note to developers: Change the log level in config.json to enable lower level (i.e., FINE, FINER and lower)
        // log messages such as the one below.
        // Lower level log messages are used sparingly to minimize noise in the code.
        logger.fine("Command word: " + commandWord + "; Arguments: " + arguments);

        switch (commandWord) {
        case WeddingOverviewCommand.COMMAND_WORD:
            if (!arguments.isEmpty()) {
                throw new ParseException(WeddingOverviewCommand.MESSAGE_USAGE); // ✅ Ensure no arguments
            }
            return new WeddingOverviewCommand();

        case SetWeddingCommand.COMMAND_WORD:
            return new SetWeddingCommand(arguments);

        case DeleteWeddingCommand.COMMAND_WORD:
            return new DeleteWeddingCommand();

        case AddGuestCommand.COMMAND_WORD:
            return new AddGuestCommandParser().parse(arguments);

        case AddGuestToTableCommand.COMMAND_WORD:
            return new AddGuestToTableCommandParser().parse(arguments);

        case DeleteGuestCommand.COMMAND_WORD:
            return new DeleteGuestCommandParser().parse(arguments);

        case DeleteGuestFromTableCommand.COMMAND_WORD:
            return new DeleteGuestFromTableCommandParser().parse(arguments);

        case CreateWeddingCommand.COMMAND_WORD:
            return new CreateWeddingCommandParser().parse(arguments);


        case SeeRsvpListCommand.COMMAND_WORD:
            return new SeeRsvpListCommandParser().parse(arguments);

        case AddCommand.COMMAND_WORD:
            return new AddCommandParser().parse(arguments);

        case EditCommand.COMMAND_WORD:
            return new EditCommandParser().parse(arguments);

        case DeleteCommand.COMMAND_WORD:
            return new DeleteCommandParser().parse(arguments);

        case ClearCommand.COMMAND_WORD:
            return new ClearCommand();

        case FindCommand.COMMAND_WORD:
            return new FindCommandParser().parse(arguments);

        case ListCommand.COMMAND_WORD:
            return new ListCommand();

        case ExitCommand.COMMAND_WORD:
            return new ExitCommand();

        case HelpCommand.COMMAND_WORD:
            return new HelpCommand();

        case AddTableCommand.COMMAND_WORD:
            return new AddTableCommandParser().parse(arguments);

        case DeleteTableCommand.COMMAND_WORD:
            return new DeleteTableCommandParser().parse(arguments);

        case GetAllTablesCommand.COMMAND_WORD:
            return new GetAllTablesCommandParser().parse(arguments);


        case FindTableCommand.COMMAND_WORD:
            return new FindTableCommandParser().parse(arguments);
        default:
            logger.finer("This user input caused a ParseException: " + userInput);
            throw new ParseException(MESSAGE_UNKNOWN_COMMAND);
        }
    }

}
