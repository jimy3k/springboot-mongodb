package com.nicole.springbootmongodb;

import com.nicole.springbootmongodb.entity.Employee;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.BasicQuery;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

@SpringBootTest
class SpringbootMongodbApplicationTests {

    @Autowired
    private MongoTemplate mongoTemplate;

    @Test
    void contextLoads() {
    }

    /*
     * 测试 创建 colleciton
     */
    @Test
    public void testCreateCollection() {
        boolean emp = mongoTemplate.collectionExists("emp");
        if (emp) {
            mongoTemplate.dropCollection("emp");
            System.out.println("emp dropped!");
        }
        mongoTemplate.createCollection("emp");
        System.out.println("emp created!");
    }

    /*
     * 测试 插入 Document
     */
    @Test
    public void testInsert() {

        //集合操作
        boolean empExists = mongoTemplate.collectionExists("employee");
        if (empExists) {
            mongoTemplate.dropCollection("employee");
            System.out.println("employee dropped!");
        }
        mongoTemplate.createCollection("employee");
        System.out.println("employee created!");

        //文档操作
        Employee employee = new Employee(1, "小明", 30, 30000.00, new Date());

        //添加文档
        //save：_id存在时更新文档
        mongoTemplate.save(employee);
        System.out.println("employee inserted and saved!");
        //insert：_id存在时抛出异常  支持批量操作
        List<Employee> list = Arrays.asList(
                new Employee(2, "张三", 21, 5000.00, new Date()),
                new Employee(3, "李四", 26, 7000.00, new Date()),
                new Employee(4, "王五", 25, 8000.00, new Date()),
                new Employee(5, "赵六", 28, 12000.00, new Date()));
        //插入多条文档
        mongoTemplate.insert(list, Employee.class);
        System.out.println("employees inserted!");
    }

    /*
     * 测试 查询 Document
     */
    @Test
    public void testFind() {

        //查询文档
        List<Employee> list = mongoTemplate.findAll(Employee.class);   //mongoTemplate.find(new Query(),Employee.class);  无条件查询，等同findAll
        System.out.println("=========================查询所有文档========================");
        list.forEach(System.out::println);

        List<Employee> list1 = mongoTemplate.find(new Query(Criteria.where("salary").gte(5000).lte(12000)), Employee.class);
        System.out.println("\n==============查询符合条件文档(5000<=salary<=12000 )================");
        list1.forEach(System.out::println);

        List<Employee> list2 = mongoTemplate.find(new Query(Criteria.where("salary").gte(5000).lte(12000)), Employee.class);
        System.out.println("\n==============查询符合条件文档(5000<=salary<=12000 )================");
        list2.forEach(System.out::println);


        List<Employee> list3 = mongoTemplate.find(new Query(Criteria.where("name").regex("张")), Employee.class);
        System.out.println("\n==============模糊查询符合条件文档(name包含'张')================");
        list3.forEach(System.out::println);

        Employee one = mongoTemplate.findOne(new Query(), Employee.class);
        System.out.println("\n=========================查询第一个符合条件文档========================");
        System.out.println(one);

        Employee employee = mongoTemplate.findById(3, Employee.class);
        System.out.println("\n=========================根据_id查询文档========================");
        System.out.println(employee);
    }

    /*
     * 测试 查询 Document 通过Json字符串
     */
    @Test
    public void testFindByJson() {

        //查询文档
        //String json = "{$or:[{age:{$gte:25}},{salary:{$gte:4000}}]}";
        String json = "{$and:[{age:{$gte:25}},{salary:{$gte:4000}}]}";
        Query query = new BasicQuery(json);

        List<Employee> list = mongoTemplate.find(query, Employee.class);   //mongoTemplate.find(new Query(),Employee.class);  无条件查询，等同findAll
        System.out.println("=========================查询符合条件所有文档========================");
        list.forEach(System.out::println);

    }
}
