package tec.bd.repository.inmemdb;


import tec.bd.todo.Status;
import tec.bd.todo.TodoRecord;
import tec.bd.todo.repository.TodoRepository;

import java.util.Date;
import java.util.List;
import java.util.UUID;

public class TodoRepositoryListImpl implements TodoRepository {

    private List<TodoRecord> todoData;

    public TodoRepositoryListImpl(List<TodoRecord> todoData) {
        this.todoData = todoData;
    }

    @Override
    public List<TodoRecord> findAll() {
        return this.todoData;
    }

    @Override
    public List<TodoRecord> findAll(Status status) {
        return null;
    }

    @Override
    public TodoRecord findById(String id) {
        var todoRecord = this.todoData
                .stream()
                .filter(e -> e.getId().equals(id))
                .findFirst();

        return todoRecord.orElse(null);
    }

    @Override
    public TodoRecord save(TodoRecord todoRecord) {
        todoRecord.setId(UUID.randomUUID().toString());
        this.todoData.add(todoRecord);
        return todoRecord;
    }

    @Override
    public void remove(String id) {
        var todoRecord = this.findById(id);

        if (id.equals(null)){
            System.out.println("No existe");
        } else {
            this.todoData.remove(todoRecord);
        }

    }

    @Override
    public TodoRecord update(TodoRecord todoRecord) {
        return null;
    }

    @Override
    public List<TodoRecord> findByPatternInTitle(String textToSearch) {
        return null;
    }

    @Override
    public List<TodoRecord> findByBetweenStartDates(Date startDate, Date endDate) {
        return null;
    }
}