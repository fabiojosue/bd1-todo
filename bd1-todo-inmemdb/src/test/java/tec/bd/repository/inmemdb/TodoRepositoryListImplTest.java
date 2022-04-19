package tec.bd.repository.inmemdb;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestTemplate;
import tec.bd.repository.inmemdb.TodoRepositoryListImpl;
import tec.bd.todo.Status;
import tec.bd.todo.TodoRecord;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import static org.mockito.BDDMockito.*;
import static org.assertj.core.api.Assertions.*;

public class TodoRepositoryListImplTest {

    TodoRepositoryListImpl repository;

    @BeforeEach
    public void setup() {
        var simpleTodoRecord = mock(TodoRecord.class);

        given(simpleTodoRecord.getId()).willReturn("simple-todo");

        List<TodoRecord> todoRecordList = new ArrayList<>();
        todoRecordList.add(simpleTodoRecord);
        this.repository = new TodoRepositoryListImpl(todoRecordList);
    }

    @Test
    public void findAll() throws Exception {

        var actual = repository.findAll();

        assertThat(actual).isNotEmpty();
    }

    @Test
    public void findById() throws Exception {
        var actual = repository.findById("simple-todo");

        assertThat(actual).isNotNull();
        assertThat(actual.getId()).isEqualTo("simple-todo");
    }

    @Test
    public void whenIdNotFoundThenNull() throws Exception {

        var actual = repository.findById("no-exists");

        //assertThat(actual).isNotNull();

    }

    @Test
    public void save() throws Exception {

        var todoRecord = new TodoRecord("Desayuno");

        var previousSize = repository.findAll().size();
        var newTodoRecord = repository.save(todoRecord);
        var actual = repository.findAll();

        assertThat(newTodoRecord).isNotNull();
        assertThat(newTodoRecord.getId()).isNotBlank();
        assertThat(actual.size()).isGreaterThan(previousSize);
        assertThat(actual).contains(todoRecord);


    }

    @Test
    public void remove() throws Exception {

        repository.remove("simple-todo");
        var actual = repository.findById("simple-todo");

        assertThat(actual).isNull();

    }

    @Test
    public void update() throws Exception {

    }

    @Test
    public void findByPatternInTitle() throws Exception {

        TodoRecord todo = new TodoRecord("Test");
        repository.save(todo);

        var actual = repository.findByPatternInTitle("Tes");

        assertThat(actual).contains(todo);
    }

    @Test
    public void findByBetweenStartDates() throws Exception {
        //Date firstDate1 = new Date(int year, int month, int date);
        Date d1 = new Date(2022, 4, 1);
        Date d2 = new Date(2022, 4, 3);
        Date d3 = new Date(2022, 4, 9);
        TodoRecord todo = new TodoRecord("Homework", "Finish my data bases homework", Status.NEW,d1,d2);
        repository.save(todo);

        //No null Test
        var actual1 = repository.findByBetweenStartDates(d1,d2);
        assertThat(actual1).contains(todo);

        //Null Test
        var actual2 = repository.findByBetweenStartDates(d1,d3);
        assertThat(actual2).isEmpty();
    }

}