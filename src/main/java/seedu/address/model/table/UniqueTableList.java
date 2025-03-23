package seedu.address.model.table;

import static seedu.address.commons.util.CollectionUtil.requireAllNonNull;
import java.util.Iterator;
import java.util.NoSuchElementException;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import seedu.address.model.person.Person;
import seedu.address.model.person.RsvpList;
import seedu.address.model.person.exceptions.DuplicatePersonException;
import seedu.address.model.table.exceptions.TableNotFoundException;

/**
 * Represents a unique list of tables in a wedding.
 * <p>
 * This class ensures that tables remain unique within the list.
 * It provides methods for adding, deleting, searching, and managing guests within tables.
 * </p>
 * <p>
 * This list does not allow duplicate tables based on {@code Table#isSameTable(Table)}.
 * </p>
 */
public class UniqueTableList implements Iterable<Table> {

    private final ObservableList<Table> internalList = FXCollections.observableArrayList();
    private final ObservableList<Table> internalUnmodifiableList =
            FXCollections.unmodifiableObservableList(internalList);

    /**
     * Checks if a table with the same identity as {@code toCheck} exists in the list.
     *
     * @param toCheck The table to check.
     * @return {@code true} if the table exists, otherwise {@code false}.
     */
    public boolean contains(Table toCheck) {
        return internalList.stream().anyMatch(toCheck::isSameTable);
    }

    public Iterator<Table> iterator() {
        return internalList.iterator();
    }

    /**
     * Adds a new table to the list.
     * Ensures that the table does not already exist in the list.
     *
     * @param toAdd The table to add.
     * @throws IllegalArgumentException if a table with the same ID already exists.
     */
    public void addTable(Table toAdd) {
        if (contains(toAdd)) {
            throw new IllegalArgumentException("Table with ID " + toAdd.getTableId() + " already exists.");
        }
        internalList.add(toAdd);
    }

    /**
     * Deletes a table from the list by its ID.
     *
     * @param tableId The ID of the table to be deleted.
     * @throws TableNotFoundException if the table does not exist.
     */
    public void deleteTable(int tableId) {
        Table table = findTable(tableId);
        if (table != null) {
            internalList.remove(table);
            return;
        }
        throw new TableNotFoundException();
    }

    public void deleteTable(Table table) {
        internalList.remove(table);
    }

    /**
     * Finds a table in the list by its ID.
     *
     * @param tableId The ID of the table to find.
     * @return An {@code Optional} containing the table if found, otherwise an empty {@code Optional}.
     */
    public Table findTable(int tableId) {
        try {
            return internalList.stream().filter(table -> table.getTableId() == tableId)
                .findFirst().get();
        } catch (NoSuchElementException nsee) {
            return null;
        }
    }

    public Table findTable(Table table) {
        try {
            return internalList.stream().filter(t -> t.getTableId() == table.getTableId())
                .findFirst().get();
        } catch (NoSuchElementException nsee) {
            return null;
        }
    }

    /**
     * Checks whether a table with the given ID exists in the list.
     *
     * @param tableId The ID of the table to check.
     * @return {@code true} if the table exists, otherwise {@code false}.
     */
    public boolean hasTable(int tableId) {
        return internalList.stream().anyMatch(table -> table.getTableId() == tableId);
    }

    /**
     * Assigns a guest to a table based on their name.
     * <p>
     * The method ensures that the table exists and that the guest is present in the RSVP list.
     * If the table is full, an exception is thrown.
     * </p>
     *
     * @param tableId  The ID of the table.
     * @param guest The new guest to be added in this table.
     * @throws TableNotFoundException if the table does not exist.
     * @throws IllegalArgumentException if the guest is not found or the table is full.
     */
    public void assignGuestToTable(int tableId, Person guest) {
        Table table = findTable(tableId);
        if (table == null) {
            throw new TableNotFoundException();
        }

        RsvpList newList = new RsvpList();

        for (Person g : table.getGuests()) {
            newList.add(g);
        }

        newList.add(guest);

        if (newList.size() >= table.getCapacity()) {
            throw new IllegalArgumentException("Table is full.");
        }

        Table updatedTable = new Table(table.getTableId(), table.getCapacity(), newList);
        internalList.set(internalList.indexOf(table), updatedTable);
    }

    public void assignGuestToTable(Table table, Person guest) {
        assignGuestToTable(table.getTableId(), guest);
    }

    /**
     * Removes a guest from a table.
     * <p>
     * The method finds the specified table, removes the guest, and updates the table list.
     * </p>
     *
     * @param tableId  The ID of the table.
     * @param guest  The guest to be added.
     * @throws TableNotFoundException if the table does not exist.
     */
    public void deleteGuestFromTable(int tableId, Person guest) {
        Table table = findTable(tableId);
        if (table == null) {
            throw new TableNotFoundException();
        }

        RsvpList rsvpList = new RsvpList();

        table.getGuests().stream()
                .filter(g -> !g.equals(guest))
                .forEach(g -> rsvpList.add(g));

        Table updatedTable = new Table(table.getTableId(), table.getCapacity(), rsvpList);
        internalList.set(internalList.indexOf(table), updatedTable);
    }

    public void setTable(Table target, Table editedTable) {
        requireAllNonNull(target, editedTable);

        int index = internalList.indexOf(target);
        if (index == -1) {
            throw new TableNotFoundException();
        }

        if (!target.isSameTable(editedTable) && contains(editedTable)) {
            throw new DuplicatePersonException();
        }

        internalList.set(index, editedTable);
    }
    /**
     * Returns the list of tables as an unmodifiable {@code ObservableList}.
     * This ensures that the list cannot be modified externally.
     *
     * @return An unmodifiable list of tables.
     */
    public ObservableList<Table> asUnmodifiableObservableList() {
        return internalUnmodifiableList;
    }

    public int size() {
        return internalList.size();
    }
}
