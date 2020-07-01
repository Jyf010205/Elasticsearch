import com.sgzs.elasticsearch.ElasticsearchApplication;
import com.sgzs.elasticsearch.entity.Bank;
import com.sgzs.elasticsearch.entity.Item;
import com.sgzs.elasticsearch.service.BankService;
import com.sgzs.elasticsearch.service.ItemService;
import org.elasticsearch.index.query.MatchQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * @author: jianyufeng
 * @description:
 * @date: 2020/6/30 12:13
 */
@SpringBootTest(classes = ElasticsearchApplication.class)
@RunWith(SpringRunner.class)
public class ElasticSearctTest {
    @Autowired
    private ElasticsearchTemplate elasticsearchTemplate;
    @Autowired
    ItemService itemService;
    @Autowired
    BankService bankService;

    @Test
    public void testCreate() {
        // 创建索引，会根据Item类的@Document注解信息来创建
        elasticsearchTemplate.createIndex(Item.class);
        // 配置映射，会根据Item类中的id、Field等字段来自动完成映射
        elasticsearchTemplate.putMapping(Item.class);
    }

    /**
     * 新增
     */
    @Test
    public void testAdd(){
        Item item = new Item(1L, "小米手机7", " 手机",
                "小米", 3499.00, "http://image.leyou.com/13123.jpg");
        itemService.save(item);
    }

    /**
     * 修改，id存在就是修改，否则为插入
     */
    @Test
    public void testUpdate(){
        Item item = new Item(1L, "小米手机7", " 手机",
                "小米", 3499.00, "http://image.leyou.com/13123.jpg");
        itemService.save(item);
    }

    /**
     * 批量新增
     */
    @Test
    public void indexList() {
        List<Item> list = new ArrayList<>();
        list.add(new Item(1L, "小米手机7", "手机", "小米", 3299.00, "http://image.leyou.com/13123.jpg"));
        list.add(new Item(2L, "坚果手机R1", "手机", "锤子", 3699.00, "http://image.leyou.com/13123.jpg"));
        list.add(new Item(3L, "华为META10", "手机", "华为", 4499.00, "http://image.leyou.com/13123.jpg"));
        list.add(new Item(4L, "小米Mix2S", "手机", "小米", 4299.00, "http://image.leyou.com/13123.jpg"));
        list.add(new Item(5L, "荣耀V10", "手机", "华为", 2799.00, "http://image.leyou.com/13123.jpg"));
        // 接收对象集合，实现批量新增
        itemService.saveAll(list);
    }

    /**
     * 删除
     */
    @Test
    public void testDelete(){
        itemService.deleteById(1L);
    }

    /**
     * 根据id查询
     */
    @Test
    public void testQuery(){
        Optional<Item> optional = itemService.findById(2L);
        System.out.println(optional.get());
    }

    /**
     * 查询全部，按价格降序
     */
    @Test
    public void testFind(){
        Iterable<Item> items = itemService.findAll(Sort.by(Sort.Direction.DESC, "price"));
        items.forEach(item -> System.out.println(item));
    }

    /**
     * 自定义方法查询
     */
    @Test
    public void definedFind(){
        List<Item> items = itemService.findByPriceBetween(2000.00, 3500.00);
        items.forEach(item -> System.out.println(item));
    }

    /**
     * 高级查询，基本查询
     */
    @Test
    public void testBaseQuery(){
        MatchQueryBuilder queryBuilder = QueryBuilders.matchQuery("title", "小米");
        Iterable<Item> items = itemService.search(queryBuilder);
        items.forEach(System.out::println);
    }

    /**
     * 高级查询，自定义查询
     */
    @Test
    public void testNaviceQuery(){
        //构建查询条件
        NativeSearchQueryBuilder queryBuilder = new NativeSearchQueryBuilder();
        //添加基本的分词查询
        queryBuilder.withQuery(QueryBuilders.matchQuery("title", "小米"));

        int page = 0;
        int size = 3;
        queryBuilder.withPageable(PageRequest.of(page,size));

        //执行搜索，获取结果
        Page<Item> items = itemService.search(queryBuilder.build());
        // 打印总条数
        System.out.println(items.getTotalElements());
        // 打印总页数
        System.out.println(items.getTotalPages());
        //每页大小
        System.out.println(items.getSize());
        items.forEach(System.out::println);
    }

    /**
     * 查询银行数据
     */
    @Test
    public void queryBank(){
        Optional<Bank> bank = bankService.findById(1L);
        System.out.println(bank.get());
    }

}
